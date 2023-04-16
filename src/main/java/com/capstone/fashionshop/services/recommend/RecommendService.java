package com.capstone.fashionshop.services.recommend;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.mapper.ProductMapper;
import com.capstone.fashionshop.models.entities.product.Product;
import com.capstone.fashionshop.models.entities.user.User;
import com.capstone.fashionshop.payload.ResponseObject;
import com.capstone.fashionshop.payload.response.ProductListRes;
import com.capstone.fashionshop.repository.ProductRepository;
import com.capstone.fashionshop.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecommendService implements IRecommendService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * @param id id of user
     * @return recommend product list for user authenticate or product list sort by discount
     */
    @Override
    public ResponseEntity<?> recommendProduct(String id) {
        Page<Product> products = null;
        if (id != null) {
            Optional<User> user = userRepository.findUserByIdAndState(id, Constants.USER_STATE_ACTIVATED);
            if (user.isPresent() &&
                    !user.get().getRecommendRanking().isEmpty()) {
                List<ObjectId> objectIds = user.get().getRecommendRanking().
                        entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                        .limit(3)
                        .map(x -> new ObjectId(x.getKey().toString()))
                        .collect(Collectors.toList());
                Pageable pageable = PageRequest.of(0, 10);
                products = productRepository.findAllByRecommendCategoryOrBrand(objectIds,
                        objectIds, Constants.ENABLE, pageable);
            }
        }
        if (products == null) {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("discount").descending());
            products = productRepository.findAllByState(Constants.ENABLE, pageable);
        }
        List<ProductListRes> resList = products.getContent().stream()
                .map(productMapper::toProductListRes).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(true, "Get recommend product success",
                        resList));
    }
}
