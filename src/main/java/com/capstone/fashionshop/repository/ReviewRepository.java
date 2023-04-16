package com.capstone.fashionshop.repository;

import com.capstone.fashionshop.models.entities.Review;
import com.capstone.fashionshop.payload.aggregate.RecommendAggregate;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    Page<Review> findAllByProduct_IdAndEnable(ObjectId productId, boolean enable, Pageable pageable);
    Optional<Review> findReviewByOrderItem_IdAndUser_Id(ObjectId orderItemId, ObjectId userId);
    @Aggregation("{ '$project': { '_id' : '$_id', 'rate' : '$rate', 'product' : '$product', 'user' : '$user'} }")
    List<RecommendAggregate> getAllReview();
    @Aggregation(pipeline = {"{$match : {'user' :  ?0}} ",
            "{ '$project': { '_id' : '$_id' , 'rate' : '$rate', 'product' : '$product', 'user' : '$user'}}"})
    List<RecommendAggregate> getReviewByUserId(ObjectId userId);
}
