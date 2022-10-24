package com.capstone.fashionshop.services.product.option;

import com.capstone.fashionshop.config.CloudinaryConfig;
import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.exception.NotFoundException;
import com.capstone.fashionshop.models.entities.product.Product;
import com.capstone.fashionshop.models.entities.product.ProductImage;
import com.capstone.fashionshop.models.entities.product.ProductOption;
import com.capstone.fashionshop.models.entities.product.ProductVariant;
import com.capstone.fashionshop.payload.ResponseObject;
import com.capstone.fashionshop.payload.request.ProductOptionReq;
import com.capstone.fashionshop.repository.ProductOptionRepository;
import com.capstone.fashionshop.repository.ProductRepository;
import com.mongodb.MongoWriteException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductOptionService implements IProductOptionService {
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final CloudinaryConfig cloudinary;

    @Override
    @Transactional
    public ResponseEntity<?> addOption(String productId ,ProductOptionReq req) {
        Optional<ProductOption> checkOption = productOptionRepository.findByNameAndVariantsColorAndProductId(
                req.getName(), req.getColor(), new ObjectId(productId));
        if (checkOption.isPresent()) {
            throw new AppException(HttpStatus.CONFLICT.value(),
                    String.format("Option with name: %s, color code: %s, product id: %s already exists",
                            req.getName(), req.getColor(), productId));
        }
        Optional<ProductOption> option = productOptionRepository.findByNameAndProduct_Id(req.getName(), new ObjectId(productId));
        Optional<Product> product = productRepository.findProductByIdAndState(productId, Constants.ENABLE);
        if (product.isEmpty()) throw new NotFoundException("Can not found product with id: "+productId);
        // case does not exist size
        if (option.isEmpty()) {
            ProductOption productOption = new ProductOption(req.getName(), req.getExtraFee());
            productOption.setProduct(product.get());
            processVariant(productOption, req.getColor(), req.getImages(), req.getStock(), product.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseObject(true, "Add product option success", productOption));
        } else {
            processVariant(option.get(), req.getColor(), req.getImages(), req.getStock(), product.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseObject(true, "Add product option success", option.get()));
        }
    }

    public void processUploadImage (List<MultipartFile> images, String color, Product product) {
        if (images == null || images.isEmpty()) throw new AppException(HttpStatus.BAD_REQUEST.value(), "images is empty");
        for (int i = 0; i < images.size(); i++) {
            try {
                String url = cloudinary.uploadImage(images.get(i), null);
                if (i == 0) product.getImages().add(new ProductImage(UUID.randomUUID().toString(), url, true, color));
                else product.getImages().add(new ProductImage(UUID.randomUUID().toString(), url, false, color));
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new AppException(HttpStatus.EXPECTATION_FAILED.value(), "Error when upload images");
            }
            productRepository.save(product);
        }
//        return product.getImages();
    }

    public void processVariant (ProductOption productOption ,String color, List<MultipartFile> files,
                                Long stock, Product product) {
        List<ProductImage> images = product.getImages()
                .stream().filter(i -> i.getColor().equals(color)).collect(Collectors.toList());
        if (images.isEmpty()) processUploadImage(files, color, product);
        ProductVariant variants = new ProductVariant(UUID.randomUUID(), color, stock);
        productOption.getVariants().add(variants);
        try {
            productOptionRepository.save(productOption);
        } catch (MongoWriteException e) {
            log.error(e.getMessage());
            throw new AppException(HttpStatus.CONFLICT.value(), "Color already exists");
        }
    }

    @Override
    public ResponseEntity<?> findOptionById(String id) {
        Optional<ProductOption> productOption = productOptionRepository.findById(id);
        if (productOption.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Get product option success", productOption.get()));
        throw new NotFoundException("Can not found product option with id: "+id);
    }

    @Override
    public ResponseEntity<?> findOptionByProductId(String id) {
        List<ProductOption> productOptions = productOptionRepository.findAllByProduct_Id(new ObjectId(id));
        if (productOptions.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Get product option success", productOptions));
        } throw new NotFoundException("Can not found any product option with id: "+id);
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateOptionVariant(String id, String variantColor, ProductOptionReq req) {
        Optional<ProductOption> productOption = productOptionRepository.findByIdAndVariantColor(id, variantColor);
        if (productOption.isPresent()) {
            productOption.get().setName(req.getName());
            productOption.get().setExtraFee(req.getExtraFee());
            productOption.get().getVariants().forEach(variant -> {
                if (variant.getColor().equals(variantColor)) {
                    variant.setStock(req.getStock());
                    if (!variant.getColor().equals(req.getColor())) {
                        variant.setColor(req.getColor());
//                        List<ProductImage> images = productOption.get().getProduct().getImages()
//                                .stream().filter(i -> i.getColor().equals(variantColor)).collect(Collectors.toList());
//                        if (!images.isEmpty()) variant.setImages(images);
                    }
                }
            });
            try {
                productOptionRepository.save(productOption.get());
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(true, "Update product option success", productOption.get()));
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new AppException(HttpStatus.EXPECTATION_FAILED.value(), "Error when update option");
            }

        } throw new NotFoundException("Can not found product option with id: "+id);
    }
}
