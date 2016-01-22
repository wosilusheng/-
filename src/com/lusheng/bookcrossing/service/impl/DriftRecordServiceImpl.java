package com.lusheng.bookcrossing.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lusheng.bookcrossing.dao.BaseDao;
import com.lusheng.bookcrossing.model.DriftRecord;
import com.lusheng.bookcrossing.service.DriftRecordService;

@Service("driftRecordService")
public class DriftRecordServiceImpl extends BaseServiceImpl<DriftRecord>
		implements DriftRecordService {
	@Resource(name = "driftRecordDao")
	@Override
	public void setDao(BaseDao<DriftRecord> dao) {
		super.setDao(dao);
	}

	@Override
	public DriftRecord getDriftingRecordByBid(Integer bid) {
		String hql = "from DriftRecord d where d.book.bid=? and d.status=?";
		List<DriftRecord> driftRecords = this.findEntityByHQL(hql, bid,
				DriftRecord.DRIFTING_STATUS);
		if (driftRecords == null || driftRecords.size() == 0) {
			return null;
		}
		return driftRecords.get(0);
	}

	@Override
	public DriftRecord getLastDriftedRecordByBid(Integer bid) {
		String hql = "from DriftRecord d where d.book.bid=? and d.status=? order by d.startTime desc";
		List<DriftRecord> driftRecords = this.findEntityByHQL(hql, bid,
				DriftRecord.DRIFTED_STATUS);
		if (driftRecords == null || driftRecords.size() == 0) {
			return null;
		}
		return driftRecords.get(0);
	}

	@Override
	public List<DriftRecord> getDriftRecordsByBidHasPage(Integer bid,
			int pageNum, int pageSize) {
		String hql = "from DriftRecord d where d.book.bid=?";
		return this.findEntityHasPageByHQL(hql, (pageNum - 1) * pageSize,
				pageSize, bid);
	}

	@Override
	public int getDriftRecordsCountByBid(Integer bid) {
		String hql = "select count(*) from DriftRecord d where d.book.bid=?";
		return (int) (long) this.uniqueResult(hql, bid);
	}

	@Override
	public List<DriftRecord> getDriftRecordsByUidHasPageHasPage(Integer uid, int pageNum,
			int pageSize) {
		String hql="from DriftRecord d where d.preUser.uid=? or d.nextUser.uid=?";
		return this.findEntityHasPageByHQL(hql, (pageNum - 1) * pageSize,
				pageSize, uid,uid);
	}

	@Override
	public int getDriftRecordsCountByUid(Integer uid) {
		String hql = "select count(*) from DriftRecord d where d.preUser.uid=? or d.nextUser.uid=?";
		return (int) (long) this.uniqueResult(hql, uid,uid);
	}

}
