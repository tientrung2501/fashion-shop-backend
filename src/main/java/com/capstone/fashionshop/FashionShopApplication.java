package com.capstone.fashionshop;

import com.capstone.fashionshop.models.entities.User;
import com.capstone.fashionshop.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class FashionShopApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(FashionShopApplication.class, args);
		UserRepository userRepository = context.getBean(UserRepository.class);
		System.out.println("--------------- Insert -----------------");
		User user = new User();
		user.setName("Ronaldo");
		user.setId("1");
		userRepository.save(user);

		List<User> allUsers = userRepository.findAll();
		for(User player: allUsers) {
			System.out.println(player);
		}
	}

}
