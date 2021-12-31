package com.repository;

import java.util.List;

import com.entity.Category;
import com.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findTop12BySaleEqualsOrderByLastUpdateDesc(int sale);
	
    List<Product> findTop8BySaleEqualsOrderByLastUpdateDesc(int sale);

    List<Product> findTop8BySaleGreaterThanOrderByLastUpdateDesc(int sale);

    List<Product> findByCategoryAndSaleEqualsOrderByLastUpdateDesc(Category category, int sale);

    List<Product> findByCategoryAndSaleGreaterThanOrderByLastUpdateDesc(Category category, int sale);

    List<Product> findTop4ByCategoryAndSaleEqualsAndIdNotOrderByLastUpdateDesc(Category category, int sale, Long id);

    List<Product> findTop4ByCategoryAndSaleGreaterThanAndIdNotOrderByLastUpdateDesc(Category category, int sale, Long id);
}
