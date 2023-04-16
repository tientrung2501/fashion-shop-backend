package com.capstone.fashionshop.utils;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.models.entities.user.User;
import com.capstone.fashionshop.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.capstone.fashionshop.config.Constants.*;

@Component
@Slf4j
@Getter
@Setter
public class RecommendCheckUtils implements Runnable {
    private UserRepository userRepository;
    private String type;
    private String catId;
    private String brandId;
    private String userId;

    @Override
    @Async
    @Transactional
    public void run() {
        log.info("Start add recommend score!");
        if (!type.isBlank() && !catId.isBlank() && !brandId.isBlank() && userId != null) {
            Optional<User> user = userRepository.findUserByIdAndState(userId, Constants.USER_STATE_ACTIVATED);
            if (user.isPresent()) {
                try {
                    addScoreToUser(user.get(),catId);
                    addScoreToUser(user.get(),brandId);
                    userRepository.save(user.get());
                } catch (Exception e) {
                    log.error(e.getMessage());
                    log.error("Failed to save recommendation score!");
                }
            }
        } else log.error("Invalid input data in Recommend check utils!");
        log.info("Add recommend score end!");
    }

    public void addScoreToUser (User user, String id) {
        int catScore = 1;
        if (user.getRecommendRanking().containsKey(id)) {
            catScore = user.getRecommendRanking().get(id);
            switch (type) {
                case REVIEW_GOOD_TYPE: catScore+=5 ;break;
                case CART_TYPE: catScore+=3 ;break;
                default: catScore+=1;
            }
        }
        user.getRecommendRanking().put(id,
                catScore);
    }
}
