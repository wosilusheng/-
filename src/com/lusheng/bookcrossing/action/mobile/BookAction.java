package com.lusheng.bookcrossing.action.mobile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.lusheng.bookcrossing.model.Book;
import com.lusheng.bookcrossing.model.Category;
import com.lusheng.bookcrossing.model.DriftRecord;
import com.lusheng.bookcrossing.model.User;
import com.lusheng.bookcrossing.service.BookService;
import com.lusheng.bookcrossing.service.CategoryService;
import com.lusheng.bookcrossing.service.DriftRecordService;
import com.lusheng.bookcrossing.service.UserService;
import com.lusheng.bookcrossing.uitls.BookcrossingUtils;
import com.lusheng.bookcrossing.uitls.Config;
import com.lusheng.bookcrossing.uitls.JsonUtils;
import com.lusheng.bookcrossing.uitls.ParameterUtils;
import com.lusheng.bookcrossing.uitls.TextUtils;
import com.lusheng.bookcrossing.uitls.UUIDUtils;
/**
 * 漂书功能：搜索漂书、查看漂书信息、发布漂书信息、修改漂书信息、请求漂书、漂流图书
 * @author lusheng
 */
@Controller
@RequestMapping("mobile")
public class BookAction {
	@Autowired
	private BookService bookService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private UserService userService;
	@Autowired
	private DriftRecordService driftRecordService;
	@ResponseBody
	@RequestMapping("/publishBook")
	public String publishBook(HttpServletRequest request){
		Integer uid=(Integer) request.getAttribute("uid");
		String bookname=ParameterUtils.getTrimString(request, "bookname");
		if(TextUtils.isEmpty(bookname)){
			return JsonUtils.getErrorMsg(Config.EMPTY_PRAMETERS_ERROR_CODE, "书名不能为空");
		}
		String author=ParameterUtils.getTrimString(request, "author");
		if(TextUtils.isEmpty(author)){
			return JsonUtils.getErrorMsg(Config.EMPTY_PRAMETERS_ERROR_CODE, "作者不能为空");
		}
		String press=ParameterUtils.getTrimString(request, "press");
		if(TextUtils.isEmpty(press)){
			return JsonUtils.getErrorMsg(Config.EMPTY_PRAMETERS_ERROR_CODE, "出版社不能为空");
		}
		long publishTime=ParameterUtils.getLongNoCareException(request, "publishTime", -1);
		if(publishTime==-1){
			return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "出版时间格式不对");
		}
		Book book=new Book();
		book.setBookname(bookname);
		book.setAuthor(author);
		book.setPress(press);
		book.setPublishTime(new Date(publishTime));
		book.setShareTime(new Date());
		book.setStatus(Book.NO_AUDIT);
		User user=new User();
		user.setUid(uid);
		book.setShareUser(user);
		book.setBookcrossingId(UUIDUtils.randomUUID());
		try {
			bookService.saveEntity(book);
			return JsonUtils.getSuccessMsg(new BookWrapper(book));
		} catch (Exception e) {
			return JsonUtils.getServerBugErrorMsg();
		}
	}
	@ResponseBody
	@RequestMapping("/getMyPublishBook")
	public String getMyPublishBook(HttpServletRequest request){
		Integer uid=(Integer) request.getAttribute("uid");
		int pageNum=ParameterUtils.getIntNoCareException(request, "pageNum", 1);
		int pageSize=ParameterUtils.getIntNoCareException(request, "pageSize", 10);
		int status=ParameterUtils.getIntNoCareException(request, "status", Book.NO_AUDIT);
		pageSize=pageSize==0?10:pageSize;
		List<Book> books=bookService.getBooksByStatusAndSuidHasPage(uid, status, pageNum, pageSize);
		int count=bookService.getBookCountByStatusAndSuid(uid, status);
		int pageCount=(count-1)/pageSize+1;
		Map<String,Object> map=new LinkedHashMap<String, Object>();
		map.put("pageNum", pageNum);
		map.put("pageCount", pageCount);
		map.put("books", wrapBooks(books));
		return JsonUtils.getSuccessMsg(map);
	}
	@ResponseBody
	@RequestMapping("/updateBook")
	public String updateBook(HttpServletRequest request){
		Integer uid=(Integer) request.getAttribute("uid");
		Integer bid=ParameterUtils.getIntNoCareException(request, "bid", -1);
		if(bid==-1){
			return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "bid格式不对");
		}
		Book book=bookService.getEntity(bid);
		if(book==null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "不存在此漂书");
		}
		if(book.getStatus()==Book.PASS_STATUS){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "此漂书已通过审核不能再修改");
		}
		if(!book.getShareUser().getUid().equals(uid)){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "不允许修改别人发布的漂书");
		}
		String bookname=ParameterUtils.getTrimString(request, "bookname");
		if(!TextUtils.isEmpty(bookname)){
			book.setBookname(bookname);
		}
		String author=ParameterUtils.getTrimString(request, "author");
		if(!TextUtils.isEmpty(author)){
			book.setAuthor(author);
		}
		String press=ParameterUtils.getTrimString(request, "press");
		if(!TextUtils.isEmpty(press)){
			book.setPress(press);
		}
		long publishTime=ParameterUtils.getLongNoCareException(request, "publishTime", -1);
		if(publishTime==-1){
			book.setPublishTime(new Date(publishTime));
		}
		try {
			bookService.updateEntity(book);
			return JsonUtils.getSuccessMsg(null);
		} catch (Exception e) {
			return JsonUtils.getServerBugErrorMsg();
		}
	}

	@ResponseBody
	@RequestMapping("/changeBookImage")
	public String changeBookImage(HttpServletRequest request){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Integer uid=(Integer) multipartRequest.getAttribute("uid");
		Integer bid=ParameterUtils.getIntNoCareException(request, "bid", -1);
		if(bid==-1){
			return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "bid格式不对");
		}
		Book book=bookService.getEntity(bid);
		if(book==null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "不存在此漂书");
		}
		if(book.getStatus()==Book.PASS_STATUS){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "此漂书已通过审核不能再修改");
		}
		if(!book.getShareUser().getUid().equals(uid)){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "不允许修改别人发布的漂书");
		}
		MultipartFile file = multipartRequest.getFile("file");
		if(file==null||file.getSize()==0){
			return JsonUtils.getErrorMsg(Config.EMPTY_PRAMETERS_ERROR_CODE, "文件不能为空");
		}
		String filename=file.getOriginalFilename().toLowerCase();
		if(!(filename.endsWith(".jpg")||filename.endsWith(".png"))){
			return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "文件格式不对");
		}
		String lastImagePath=book.getImagePath();
		String imagePath=Config.BOOK_BASE_PATH+UUIDUtils.randomUUID()+filename.substring(filename.lastIndexOf("."));
		String path=request.getServletContext().getRealPath(imagePath);
		InputStream is=null;
		FileOutputStream fos=null;
		try {
			is=file.getInputStream();
			fos=new FileOutputStream(new File(path));
			int len=0;
			byte[] buff=new byte[1024];
			while((len=is.read(buff))!=-1){
				fos.write(buff, 0, len);
			}
			fos.flush();
		} catch (Exception e) {
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "文件传输错误");
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		book.setImagePath(imagePath);
		try {
			bookService.updateEntity(book);
			if(lastImagePath!=null){
				File lastFile=new File(request.getServletContext().getRealPath(lastImagePath));
				if(lastFile.exists()){
					lastFile.delete();
				}
			}
			return JsonUtils.getSuccessMsg(new BookWrapper(book));
		} catch (Exception e) {
			return JsonUtils.getServerBugErrorMsg();
		}
	}
	@ResponseBody
	@RequestMapping("/deleteBook")
	public String deleteBook(HttpServletRequest request){
		Integer uid=(Integer) request.getAttribute("uid");
		Integer bid=ParameterUtils.getIntNoCareException(request, "bid", -1);
		if(bid==-1){
			return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "bid格式不对");
		}
		Book book=bookService.getEntity(bid);
		if(book==null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "不存在此漂书");
		}
		if(book.getStatus()==Book.PASS_STATUS){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "此漂书已通过审核不能删除");
		}
		if(!book.getShareUser().getUid().equals(uid)){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "不允许删除别人发布的漂书");
		}
		try {
			bookService.deleteEntity(book);
			return JsonUtils.getSuccessMsg(null);
		} catch (Exception e) {
			return JsonUtils.getServerBugErrorMsg();
		}
	}

	@ResponseBody
	@RequestMapping("/searchBookByBookcrossingId")
	public String searchBookByBookcrossingId(HttpServletRequest request){
		String bookcrossingId=ParameterUtils.getTrimString(request, "bookcrossingId");
		if(TextUtils.isEmpty(bookcrossingId)){
			return JsonUtils.getErrorMsg(Config.EMPTY_PRAMETERS_ERROR_CODE, "漂书号不能为空");
		}
		Book book=bookService.getPassAuditBookByBookcrossingId(bookcrossingId);
		if(book!=null){
			return JsonUtils.getSuccessMsg(new BookWrapper(book));
		}else{
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "搜索结果为空");
		}
	}
	@ResponseBody
	@RequestMapping("/searchBookByBookname")
	public String searchBookByBookname(HttpServletRequest request){
		String bookname=ParameterUtils.getTrimString(request, "bookname");
		if(TextUtils.isEmpty(bookname)){
			return JsonUtils.getErrorMsg(Config.EMPTY_PRAMETERS_ERROR_CODE, "书名不能为空");
		}
		int searchType=ParameterUtils.getIntNoCareException(request, "searchType", Book.ACCURATE_SEARCH_TPYE);
		int pageNum=ParameterUtils.getIntNoCareException(request, "pageNum", 1);
		int pageSize=ParameterUtils.getIntNoCareException(request, "pageSize", 10);
		pageSize=pageSize==0?10:pageSize;
		List<Book> books=bookService.getPassAuditBooksByNameHasPage(bookname, searchType, pageNum, pageSize);
		int count=bookService.getPassAuditBooksCountByName(bookname, searchType);
		int pageCount=(count-1)/pageSize+1;
		Map<String,Object> map=new LinkedHashMap<String, Object>();
		map.put("pageNum", pageNum);
		map.put("pageCount", pageCount);
		map.put("books", wrapBooks(books));
		return JsonUtils.getSuccessMsg(map);
	}
	@ResponseBody
	@RequestMapping("/listBookByCid")
	public String listBookByCid(HttpServletRequest request){
		Integer cid=ParameterUtils.getIntNoCareException(request, "cid", -1);
		if(cid==-1){
			return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "cid格式不对");
		}
		Category category=categoryService.getEntity(cid);
		if(category==null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "不存在此类别");
		}
		if(category.getParent()==null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "不能通过一级分类列出漂书");
		}
		int pageNum=ParameterUtils.getIntNoCareException(request, "pageNum", 1);
		int pageSize=ParameterUtils.getIntNoCareException(request, "pageSize", 10);
		pageSize=pageSize==0?10:pageSize;
		List<Book> books=bookService.getPassAuditBooksByCategoryHasPage(cid, pageNum, pageSize);
		int count=bookService.getPassAuditBooksCountByCategory(cid);
		int pageCount=(count-1)/pageSize+1;
		Map<String,Object> map=new LinkedHashMap<String, Object>();
		map.put("pageNum", pageNum);
		map.put("pageCount", pageCount);
		map.put("books", wrapBooks(books));
		return JsonUtils.getSuccessMsg(map);
	}

	@ResponseBody
	@RequestMapping("/destineBook")
	public String destineBook(HttpServletRequest request){
		Integer uid=(Integer) request.getAttribute("uid");
		Integer bid=ParameterUtils.getIntNoCareException(request, "bid", -1);
		if(bid==-1){
			return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "bid格式不对");
		}
		Book book=bookService.getPassAuditBookByBid(bid);
		if(book==null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "不存在此漂书");
		}
		if(book.getNextUser()!=null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "该漂书已被预订");
		}
		if(book.getCurrUser()!=null){
			if(book.getCurrUser().getUid().equals(bid)){
				return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "你已经是该漂书的当前借阅者，不需要预订");
			}
		}
		if(book.getCurrUser()==null&&book.getShareUser().getUid().equals(uid)){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "这是你分享的漂书，并且还没有开始漂流，你不能预订");
		}
		int hasCount=bookService.getDestineOrBorrowPassAuditBookCountByUid(uid);
		User user=userService.getUserByUid(uid);
		int maxCount=BookcrossingUtils.getCanBorrowOrDestineBookNumByLevel(user.getLevel());
		if(hasCount>=maxCount){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "你最多借阅或预订"+maxCount+"本漂书，目前借阅和预订的漂书数目为"+hasCount+"，不能再预订了");
		}
		try {
			bookService.destinePassAuditBook(bid, uid);
			return JsonUtils.getSuccessMsg(null);
		} catch (Exception e) {
			return JsonUtils.getServerBugErrorMsg();
		}
	}
	@ResponseBody
	@RequestMapping("/cancelDestineBook")
	public String cancelDestineBook(HttpServletRequest request){
		Integer uid=(Integer) request.getAttribute("uid");
		Integer bid=ParameterUtils.getIntNoCareException(request, "bid", -1);
		if(bid==-1){
			return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "bid格式不对");
		}
		Book book=bookService.getPassAuditBookByBid(bid);
		if(book==null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "不存在此漂书");
		}
		if(book.getNextUser()==null||!book.getNextUser().getUid().equals(uid)){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "你没有预订该漂书");
		}
		if(driftRecordService.getDriftingRecordByBid(bid)!=null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "该漂书已经在漂流途中，不能取消预订");
		}
		try {
			bookService.cancelDestineBook(bid);
			return JsonUtils.getSuccessMsg(null);
		} catch (Exception e) {
			return JsonUtils.getServerBugErrorMsg();
		}
	}
	@ResponseBody
	@RequestMapping("/driftingBook")
	public String driftingBook(HttpServletRequest request){
		Integer uid=(Integer) request.getAttribute("uid");
		Integer bid=ParameterUtils.getIntNoCareException(request, "bid", -1);
		if(bid==-1){
			return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "bid格式不对");
		}
		Book book=bookService.getPassAuditBookByBid(bid);
		if(book==null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "不存在此漂书");
		}
		if(book.getNextUser()==null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "该漂书还没有被预定，不能漂流");
		}
		if(driftRecordService.getDriftingRecordByBid(bid)!=null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "该漂书已经在漂流途中，不需要重复漂流");
		}
		//第一次漂书
		if(book.getCurrUser()==null){
			if(!book.getShareUser().getUid().equals(uid)){
				return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "你不是该书的分享者，不能启动第一次漂书");
			}
		}else{
			if(!book.getCurrUser().getUid().equals(uid)){
				return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "你不是该书的当前借阅者，不能启动漂书");
			}
		}
		DriftRecord driftRecord=new DriftRecord();
		driftRecord.setPreUser(book.getCurrUser());
		driftRecord.setNextUser(book.getNextUser());
		driftRecord.setBook(book);
		driftRecord.setStatus(DriftRecord.DRIFTING_STATUS);
		try {
			driftRecordService.saveEntity(driftRecord);
			return JsonUtils.getSuccessMsg(null);
		} catch (Exception e) {
			return JsonUtils.getServerBugErrorMsg();
		}
	}
	@ResponseBody
	@RequestMapping("/confirmObtainBook")
	public String confirmObtainBook(HttpServletRequest request){
		Integer uid=(Integer) request.getAttribute("uid");
		Integer bid=ParameterUtils.getIntNoCareException(request, "bid", -1);
		if(bid==-1){
			return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "bid格式不对");
		}
		Book book=bookService.getPassAuditBookByBid(bid);
		if(book==null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "不存在此漂书");
		}
		User nextUser=book.getNextUser();
		if(nextUser==null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "该漂书还没有被预定，不能确认收到漂书");
		}
		if(!nextUser.getUid().equals(uid)){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "你没有预订该漂书");
		}
		DriftRecord driftRecord=driftRecordService.getDriftingRecordByBid(bid);
		if(driftRecord==null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "该漂书还没漂流");
		}
		DriftRecord lastDriftRecord=driftRecordService.getLastDriftedRecordByBid(bid);
		if(lastDriftRecord!=null){
			lastDriftRecord.setEndTime(new Date());
		}
		driftRecord.setNextUser(nextUser);
		driftRecord.setStartTime(new Date());
		driftRecord.setStatus(DriftRecord.DRIFTED_STATUS);
		boolean bookIsFirstDrift=book.getCurrUser()==null;
		book.setCurrUser(nextUser);
		book.setNextUser(null);
		try {
			bookService.confirmObtainBook(book, driftRecord, bookIsFirstDrift);
			return JsonUtils.getSuccessMsg(null);
		} catch (Exception e) {
			return JsonUtils.getServerBugErrorMsg();
		}
	}
	@ResponseBody
	@RequestMapping("/getMyBorrowOrDestineBook")
	public String getMyBorrowOrDestineBook(HttpServletRequest request){
		Integer uid=(Integer) request.getAttribute("uid");
		User user=userService.getEntity(uid);
		int canBorrowOrDestineBookNum=BookcrossingUtils.getCanBorrowOrDestineBookNumByLevel(user.getLevel());
		List<Book> books=bookService.getPassAuditBooksByCuidAndNuid(uid);
		int hasBorrowOrDestineBookNum=0;
		if(books!=null&&books.size()>0){
			hasBorrowOrDestineBookNum=books.size();
		}
		Map<String,Object> map=new LinkedHashMap<String, Object>();
		map.put("canBorrowBookNum", canBorrowOrDestineBookNum);
		map.put("hasBorrowOrDestineBookNum", hasBorrowOrDestineBookNum);
		map.put("books", wrapBooks(books));
		return JsonUtils.getSuccessMsg(map);
	}
	private List<BookWrapper> wrapBooks(List<Book> books){
		List<BookWrapper> bookWrappers=new ArrayList<BookAction.BookWrapper>();
		for(Book book:books){
			bookWrappers.add(new BookWrapper(book));
		}
		return bookWrappers;
	}

	private static class BookWrapper extends Book{
		public String shareUserName;
		public Integer shareUid;
		public String currUserName;
		public Integer currUid;
		public String nextUserName;
		public Integer nextUid;
		public String categoryName;
		public BookWrapper(Book book){
			setBookname(book.getBookname());
			setAuthor(book.getAuthor());
			setBid(book.getBid());
			setBookcrossingId(book.getBookcrossingId());
			Category category=book.getCategory();
			if(category!=null){
				categoryName=category.getCname();
			}
			setImagePath(book.getImagePath());
			setNopassReason(book.getNopassReason());
			setStatus(book.getStatus());
			setShareTime(book.getShareTime());
			setPublishTime(book.getPublishTime());
			setPress(book.getPress());
			User shareUser=book.getShareUser();
			if(shareUser!=null){
				shareUserName=shareUser.getUsername();
				shareUid=shareUser.getUid();
			}
			User currUser=book.getCurrUser();
			if(currUser!=null){
				currUserName=currUser.getUsername();
				currUid=currUser.getUid();
			}
			User nextUser=book.getNextUser();
			if(nextUser!=null){
				nextUserName=nextUser.getUsername();
				nextUid=nextUser.getUid();
			}
		}
	}
}
