package com.lusheng.bookcrossing.model;

public class Manager {
	private Integer mid;
	private String accountNumber;
	private String password;
	public Integer getMid() {
		return mid;
	}
	public void setMid(Integer mid) {
		this.mid = mid;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "Manager [mid=" + mid + ", accountNumber=" + accountNumber
				+ ", password=" + password + "]";
	}
}
