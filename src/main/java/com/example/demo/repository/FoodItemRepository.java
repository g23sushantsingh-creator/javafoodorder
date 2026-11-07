package com.example.demo.repository;

import com.example.demo.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    List<FoodItem> findAllByOrderByNameAsc();

    java.util.Optional<FoodItem> findFirstByName(String name);

    boolean existsByName(String name);

    List<FoodItem> findByCategory(String category);
}
