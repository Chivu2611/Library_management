package com.knf.dev.librarymanagementsystem;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.knf.dev.librarymanagementsystem.entity.Author;
import com.knf.dev.librarymanagementsystem.entity.Book;
import com.knf.dev.librarymanagementsystem.entity.Category;
import com.knf.dev.librarymanagementsystem.entity.Publisher;
import com.knf.dev.librarymanagementsystem.entity.Role;
import com.knf.dev.librarymanagementsystem.entity.User;
import com.knf.dev.librarymanagementsystem.repository.UserRepository;
import com.knf.dev.librarymanagementsystem.service.BookService;

@SpringBootApplication
public class Application {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        System.out.println();
        System.out.println("==========================================");
        System.out.println("       HỆ THỐNG QUẢN LÝ THƯ VIỆN");
        System.out.println("       Khởi động thành công!");
        System.out.println("       URL: http://localhost:9080");
        System.out.println("==========================================");
        System.out.println();
    }

    @Bean
    public CommandLineRunner initialCreate() {
        return (args) -> {

            var book = new Book(
                    "9786041234567",
                    "Dế Mèn Phiêu Lưu Ký",
                    "DMPLK001",
                    "Tác phẩm văn học thiếu nhi nổi tiếng"
            );
            book.addAuthors(new Author(
                    "Tô Hoài",
                    "Nhà văn Việt Nam"
            ));
            book.addCategories(new Category(
                    "Văn học thiếu nhi"
            ));
            book.addPublishers(new Publisher(
                    "Nhà xuất bản Kim Đồng"
            ));
            bookService.createBook(book);


            var book1 = new Book(
                    "9786042345678",
                    "Cho Tôi Xin Một Vé Đi Tuổi Thơ",
                    "CTV001",
                    "Tác phẩm viết về thế giới tuổi thơ"
            );
            book1.addAuthors(new Author(
                    "Nguyễn Nhật Ánh",
                    "Nhà văn Việt Nam"
            ));
            book1.addCategories(new Category(
                    "Văn học"
            ));
            book1.addPublishers(new Publisher(
                    "Nhà xuất bản Trẻ"
            ));
            bookService.createBook(book1);


            var book2 = new Book(
                    "9786043456789",
                    "Lão Hạc",
                    "LH001",
                    "Tác phẩm văn học hiện thực Việt Nam"
            );
            book2.addAuthors(new Author(
                    "Nam Cao",
                    "Nhà văn Việt Nam"
            ));
            book2.addCategories(new Category(
                    "Văn học Việt Nam"
            ));
            book2.addPublishers(new Publisher(
                    "Nhà xuất bản Văn học"
            ));
            bookService.createBook(book2);


            var user = new User(
                    "admin",
                    "admin",
                    "admin@.in",
                    passwordEncoder.encode("123456"),
                    Arrays.asList(new Role("ROLE_ADMIN"))
            );

            userRepository.save(user);
        };
    }
}