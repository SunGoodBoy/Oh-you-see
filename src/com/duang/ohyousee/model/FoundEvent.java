package com.duang.ohyousee.model;

public class FoundEvent extends Event	{

	private long found_event_id;//拾获事件编号

	private String found_date = getDate();	//拾获日期,从父类获取
	private String found_place = getPlace();//拾获地点,从父类获取
	
	private boolean returned_flag;	//归还标志

	//构造函数
	public FoundEvent()	{
		super();
	}
	
	
	/**
	 * @return the found_event_id
	 */
	public long getFound_event_id() {
		return found_event_id;
	}

	/**
	 * @param found_event_id the found_event_id to set
	 */
	public void setFound_event_id(long found_event_id) {
		this.found_event_id = found_event_id;
	}

	/**
	 * @return the found_date
	 */
	public String getFound_date() {
		return found_date;
	}

	/**
	 * @param found_date the found_date to set
	 */
	public void setFound_date(String found_date) {
		this.found_date = found_date;
	}

	/**
	 * @return the found_place
	 */
	public String getFound_place() {
		return found_place;
	}

	/**
	 * @param found_place the found_place to set
	 */
	public void setFound_place(String found_place) {
		this.found_place = found_place;
	}

	/**
	 * @return the returned_flag
	 */
	public boolean isReturned_flag() {
		return returned_flag;
	}

	/**
	 * @param returned_flag the returned_flag to set
	 */
	public void setReturned_flag(boolean returned_flag) {
		this.returned_flag = returned_flag;
	}
}
