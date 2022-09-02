package com.capstone.fashionshop.repository;

import com.capstone.fashionshop.models.entities.product.ProductOption;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductOptionRepository extends MongoRepository<ProductOption, String> {
    Optional<ProductOption> findByNameAndProduct_Id(String name, ObjectId productId);

    @Query(value = "{'name': ?0, 'variants.color': ?1, 'product.id': ?2}")
    Optional<ProductOption> findByNameAndVariantsColorAndProductId(String name, String colorCode, ObjectId productId);
    @Query(value = "{''variants.color': ?0, 'product.id': ?1}")
    Optional<ProductOption> findByVariantsColorAndProductId(String colorCode, ObjectId productId);
}
