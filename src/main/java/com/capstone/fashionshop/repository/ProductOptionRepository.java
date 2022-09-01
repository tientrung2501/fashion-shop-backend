package com.capstone.fashionshop.repository;

import com.capstone.fashionshop.models.entities.product.ProductOption;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionRepository extends MongoRepository<ProductOption, String> {
}
