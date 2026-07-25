package com.knf.dev.librarymanagementsystem.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.knf.dev.librarymanagementsystem.entity.Book;
import com.knf.dev.librarymanagementsystem.entity.User;

import com.knf.dev.librarymanagementsystem.service.AuthorService;
import com.knf.dev.librarymanagementsystem.service.BookService;
import com.knf.dev.librarymanagementsystem.service.BorrowService;
import com.knf.dev.librarymanagementsystem.service.CategoryService;
import com.knf.dev.librarymanagementsystem.service.FavoriteService;
import com.knf.dev.librarymanagementsystem.service.PublisherService;
import com.knf.dev.librarymanagementsystem.service.UserService;


@Controller
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final PublisherService publisherService;

    private final FavoriteService favoriteService;
    private final UserService userService;
    private final BorrowService borrowService;


    private static final String UPLOAD_DIR = "uploads/books/";


    public BookController(
            PublisherService publisherService,
            CategoryService categoryService,
            BookService bookService,
            AuthorService authorService,
            FavoriteService favoriteService,
            UserService userService,
            BorrowService borrowService) {

        this.publisherService = publisherService;
        this.categoryService = categoryService;
        this.bookService = bookService;
        this.authorService = authorService;

        this.favoriteService = favoriteService;
        this.userService = userService;
        this.borrowService = borrowService;
    }


	// ==============================
	// DANH SÁCH SÁCH
	// ==============================

	@RequestMapping({ "/books", "/" })
	public String findAllBooks(
			Model model,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {

		var currentPage = page.orElse(1);
		var pageSize = size.orElse(5);

		var bookPage = bookService.findPaginated(
				PageRequest.of(currentPage - 1, pageSize));

		model.addAttribute("books", bookPage);

		var totalPages = bookPage.getTotalPages();

		if (totalPages > 0) {

			var pageNumbers = IntStream
					.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());

			model.addAttribute("pageNumbers", pageNumbers);
		}

		return "list-books";
	}


	// ==============================
	// TÌM KIẾM SÁCH
	// ==============================

	@RequestMapping("/searchBook")
	public String searchBook(
			@Param("keyword") String keyword,
			Model model) {

		model.addAttribute(
				"books",
				bookService.searchBooks(keyword));

		model.addAttribute("keyword", keyword);

		return "list-books";
	}


	// ==============================
	// CHI TIẾT SÁCH
	// ==============================

	@RequestMapping("/book/{id}")
	public String findBookById(
			@PathVariable("id") Long id,
			Model model,
			Principal principal) {

		Book book = bookService.findBookById(id);

		model.addAttribute("book", book);


		if (principal != null) {

			User user = userService.findByEmail(
					principal.getName()
			);

			boolean favorite =
					favoriteService.isFavorite(user, book);

			model.addAttribute(
					"isFavorite",
					favorite
			);

			boolean borrowing =
                borrowService.isBorrowing(user, id);

        model.addAttribute(
                "isBorrowing",
                borrowing
        );
		}

		return "list-book";
	}

	// ==============================
	// FORM THÊM SÁCH
	// ==============================

	@GetMapping("/add")
	public String showCreateForm(
			Book book,
			Model model) {

		loadFormData(model);

		return "add-book";
	}


	// ==============================
	// THÊM SÁCH
	// ==============================

	@RequestMapping("/add-book")
	public String createBook(
			Book book,
			@RequestParam(value = "coverFile", required = false)
			MultipartFile coverFile,
			BindingResult result,
			Model model) {

		if (result.hasErrors()) {

			loadFormData(model);

			return "add-book";
		}

		if (coverFile != null && !coverFile.isEmpty()) {

			try {

				String fileName = saveCoverImage(coverFile);

				book.setCoverImage(fileName);

			} catch (IOException e) {

				e.printStackTrace();

				model.addAttribute(
						"uploadError",
						"Không thể tải ảnh bìa lên.");

				loadFormData(model);

				return "add-book";
			}
		}

		bookService.createBook(book);

		return "redirect:/books";
	}


	// ==============================
	// FORM CẬP NHẬT SÁCH
	// ==============================

	@GetMapping("/update/{id}")
	public String showUpdateForm(
			@PathVariable("id") Long id,
			Model model) {

		model.addAttribute(
				"book",
				bookService.findBookById(id));

		loadFormData(model);

		return "update-book";
	}


	// ==============================
	// CẬP NHẬT SÁCH
	// ==============================

	@RequestMapping("/update-book/{id}")
	public String updateBook(
			@PathVariable("id") Long id,
			Book book,
			@RequestParam(value = "coverFile", required = false)
			MultipartFile coverFile,
			BindingResult result,
			Model model) {

		Book existingBook = bookService.findBookById(id);

		if (result.hasErrors()) {

			book.setId(id);

			// Giữ ảnh cũ khi form có lỗi
			book.setCoverImage(existingBook.getCoverImage());

			loadFormData(model);

			return "update-book";
		}

		book.setId(id);

		// Có chọn ảnh mới
		if (coverFile != null && !coverFile.isEmpty()) {

			try {

				String fileName = saveCoverImage(coverFile);

				book.setCoverImage(fileName);

			} catch (IOException e) {

				e.printStackTrace();

				book.setCoverImage(
						existingBook.getCoverImage());

				model.addAttribute(
						"uploadError",
						"Không thể tải ảnh bìa mới lên.");

				loadFormData(model);

				return "update-book";
			}

		} else {

			// Không chọn ảnh mới -> giữ ảnh cũ
			book.setCoverImage(
					existingBook.getCoverImage());
		}

		bookService.updateBook(book);

		return "redirect:/books";
	}


	// ==============================
	// XÓA SÁCH
	// ==============================

	@RequestMapping("/remove-book/{id}")
	public String deleteBook(
			@PathVariable("id") Long id,
			Model model) {

		bookService.deleteBook(id);

		return "redirect:/books";
	}


	// ==============================
	// LOAD DỮ LIỆU CHO FORM
	// ==============================

	private void loadFormData(Model model) {

		model.addAttribute(
				"categories",
				categoryService.findAllCategories());

		model.addAttribute(
				"authors",
				authorService.findAllAuthors());

		model.addAttribute(
				"publishers",
				publisherService.findAllPublishers());
	}


	// ==============================
	// LƯU ẢNH BÌA
	// ==============================

	private String saveCoverImage(
			MultipartFile coverFile)
			throws IOException {

		Path uploadPath =
				Paths.get(UPLOAD_DIR);

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		String originalFileName =
				coverFile.getOriginalFilename();

		// Phòng trường hợp tên file null
		if (originalFileName == null
				|| originalFileName.isBlank()) {

			originalFileName = "book-cover";
		}

		// Loại bỏ đường dẫn nếu trình duyệt gửi kèm
		originalFileName =
				Paths.get(originalFileName)
						.getFileName()
						.toString();

		String fileName =
				UUID.randomUUID()
				+ "_"
				+ originalFileName;

		Path filePath =
				uploadPath.resolve(fileName);

		Files.copy(
				coverFile.getInputStream(),
				filePath,
				StandardCopyOption.REPLACE_EXISTING);

		return fileName;
	}
}