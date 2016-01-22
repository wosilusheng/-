package com.lusheng.bookcrossing.model;

public class User {
	public static final String PASSWORD_OF_RESET="111111";
	public static final int ACCURATE_SEARCH_TPYE=1;
	public static final int VAGUE_SEARCH_TPYE=2;
	private Integer uid;
	private String username;//用户名
	private String password;//密码
	private String emailAddress;//邮箱地址
	private String phoneNumber;//电话号码
	private String sex;//性别
	private String signature;//个性签名
	private String avatarPath;//头像路径
	private int level;//等级
	private int points;//积分数
	private String token;//手机端令牌
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getAvatarPath() {
		return avatarPath;
	}
	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	@Override
	public String toString() {
		return "User [uid=" + uid + ", username=" + username + ", password="
				+ password + ", emailAddress=" + emailAddress
				+ ", phoneNumber=" + phoneNumber + ", sex=" + sex
				+ ", signature=" + signature + ", avatarPath=" + avatarPath
				+ ", level=" + level + ", points=" + points + ", token="
				+ token + "]";
	}
}
