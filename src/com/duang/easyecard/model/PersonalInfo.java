package com.duang.easyecard.model;

public class PersonalInfo 
{

	private String title;
	private String content;
	
	//�޲ι��캯��
	public PersonalInfo()
	{
		super();
	}
	
	//���캯��
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
}
