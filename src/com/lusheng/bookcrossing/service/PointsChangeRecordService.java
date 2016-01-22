package com.lusheng.bookcrossing.service;

import java.util.List;

import com.lusheng.bookcrossing.model.PointsChangeRecord;

public interface PointsChangeRecordService extends BaseService<PointsChangeRecord>{
	List<PointsChangeRecord> getPointsChangeRecordsByUidHasPage(Integer uid,int pageNum,int pageSize);
	int getPointsChangeRecordsCountByUid(Integer uid);
}
