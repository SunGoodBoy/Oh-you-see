package com.duang.easyecard.model;

public class Event {

	private long event_id;			//事件序号
	
	protected User owner;

	protected User founder;
	
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
	
	//设置事件中的失主
	public void setEvent_owner(User owner)	{
		this.owner = owner;
	}
	//获取事件中的失主
	public User getEvent_owner()	{
		return owner;
	}
	
	//设置事件中的拾获者
	public void setEvent_founder(User founder)	{
		this.founder = founder;
	}
	//获取事件的拾获者
	public User getEvent_founder()	{
		return founder;
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
