package com.capstone.fashionshop.services.product;

import com.capstone.fashionshop.config.CloudinaryConfig;
import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.exception.NotFoundException;
import com.capstone.fashionshop.mapper.ProductMapper;
import com.capstone.fashionshop.models.entities.Brand;
import com.capstone.fashionshop.models.entities.Category;
import com.capstone.fashionshop.models.entities.product.Product;
import com.capstone.fashionshop.models.entities.product.ProductAttribute;
import com.capstone.fashionshop.models.entities.product.ProductImage;
import com.capstone.fashionshop.payload.ResponseObject;
import com.capstone.fashionshop.payload.request.ProductReq;
import com.capstone.fashionshop.payload.response.ProductListRes;
import com.capstone.fashionshop.payload.response.ProductRes;
import com.capstone.fashionshop.repository.BrandRepository;
import com.capstone.fashionshop.repository.CategoryRepository;
import com.capstone.fashionshop.repository.ProductOptionRepository;
import com.capstone.fashionshop.repository.ProductRepository;
import com.mongodb.MongoWriteException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductMapper productMapper;
    private final CloudinaryConfig cloudinary;
    @Override
    public ResponseEntity<?> findAll(String state, Pageable pageable) {
        Page<Product> products;
        if (state.equalsIgnoreCase(Constants.ENABLE) || state.equalsIgnoreCase(Constants.DISABLE))
            products = productRepository.findAllByState(state.toLowerCase(), pageable);
        else products = productRepository.findAll(pageable);
        List<ProductListRes> resList = products.getContent().stream().map(productMapper::toProductListRes).collect(Collectors.toList());
        ResponseEntity<?> resp = addPageableToRes(products, resList);
        if (resp != null) return resp;
        throw new NotFoundException("Can not found any product");
    }

    private ResponseEntity<?> addPageableToRes(Page<Product> products, List<ProductListRes> resList) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("list", resList);
        resp.put("totalQuantity", products.getTotalElements());
        resp.put("totalPage", products.getTotalPages());
        if (resList.size() >0 )
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Get all product success", resp));
        return null;
    }

    @Override
    public ResponseEntity<?> findById(String id) {
        Optional<Product> product = productRepository.findProductByIdAndState(id, Constants.ENABLE);
        if (product.isPresent()) {
            ProductRes res = productMapper.toProductRes(product.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Get product success", res));
        }
        throw new NotFoundException("Can not found any product with id: "+id);
    }

    @Override
    public ResponseEntity<?> findByCategoryIdOrBrandId(String id, Pageable pageable) {
        Page<Product> products;
        try {
            Optional<Category> category = categoryRepository.findCategoryByIdAndState(id, Constants.ENABLE);
            if (category.isPresent()) {
                List<ObjectId> subCat = category.get().getSubCategories().stream().map(c -> new ObjectId(c.getId())).collect(Collectors.toList());
                products = productRepository.findProductsByCategory(new ObjectId(id),
                        subCat, pageable);
            } else products = productRepository.findAllByCategory_IdOrBrand_IdAndState(new ObjectId(id),
                      new ObjectId(id),Constants.ENABLE, pageable);
        } catch (Exception e) {
            throw new AppException(HttpStatus.BAD_REQUEST.value(), "Error when finding");
        }
        List<ProductListRes> resList = products.stream().map(productMapper::toProductListRes).collect(Collectors.toList());
        ResponseEntity<?> resp = addPageableToRes(products, resList);
        if (resp != null) return resp;
        throw new NotFoundException("Can not found any product with category or brand id: "+id);
    }

    @Override
    public ResponseEntity<?> search(String key, Pageable pageable) {
        Page<Product> products;
        try {
            products = productRepository.findAllBy(TextCriteria
                            .forDefaultLanguage().matchingAny(key),
                    pageable);
        } catch (Exception e) {
            throw new NotFoundException("Can not found any product with: "+key);
        }
        List<ProductListRes> resList = products.getContent().stream().map(productMapper::toProductListRes).collect(Collectors.toList());
        ResponseEntity<?> resp = addPageableToRes(products, resList);
        if (resp != null) return resp;
        throw new NotFoundException("Can not found any product with: "+key);
    }

    @Override
    public ResponseEntity<?> addProduct(ProductReq req) {
        if (req != null) {
            Product product = productMapper.toProduct(req);
            try {
                productRepository.save(product);
            } catch (Exception e) {
                throw new AppException(HttpStatus.CONFLICT.value(), "Product name already exists");
            }
            ProductRes res = productMapper.toProductRes(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseObject(true, "Add product successfully ", res)
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(false, "Request is null", "")
        );
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateProduct(String id, ProductReq req) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent() && req != null) {
            processUpdate(req, product.get());
            try {
                productRepository.save(product.get());
            } catch (MongoWriteException e) {
                throw new AppException(HttpStatus.CONFLICT.value(), "Product name already exists");
            } catch (Exception e) {
                throw new AppException(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
            }
            ProductRes res = productMapper.toProductRes(product.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Update product successfully ", res)
            );
        }
        throw new NotFoundException("Can not found product with id: "+id);
    }

    public void processUpdate(ProductReq req, Product product) {
        if (!req.getName().equals(product.getName()))
            product.setName(req.getName());
        if (!req.getDescription().equals(product.getDescription()))
            product.setDescription(req.getDescription());
        if (!req.getPrice().equals(product.getPrice()))
            product.setPrice(req.getPrice());
        if (!req.getCategory().equals(product.getCategory().getId())) {
            Optional<Category> category = categoryRepository.findCategoryByIdAndState(req.getCategory(), Constants.ENABLE);
            if (category.isPresent())
                product.setCategory(category.get());
            else throw new NotFoundException("Can not found category with id: "+req.getCategory());
        }
        if (!req.getBrand().equals(product.getBrand().getId())) {
            Optional<Brand> brand = brandRepository.findBrandByIdAndState(req.getBrand(), Constants.ENABLE);
            if (brand.isPresent())
                product.setBrand(brand.get());
            else throw new NotFoundException("Can not found brand with id: "+req.getBrand());
        }
        if (req.getState() != null && !req.getState().isEmpty() &&
                (req.getState().equalsIgnoreCase(Constants.ENABLE) ||
                req.getState().equalsIgnoreCase(Constants.DISABLE)))
            product.setState(req.getState());
        else throw new AppException(HttpStatus.BAD_REQUEST.value(), "Invalid state");
    }

    @Override
    public ResponseEntity<?> deactivatedProduct(String id) {
        Optional<Product> product = productRepository.findProductByIdAndState(id, Constants.ENABLE);
        if (product.isPresent()) {
            product.get().setState(Constants.DISABLE);
            productRepository.save(product.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Delete product successfully ", "")
            );
        } throw new NotFoundException("Can not found product with id: "+id);
    }

    @Override
    @Transactional
    public ResponseEntity<?> destroyProduct(String id) {
        Optional<Product> product = productRepository.findProductByIdAndState(id, Constants.ENABLE);
        if (product.isPresent()) {
            try {
                productRepository.deleteById(product.get().getId());
                productOptionRepository.deleteByProduct_Id(product.get().getId());
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new NotFoundException("Error when destroy product with id: "+id);
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Destroy product successfully ", "")
            );
        } throw new NotFoundException("Can not found product with id: "+id);
    }

    @Override
    @Transactional
    public ResponseEntity<?> addAttribute(String id, ProductAttribute req) {
        Optional<Product> product = productRepository.findProductByIdAndState(id, Constants.ENABLE);
        if (product.isPresent()) {
            if (product.get().getAttr().stream().anyMatch(a -> a.getName().equals(req.getName())))
                throw new AppException(HttpStatus.CONFLICT.value(), "Attribute name already exists");
            ProductAttribute attribute = new ProductAttribute(req.getName(), req.getVal());
            product.get().getAttr().add(attribute);
            productRepository.save(product.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Add attribute successfully", attribute)
            );
        } throw new NotFoundException("Can not found product with id: "+id);
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateAttribute(String id, ProductAttribute req) {
        Optional<Product> product = productRepository.findProductByIdAndState(id, Constants.ENABLE);
        if (product.isPresent()) {
            product.get().getAttr().forEach(a -> {
                if (a.getName().equals(req.getName())) a.setVal(req.getVal());
            });
            productRepository.save(product.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Update attribute successfully", "")
            );
        } throw new NotFoundException("Can not found product with id: "+id);
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteAttribute(String id, String name) {
        Optional<Product> product = productRepository.findProductByIdAndState(id, Constants.ENABLE);
        if (product.isPresent() && !name.isBlank()) {
            product.get().getAttr().removeIf(a -> a.getName().equals(name));
            productRepository.save(product.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Delete attribute successfully", "")
            );
        } throw new NotFoundException("Can not found product with id: "+id);
    }

    @Override
    @Transactional
    public ResponseEntity<?> addImagesToProduct(String id, String color, List<MultipartFile> files) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            try {
                if (files == null || files.isEmpty() || color.isEmpty()) throw new AppException(HttpStatus.BAD_REQUEST.value(), "Images and color is require");
                files.forEach(f -> {
                    try {
                        String url = cloudinary.uploadImage(f, null);
                        product.get().getImages().add(new ProductImage(UUID.randomUUID().toString(), url, false, color));
                    } catch (IOException e) {
                        log.error(e.getMessage());
                        throw new AppException(HttpStatus.EXPECTATION_FAILED.value(), "Error when upload images");
                    }
                    productRepository.save(product.get());
                });
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(true, "Add image to product successfully", product.get().getImages())
                );
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new NotFoundException("Error when save image: " + e.getMessage());
            }
        } throw new NotFoundException("Can not found product with id: " + id);
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteImageFromProduct(String id, String imageId) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent() && !product.get().getImages().isEmpty()) {
            try {
                Optional<ProductImage> checkDelete = product.get().getImages().stream().filter(i -> i.getImageId().equals(imageId)).findFirst();
                if (checkDelete.isPresent()) {
                    cloudinary.deleteImage(checkDelete.get().getUrl());
                    product.get().getImages().remove(checkDelete.get());
                    productRepository.save(product.get());
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject(true, "Delete image successfully", imageId)
                    );
                } else throw new NotFoundException("Can not found image in product with id: " + imageId);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new NotFoundException("Can not found product with id: " + id);
            }
        } throw new NotFoundException("Can not found any image or product with id: " + id);
    }
}
