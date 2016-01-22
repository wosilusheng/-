package com.lusheng.bookcrossing.service;

import java.util.List;

import com.lusheng.bookcrossing.model.Category;

public interface CategoryService extends BaseService<Category>{
	List<Category> getFirstLevelCategories();
	List<Category> getSecondLevelCategories(Integer pid);
	void addFirstLevelCategory(String cname);
	void addSecondLevelCategory(String cname,Integer pid);
	void deleteCategory(Integer cid);
	void updateFirstLevelCategory(String cname,Integer cid);
	void updateSecondLevelCategory(String cname,Integer pid,Integer cid);
}
