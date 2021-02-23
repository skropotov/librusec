package com.skropotov.librusec.loader;

import java.util.Arrays;
import java.util.Date;

public class BookInfo {
	private String name;
	private String archiveName;
	private String filename;
	private String seria;
	private Long seriaNum;
	private Date loadedDate;
	private Boolean isDeleted;
	private String language;
	private int bookSize;
	private String[] genres;
	private AuthorInfo[] authors;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getSeria() {
		return seria;
	}
	public void setSeria(String seria) {
		this.seria = seria;
	}
	public Long getSeriaNum() {
		return seriaNum;
	}
	public void setSeriaNum(Long seriaNum) {
		this.seriaNum = seriaNum;
	}
	public Date getLoadedDate() {
		return loadedDate;
	}
	public void setLoadedDate(Date loadedDate) {
		this.loadedDate = loadedDate;
	}
	public Boolean getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String[] getGenres() {
		return genres;
	}
	public void setGenres(String[] genres) {
		this.genres = genres;
	}
	public AuthorInfo[] getAuthors() {
		return authors;
	}
	public void setAuthors(AuthorInfo[] authors) {
		this.authors = authors;
	}
	
	public int getBookSize() {
		return bookSize;
	}
	public void setBookSize(int bookSize) {
		this.bookSize = bookSize;
	}
	public BookInfo(String name, String archiveName, String filename, String seria, Long seriaNum, Date loadedDate,
			Boolean isDeleted, String language, String[] genres,
			AuthorInfo[] authors, int bookSize) {
		super();
		this.name = name;
		this.archiveName = archiveName;
		this.filename = filename;
		this.seria = seria;
		this.seriaNum = seriaNum;
		this.loadedDate = loadedDate;
		this.isDeleted = isDeleted;
		this.language = language;
		this.genres = genres;
		this.authors = authors;
		this.bookSize = bookSize;
	}
	@Override
	public String toString() {
		return "BookInfo [name=" + name + ", archiveName=" + archiveName + ", filename=" + filename + ", seria=" + seria
				+ ", seriaNum=" + seriaNum + ", loadedDate=" + loadedDate + ", genres=" + Arrays.toString(genres)
				+ ", authors=" + Arrays.toString(authors) + "]";
	}
	
}
