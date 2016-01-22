package com.lusheng.bookcrossing.model;

import java.util.Date;

/**
 * 漂流记录
 * 
 * @author 卢晟
 * 
 */
public class DriftRecord {
	public static final int DRIFTING_STATUS = 1;
	public static final int DRIFTED_STATUS = 2;
	private Integer drid;
	private Date startTime;// 开始时间
	private Date endTime;// 结束时间
	private int status;// 漂流状态
	private User preUser;// 上一个借阅者
	private User nextUser;// 下一个借阅者
	private Book book;

	public Integer getDrid() {
		return drid;
	}

	public void setDrid(Integer drid) {
		this.drid = drid;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public User getPreUser() {
		return preUser;
	}

	public void setPreUser(User preUser) {
		this.preUser = preUser;
	}

	public User getNextUser() {
		return nextUser;
	}

	public void setNextUser(User nextUser) {
		this.nextUser = nextUser;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	@Override
	public String toString() {
		return "DriftRecord [drid=" + drid + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", status=" + status + ", preUser="
				+ preUser + ", nextUser=" + nextUser + ", book=" + book + "]";
	}
}
