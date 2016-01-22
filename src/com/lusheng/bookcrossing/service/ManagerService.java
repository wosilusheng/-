package com.lusheng.bookcrossing.service;

import com.lusheng.bookcrossing.model.Manager;

public interface ManagerService extends BaseService<Manager>{
	Manager login(String accountNumber,String password);
	void changePassword(Integer mid,String newPwd);
}
