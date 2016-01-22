package com.lusheng.bookcrossing.action.mobile;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lusheng.bookcrossing.model.Book;
import com.lusheng.bookcrossing.model.PraiseTrampleRecord;
import com.lusheng.bookcrossing.model.Review;
import com.lusheng.bookcrossing.model.User;
import com.lusheng.bookcrossing.service.BookService;
import com.lusheng.bookcrossing.service.PraiseTrampleRecordService;
import com.lusheng.bookcrossing.service.ReviewService;
import com.lusheng.bookcrossing.uitls.Config;
import com.lusheng.bookcrossing.uitls.JsonUtils;
import com.lusheng.bookcrossing.uitls.ParameterUtils;
import com.lusheng.bookcrossing.uitls.TextUtils;
/**
 * 书评功能：撰写书评、删除书评、查看书评、点赞书评、点踩书评
 * @author lusheng
 * private Integer rid;
	private String content;//内容
	private Date time;//发表时间
	private int praiseNum;//点赞数
	private int trampleNum;//点踩数
	private Book book;//漂书
	private User user;//评论者
 */
@Controller
@RequestMapping("mobile")
public class BookReviewAction {
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private BookService bookService;
	@Autowired
	private PraiseTrampleRecordService praiseTrampleRecordService;
	@ResponseBody
	@RequestMapping("/writeReview")
	public String writeReview(HttpServletRequest request){
		Integer uid=(Integer) request.getAttribute("uid");
		String content=ParameterUtils.getTrimString(request, "content");
		if(TextUtils.isEmpty(content)){
			return JsonUtils.getErrorMsg(Config.EMPTY_PRAMETERS_ERROR_CODE, "评论内容不能为空");
		}
		Integer bid=null;
		try {
			bid=ParameterUtils.getInt(request, "bid");
		} catch (Exception e) {
			return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "漂书id不合法");
		}
		Book book=bookService.getEntity(bid);
		if(book==null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "不存在此漂书id");
		}
		User user=new User();
		user.setUid(uid);
		Review review=new Review();
		review.setContent(content);
		review.setTime(new Date());
		review.setBook(book);
		review.setUser(user);
		try {
			reviewService.saveEntity(review);
			return JsonUtils.getSuccessMsg(null);
		} catch (Exception e) {
			return JsonUtils.getServerBugErrorMsg();
		}
	}
	@ResponseBody
	@RequestMapping("/lookReviewByBid")
	public String lookReviewByBid(HttpServletRequest request){
		Integer bid=null;
		try {
			bid=ParameterUtils.getInt(request, "bid");
		} catch (Exception e) {
			return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "bid格式不对");
		}
		List<Review> reviews = reviewService.getReviewsByBid(bid);
		return JsonUtils.getSuccessMsg(wrapperReviews(reviews));
	}
	
	@ResponseBody
	@RequestMapping("/lookMyReviews")
	public String lookMyReviews(HttpServletRequest request){
		Integer uid=(Integer) request.getAttribute("uid");
		int pageNum=ParameterUtils.getIntNoCareException(request, "pageNum", 1);
		int pageSize=ParameterUtils.getIntNoCareException(request, "pageSize", 10);
		pageSize=pageSize==0?10:pageSize;
		List<Review> reviews = reviewService.getReviewsByUidHasPage(uid, pageNum, pageSize);
		int count=reviewService.getReviewsCountByUid(uid);
		int pageCount=(count-1)/pageSize+1;
		Map<String,Object> map=new LinkedHashMap<String,Object>();
		map.put("pageNum", pageNum);
		map.put("pageCount",pageCount);
		map.put("reviews", wrapperReviews(reviews));
		return JsonUtils.getSuccessMsg(map);
	}
	@ResponseBody
	@RequestMapping("/deleteReview")
	public String deleteReview(HttpServletRequest request){
		Integer uid=(Integer) request.getAttribute("uid");
		Integer rid=null;
		try {
			rid=ParameterUtils.getInt(request, "rid");
		} catch (Exception e) {
			return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "rid格式不对");
		}
		Review review = reviewService.getEntity(rid);
		if(review==null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "不存在此书评");
		}
		Integer tempUid=review.getUser().getUid();
		if(!uid.equals(tempUid)){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "无权限删除此书评");
		}
		try {
			reviewService.deleteReviewByRid(rid);
			return JsonUtils.getSuccessMsg(null);
		} catch (Exception e) {
			return JsonUtils.getServerBugErrorMsg();
		}
	}
	
	@ResponseBody
	@RequestMapping("/praiseReview")
	public String praiseReview(HttpServletRequest request){
		return praiseOrTrampleReview(request, true);
	}
	@ResponseBody
	@RequestMapping("/trampleReview")
	public String trampleReview(HttpServletRequest request){
		return praiseOrTrampleReview(request, false);
	}
	
	private String praiseOrTrampleReview(HttpServletRequest request,boolean isPraise){
		Integer uid=(Integer) request.getAttribute("uid");
		Integer rid=null;
		try {
			rid=ParameterUtils.getInt(request, "rid");
		} catch (Exception e) {
			return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "rid格式不对");
		}
		Review review=reviewService.getEntity(rid);
		if(review==null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "不存在此书评");
		}
		if(review.getUser().getUid().equals(uid)){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "不允许对自己写的书评点踩点赞");
		}
		PraiseTrampleRecord praiseTrampleRecord = praiseTrampleRecordService.getPraiseTrampleRecordByUidAndRid(uid, rid);
		if(praiseTrampleRecord!=null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "您已经对此书评点过踩或者点过赞");
		}
		try {
			if(isPraise){
				reviewService.praiseReview(rid, uid);
			}else{
				reviewService.trampleReview(rid, uid);
			}
			return JsonUtils.getSuccessMsg(null);
		} catch (Exception e) {
			return JsonUtils.getServerBugErrorMsg();
		}
	}
	private List<ReviewWrapper> wrapperReviews(List<Review> reviews){
		List<ReviewWrapper> reviewWrappers=new ArrayList<BookReviewAction.ReviewWrapper>();
		for(Review review:reviews){
			reviewWrappers.add(new ReviewWrapper(review));
		}
		return reviewWrappers;
	}
	private static class ReviewWrapper extends Review{
		public String username;
		public Integer uid;
		public String bookname;
		public Integer bid;
		public ReviewWrapper(Review review) {
			setContent(review.getContent());
			setPraiseNum(review.getPraiseNum());
			setTrampleNum(review.getTrampleNum());
			setRid(review.getRid());
			setTime(review.getTime());
			username=review.getUser().getUsername();
			uid=review.getUser().getUid();
			bookname=review.getBook().getBookname();
			bid=review.getBook().getBid();
		}
	}
}
