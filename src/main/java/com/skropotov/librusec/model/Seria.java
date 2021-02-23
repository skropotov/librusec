package com.skropotov.librusec.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Series")
public class Seria {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seria_id")
	private Long id;
	
	@Column(length = 80, nullable = false)
	private String name;

	public Seria() {
	}

	public Seria(String name) {
		this.name = name;
	}

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

	@Override
	public String toString() {
		return "Seria [id=" + id + ", name=" + name + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == Seria.class) {
			return this.name.equals(((Seria)obj).getName());
		}
		return false;
	}
	
	
}
