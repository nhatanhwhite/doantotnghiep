package com.repository;

import java.util.List;

import com.entity.CategoryNews;
import com.entity.News;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findTop4ByCategoryNewsOrderByLastUpdateDesc(CategoryNews categoryNews);

    List<News> findTop5ByOrderByLastUpdateDesc();

    List<News> findTop4ByCategoryNewsAndIdNotOrderByLastUpdateDesc(CategoryNews categoryNews, Long id);

    List<News> findAllByOrderByLastUpdateDesc();
}
