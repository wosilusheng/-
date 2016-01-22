package com.lusheng.bookcrossing.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lusheng.bookcrossing.dao.BaseDao;
import com.lusheng.bookcrossing.model.Manager;
import com.lusheng.bookcrossing.service.ManagerService;
@Service("managerService")
public class ManagerServiceImpl extends BaseServiceImpl<Manager> implements ManagerService{
	@Resource(name="managerDao")
	@Override
	public void setDao(BaseDao<Manager> dao) {
		super.setDao(dao);
	}
	@Override
	public Manager login(String accountNumber, String password) {
		String hql="from Manager m where m.accountNumber=? and m.password=?";
		return (Manager) this.uniqueResult(hql, accountNumber,password);
	}

	@Override
	public void changePassword(Integer mid, String newPwd) {
		String hql="update Manager m set m.password=? where m.mid=?";
		this.batchEntityByHQL(hql, newPwd,mid);
	}

}
