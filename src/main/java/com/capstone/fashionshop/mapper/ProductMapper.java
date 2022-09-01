package com.capstone.fashionshop.mapper;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.exception.NotFoundException;
import com.capstone.fashionshop.models.entities.Brand;
import com.capstone.fashionshop.models.entities.Category;
import com.capstone.fashionshop.models.entities.product.Product;
import com.capstone.fashionshop.payload.request.ProductReq;
import com.capstone.fashionshop.payload.response.ProductRes;
import com.capstone.fashionshop.repository.BrandRepository;
import com.capstone.fashionshop.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductMapper {
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    public Product toProduct(ProductReq req) {
        Optional<Category> category = categoryRepository.findCategoryByIdAndState(req.getCategory(), Constants.ENABLE);
        Optional<Brand> brand = brandRepository.findBrandByIdAndState(req.getBrand(), Constants.ENABLE);
        if (category.isEmpty() || brand.isEmpty())
            throw new NotFoundException("Can not found category or brand");
        return new Product(req.getName(), req.getDescription(), req.getPrice(),
                category.get(), brand.get(), Constants.ENABLE);
    }

    public ProductRes toProductRes(Product req) {
        return new ProductRes(req.getId(), req.getName(), req.getUrl(), req.getDescription(),
                req.getPrice(),req.getPrice(), 0, req.getCategory(), req.getBrand(), req.getState(),
                req.getProductOptions());
    }
}
