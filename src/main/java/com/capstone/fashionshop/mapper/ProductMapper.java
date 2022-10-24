package com.capstone.fashionshop.mapper;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.exception.NotFoundException;
import com.capstone.fashionshop.models.entities.Brand;
import com.capstone.fashionshop.models.entities.Category;
import com.capstone.fashionshop.models.entities.product.Product;
import com.capstone.fashionshop.models.entities.product.ProductImage;
import com.capstone.fashionshop.payload.request.ProductReq;
import com.capstone.fashionshop.payload.response.ProductListRes;
import com.capstone.fashionshop.payload.response.ProductRes;
import com.capstone.fashionshop.repository.BrandRepository;
import com.capstone.fashionshop.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                category.get(), brand.get(), Constants.ENABLE, req.getDiscount());
    }

    public ProductListRes toProductListRes(Product req) {
        List<ProductImage> images = new ArrayList<>();
        if (!req.getImages().isEmpty()) {
            images = req.getImages().stream()
                    .filter(ProductImage::isThumbnail).distinct().collect(Collectors.toList());
            if (images.isEmpty()) images = req.getImages();
        }
//        HashSet<Object> seen=new HashSet<>();
//        images.removeIf(e->!seen.add(e.getImageId()));

        String discountString = req.getPrice().multiply(BigDecimal.valueOf((double) (100- req.getDiscount())/100))
                .stripTrailingZeros().toPlainString();
        BigDecimal discountPrice = new BigDecimal(discountString);
        return new ProductListRes(req.getId(), req.getName(), req.getDescription(),
                req.getPrice(),discountPrice, req.getDiscount(), req.getRate(), req.getRateCount(), req.getCategory().getName(),
                req.getBrand().getName(), req.getState(), req.getCreatedDate(), req.getAttr(), images);
    }

    public ProductRes toProductRes(Product req) {
        String discountString = req.getPrice().multiply(BigDecimal.valueOf((double) (100- req.getDiscount())/100))
                .stripTrailingZeros().toPlainString();
        BigDecimal discountPrice = new BigDecimal(discountString);
        return new ProductRes(req.getId(), req.getName(), req.getDescription(),
                req.getPrice(),discountPrice, req.getDiscount(), req.getRate(), req.getRateCount(), req.getCategory().getName(),
                req.getBrand().getName(), req.getState(), req.getAttr(), req.getProductOptions(), req.getImages());
    }
}
