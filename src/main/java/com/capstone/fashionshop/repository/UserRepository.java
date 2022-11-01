package com.capstone.fashionshop.repository;

import com.capstone.fashionshop.models.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByEmailAndState(String email, String state);
    Optional<User> findUserByIdAndState(String id, String state);
    Page<User> findAllByState(String state, Pageable pageable);
    boolean existsByEmail(String email);
}
