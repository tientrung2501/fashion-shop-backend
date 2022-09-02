package com.capstone.fashionshop.repository;

import com.capstone.fashionshop.models.entities.product.ProductImage;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductImageRepository extends MongoRepository<ProductImage, String> {
    List<ProductImage> findAllByColorAndProduct_Id(String color, ObjectId productId);
}
