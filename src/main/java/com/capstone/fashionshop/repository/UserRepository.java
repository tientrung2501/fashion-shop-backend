package com.capstone.fashionshop.repository;

import com.capstone.fashionshop.models.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByEmailAndState(String email, String state);
    Optional<User> findUserByIdAndState(String id, String state);
    boolean existsByEmail(String email);
}
