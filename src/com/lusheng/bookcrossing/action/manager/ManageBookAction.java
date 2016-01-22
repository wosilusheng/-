package com.lusheng.bookcrossing.action.manager;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lusheng.bookcrossing.model.Book;
import com.lusheng.bookcrossing.model.Category;
import com.lusheng.bookcrossing.model.User;
import com.lusheng.bookcrossing.service.BookService;
import com.lusheng.bookcrossing.service.CategoryService;
import com.lusheng.bookcrossing.uitls.ParameterUtils;
import com.lusheng.bookcrossing.uitls.TextUtils;
import com.lusheng.bookcrossing.uitls.UUIDUtils;

@RequestMapping("manager")
@Controller
public class ManageBookAction implements IManagerAction{
	private static final int PAGE_SIZE = 5;
	@Autowired
	private BookService bookService;
	@Autowired
	private CategoryService categoryService;
	@RequestMapping("/shareBook")
	public void shareBook(HttpServletRequest request){
		Book book;
		User user=new User();
		user.setUid(22);
		String imagePath="/file/image/avatar/test.jpg";
		for(int i=0;i<100;i++){
			book=new Book();
			book.setAuthor("作者"+i);
			book.setBookcrossingId(UUIDUtils.randomUUID());
			book.setBookname("书名"+i);
			book.setShareUser(user);
			book.setImagePath(imagePath);
			book.setPress("出版社"+i);
			book.setPublishTime(new Date(System.currentTimeMillis()-i*1000*60*60*24));
			book.setShareTime(new Date(System.currentTimeMillis()-i*1000000));
			book.setStatus(Book.NO_AUDIT);
			bookService.saveEntity(book);
		}
	}
	@RequestMapping("/auditBook")
	public String auditBook(HttpServletRequest request){
		int status=ParameterUtils.getIntNoCareException(request, "status", Book.NO_AUDIT);
		int pageNum=ParameterUtils.getIntNoCareException(request, "pageNum", 1);
		List<Book> books=bookService.getBooksByStatusHasPage(status, pageNum, PAGE_SIZE);
		int bookCount=bookService.getBookCountByStatus(status);
		int pageCount=(bookCount-1)/PAGE_SIZE+1;
		request.setAttribute("status", status);
		request.setAttribute("books", books);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("pageCount", pageCount);
		if(status==Book.NO_AUDIT){
			List<Category> categories = categoryService.getFirstLevelCategories();
			request.setAttribute("categories", categories);
		}
		return BASE_PATH+"/auditBook";
	}
	@RequestMapping("/auditPass")
	public void auditPass(HttpServletRequest request,PrintWriter out){
		int bid;
		int cid;
		try {
			bid=ParameterUtils.getInt(request, "bid");
			cid=ParameterUtils.getInt(request, "cid");
		} catch (Exception e) {
			out.write(ERROR_CODE);
			out.flush();
			return;
		}
		try {
			bookService.auditPass(bid, cid);
			out.write(SUCCESS_CODE);
		} catch (Exception e) {
			out.write(ERROR_CODE);
		}
		out.flush();
	}
	@RequestMapping("/auditNoPass")
	public void auditNoPass(HttpServletRequest request,PrintWriter out){
		String nopassReason=ParameterUtils.getTrimString(request, "nopassReason");
		int bid;
		try {
			bid=ParameterUtils.getInt(request, "bid");
		} catch (Exception e) {
			out.write(ERROR_CODE);
			out.flush();
			return;
		}
		try {
			bookService.auditNoPass(bid, nopassReason);
			out.write(SUCCESS_CODE);
		} catch (Exception e) {
			out.write(ERROR_CODE);
		}
		out.flush();
	}
	
	@RequestMapping("/changeCategory")
	public void changeCategory(HttpServletRequest request,PrintWriter out){
		int bid;
		int cid;
		try {
			bid=ParameterUtils.getInt(request, "bid");
			cid=ParameterUtils.getInt(request, "cid");
		} catch (Exception e) {
			out.write(ERROR_CODE);
			out.flush();
			return;
		}
		try {
			bookService.changeCategory(bid, cid);
			out.write(SUCCESS_CODE);
		} catch (Exception e) {
			out.write(ERROR_CODE);
		}
		out.flush();
	}
	@RequestMapping("/searchBook")
	public String searchBook(HttpServletRequest request){
		String searchContent=ParameterUtils.getTrimString(request, "searchContent");
		int searchFieldCode=ParameterUtils.getIntNoCareException(request, "searchFieldCode", Book.SEARCH_BOOKCROSSINGID_FIELD_CODE);
		int searchType=ParameterUtils.getIntNoCareException(request, "searchType", Book.ACCURATE_SEARCH_TPYE);
		int pageNum=ParameterUtils.getIntNoCareException(request, "pageNum", 1);
		List<Book> books=new ArrayList<Book>();
		int pageCount = 0;
		if(!TextUtils.isEmpty(searchContent)){
			if(searchFieldCode==Book.SEARCH_BOOKCROSSINGID_FIELD_CODE){
				Book book=bookService.getPassAuditBookByBookcrossingId(searchContent);
				if(book!=null){
					books.add(book);
					if(books.size()==1){
						pageCount=1;
					}
				}
			}else if(searchFieldCode==Book.SEARCH_BOOKNAME_FIELD_CODE){
				books.addAll(bookService.getPassAuditBooksByNameHasPage(searchContent, searchType, pageNum, PAGE_SIZE));
				pageCount=(bookService.getPassAuditBooksCountByName(searchContent, searchType)-1)/PAGE_SIZE+1;
			}
		}
		request.setAttribute("searchContent", searchContent);
		request.setAttribute("searchFieldCode", searchFieldCode);
		request.setAttribute("searchType", searchType);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("books", books);
		request.setAttribute("pageCount", pageCount);
		return BASE_PATH+"/searchBook";
	}
	@RequestMapping("/listBook")
	public String listBook(HttpServletRequest request){
		int pid=ParameterUtils.getIntNoCareException(request, "pid", -1);
		int cid=ParameterUtils.getIntNoCareException(request, "cid", -1);
		int pageNum=ParameterUtils.getIntNoCareException(request, "pageNum", 1);
		List<Category> categories=categoryService.getFirstLevelCategories();
		List<Category> subCategories = null;
		List<Book> books = null;
		int pageCount = 0;
		if(pid!=-1&&cid!=-1){
			subCategories=categoryService.getSecondLevelCategories(pid);
			books=bookService.getPassAuditBooksByCategoryHasPage(cid, pageNum, PAGE_SIZE);
			pageCount=(bookService.getPassAuditBooksCountByCategory(cid)-1)/PAGE_SIZE+1;
		}
		request.setAttribute("pid", pid);
		request.setAttribute("cid", cid);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("categories", categories);
		request.setAttribute("subCategories", subCategories);
		request.setAttribute("books", books);
		request.setAttribute("pageCount", pageCount);
		return BASE_PATH+"/listBook";
	}
	@RequestMapping("/bookInfo")
	public String bookInfo(HttpServletRequest request){
		int bid=ParameterUtils.getIntNoCareException(request, "bid", 0);
		Book book=bookService.getEntity(bid);
		List<Category> categories=categoryService.getFirstLevelCategories();
		request.setAttribute("book", book);
		request.setAttribute("categories", categories);
		return BASE_PATH+"/bookInfo";
	}
}
