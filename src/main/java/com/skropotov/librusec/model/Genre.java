package com.skropotov.librusec.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

@Entity
@Table(name = "Genres")
public class Genre {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "genre_id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Genre parent;
	
	@OneToMany
	@OrderColumn
	@JoinColumn(name = "parent_id")
	private Set<Genre> children = new HashSet<Genre>();
	
	@Column(length = 128, nullable = false)
	private String name;
	
	@Column(length = 30, nullable = true)
	private String fb2code;
	
	@ManyToMany(mappedBy = "genres")
	private Set<Book> books;

	public Genre() {
	}

	public Genre(Genre parent, String name) {
		this.parent = parent;
		this.name = name;
		
		if (parent != null) {
			parent.getChildren().add(this);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Genre getParent() {
		return parent;
	}

	public void setParent(Genre parent) {
		this.parent.getChildren().remove(this);
		this.parent = parent;
		if (parent != null) {
			parent.getChildren().add(this);
		}
	}

	public Set<Genre> getChildren() {
		return children;
	}

	public void setChildren(Set<Genre> children) {
		this.children = children;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getFb2code() {
		return fb2code;
	}

	public void setFb2code(String fb2code) {
		this.fb2code = fb2code;
	}

	@Override
	public String toString() {
		return "Genre [id=" + id + ", name=" + name + "]";
	}
	
}
