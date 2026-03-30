# Hướng dẫn đẩy Code lên GitHub và chạy CI/CD

Dự án này đã được mình cấu hình sẵn luồng CI/CD qua GitHub Actions (gồm White-box và Black-box testing). Khi bạn đẩy code lên, các bài test bảo mật sẽ tự động chạy. Dưới đây là các bước để bạn đẩy (push) code mới nhất lên repo của mình.

## Trường hợp 1: Bạn đã clone repo từ GitHub về máy
Nếu thư mục `demo` hiện tại (trong máy của bạn) đã có sẵn kết nối với GitHub từ trước (bạn dùng lệnh `git clone` để tải về), hãy thực hiện lần lượt 3 lệnh sau trong Terminal của thư mục dự án:

```bash
# B1: Thêm tất cả thay đổi mới vào danh sách chờ
git add .

# B2: Tạo một ghi chú (commit) về những thay đổi này
git commit -m "Cập nhật bản Full DevSecOps: UI Premium, JaCoCo, Dep-Check, Custom Error"

# B3: Đẩy thẳng lên nhánh main của GitHub
git push origin main
```

*(Nếu Terminal hiện yêu cầu bạn đăng nhập, hãy nhập tài khoản GitHub hoặc Personal Access Token của GitHub).*

---

## Trường hợp 2: Đây là code mới cứng, chưa liên kết với GitHub
Nếu thư mục này chưa có Git hoặc chưa từng đẩy lên Repo `huyhuy23022004/demo-zap-ci` bao giờ, bạn phải làm theo các bước này:

**Bước 1: Khởi tạo Git cho thư mục (nếu chưa có)**
```bash
git init
```

**Bước 2: Phê duyệt những thay đổi**
```bash
git add .
git commit -m "Initialize Full DevSecOps Project"
```

**Bước 3: Gắn link kết nối tới repo GitHub của bạn**
```bash
# Thay đổi tên nhánh chính thành main
git branch -M main

# Liên kết với Repo GitHub của bạn
git remote add origin https://github.com/huyhuy23022004/demo-zap-ci.git
```
*(Nếu nó báo `remote origin already exists` thì bỏ qua lệnh ở trên)*

**Bước 4: Đẩy code lên Repo**
```bash
# Nếu repo trên mạng rỗng:
git push -u origin main

# Nếu repo rên mạng ĐÃ CÓ FILE (như README, workflows lúc nãy), bạn cần ÉP lệnh Push này đè lên:
git push -u origin main --force
```

---

## Cách kiểm tra Thành quả (Xác nhận CI/CD tự động chạy)

Sau khi báo lỗi đẩy (`git push`) thành công 100%, hãy:
1. Mở trình duyệt và vào trang GitHub của project: **[https://github.com/huyhuy23022004/demo-zap-ci](https://github.com/huyhuy23022004/demo-zap-ci)**
2. Chuyển sang thẻ **Actions** (nằm ở thanh ngang phía trên).
3. Bạn sẽ thấy 2 luồng Workflows có tên là:
   - 🟢 `White-Box Security (SAST & Unit Test & SCA)`
   - 🟢 `ZAP Full Scan` (hoặc Baseline Scan)
4. Mở từng luồng lên xem nó đang chạy. Sau khi nó chạy xong (đợi tầm 4-6 phút), kéo xuống phần **Artifacts** để tải toàn bộ các Báo cáo Bảo mật (File *.html, *.xml) về nộp đồ án.
