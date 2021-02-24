package com.skropotov.librusec.controller.tree;

public class GenreTree {
	private String id;
	private String parent;
	private String text;
	
	public GenreTree(String id, String text, String parent) {
		super();
		this.id = id;
		this.text = text;
		this.parent = parent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	@Override
	public String toString() {
		return "id=" + this.id + ", text=" + this.text + ", parent=" + this.parent;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == GenreTree.class) {
			return this.toString().equals(((GenreTree)obj).toString());
		}
		return false;
	}
}
