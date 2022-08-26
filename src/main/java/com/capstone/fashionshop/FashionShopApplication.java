package com.capstone.fashionshop;

import com.capstone.fashionshop.models.entities.User;
import com.capstone.fashionshop.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.util.List;

@SpringBootApplication
@EnableMongoAuditing
public class FashionShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(FashionShopApplication.class, args);
	}

}
