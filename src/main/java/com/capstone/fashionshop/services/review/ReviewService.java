package com.capstone.fashionshop.services.review;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.exception.NotFoundException;
import com.capstone.fashionshop.mapper.ReviewMapper;
import com.capstone.fashionshop.models.entities.Review;
import com.capstone.fashionshop.models.entities.order.OrderItem;
import com.capstone.fashionshop.models.entities.product.Product;
import com.capstone.fashionshop.models.entities.user.User;
import com.capstone.fashionshop.payload.ResponseObject;
import com.capstone.fashionshop.payload.request.ReviewReq;
import com.capstone.fashionshop.payload.response.ReviewRes;
import com.capstone.fashionshop.repository.OrderItemRepository;
import com.capstone.fashionshop.repository.ProductRepository;
import com.capstone.fashionshop.repository.ReviewRepository;
import com.capstone.fashionshop.repository.UserRepository;
import com.capstone.fashionshop.utils.RecommendCheckUtils;
import lombok.AllArgsConstructor;
import lombok.Synchronized;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewService implements IReviewService{
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final ReviewMapper reviewMapper;
    private final RecommendCheckUtils recommendCheckUtils;
    private final TaskScheduler taskScheduler;

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
    @Transactional(rollbackFor = Exception.class)
    @Synchronized
    public ResponseEntity<?> addReview(String userId, ReviewReq req) {
        Optional<Review> review = reviewRepository.findReviewByOrderItem_IdAndUser_Id(
                new ObjectId(req.getOrderItemId()), new ObjectId(userId));
        if (review.isPresent()) throw new AppException(HttpStatus.CONFLICT.value(), "You already review this product");
        Optional<User> user = userRepository.findUserByIdAndState(userId, Constants.USER_STATE_ACTIVATED);
        if (user.isPresent()) {
            Optional<OrderItem> orderItem = orderItemRepository.findById(req.getOrderItemId());
            if (orderItem.isPresent() && !orderItem.get().isReviewed()) {
                if (!orderItem.get().getOrder().getState().equals(Constants.ORDER_STATE_DONE)
                || !orderItem.get().getOrder().getUser().getId().equals(userId))
                    throw new AppException(HttpStatus.CONFLICT.value(), "You don't have permission");
                Optional<Product> product = productRepository.findProductByIdAndState(orderItem.get().getItem().getProduct().getId(), Constants.ENABLE);
                if (product.isEmpty()) throw new NotFoundException("Can not found this product");
                Review newReview = new Review(req.getContent(), req.getRate(),
                        product.get(), orderItem.get(), user.get(), true);
                reviewRepository.save(newReview);
                double rate = ((product.get().getRate() * (product.get().getRateCount() - 1)) + req.getRate())/ product.get().getRateCount();
                product.get().setRate(rate);
                productRepository.save(product.get());
                orderItem.get().setReviewed(true);
                orderItemRepository.save(orderItem.get());
                addScoreToRecommendation(product.get().getCategory().getId(),
                        product.get().getBrand().getId(), userId, req.getRate());
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(true, "Add review success ", newReview));
            } throw new NotFoundException("Can not found order item or order item already reviewed with id: " + req.getOrderItemId());
        } throw new NotFoundException("Can not found user with id: " + userId);
    }

    private void addScoreToRecommendation(String catId, String brandId, String userId, double rate) {
        recommendCheckUtils.setCatId(catId);
        recommendCheckUtils.setBrandId(brandId);
        recommendCheckUtils.setUserId(userId);
        recommendCheckUtils.setUserRepository(userRepository);
        recommendCheckUtils.setType(Constants.VIEW_TYPE);
        if (rate > 3) recommendCheckUtils.setType(Constants.REVIEW_GOOD_TYPE);
        taskScheduler.schedule(recommendCheckUtils, new Date(System.currentTimeMillis()));
    }
}
