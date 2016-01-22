package com.lusheng.bookcrossing.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lusheng.bookcrossing.dao.BaseDao;
import com.lusheng.bookcrossing.model.PraiseTrampleRecord;
import com.lusheng.bookcrossing.service.PraiseTrampleRecordService;
@Service("praiseTrampleRecordService")
public class PraiseTrampleRecordServiceImpl extends BaseServiceImpl<PraiseTrampleRecord> implements PraiseTrampleRecordService{

	@Resource(name="praiseTrampleRecordDao")
	@Override
	public void setDao(BaseDao<PraiseTrampleRecord> dao) {
		super.setDao(dao);
	}
	@Override
	public PraiseTrampleRecord getPraiseTrampleRecordByUidAndRid(Integer uid,
			Integer rid) {
		String hql="from PraiseTrampleRecord p where p.user.uid=? and p.review.rid=?";
		return (PraiseTrampleRecord) this.uniqueResult(hql, uid,rid);
	}
}
