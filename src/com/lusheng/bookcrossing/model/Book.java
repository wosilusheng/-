package com.lusheng.bookcrossing.model;

import java.util.Date;

public class Book {
	public static int NO_AUDIT=0;//还没有审核
	public static int NO_PASS_STATUS=1;//未通过审核
	public static int PASS_STATUS=2;//通过审核
	public static final int ACCURATE_SEARCH_TPYE=1;//精确搜索
	public static final int VAGUE_SEARCH_TPYE=2;
	public static final int SEARCH_BOOKCROSSINGID_FIELD_CODE=1;
	public static final int SEARCH_BOOKNAME_FIELD_CODE=2;
	private Integer bid;
	private String bookcrossingId;//漂书号
	private String bookname;
	private String author;
	private String imagePath;
	private String press;//出版社
	private Date publishTime;//出版时间
	private Date shareTime;//分享时间
	private User shareUser;//分享者
	private User currUser;//当前借阅者
	private User nextUser;//预订漂书者
	private int status;//审核状态
	private String nopassReason;//审核不通过原因
	private Category category;
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Integer getBid() {
		return bid;
	}
	public void setBid(Integer bid) {
		this.bid = bid;
	}
	public String getBookname() {
		return bookname;
	}
	public void setBookname(String bookname) {
		this.bookname = bookname;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getPress() {
		return press;
	}
	public void setPress(String press) {
		this.press = press;
	}
	public Date getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
	public Date getShareTime() {
		return shareTime;
	}
	public void setShareTime(Date shareTime) {
		this.shareTime = shareTime;
	}
	public User getShareUser() {
		return shareUser;
	}
	public void setShareUser(User shareUser) {
		this.shareUser = shareUser;
	}
	public User getCurrUser() {
		return currUser;
	}
	public void setCurrUser(User currUser) {
		this.currUser = currUser;
	}
	public User getNextUser() {
		return nextUser;
	}
	public void setNextUser(User nextUser) {
		this.nextUser = nextUser;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getBookcrossingId() {
		return bookcrossingId;
	}
	public void setBookcrossingId(String bookcrossingId) {
		this.bookcrossingId = bookcrossingId;
	}
	public String getNopassReason() {
		return nopassReason;
	}
	public void setNopassReason(String nopassReason) {
		this.nopassReason = nopassReason;
	}
	@Override
	public String toString() {
		return "Book [bid=" + bid + ", bookcrossingId=" + bookcrossingId
				+ ", bookname=" + bookname + ", author=" + author
				+ ", imagePath=" + imagePath + ", press=" + press
				+ ", publishTime=" + publishTime + ", shareTime=" + shareTime
				+ ", shareUser=" + shareUser + ", currUser=" + currUser
				+ ", nextUser=" + nextUser + ", status=" + status
				+ ", nopassReason=" + nopassReason + ", category=" + category
				+ "]";
	}
}
