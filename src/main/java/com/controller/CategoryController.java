package com.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.entity.Category;
import com.entity.Product;
import com.entity.ProductImage;
import com.payload.MessageResponse;
import com.repository.ProductImageRepository;
import com.response.CategoryResponse;
import com.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/category", produces = "application/json")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ProductImageRepository productImageRepository;

    @PostMapping("/save")
    public ResponseEntity<MessageResponse> save (@RequestBody Category category) {
        Category result = categoryService.save(category);

        if(result != null) {
            return ResponseEntity.ok(new MessageResponse("success"));
        }

        return ResponseEntity.ok(new MessageResponse("failed"));
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<CategoryResponse>> findAll() {
        List<Category> categories = categoryService.findAll();
        List<CategoryResponse> categoriesReponse = new ArrayList<>();
        for (Category cat : categories) {
			CategoryResponse categoryResponse = new CategoryResponse();
			categoryResponse.setId(cat.getId());
			categoryResponse.setCategoryName(cat.getCategoryName());
			categoryResponse.setLastUpdate(cat.getLastUpdate());
			categoryResponse.setUserSystem(cat.getUserSystem());
			categoryResponse.setProducts(cat.getProducts());
			categoriesReponse.add(categoryResponse);
		}
        return ResponseEntity.ok(categoriesReponse);
    }

    @GetMapping("/find-id/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id) {
        Category category = categoryService.findById(id);

        return ResponseEntity.ok(category);
    }

    @PutMapping("/update")
    public ResponseEntity<MessageResponse> update(@RequestBody Category category) {
        Category result = categoryService.update(category);

        if(result != null) {
            return ResponseEntity.ok(new MessageResponse("success"));
        }

        return ResponseEntity.ok(new MessageResponse("failed"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
        try{
            categoryService.delete(id);

            return ResponseEntity.ok(new MessageResponse("success"));
        } catch(Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(new MessageResponse("failed"));
    }

    @GetMapping("/all/find-all")
    public ResponseEntity<List<CategoryResponse>> findByCategory() {
        List<Category> categories = categoryService.findAll();
        List<CategoryResponse> categoriesReponse = new ArrayList<>();
        List<ProductImage> productImages = productImageRepository.findAll();

		Map<Long, List<ProductImage>> map = new HashMap<>();

		for (ProductImage productImage : productImages) {
			Long id = productImage.getProduct().getId();

			List<ProductImage> listImages = new ArrayList<>();

			if (productImage.getProduct().getId() == id) {
				listImages.add(productImage);
			}

			map.put(productImage.getProduct().getId(), listImages);
		}
        for (Category cat : categories) {
			CategoryResponse categoryResponse = new CategoryResponse();
			categoryResponse.setId(cat.getId());
			categoryResponse.setCategoryName(cat.getCategoryName());
			categoryResponse.setLastUpdate(cat.getLastUpdate());
			categoryResponse.setUserSystem(cat.getUserSystem());
			categoryResponse.setProducts(cat.getProducts());
			for (Product product : categoryResponse.getProducts()) {
				String image = "";

				if (map.get(product.getId()) != null && map.get(product.getId()).size() > 0) {
					image = map.get(product.getId()).get(0).getImage();
				}

				product.setImage(MvcUriComponentsBuilder
			            .fromMethodName(NewsController.class, "getFile", image).build().toString());
			}
			categoriesReponse.add(categoryResponse);
		}
        return ResponseEntity.ok(categoriesReponse);

    }

    @GetMapping("/all/find-id/{id}")
    public ResponseEntity<Category> findByIdAll(@PathVariable Long id) {
        Category category = categoryService.findById(id);

        return ResponseEntity.ok(category);
    }
}
