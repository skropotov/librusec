package com.skropotov.librusec.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Authors")
public class Author {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "author_id")
	private Long id;
	
	@Column(length = 128, nullable = false)
	private String primaryName;

	@Column(length = 128, nullable = false)
	private String lastName;

	@Column(length = 128)
	private String middleName;
	
	@Column
	private String fullName;
	
	@ManyToMany(mappedBy = "authors")
	private Set<Book> books;

	public Author() {
	}

	public Author(String lastName, String primaryName, String middleName) {
		this.primaryName = primaryName;
		this.lastName = lastName;
		this.middleName = middleName;
		if (middleName != "") {
			this.fullName = lastName + " " + primaryName + " " + middleName;
		} else {
			this.fullName = lastName + " " + primaryName;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrimaryName() {
		return primaryName;
	}

	public void setPrimaryName(String primaryName) {
		this.primaryName = primaryName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	
	public String getFullName() {
		return this.fullName;
	}

	@Override
	public String toString() {
		return "Author [id=" + id + ", name=" + this.fullName + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == Author.class) {
			return this.fullName.equals(((Author)obj).getFullName());
		}
		return false;
	}

	
}
