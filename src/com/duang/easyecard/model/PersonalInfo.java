package com.duang.easyecard.model;

public class PersonalInfo 
{

	private String title;
	private String content;
	private int imgId;
	
	//无参构造函数
	public PersonalInfo()
	{
		super();
	}
	
	//构造函数
	public PersonalInfo(String title)
	{
		super();
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the imgId
	 */
	public int getImgId() {
		return imgId;
	}

	/**
	 * @param imgId the imgId to set
	 */
	public void setImgId(int imgId) {
		this.imgId = imgId;
	}
}
