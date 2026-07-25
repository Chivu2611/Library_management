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
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "authors")
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name", length = 100, nullable = false, unique = true)
	private String name;

	@Column(name = "description", length = 250, nullable = false)
	private String description;

	// Ảnh tác giả
	// Database chỉ lưu tên file ảnh
	@Column(name = "image", length = 255)
	private String image;

	@ManyToMany(
			fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE,
					CascadeType.REMOVE
			},
			mappedBy = "authors"
	)
	private Set<Book> books = new HashSet<Book>();


	// =========================
	// CONSTRUCTOR
	// =========================

	public Author() {
		super();
	}

	public Author(String name, String description) {
		this.name = name;
		this.description = description;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Set<Book> getBooks() {
		return books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}
}