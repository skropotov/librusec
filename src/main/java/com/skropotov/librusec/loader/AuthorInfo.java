package com.skropotov.librusec.loader;

public class AuthorInfo {
	private String primaryName;
	private String lastName;
	private String middleName;
	
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
	public AuthorInfo(String primaryName, String lastName, String middleName) {
		super();
		this.primaryName = primaryName;
		this.lastName = lastName;
		this.middleName = middleName;
	}
	@Override
	public String toString() {
		return "AuthorInfo [primaryName=" + primaryName + ", lastName=" + lastName + ", middleName=" + middleName + "]";
	}
	
	public String getFullName() {
		if (!middleName.equals("")) {
			return lastName + " " + primaryName + " " + middleName;
		} else {
			return lastName + " " + primaryName;
		}
		
	}
	
}
