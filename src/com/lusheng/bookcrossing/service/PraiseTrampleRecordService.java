package com.lusheng.bookcrossing.service;

import com.lusheng.bookcrossing.model.PraiseTrampleRecord;

public interface PraiseTrampleRecordService extends BaseService<PraiseTrampleRecord>{
	PraiseTrampleRecord getPraiseTrampleRecordByUidAndRid(Integer uid,Integer rid);
}
