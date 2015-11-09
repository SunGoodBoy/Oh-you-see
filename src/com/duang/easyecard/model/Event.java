package com.duang.easyecard.model;

public class Event {

	private long event_id;			//事件序号
	
	private String owner_stu_id;	//失主学号
	private String owner_name;		//失主姓名
	private String owner_contact;	//失主联系方式
	
	private String founder_name;	//拾获者
	private String founder_stu_id;	//拾获者学号
	private String fouder_contact;	//拾获者联系方式
	
	private String date;			//事件日期
	private String place;			//事件地点
	private String duration;		//事件时长
	private String due_date;		//事件到期日期

	private boolean close_flag;		//关闭事件标志

	//无参构造函数
	public Event(){}
	
	//关闭事件
	private void closeEvent(){
		setClose_flag(true);
	}
	
	//事件到期后自动关闭事件
	private void eventSelfClose()	{
		//如果事件时长到期，自动关闭事件
		if (duration == due_date)
			closeEvent();
	}
	
	
	
	/**
	 * @return the event_id
	 */
	public long getEvent_id() {
		return event_id;
	}

	/**
	 * @param event_id the event_id to set
	 */
	public void setEvent_id(long event_id) {
		this.event_id = event_id;
	}

	/**
	 * @return the owner_stu_id
	 */
	public String getOwner_stu_id() {
		return owner_stu_id;
	}

	/**
	 * @param owner_stu_id the owner_stu_id to set
	 */
	public void setOwner_stu_id(String owner_stu_id) {
		this.owner_stu_id = owner_stu_id;
	}

	/**
	 * @return the owner_name
	 */
	public String getOwner_name() {
		return owner_name;
	}

	/**
	 * @param owner_name the owner_name to set
	 */
	public void setOwner_name(String owner_name) {
		this.owner_name = owner_name;
	}

	/**
	 * @return the owner_contact
	 */
	public String getOwner_contact() {
		return owner_contact;
	}

	/**
	 * @param owner_contact the owner_contact to set
	 */
	public void setOwner_contact(String owner_contact) {
		this.owner_contact = owner_contact;
	}

	/**
	 * @return the founder_name
	 */
	public String getFounder_name() {
		return founder_name;
	}

	/**
	 * @param founder_name the founder_name to set
	 */
	public void setFounder_name(String founder_name) {
		this.founder_name = founder_name;
	}

	/**
	 * @return the founder_stu_id
	 */
	public String getFounder_stu_id() {
		return founder_stu_id;
	}

	/**
	 * @param founder_stu_id the founder_stu_id to set
	 */
	public void setFounder_stu_id(String founder_stu_id) {
		this.founder_stu_id = founder_stu_id;
	}

	/**
	 * @return the fouder_contact
	 */
	public String getFouder_contact() {
		return fouder_contact;
	}

	/**
	 * @param fouder_contact the fouder_contact to set
	 */
	public void setFouder_contact(String fouder_contact) {
		this.fouder_contact = fouder_contact;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the place
	 */
	public String getPlace() {
		return place;
	}

	/**
	 * @param place the place to set
	 */
	public void setPlace(String place) {
		this.place = place;
	}

	/**
	 * @return the duration
	 */
	public String getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}

	/**
	 * @return the close_flag
	 */
	public boolean isClose_flag() {
		return close_flag;
	}

	/**
	 * @param close_flag the close_flag to set
	 */
	public void setClose_flag(boolean close_flag) {
		this.close_flag = close_flag;
	}
	
}
