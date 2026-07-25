package com.knf.dev.librarymanagementsystem.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.knf.dev.librarymanagementsystem.entity.Author;
import com.knf.dev.librarymanagementsystem.service.AuthorService;

@Controller
public class AuthorController {

	private final AuthorService authorService;

	// Thư mục lưu ảnh tác giả
	private static final String UPLOAD_DIR = "uploads/authors/";

	public AuthorController(AuthorService authorService) {
		this.authorService = authorService;
	}


	// ==============================
	// DANH SÁCH TÁC GIẢ
	// ==============================

	@RequestMapping("/authors")
	public String findAllAuthors(
			Model model,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {

		var currentPage = page.orElse(1);
		var pageSize = size.orElse(5);

		var authorPage = authorService.findPaginated(
				PageRequest.of(currentPage - 1, pageSize));

		model.addAttribute("authors", authorPage);

		int totalPages = authorPage.getTotalPages();

		if (totalPages > 0) {

			var pageNumbers = IntStream
					.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());

			model.addAttribute("pageNumbers", pageNumbers);
		}

		return "list-authors";
	}


	// ==============================
	// CHI TIẾT TÁC GIẢ
	// ==============================

	@RequestMapping("/author/{id}")
	public String findAuthorById(
			@PathVariable("id") Long id,
			Model model) {

		model.addAttribute(
				"author",
				authorService.findAuthorById(id));

		return "list-author";
	}


	// ==============================
	// FORM THÊM TÁC GIẢ
	// ==============================

	@GetMapping("/addAuthor")
	public String showCreateForm(Author author) {

		return "add-author";
	}


	// ==============================
	// THÊM TÁC GIẢ + ẢNH
	// ==============================

	@RequestMapping("/add-author")
	public String createAuthor(
			Author author,
			@RequestParam(value = "imageFile", required = false)
			MultipartFile imageFile,
			BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			return "add-author";
		}

		// Nếu có chọn ảnh
		if (imageFile != null && !imageFile.isEmpty()) {

			try {

				String fileName = saveAuthorImage(imageFile);

				author.setImage(fileName);

			} catch (IOException e) {

				e.printStackTrace();

				model.addAttribute(
						"uploadError",
						"Không thể tải ảnh tác giả lên.");

				return "add-author";
			}
		}

		authorService.createAuthor(author);

		return "redirect:/authors";
	}


	// ==============================
	// FORM CẬP NHẬT TÁC GIẢ
	// ==============================

	@GetMapping("/updateAuthor/{id}")
	public String showUpdateForm(
			@PathVariable("id") Long id,
			Model model) {

		model.addAttribute(
				"author",
				authorService.findAuthorById(id));

		return "update-author";
	}


	// ==============================
	// CẬP NHẬT TÁC GIẢ + ẢNH
	// ==============================

	@RequestMapping("/update-author/{id}")
	public String updateAuthor(
			@PathVariable("id") Long id,
			Author author,
			@RequestParam(value = "imageFile", required = false)
			MultipartFile imageFile,
			BindingResult result,
			Model model) {

		// Lấy tác giả hiện tại trong database
		Author existingAuthor =
				authorService.findAuthorById(id);

		if (result.hasErrors()) {

			author.setId(id);

			// Giữ ảnh hiện tại
			author.setImage(
					existingAuthor.getImage());

			return "update-author";
		}

		author.setId(id);


		// Nếu chọn ảnh mới
		if (imageFile != null && !imageFile.isEmpty()) {

			try {

				String fileName =
						saveAuthorImage(imageFile);

				author.setImage(fileName);

			} catch (IOException e) {

				e.printStackTrace();

				// Upload lỗi -> giữ ảnh cũ
				author.setImage(
						existingAuthor.getImage());

				model.addAttribute(
						"uploadError",
						"Không thể tải ảnh tác giả mới lên.");

				return "update-author";
			}

		} else {

			// Không chọn ảnh mới -> giữ nguyên ảnh cũ
			author.setImage(
					existingAuthor.getImage());
		}

		authorService.updateAuthor(author);

		return "redirect:/authors";
	}


	// ==============================
	// XÓA TÁC GIẢ
	// ==============================

	@RequestMapping("/remove-author/{id}")
	public String deleteAuthor(
			@PathVariable("id") Long id,
			Model model) {

		authorService.deleteAuthor(id);

		return "redirect:/authors";
	}


	// ==============================
	// HÀM LƯU ẢNH TÁC GIẢ
	// ==============================

	private String saveAuthorImage(
			MultipartFile imageFile)
			throws IOException {

		Path uploadPath =
				Paths.get(UPLOAD_DIR);

		// Tự tạo uploads/authors nếu chưa có
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		String originalFileName =
				imageFile.getOriginalFilename();

		// Tránh trường hợp filename null/rỗng
		if (originalFileName == null
				|| originalFileName.isBlank()) {

			originalFileName = "author-image";
		}

		// Chỉ lấy tên file, loại bỏ path nếu có
		originalFileName =
				Paths.get(originalFileName)
						.getFileName()
						.toString();

		// Tạo tên duy nhất
		String fileName =
				UUID.randomUUID()
				+ "_"
				+ originalFileName;

		Path filePath =
				uploadPath.resolve(fileName);

		// Lưu file
		Files.copy(
				imageFile.getInputStream(),
				filePath,
				StandardCopyOption.REPLACE_EXISTING);

		return fileName;
	}
}