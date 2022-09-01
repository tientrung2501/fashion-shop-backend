package com.capstone.fashionshop.repository;

import com.capstone.fashionshop.models.entities.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findAllByState(String state);
    List<Category> findAllByRoot(boolean isRoot);
    Optional<Category> findCategoryByIdAndState(String id, String state);
}
