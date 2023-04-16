package com.capstone.fashionshop.controllers;

import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.models.entities.user.User;
import com.capstone.fashionshop.payload.request.ReviewReq;
import com.capstone.fashionshop.security.jwt.JwtUtils;
import com.capstone.fashionshop.services.review.IReviewService;
import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ReviewController {
    private final IReviewService reviewService;
    private final JwtUtils jwtUtils;

    @GetMapping(path = "/reviews/{productId}")
    public ResponseEntity<?> findByProductId (@PathVariable("productId") String productId,
                                              @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable){
        return reviewService.findByProductId(productId, pageable);
    }

    @PostMapping(path = "/reviews")
    public ResponseEntity<?> addReview (@Valid @RequestBody ReviewReq req,
                                        HttpServletRequest request){
        User user = jwtUtils.getUserFromJWT(jwtUtils.getJwtFromHeader(request));
        if (!user.getId().isBlank())
            return reviewService.addReview(user.getId(), req);
        throw new AppException(HttpStatus.FORBIDDEN.value(), "You don't have permission! Token is invalid");
    }
}
