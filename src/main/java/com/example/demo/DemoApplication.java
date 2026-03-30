package com.example.demo;

import com.example.demo.model.Product;
import com.example.demo.model.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(ProductRepository repository) {
		return args -> {
			repository.save(new Product("Laptop Dell XPS 15",
					"Laptop cao cấp màn hình OLED 3.5K, Intel Core i9, 32GB RAM, 1TB SSD. Thiết kế sang trọng dành cho dân chuyên nghiệp.",
					42990000, "Laptop",
					"https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=400&h=300&fit=crop"));
			repository.save(new Product("Bàn phím cơ Keychron K8",
					"Bàn phím cơ không dây Bluetooth 5.1, hot-swap switch, đèn RGB, tương thích Mac/Win.",
					2490000, "Phụ kiện",
					"https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=400&h=300&fit=crop"));
			repository.save(new Product("Chuột Logitech MX Master 3S",
					"Chuột không dây cao cấp, cảm biến 8000 DPI, sạc USB-C, thiết kế công thái học.",
					2290000, "Phụ kiện",
					"https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=400&h=300&fit=crop"));
			repository.save(new Product("Tai nghe Sony WH-1000XM5",
					"Tai nghe chống ồn chủ động hàng đầu thế giới, pin 30 giờ, codec LDAC Hi-Res Audio.",
					8490000, "Phụ kiện",
					"https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400&h=300&fit=crop"));
			repository.save(new Product("Màn hình LG UltraFine 27\"",
					"Màn hình 4K IPS 27 inch, HDR400, 99% sRGB, USB-C 96W, chân đế ergonomic.",
					12990000, "Màn hình",
					"https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=400&h=300&fit=crop"));
			repository.save(new Product("MacBook Air M3",
					"MacBook Air chip M3, 16GB RAM, 512GB SSD, màn hình Liquid Retina 15.3 inch.",
					32990000, "Laptop",
					"https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400&h=300&fit=crop"));
		};
	}
}
