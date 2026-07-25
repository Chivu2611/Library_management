package com.knf.dev.librarymanagementsystem.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "books")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "isbn", length = 50, nullable = false, unique = true)
	private String isbn;

	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@Column(name = "serialName", length = 50, nullable = false)
	private String serialName;

	@Column(name = "description", length = 250, nullable = false)
	private String description;

	// Ảnh bìa của sách
	// MySQL chỉ lưu tên file, ví dụ: de-men.jpg
	@Column(name = "cover_image", length = 255)
	private String coverImage;


	// =========================
	// TÁC GIẢ
	// =========================

	@ManyToMany(
			fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE,
					CascadeType.REMOVE
			}
	)
	@JoinTable(
			name = "books_authors",
			joinColumns = {
					@JoinColumn(name = "book_id")
			},
			inverseJoinColumns = {
					@JoinColumn(name = "author_id")
			}
	)
	private Set<Author> authors = new HashSet<Author>();


	// =========================
	// THỂ LOẠI
	// =========================

	@ManyToMany(
			fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			}
	)
	@JoinTable(
			name = "books_categories",
			joinColumns = {
					@JoinColumn(name = "book_id")
			},
			inverseJoinColumns = {
					@JoinColumn(name = "category_id")
			}
	)
	private Set<Category> categories = new HashSet<Category>();


	// =========================
	// NHÀ XUẤT BẢN
	// =========================

	@ManyToMany(
			fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			}
	)
	@JoinTable(
			name = "books_publishers",
			joinColumns = {
					@JoinColumn(name = "book_id")
			},
			inverseJoinColumns = {
					@JoinColumn(name = "publisher_id")
			}
	)
	private Set<Publisher> publishers = new HashSet<Publisher>();


	// =========================
	// CONSTRUCTOR
	// =========================

	public Book() {
		super();
	}

	public Book(String isbn, String name, String serialName, String description) {
		this.isbn = isbn;
		this.name = name;
		this.serialName = serialName;
		this.description = description;
	}


	// =========================
	// AUTHOR METHODS
	// =========================

	public void addAuthors(Author author) {
		this.authors.add(author);
		author.getBooks().add(this);
	}

	public void removeAuthors(Author author) {
		this.authors.remove(author);
		author.getBooks().remove(this);
	}


	// =========================
	// CATEGORY METHODS
	// =========================

	public void addCategories(Category category) {
		this.categories.add(category);
		category.getBooks().add(this);
	}

	public void removeCategories(Category category) {
		this.categories.remove(category);
		category.getBooks().remove(this);
	}


	// =========================
	// PUBLISHER METHODS
	// =========================

	public void addPublishers(Publisher publisher) {
		this.publishers.add(publisher);
		publisher.getBooks().add(this);
	}

	public void removePublishers(Publisher publisher) {
		this.publishers.remove(publisher);
		publisher.getBooks().remove(this);
	}


	// =========================
	// GETTER / SETTER
	// =========================

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSerialName() {
		return serialName;
	}

	public void setSerialName(String serialName) {
		this.serialName = serialName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCoverImage() {
		return coverImage;
	}

	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}

	public Set<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Set<Publisher> getPublishers() {
		return publishers;
	}

	public void setPublishers(Set<Publisher> publishers) {
		this.publishers = publishers;
	}
}