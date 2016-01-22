package com.lusheng.bookcrossing.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lusheng.bookcrossing.dao.BaseDao;
import com.lusheng.bookcrossing.model.Category;
import com.lusheng.bookcrossing.service.CategoryService;

@Service("categoryService")
public class CategoryServiceImpl extends BaseServiceImpl<Category> implements
		CategoryService {

	@Resource(name = "categoryDao")
	@Override
	public void setDao(BaseDao<Category> dao) {
		super.setDao(dao);
	}

	@Override
	public List<Category> getFirstLevelCategories() {
		String hql = "from Category c where c.parent.cid is null";
		return this.findEntityByHQL(hql);
	}

	@Override
	public List<Category> getSecondLevelCategories(Integer pid) {
		String hql = "from Category c where c.parent.cid =?";
		return this.findEntityByHQL(hql, pid);
	}

	@Override
	public void deleteCategory(Integer cid) {
		Category category = new Category();
		category.setCid(cid);
		this.deleteEntity(category);
	}

	@Override
	public void addFirstLevelCategory(String cname) {
		Category category = new Category();
		category.setCname(cname);
		this.saveEntity(category);
	}

	@Override
	public void addSecondLevelCategory(String cname, Integer pid) {
		String sql = "insert into category (cname,pid) values (?,?)";
		this.executeSQL(sql, cname, pid);
	}

	@Override
	public void updateFirstLevelCategory(String cname, Integer cid) {
		Category category = new Category();
		category.setCname(cname);
		category.setCid(cid);
		this.updateEntity(category);
	}

	@Override
	public void updateSecondLevelCategory(String cname, Integer pid, Integer cid) {
		String hql = "update Category c set c.cname=?,c.parent.cid=? where c.cid=?";
		this.batchEntityByHQL(hql, cname, pid, cid);
	}
}
