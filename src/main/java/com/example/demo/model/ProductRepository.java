package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Tìm kiếm sản phẩm theo tên chứa từ khóa
    List<Product> findByNameContainingIgnoreCase(String name);
}
