package com.capstone.fashionshop.services.admin;

import org.springframework.http.ResponseEntity;

public interface IAdminService {
    ResponseEntity<?> getOrderStatistical(String from, String to, String type);
    ResponseEntity<?> getAllCountByState();

}
