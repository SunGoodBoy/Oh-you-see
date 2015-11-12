package com.duang.easyecard.model;

public class Event {

	private long event_id;			//�¼����
	
	protected User owner;

	protected User founder;
	
	private String date;			//�¼�����
	private String place;			//�¼��ص�
	private String duration;		//�¼�ʱ��
	private String due_date;		//�¼���������

	private boolean close_flag;		//�ر��¼���־

	//�޲ι��캯��
	public Event(){}

	//�ر��¼�
	private void closeEvent(){
		setClose_flag(true);
	}
	
	//�¼����ں��Զ��ر��¼�
	private void eventSelfClose()	{
		//����¼�ʱ�����ڣ��Զ��ر��¼�
		if (duration == due_date)
			closeEvent();
	}
	
	//�����¼��е�ʧ��
	public void setEvent_owner(User owner)	{
		this.owner = owner;
	}
	//��ȡ�¼��е�ʧ��
	public User getEvent_owner()	{
		return owner;
	}
	
	//�����¼��е�ʰ����
	public void setEvent_founder(User founder)	{
		this.founder = founder;
	}
	//��ȡ�¼���ʰ����
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
