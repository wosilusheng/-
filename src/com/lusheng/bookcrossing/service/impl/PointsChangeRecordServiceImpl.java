package com.lusheng.bookcrossing.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lusheng.bookcrossing.dao.BaseDao;
import com.lusheng.bookcrossing.model.PointsChangeRecord;
import com.lusheng.bookcrossing.service.PointsChangeRecordService;
@Service("pointsChangeRecordService")
public class PointsChangeRecordServiceImpl extends BaseServiceImpl<PointsChangeRecord> implements PointsChangeRecordService{

	@Resource(name="pointsChangeRecordDao")
	@Override
	public void setDao(BaseDao<PointsChangeRecord> dao) {
		super.setDao(dao);
	}
	@Override
	public List<PointsChangeRecord> getPointsChangeRecordsByUidHasPage(
			Integer uid, int pageNum, int pageSize) {
		String hql="from PointsChangeRecord p where p.user.uid =? order by p.time desc";
		return this.findEntityHasPageByHQL(hql, (pageNum-1)*pageSize, pageSize, uid);
	}
	@Override
	public int getPointsChangeRecordsCountByUid(Integer uid) {
		String hql="select count(*) from PointsChangeRecord p where p.user.uid =?";
		return (int)(long) this.uniqueResult(hql, uid);
	}

}
