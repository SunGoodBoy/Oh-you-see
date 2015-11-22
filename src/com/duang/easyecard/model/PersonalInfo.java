package com.duang.easyecard.model;

public class PersonalInfo 
{

	private String title;
	private String content;
	private int imgId;
	
	private int type;
	
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

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
}
