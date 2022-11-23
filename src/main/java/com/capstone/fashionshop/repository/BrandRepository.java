package com.capstone.fashionshop.repository;

import com.capstone.fashionshop.models.entities.Brand;
import com.capstone.fashionshop.payload.aggregate.StateCountAggregate;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends MongoRepository<Brand, String> {
    List<Brand> findAllByState(String state);
    Optional<Brand> findBrandByIdAndState(String id, String state);
    @Aggregation("{ $group: { _id : $state, count: { $sum: 1 } } }")
    List<StateCountAggregate> countAllByState();
}
