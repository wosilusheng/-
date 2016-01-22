package com.lusheng.bookcrossing.service;

import java.util.List;
import com.lusheng.bookcrossing.model.User;

public interface UserService extends BaseService<User> {
	User getUserByName(String username);
	void addUser(User user);
	List<User> getUsersByPage(int pageNum, int pageSize);
	List<User> getUsersByName(int pageNum,int pageSize,String username,int searchType);
	int getUserCount();
	int getUserCountByName(String username,int searchType);
	void deleteUser(Integer uid);
	void changePassword(Integer uid,String password);
	void resetPassword(Integer uid);
	void register(String username,String password);
	User login(String username,String password);
	Integer getUidByToken(String token);
	User getUserByUid(Integer uid);
	void changePointsByUid(Integer uid,int num,String reason);
}
