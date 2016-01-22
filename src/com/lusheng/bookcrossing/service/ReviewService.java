package com.lusheng.bookcrossing.service;

import java.util.List;

import com.lusheng.bookcrossing.model.Review;

public interface ReviewService extends BaseService<Review>{
	List<Review> getReviewsByBid(Integer bid);
	List<Review> getReviewsByUidHasPage(Integer uid,int pageNum,int pageSize);
	int getReviewsCountByUid(Integer uid);
	void deleteReviewByRid(Integer rid);
	void praiseReview(Integer rid,Integer uid);
	void trampleReview(Integer rid,Integer uid);
}
