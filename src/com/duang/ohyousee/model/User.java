package com.duang.ohyousee.model;

public class User {

	private String username;
	private String stu_id;
	private String college;
	private String contact;
	
	private int imageId;
	
	private static String currentUserStuId;
	
	public User()	{
		super();
	}
	
	public User(String stu_id)	{
		super();
		this.stu_id = stu_id;
	}
	
	
	public User(String username, String stu_id, int imageId)	{
		super();
		this.username = username;
		this.stu_id = stu_id;
		this.imageId = imageId;
	}
	
	public String getUsername()	{
		return username;
	}
	
	public String getStu_id()	{
		return stu_id;
	}
	
	public String getCollege()	{
		return college;
	}
	
	public String getContact()	{
		return contact;
	}
	
	public int getImageId()	{
		return imageId;
	}
	
	public void setUsername(String username)	{
		this.username = username;
	}
	
	public void setStu_id(String stu_id)	{
		this.stu_id = stu_id;
	}
	
	public void setCollege(String college)	{
		this.college = college;
	}
	
	public void setContact(String contact)	{
		this.contact = contact;
	}
	
	public void setImageId(int imageId)	{
		this.imageId = imageId;
	}

	/**
	 * @return the currentUserStuId
	 */
	public static String getCurrentUserStuId() {
		return currentUserStuId;
	}

	/**
	 * @param currentUserStuId the currentUserStuId to set
	 */
	public static void setCurrentUserStuId(String currentUserStuId) {
		User.currentUserStuId = currentUserStuId;
	}
}
