package com.lusheng.bookcrossing.service;

import java.util.List;

import com.lusheng.bookcrossing.model.DriftRecord;

public interface DriftRecordService extends BaseService<DriftRecord>{
	DriftRecord getDriftingRecordByBid(Integer bid);
	DriftRecord getLastDriftedRecordByBid(Integer bid);
	List<DriftRecord> getDriftRecordsByBidHasPage(Integer bid,int pageNum,int pageSize);
	int getDriftRecordsCountByBid(Integer bid);
	List<DriftRecord> getDriftRecordsByUidHasPageHasPage(Integer uid,int pageNum,int pageSize);
	int getDriftRecordsCountByUid(Integer uid);
}
