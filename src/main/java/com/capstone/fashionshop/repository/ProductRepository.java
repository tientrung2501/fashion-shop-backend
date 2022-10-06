package com.capstone.fashionshop.repository;

import com.capstone.fashionshop.models.entities.product.Product;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    boolean existsProductByUrl(String url);
    Optional<Product> findProductByIdAndState(String id, String state);
    Page<Product> findAllByCategory_IdOrBrand_IdAndState(ObjectId catId, ObjectId brandId, String state, Pageable pageable);
    @Query(value = "{ $or: [{'category' : ?0},{'category':{$in: ?1}}] ," +
            "    'state' : 'enable'}")
    Page<Product> findProductsByCategory(ObjectId id, List<ObjectId> subCat, Pageable pageable);
    Page<Product> findAllBy(TextCriteria textCriteria, Pageable pageable);
    List<Product> findAllByIdIsIn(List<String> productIds);
}
