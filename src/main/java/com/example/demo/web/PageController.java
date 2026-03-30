package com.example.demo.web;

import com.example.demo.model.Product;
import com.example.demo.model.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PageController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/products")
    public String products(@RequestParam(name="search", required = false) String search, Model model) {
        List<Product> items;
        if (search != null && !search.trim().isEmpty()) {
            items = productRepository.findByNameContainingIgnoreCase(search);
            // Lỗ hổng Cố ý cho ZAP: Trả về trực tiếp biến search (chưa được làm sạch) cho view
            // Ở Thymeleaf, ta sẽ dùng th:utext để render cái này, gây ra XSS.
            model.addAttribute("searchQuery", search);
        } else {
            items = productRepository.findAll();
        }
        
        model.addAttribute("items", items);
        return "products";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("productCount", productRepository.count());
        return "admin";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
