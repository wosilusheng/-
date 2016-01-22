package com.lusheng.bookcrossing.model;

import java.util.Date;

public class Review {
	private Integer rid;
	private String content;//内容
	private Date time;//发表时间
	private int praiseNum;//点赞数
	private int trampleNum;//点踩数
	private Book book;//漂书
	private User user;//评论者
	public Integer getRid() {
		return rid;
	}
	public void setRid(Integer rid) {
		this.rid = rid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getPraiseNum() {
		return praiseNum;
	}
	public void setPraiseNum(int praiseNum) {
		this.praiseNum = praiseNum;
	}
	public int getTrampleNum() {
		return trampleNum;
	}
	public void setTrampleNum(int trampleNum) {
		this.trampleNum = trampleNum;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "Review [rid=" + rid + ", content=" + content + ", time=" + time
				+ ", praiseNum=" + praiseNum + ", trampleNum=" + trampleNum
				+ ", book=" + book + ", user=" + user + "]";
	}
}
