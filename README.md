# 🔐 DemoShop — DevSecOps Security Pipeline

[![White-Box Security](https://github.com/huyhuy23022004/demo-zap-ci/actions/workflows/whitebox-security.yml/badge.svg)](https://github.com/huyhuy23022004/demo-zap-ci/actions/workflows/whitebox-security.yml)
[![ZAP DAST Scan](https://github.com/huyhuy23022004/demo-zap-ci/actions/workflows/zap-baseline.yml/badge.svg)](https://github.com/huyhuy23022004/demo-zap-ci/actions/workflows/zap-baseline.yml)

> Ứng dụng web Spring Boot tích hợp **4 công cụ kiểm thử bảo mật tự động** vào CI/CD pipeline, kết hợp cả phương pháp kiểm thử **Hộp Trắng** (White-box) và **Hộp Đen** (Black-box).

---

## 📸 Screenshots

| Trang chủ | Sản phẩm | Đăng nhập |
|-----------|----------|-----------|
| Hero DevSecOps | Product Cards | Gradient Login |

## 🏗️ Công nghệ sử dụng

| Layer | Công nghệ |
|-------|-----------|
| **Backend** | Spring Boot 4.0.2, Spring Security, Spring Data JPA |
| **Frontend** | Thymeleaf, Bootstrap 5, Bootstrap Icons, Custom CSS |
| **Database** | H2 In-Memory Database |
| **CI/CD** | GitHub Actions (2 workflows chạy song song) |
| **SAST** | SpotBugs + FindSecBugs |
| **DAST** | OWASP ZAP Full Scan |
| **SCA** | OWASP Dependency-Check |
| **Coverage** | JaCoCo Code Coverage |
| **Container** | Docker (multi-stage build) |

## 📁 Cấu trúc dự án

```
demo/
├── .github/workflows/
│   ├── whitebox-security.yml    # CI: SAST + Unit Test + Coverage + SCA
│   └── zap-baseline.yml         # CI: DAST (OWASP ZAP)
├── src/main/java/com/example/demo/
│   ├── DemoApplication.java     # Entry point + seed data
│   ├── model/
│   │   ├── Product.java         # Entity sản phẩm
│   │   └── ProductRepository.java
│   ├── security/
│   │   └── SecurityConfig.java  # Cấu hình RBAC + CSRF (tắt cố ý)
│   └── web/
│       ├── PageController.java  # Controller chính
│       └── CustomErrorController.java  # Xử lý lỗi 403/404
├── src/main/resources/
│   ├── templates/               # Thymeleaf views (home, products, login, admin, error)
│   ├── static/css/style.css     # Custom premium CSS
│   └── application.properties   # Cấu hình H2, Actuator
├── src/test/java/
│   └── SecurityAccessTest.java  # Unit test phân quyền bảo mật
├── Dockerfile                   # Multi-stage Docker build
├── pom.xml                      # Maven + Security plugins
└── README.md
```

## 🚀 Cài đặt & Chạy

### Yêu cầu
- Java 21+
- Maven 3.9+ (hoặc dùng `mvnw` đi kèm)

### Chạy local
```bash
# Clone repository
git clone https://github.com/huyhuy23022004/demo-zap-ci.git
cd demo-zap-ci

# Chạy ứng dụng
./mvnw spring-boot:run

# Mở trình duyệt: http://localhost:8080
```

### Chạy bằng Docker
```bash
docker build -t demoshop .
docker run -d -p 8080:8080 demoshop
```

### Tài khoản demo

| Role | Username | Password |
|------|----------|----------|
| **ADMIN** | `admin` | `admin123` |
| **USER** | `user` | `user123` |

## 🔒 Kiến trúc bảo mật (4 tầng)

```
Push Code ──► GitHub Actions
                  │
        ┌─────────┴─────────┐
        ▼                    ▼
   Job 1 (Song song)    Job 2 (Song song)
   ┌──────────────┐    ┌──────────────┐
   │  HỘP TRẮNG   │    │  HỘP ĐEN     │
   │              │    │              │
   │ ✅ Unit Test  │    │ 🕷 ZAP Scan   │
   │ ✅ SpotBugs   │    │   (DAST)     │
   │ ✅ JaCoCo     │    │              │
   │ ✅ Dep-Check  │    │              │
   └──────┬───────┘    └──────┬───────┘
          ▼                    ▼
      📊 Reports           📊 Reports
      (Artifacts)          (Artifacts)
```

### 1. Unit Test bảo mật (JUnit + MockMvc)
- Khách truy cập `/` → 200 OK ✅
- Khách truy cập `/admin` → 302 Redirect Login ✅
- USER truy cập `/admin` → 403 Forbidden ✅
- ADMIN truy cập `/admin` → 200 OK ✅

### 2. SAST — SpotBugs + FindSecBugs
Quét mã nguồn tĩnh (bytecode) để tìm:
- Hardcoded passwords
- XSS vulnerabilities trong template
- Weak cryptography

### 3. DAST — OWASP ZAP Full Scan
Giả lập hacker tấn công ứng dụng đang chạy:
- Reflected XSS (qua chức năng Search)
- Missing CSRF tokens
- Missing security headers

### 4. SCA — OWASP Dependency-Check
Quét tất cả thư viện Maven để tìm lỗ hổng đã công bố (CVE).

## ⚠️ Lỗ hổng cố ý (cho mục đích demo)

| Lỗ hổng | Vị trí | Công cụ phát hiện |
|---------|--------|-------------------|
| **Reflected XSS** | `/products?search=<script>` | ZAP (DAST) |
| **CSRF disabled** | `SecurityConfig.java` | ZAP (DAST) |
| **Hardcoded password** | `SecurityConfig.java` | SpotBugs (SAST) |

## 📊 Chạy công cụ bảo mật thủ công

```bash
# Chạy Unit Test
./mvnw test

# Xem báo cáo Code Coverage (JaCoCo)
./mvnw test jacoco:report
# Mở: target/site/jacoco/index.html

# Chạy SAST (SpotBugs)
./mvnw compile spotbugs:spotbugs
# Mở: target/spotbugsXml.xml

# Chạy SCA (OWASP Dependency-Check)
./mvnw dependency-check:check
# Mở: target/dependency-check-report.html
```

## 📝 License

Đồ án môn học — Chỉ sử dụng cho mục đích học tập và nghiên cứu.
