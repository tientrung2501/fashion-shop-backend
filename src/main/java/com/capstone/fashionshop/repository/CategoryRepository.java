package com.capstone.fashionshop.repository;

import com.capstone.fashionshop.models.entities.Category;
import com.capstone.fashionshop.payload.aggregate.StateCountAggregate;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findAllByState(String state);
    List<Category> findAllByRoot(boolean isRoot);
    Optional<Category> findCategoryByIdAndState(String id, String state);
    @Aggregation("{ $group: { _id : $state, count: { $sum: 1 } } }")
    List<StateCountAggregate> countAllByState();
}
