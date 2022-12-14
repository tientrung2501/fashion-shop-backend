package com.capstone.fashionshop.services.review;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.exception.NotFoundException;
import com.capstone.fashionshop.mapper.ReviewMapper;
import com.capstone.fashionshop.models.entities.Review;
import com.capstone.fashionshop.models.entities.product.Product;
import com.capstone.fashionshop.models.entities.user.User;
import com.capstone.fashionshop.payload.ResponseObject;
import com.capstone.fashionshop.payload.request.ReviewReq;
import com.capstone.fashionshop.payload.response.ReviewRes;
import com.capstone.fashionshop.repository.ProductRepository;
import com.capstone.fashionshop.repository.ReviewRepository;
import com.capstone.fashionshop.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Synchronized;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewService implements IReviewService{
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ResponseEntity<?> findByProductId(String productId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAllByProduct_IdAndEnable(new ObjectId(productId) , true, pageable);
        if (reviews.isEmpty()) throw new NotFoundException("Can not found any review");
        List<ReviewRes> resList = reviews.getContent().stream().map(reviewMapper::toReviewRes).collect(Collectors.toList());
        Map<String, Object> resp = new HashMap<>();
        resp.put("list", resList);
        resp.put("totalQuantity", reviews.getTotalElements());
        resp.put("totalPage", reviews.getTotalPages());
        if (reviews.isEmpty()) throw new NotFoundException("Can not found any review");
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(true, "Get review by product success ", resp));
    }

    @Override
    @Transactional
    @Synchronized
    public ResponseEntity<?> addReview(String userId, ReviewReq req) {
        Optional<Review> review = reviewRepository.findReviewByProduct_IdAndUser_Id(
                new ObjectId(req.getProductId()), new ObjectId(userId));
        if (review.isPresent()) throw new AppException(HttpStatus.CONFLICT.value(), "You already review this product");
        Optional<User> user = userRepository.findUserByIdAndState(userId, Constants.USER_STATE_ACTIVATED);
        if (user.isPresent()) {
            Optional<Product> product = productRepository.findProductByIdAndState(req.getProductId(), Constants.ENABLE);
            if (product.isPresent()) {
                Review newReview = new Review(req.getContent(), req.getRate(), product.get(), user.get(), true);
                reviewRepository.save(newReview);
                double rate = ((product.get().getRate() * product.get().getRateCount()) + req.getRate())/ (product.get().getRateCount());
                product.get().setRate(rate);
                productRepository.save(product.get());
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(true, "Add review success ", newReview));
            } throw new NotFoundException("Can not found product with id: " + req.getProductId());
        } throw new NotFoundException("Can not found user with id: " + userId);
    }
}
