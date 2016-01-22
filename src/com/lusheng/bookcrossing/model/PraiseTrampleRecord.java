package com.lusheng.bookcrossing.model;

public class PraiseTrampleRecord {
	public static final int PRAISE_TYPE=1;
	public static final int TRAMPLE_TYPE=2;
	private Integer ptid;
	private int type;//类型(点赞还是点踩)
	private Review review;
	private User user;
	public Integer getPtid() {
		return ptid;
	}
	public void setPtid(Integer ptid) {
		this.ptid = ptid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Review getReview() {
		return review;
	}
	public void setReview(Review review) {
		this.review = review;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "PraiseTrampleRecord [ptid=" + ptid + ", type=" + type
				+ ", review=" + review + ", user=" + user + "]";
	}

}
