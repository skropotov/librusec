package com.skropotov.librusec.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

@Entity
@Table(name = "Books")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_id")
	private Long id;
	
	@Column
	private Long lib_id;
	
	@Column(length = 150, nullable = false)
	private String title;
	
	@OneToOne
	@OrderColumn
	@JoinColumn(name = "seria_id")
	private Seria seria;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "book_authors", joinColumns = {@JoinColumn(name = "book_id")}, inverseJoinColumns = {@JoinColumn(name = "author_id")})
	Set<Author> authors = new HashSet<Author>();
	
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(name = "book_genres", joinColumns = {@JoinColumn(name = "book_id")}, inverseJoinColumns = {@JoinColumn(name = "genre_id")})
	Set<Genre> genres = new HashSet<Genre>();
	
	@Column
	private Date additionDate;
	
	@Column(length = 2)
	private String language;
	
	@Column(length = 200)
	private String archiveName;
	
	@Column(length = 170)
	private String filename;
	
	@Column
	private int size;
	
	@Column
	private int isDeleted;
	
	@Column
	private Long seriaNum;
	
	public Book() {
	}

	public Book(Long lib_id, String title, Seria seria, Set<Author> authors, Set<Genre> genres, Date additionDate,
			String language, String archiveName, String filename, int size, int isDeleted, Long seriaNum) {
		super();
		this.lib_id = lib_id;
		this.title = title;
		this.seria = seria;
		this.authors = authors;
		this.genres = genres;
		this.additionDate = additionDate;
		this.language = language;
		this.archiveName = archiveName;
		this.filename = filename;
		this.size = size;
		this.isDeleted = isDeleted;
		this.seriaNum = seriaNum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLib_id() {
		return lib_id;
	}

	public void setLib_id(Long lib_id) {
		this.lib_id = lib_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Seria getSeria() {
		return seria;
	}
	
	public void setSeria(Seria seria) {
		this.seria = seria;
	}

	public Set<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}

	public Set<Genre> getGenres() {
		return genres;
	}

	public void setGenres(Set<Genre> genres) {
		this.genres = genres;
	}

	public Date getAdditionDate() {
		return additionDate;
	}
	
	public String getFormattedAdditionDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		return dateFormat.format(additionDate);
	}

	public void setAdditionDate(Date additionDate) {
		this.additionDate = additionDate;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getArchiveName() {
		return archiveName;
	}

	public void setArchiveName(String archiveName) {
		this.archiveName = archiveName;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Long getSeriaNum() {
		return seriaNum;
	}

	public void setSeriaNum(Long seriaNum) {
		this.seriaNum = seriaNum;
	}
	
	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == Book.class) {
			return this.filename.equals(((Book)obj).getFilename());
		}
		return false;
	}

	
}
