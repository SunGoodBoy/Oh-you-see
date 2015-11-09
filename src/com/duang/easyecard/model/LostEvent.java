package com.duang.easyecard.model;

public class LostEvent extends Event	{

	private long lost_event_id;		//��ʧ�¼����

	private String lost_date = getDate();		//��ʧ����,�Ӹ����ȡ
	private String lost_place = getPlace();		//��ʧ�ص㣬�Ӹ����ȡ

	private boolean found_flag;		//���ҵ���־

	//���캯��
	public LostEvent()	{
		super();
	}


	/**
	 * @return the lost_event_id
	 */
	public long getLost_event_id() {
		return lost_event_id;
	}


	/**
	 * @param lost_event_id the lost_event_id to set
	 */
	public void setLost_event_id(long lost_event_id) {
		this.lost_event_id = lost_event_id;
	}


	/**
	 * @return the lost_date
	 */
	public String getLost_date() {
		return lost_date;
	}


	/**
	 * @param lost_date the lost_date to set
	 */
	public void setLost_date(String lost_date) {
		this.lost_date = lost_date;
	}


	/**
	 * @return the lost_place
	 */
	public String getLost_place() {
		return lost_place;
	}


	/**
	 * @param lost_place the lost_place to set
	 */
	public void setLost_place(String lost_place) {
		this.lost_place = lost_place;
	}


	/**
	 * @return the found_flag
	 */
	public boolean isFound_flag() {
		return found_flag;
	}


	/**
	 * @param found_flag the found_flag to set
	 */
	public void setFound_flag(boolean found_flag) {
		this.found_flag = found_flag;
	}
	
	
}
