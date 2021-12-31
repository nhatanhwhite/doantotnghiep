package com.service;

import java.util.List;

import com.entity.Category;
import com.entity.Product;
import com.service.dto.DataDTO;

import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    Product save(String product, String category, String thoroughbred, List<MultipartFile> files);

    Product findById(Long id);

    Product update(String product, String category, String thoroughbred);
    
    List<Product> findAll();

    void delete(Long id);

    Product updateImage(String id, List<MultipartFile> files);

    List<DataDTO> findTop8ProductNews();

    List<DataDTO> findTop8ProductSale();

    List<DataDTO> findByCategory(Category category);

    List<DataDTO> findByCategoryAndSale(Category category);

    List<DataDTO> findByProductLike(Long category, int sale, Long productId);
    
    List<DataDTO> findAll2();
}
