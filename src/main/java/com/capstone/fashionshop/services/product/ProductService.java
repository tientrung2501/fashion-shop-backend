package com.capstone.fashionshop.services.product;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.exception.NotFoundException;
import com.capstone.fashionshop.mapper.ProductMapper;
import com.capstone.fashionshop.models.entities.Brand;
import com.capstone.fashionshop.models.entities.Category;
import com.capstone.fashionshop.models.entities.product.Product;
import com.capstone.fashionshop.models.entities.product.ProductAttribute;
import com.capstone.fashionshop.payload.ResponseObject;
import com.capstone.fashionshop.payload.request.ProductReq;
import com.capstone.fashionshop.payload.response.ProductListRes;
import com.capstone.fashionshop.payload.response.ProductRes;
import com.capstone.fashionshop.repository.*;
import com.capstone.fashionshop.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductMapper productMapper;
    @Override
    public ResponseEntity<?> findAll(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        List<ProductListRes> resList = products.getContent().stream().map(productMapper::toProductListRes).collect(Collectors.toList());
        if (resList.size() >0 )
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Get all product success", resList));
        throw new NotFoundException("Can not found any product");
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
    public ResponseEntity<?> findByCategoryIdAndBrandId(String id, Pageable pageable) {
        List<Product> products;
        try {
            products = productRepository.findAllByCategory_IdOrBrand_IdAndState(new ObjectId(id),
                    new ObjectId(id),Constants.ENABLE, pageable);
        } catch (Exception e) {
            throw new AppException(HttpStatus.BAD_REQUEST.value(), "Error when finding");
        }
        List<ProductListRes> resList = products.stream().map(productMapper::toProductListRes).collect(Collectors.toList());
        if (resList.size() > 0 )
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Get all product success", resList));
        throw new NotFoundException("Can not found any product with category or brand id: "+id);
    }

    @Override
    public ResponseEntity<?> search(String key, Pageable pageable) {
        return null;
    }

    @Override
    public ResponseEntity<?> addProduct(ProductReq req) {
        if (req != null) {
            Product product = productMapper.toProduct(req);
            String url = "/" + StringUtils.toSlug(product.getCategory().getName()) +
                     "/" + StringUtils.toSlug(product.getName());
            if (productRepository.existsProductByUrl(url))
                url = url + System.currentTimeMillis();
            product.setUrl(url);
            try {
                productRepository.save(product);
            } catch (Exception e) {
                throw new AppException(HttpStatus.CONFLICT.value(), "Product name already exists");
            }
            ProductListRes res = productMapper.toProductListRes(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseObject(true, "Add product successfully ", res)
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(false, "Request is null", "")
        );
    }

    @Override
    public ResponseEntity<?> updateProduct(String id, ProductReq req) {
        Optional<Product> product = productRepository.findProductByIdAndState(id, Constants.ENABLE);
        if (product.isPresent() && req != null) {
            processUpdate(req, product.get());
            try {
                productRepository.save(product.get());
            } catch (Exception e) {
                throw new AppException(HttpStatus.CONFLICT.value(), "Product name already exists");
            }
            ProductListRes res = productMapper.toProductListRes(product.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Add product successfully ", res)
            );
        }
        throw new NotFoundException("Can not found product with id: "+id);
    }

    public void processUpdate(ProductReq req, Product product) {
        if (!req.getName().equals(product.getName())) {
            product.setName(req.getName());
            String url = "/" + StringUtils.toSlug(req.getName());
            if (productRepository.existsProductByUrl(url))
                url = url + System.currentTimeMillis();
            product.setUrl(url);
        }
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
                productImageRepository.deleteByProduct_Id(product.get().getId());
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
    public ResponseEntity<?> deleteAttribute(String id, String name) {
        Optional<Product> product = productRepository.findProductByIdAndState(id, Constants.ENABLE);
        if (product.isPresent()) {
            product.get().getAttr().removeIf(a -> a.getName().equals(name));
            productRepository.save(product.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Delete attribute successfully", "")
            );
        } throw new NotFoundException("Can not found product with id: "+id);
    }
}
