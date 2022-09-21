package com.capstone.fashionshop.services.category;

import com.capstone.fashionshop.config.CloudinaryConfig;
import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.exception.NotFoundException;
import com.capstone.fashionshop.models.entities.Category;
import com.capstone.fashionshop.payload.ResponseObject;
import com.capstone.fashionshop.payload.request.CategoryReq;
import com.capstone.fashionshop.repository.CategoryRepository;
import com.mongodb.DuplicateKeyException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final CloudinaryConfig cloudinary;

    @Override
    public ResponseEntity<?> findAll() {
        List<Category> list = categoryRepository.findAllByState(Constants.ENABLE);
        if (list.size() > 0)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Get all category success", list));
        throw new NotFoundException("Can not found any category");
    }

    @Override
    public ResponseEntity<?> findRoot() {
        List<Category> list = categoryRepository.findAllByRoot(true);
        if (list.size() > 0)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Get all root category success", list));
        throw new NotFoundException("Can not found any category");
    }

    @Override
    public ResponseEntity<?> findCategoryById(String id) {
        Optional<Category> category = categoryRepository.findCategoryByIdAndState(id, Constants.ENABLE);
        if (category.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Get category success", category));
        throw new NotFoundException("Can not found category with id: " + id);
    }

    @Override
    @Transactional
    public ResponseEntity<?> addCategory(CategoryReq req) {
        String imgUrl = null;
        if (req.getFile() != null && !req.getFile().isEmpty()) {
            try {
                imgUrl = cloudinary.uploadImage(req.getFile(), null);
            } catch (IOException e) {
                throw new AppException(HttpStatus.EXPECTATION_FAILED.value(), "Error when upload image");
            }
        }
        Category category = new Category(req.getName(), imgUrl , Constants.ENABLE);
        try {
            // Add child category
            if (!req.getParent_category().equals("-1") && !req.getParent_category().isBlank()){
                Optional<Category> parentCategory = categoryRepository.findById(req.getParent_category());
                if (parentCategory.isPresent()) {
                    category.setRoot(false);
                    categoryRepository.save(category);
                    parentCategory.get().getSubCategories().add(category);
                    categoryRepository.save(parentCategory.get());
                } else throw new NotFoundException("Can not found category with id: "+req.getParent_category());
            } else categoryRepository.save(category);
        } catch (Exception e) {
            throw new AppException(HttpStatus.EXPECTATION_FAILED.value(), "Category name already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject(true, "create category success", category));
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateCategory(String id, CategoryReq req) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            category.get().setName(req.getName());
            try {
                categoryRepository.save(category.get());
            } catch (DuplicateKeyException e) {
                throw new AppException(HttpStatus.CONFLICT.value(), "Category name already exists");
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Update category success", category));
        }
        throw new NotFoundException("Can not found category with id: " + id);
    }

    @Override
    public ResponseEntity<?> updateCategoryImage(String id, MultipartFile file) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            if (file != null && !file.isEmpty()) {
                try {
                    String imgUrl = cloudinary.uploadImage(file, category.get().getImage());
                    category.get().setImage(imgUrl);
                    categoryRepository.save(category.get());
                } catch (IOException e) {
                    throw new AppException(HttpStatus.EXPECTATION_FAILED.value(), "Error when upload image");
                }
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(true, "Update category image success", category));
            }
        }
        throw new NotFoundException("Can not found category with id: " + id);
    }

    @Override
    @Transactional
    public ResponseEntity<?> deactivatedCategory(String id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(true, "delete category success with id: "+id,""));

//        Optional<Category> category = categoryRepository.findById(id);
//        if (category.isPresent()) {
//            category.get().setState(Constants.DISABLE);
//            category.get().getSubCategories().forEach(c -> {
//                c.setState(Constants.DISABLE);
//            });
//            categoryRepository.saveAll(category.get().getSubCategories());
//            categoryRepository.save(category.get());
//            return ResponseEntity.status(HttpStatus.OK).body(
//                    new ResponseObject(true, "Update category success", category));
//        }
//        throw new NotFoundException("Can not found category with id: " + id);
    }
}
