package com.capstone.fashionshop.repository;

import com.capstone.fashionshop.models.entities.Review;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    Page<Review> findAllByProduct_IdAndEnable(ObjectId productId, boolean enable, Pageable pageable);
    Optional<Review> findReviewByProduct_IdAndUser_Id(ObjectId productId, ObjectId userId);
    Page<Review> findAllByUser_Id(String userId, Pageable pageable);
}
