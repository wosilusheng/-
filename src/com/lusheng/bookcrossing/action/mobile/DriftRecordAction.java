package com.lusheng.bookcrossing.action.mobile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lusheng.bookcrossing.model.Book;
import com.lusheng.bookcrossing.model.DriftRecord;
import com.lusheng.bookcrossing.model.User;
import com.lusheng.bookcrossing.service.BookService;
import com.lusheng.bookcrossing.service.DriftRecordService;
import com.lusheng.bookcrossing.uitls.Config;
import com.lusheng.bookcrossing.uitls.JsonUtils;
import com.lusheng.bookcrossing.uitls.ParameterUtils;
@Controller
@RequestMapping("mobile")
public class DriftRecordAction {
	@Autowired
	private BookService bookService;
	@Autowired
	private DriftRecordService driftRecordService;
	@ResponseBody
	@RequestMapping("/lookDriftRecordByBid")
	public String lookDriftRecordByBid(HttpServletRequest request){
		Integer bid=ParameterUtils.getIntNoCareException(request, "bid", -1);
		int pageNum=ParameterUtils.getIntNoCareException(request, "pageNum", 1);
		int pageSize=ParameterUtils.getIntNoCareException(request, "pageSize", 10);
		if(bid==-1){
			return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "bid格式不对");
		}
		Book book=bookService.getEntity(bid);
		if(book==null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "不存在此漂书");
		}
		int count=driftRecordService.getDriftRecordsCountByBid(bid);
		pageSize=pageSize==0?10:pageSize;
		int pageCount=(count-1)/pageSize+1;
		List<DriftRecord> driftRecords=driftRecordService.getDriftRecordsByBidHasPage(bid, pageNum, pageSize);
		Map<String,Object> map=new LinkedHashMap<String, Object>();
		map.put("pageNum", pageNum);
		map.put("pageCount", pageCount);
		map.put("driftRecords", wrapDriftRecords(driftRecords));
		return JsonUtils.getSuccessMsg(map);
	}
	@ResponseBody
	@RequestMapping("/lookMyDriftRecord")
	public String lookMyDriftRecord(HttpServletRequest request){
		Integer uid=(Integer) request.getAttribute("uid");
		int pageNum=ParameterUtils.getIntNoCareException(request, "pageNum", 1);
		int pageSize=ParameterUtils.getIntNoCareException(request, "pageSize", 10);
		int count=driftRecordService.getDriftRecordsCountByUid(uid);
		pageSize=pageSize==0?10:pageSize;
		int pageCount=(count-1)/pageSize+1;
		List<DriftRecord> driftRecords=driftRecordService.getDriftRecordsByUidHasPageHasPage(uid, pageNum, pageSize);
		Map<String,Object> map=new LinkedHashMap<String, Object>();
		map.put("pageNum", pageNum);
		map.put("pageCount", pageCount);
		map.put("driftRecords", wrapDriftRecords(driftRecords));
		return JsonUtils.getSuccessMsg(map);
	}
	private List<DriftRecordWrapper> wrapDriftRecords(List<DriftRecord> driftRecords){
		List<DriftRecordWrapper> driftRecordWrappers=new ArrayList<DriftRecordAction.DriftRecordWrapper>();
		for(DriftRecord driftRecord:driftRecords){
			driftRecordWrappers.add(new DriftRecordWrapper(driftRecord));
		}
		return driftRecordWrappers;
	}
	private static class DriftRecordWrapper extends DriftRecord{
		public Integer preUid;
		public String preUsername;
		public Integer nextUid;
		public String nextUsername;
		public Integer bid;
		public String bookname;
		public DriftRecordWrapper(DriftRecord driftRecord){
			setDrid(driftRecord.getDrid());
			setStartTime(driftRecord.getStartTime());
			setEndTime(driftRecord.getEndTime());
			setStatus(driftRecord.getStatus());
			User preUser=driftRecord.getPreUser();
			if(preUser!=null){
				preUid=preUser.getUid();
				preUsername=preUser.getUsername();
			}
			User nextUser=driftRecord.getNextUser();
			if(nextUser!=null){
				nextUid=nextUser.getUid();
				nextUsername=nextUser.getUsername();
			}
			Book book = driftRecord.getBook();
			if(book!=null){
				bid = book.getBid();
				bookname=book.getBookname();
			}
		}
	}
	
}
