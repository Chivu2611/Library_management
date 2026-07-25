# Library Management System

Library Management System được xây dựng bằng **Java 17, Spring Boot, Spring Security, Thymeleaf, Spring Data JPA và MySQL**.

## Requirements

Trước khi chạy project, cần cài đặt:

- JDK 17
- Apache Maven
- MySQL 8.x
- Git
- IDE: VS Code / IntelliJ IDEA / Eclipse / NetBeans

Kiểm tra Java:

```bash
java -version
```

Kiểm tra Maven:

```bash
mvn -version
```

---

# Local Setup

## Step 1: Clone project

```bash
git clone <YOUR_REPOSITORY_URL>
```

Di chuyển vào thư mục project:

```bash
cd librarymanagementsystem
```

---

## Step 2: Tạo MySQL Database

Khởi động MySQL Server và tạo database:

```sql
CREATE DATABASE library_management
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

Có thể kiểm tra bằng:

```sql
SHOW DATABASES;
```

---

## Step 3: Cấu hình Database

Mở file:

```text
src/main/resources/application.properties
```

Cấu hình:

```properties
server.port=9080
server.error.whitelabel.enabled=false

# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/library_management?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh&allowPublicKeyRetrieval=true

spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

Thay:

```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

bằng mật khẩu MySQL trên máy của bạn.

Nếu tài khoản MySQL không phải `root`, thay cả:

```properties
spring.datasource.username=YOUR_MYSQL_USERNAME
```

> Project sử dụng `spring.jpa.hibernate.ddl-auto=update`, vì vậy Hibernate sẽ tự tạo/cập nhật các bảng cần thiết khi ứng dụng khởi động.

---

## Step 4: Build project

Tại thư mục chứa `pom.xml`, chạy:

```bash
mvn clean install
```

Nếu thành công sẽ xuất hiện:

```text
BUILD SUCCESS
```

---

## Step 5: Chạy project

```bash
mvn spring-boot:run
```

Khi Spring Boot khởi động thành công, mở trình duyệt:

```text
http://localhost:9080
```

---

# Database

Project sử dụng:

- MySQL 8.x
- Spring Data JPA
- Hibernate
- MySQL Connector/J

Database mặc định:

```text
library_management
```

Các dữ liệu chính của hệ thống gồm:

- Books
- Authors
- Categories
- Publishers
- Users
- Roles
- Borrows
- Favorites

---

# User Roles

Hệ thống sử dụng Spring Security với hai quyền:

```text
ROLE_ADMIN
ROLE_USER
```

- `ROLE_ADMIN`: quản lý sách, tác giả, thể loại, nhà xuất bản...
- `ROLE_USER`: xem sách, mượn/trả sách, yêu thích sách và quản lý sách cá nhân.

Tài khoản đăng ký thông thường được cấp `ROLE_USER`.

> Với database mới hoàn toàn, cần có tài khoản được gán `ROLE_ADMIN` để sử dụng các chức năng quản trị.

---

# Image Uploads

Ảnh sách được lưu tại:

```text
uploads/books/
```

Ảnh tác giả được lưu tại:

```text
uploads/authors/
```

Database chỉ lưu tên file ảnh. Nếu chuyển database sang máy khác và muốn giữ ảnh cũ, cần chuyển cả các thư mục upload tương ứng.

---

# Common Problems

### MySQL connection failed

Kiểm tra:

- MySQL Server đã chạy chưa.
- Database `library_management` đã được tạo chưa.
- Username và password trong `application.properties` có đúng không.
- MySQL có đang sử dụng port `3306` không.

### Port 9080 already in use

Có thể đổi:

```properties
server.port=9080
```

sang port khác, ví dụ:

```properties
server.port=8080
```

### Maven build

Không chạy:

```bash
mvn
```

mà sử dụng:

```bash
mvn clean install
```

hoặc:

```bash
mvn spring-boot:run
```

---

# Quick Start

```text
1. Install Java 17, Maven and MySQL
2. Clone project
3. Create database: library_management
4. Configure MySQL username/password in application.properties
5. Run: mvn clean install
6. Run: mvn spring-boot:run
7. Open: http://localhost:9080
```