package com.lusheng.bookcrossing.model;

import java.util.ArrayList;
import java.util.List;

public class Category {
	private Integer cid;
	private String cname;
	private Category parent;
	private List<Category> subCategories=new ArrayList<Category>();
	public List<Category> getSubCategories() {
		return subCategories;
	}
	public void setSubCategories(List<Category> subCategories) {
		this.subCategories = subCategories;
	}
	public Integer getCid() {
		return cid;
	}
	public void setCid(Integer cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
	@Override
	public String toString() {
		return "Category [cid=" + cid + ", cname=" + cname + ", parent="
				+ parent + "]";
	}
}
