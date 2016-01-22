package com.lusheng.bookcrossing.model;

import java.util.Date;

public class PointsChangeRecord {
	private Integer pcrid;
	private Date time;
	private int changeNum;//改变数
	private String reason;//改变原因
	private User user;
	public Integer getPcrid() {
		return pcrid;
	}
	public void setPcrid(Integer pcrid) {
		this.pcrid = pcrid;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getChangeNum() {
		return changeNum;
	}
	public void setChangeNum(int changeNum) {
		this.changeNum = changeNum;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "PointsChangeRecord [pcrid=" + pcrid + ", time=" + time
				+ ", changeNum=" + changeNum + ", reason=" + reason + ", user="
				+ user + "]";
	}
}
