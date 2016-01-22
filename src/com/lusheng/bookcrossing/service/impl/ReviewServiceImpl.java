package com.lusheng.bookcrossing.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lusheng.bookcrossing.dao.BaseDao;
import com.lusheng.bookcrossing.model.PraiseTrampleRecord;
import com.lusheng.bookcrossing.model.Review;
import com.lusheng.bookcrossing.model.User;
import com.lusheng.bookcrossing.service.ReviewService;
import com.lusheng.bookcrossing.service.UserService;
@Service("reviewService")
public class ReviewServiceImpl extends BaseServiceImpl<Review> implements ReviewService{
	@Autowired
	private UserService userService;
	@Resource(name="praiseTrampleRecordDao")
	private BaseDao<PraiseTrampleRecord> praiseTrampleRecordDao;
	@Resource(name="reviewDao")
	@Override
	public void setDao(BaseDao<Review> dao) {
		super.setDao(dao);
	}

	@Override
	public List<Review> getReviewsByBid(Integer bid) {
		String hql="from Review r where r.book.bid=?";
		return this.findEntityByHQL(hql, bid);
	}

	@Override
	public List<Review> getReviewsByUidHasPage(Integer uid, int pageNum,
			int pageSize) {
		String hql="from Review r where r.user.uid=? order by r.time desc";
		return this.findEntityHasPageByHQL(hql, (pageNum-1)*pageSize, pageSize, uid);
	}

	@Override
	public int getReviewsCountByUid(Integer uid) {
		String hql="select count(*) from Review r where r.user.uid=?";
		return (int)(long) this.uniqueResult(hql, uid);
	}

	@Override
	public void deleteReviewByRid(Integer rid) {
		String hql1="delete from PraiseTrampleRecord p where p.review.rid=?";
		this.batchEntityByHQL(hql1, rid);
		String hql2="delete from Review r where r.rid=?";
		this.batchEntityByHQL(hql2, rid);
	}

	@Override
	public void praiseReview(Integer rid, Integer uid) {
		Review review=this.getEntity(rid);
		if(review==null){
			return;
		}
		review.setPraiseNum(review.getPraiseNum()+1);
		if(review.getPraiseNum()==10){
			userService.changePointsByUid(review.getUser().getUid(), 20,"对于漂书"+review.getBook().getBookname()+"的书评得到10个赞");
		}
		PraiseTrampleRecord praiseTrampleRecord=new PraiseTrampleRecord();
		praiseTrampleRecord.setReview(review);
		User user=new User();
		user.setUid(uid);
		praiseTrampleRecord.setUser(user);
		praiseTrampleRecord.setType(PraiseTrampleRecord.PRAISE_TYPE);
		praiseTrampleRecordDao.saveEntity(praiseTrampleRecord);
	}

	@Override
	public void trampleReview(Integer rid, Integer uid) {
		Review review=this.getEntity(rid);
		if(review==null){
			return;
		}
		review.setTrampleNum(review.getTrampleNum()+1);
		if(review.getTrampleNum()==10){
			userService.changePointsByUid(review.getUser().getUid(), -20,"对于漂书"+review.getBook().getBookname()+"的书评得到10个踩");
		}
		PraiseTrampleRecord praiseTrampleRecord=new PraiseTrampleRecord();
		praiseTrampleRecord.setReview(review);
		User user=new User();
		user.setUid(uid);
		praiseTrampleRecord.setUser(user);
		praiseTrampleRecord.setType(PraiseTrampleRecord.TRAMPLE_TYPE);
		praiseTrampleRecordDao.saveEntity(praiseTrampleRecord);
	}
}
