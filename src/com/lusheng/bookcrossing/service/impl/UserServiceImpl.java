package com.lusheng.bookcrossing.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lusheng.bookcrossing.dao.BaseDao;
import com.lusheng.bookcrossing.model.PointsChangeRecord;
import com.lusheng.bookcrossing.model.User;
import com.lusheng.bookcrossing.service.UserService;
import com.lusheng.bookcrossing.uitls.BookcrossingUtils;
import com.lusheng.bookcrossing.uitls.UUIDUtils;
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService{
	@Resource(name="pointsChangeRecordDao")
	private BaseDao<PointsChangeRecord> pointsChangeRecordDao;
	@Resource(name="userDao")
	@Override
	public void setDao(BaseDao<User> dao) {
		super.setDao(dao);
	}
	@Override
	public User getUserByName(String username) {
		String hql="from User u where u.username=?";
		return (User) this.uniqueResult(hql, username);
	}
	@Override
	public void addUser(User user) {
		user.setLevel(1);
		user.setPoints(50);
		user.setToken(UUIDUtils.randomUUID());
		this.saveEntity(user);
		PointsChangeRecord pointsChangeRecord=new PointsChangeRecord();
		pointsChangeRecord.setChangeNum(50);
		pointsChangeRecord.setReason("新注册用户送50积分");
		pointsChangeRecord.setUser(user);
		pointsChangeRecord.setTime(new Date());
		pointsChangeRecordDao.saveEntity(pointsChangeRecord);
	}
	@Override
	public List<User> getUsersByPage(int pageNum, int pageSize) {
		String hql="from User";
		return this.findEntityHasPageByHQL(hql, (pageNum-1)*pageSize, pageSize);
	}
	@Override
	public List<User> getUsersByName(int pageNum, int pageSize,
			String username, int searchType) {
		String hql;
		if(searchType==User.ACCURATE_SEARCH_TPYE){
			hql="from User u where u.username=?";
			return this.findEntityHasPageByHQL(hql, (pageNum-1)*pageSize, pageSize,username);
		}else{
			hql="from User u where u.username like ?";
			return this.findEntityHasPageByHQL(hql, (pageNum-1)*pageSize, pageSize,"%"+username+"%");
		}
	}
	@Override
	public int getUserCount() {
		String hql="select count(*) from User";
		return (int)(long)this.uniqueResult(hql);
	}
	@Override
	public int getUserCountByName(String username, int searchType) {
		String hql;
		if(searchType==User.ACCURATE_SEARCH_TPYE){
			hql="select count(*) from User u where u.username=?";
			return (int)(long)this.uniqueResult(hql,username);
		}else{
			hql="select count(*) from User u where u.username like ?";
			return (int)(long)this.uniqueResult(hql,"%"+username+"%");
		}
	}
	@Override
	public void deleteUser(Integer uid) {
		String hql1="delete PointsChangeRecord p where p.user.uid=?";
		pointsChangeRecordDao.batchEntityByHQL(hql1, uid);
		String hql2="delete User u where u.uid=?";
		this.batchEntityByHQL(hql2, uid);
	}
	@Override
	public void changePassword(Integer uid, String password) {
		String hql="update User u set u.password=? where uid=?";
		this.batchEntityByHQL(hql, password,uid);
	}
	@Override
	public void resetPassword(Integer uid) {
		this.changePassword(uid, User.PASSWORD_OF_RESET);
	}
	@Override
	public void register(String username, String password) {
		User user=new User();
		user.setUsername(username);
		user.setPassword(password);
		this.addUser(user);
	}
	@Override
	public User login(String username, String password) {
		String hql="from User u where u.username=? and u.password=?";
		return (User) this.uniqueResult(hql, username,password);
	}
	@Override
	public Integer getUidByToken(String token) {
		String hql="select u.uid from User u where u.token=?";
		return (Integer) this.uniqueResult(hql, token);
	}
	@Override
	public User getUserByUid(Integer uid) {
		String hql="from User u where u.uid=?";
		return (User) this.uniqueResult(hql, uid);
	}
	@Override
	public void changePointsByUid(Integer uid,int num,String reason) {
		User user = this.getEntity(uid);
		if(user==null){
			return;
		}
		int points=user.getPoints()+num;
		user.setPoints(points<0?0:points);
		user.setLevel(BookcrossingUtils.getLevelByPoints(user.getPoints()));
		this.updateEntity(user);
		PointsChangeRecord pointsChangeRecord=new PointsChangeRecord();
		pointsChangeRecord.setChangeNum(num);
		pointsChangeRecord.setReason(reason);
		pointsChangeRecord.setUser(user);
		pointsChangeRecord.setTime(new Date());
		pointsChangeRecordDao.saveEntity(pointsChangeRecord);
	}
	
}
