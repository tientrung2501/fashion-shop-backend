package com.capstone.fashionshop.services.brand;

import com.capstone.fashionshop.config.CloudinaryConfig;
import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.exception.NotFoundException;
import com.capstone.fashionshop.models.entities.Brand;
import com.capstone.fashionshop.payload.ResponseObject;
import com.capstone.fashionshop.repository.BrandRepository;
import com.mongodb.DuplicateKeyException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BrandService implements IBrandService {
    private final BrandRepository brandRepository;
    private final CloudinaryConfig cloudinary;

    @Override
    public ResponseEntity<?> findAll() {
        List<Brand> list = brandRepository.findAllByState(Constants.ENABLE);
        if (list.size() > 0)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Get all brand success", list));
        throw new NotFoundException("Can not found any brand");
    }

    @Override
    public ResponseEntity<?> findBrandById(String id) {
        Optional<Brand> brand = brandRepository.findBrandByIdAndState(id, Constants.ENABLE);
        if (brand.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Get brand success", brand));
        throw new NotFoundException("Can not found brand with id: " + id);
    }

    @Override
    public ResponseEntity<?> addBrand(String name, MultipartFile file) {
        String imgUrl = null;
        if (file != null && !file.isEmpty()) {
            try {
                imgUrl = cloudinary.uploadImage(file, null);
            } catch (IOException e) {
                throw new AppException(HttpStatus.EXPECTATION_FAILED.value(), "Error when upload image");
            }
        }
        Brand brand = new Brand(name, imgUrl , Constants.ENABLE);
        try {
            brandRepository.save(brand);
        } catch (DuplicateKeyException e) {
            throw new AppException(HttpStatus.CONFLICT.value(), "Brand name already exists");
        } catch (Exception e) {
            throw new AppException(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject(true, "Create brand success", brand));
    }

    @Override
    public ResponseEntity<?> updateBrand(String id, String name, MultipartFile file) {
        Optional<Brand> brand = brandRepository.findById(id);
        if (brand.isPresent()) {
            brand.get().setName(name);
            if (file != null && !file.isEmpty()) {
                try {
                    String imgUrl = cloudinary.uploadImage(file, brand.get().getImage());
                    brand.get().setImage(imgUrl);
                } catch (IOException e) {
                    throw new AppException(HttpStatus.EXPECTATION_FAILED.value(), "Error when upload image");
                }
            }
            try {
                brandRepository.save(brand.get());
            } catch (DuplicateKeyException e) {
                throw new AppException(HttpStatus.CONFLICT.value(), "Brand name already exists");
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Update brand success", brand));
        }
        throw new NotFoundException("Can not found brand with id: " + id);
    }

    @Override
    public ResponseEntity<?> deactivatedBrand(String id) {
        brandRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(true, "delete brand success with id: "+id,""));
    }
}
