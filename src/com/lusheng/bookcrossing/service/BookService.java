package com.lusheng.bookcrossing.service;

import java.util.List;

import com.lusheng.bookcrossing.model.Book;
import com.lusheng.bookcrossing.model.DriftRecord;

public interface BookService extends BaseService<Book>{
	void auditPass(Integer bid,Integer cid);
	void auditNoPass(Integer bid,String nopassReason);
	void changeCategory(Integer bid,Integer cid);
	List<Book> getBooksByStatusHasPage(int status,int pageNum,int pageSize);
	List<Book> getBooksByStatusAndSuidHasPage(Integer uid,int status,int pageNum,int pageSize);
	int getBookCountByStatus(int status);
	int getBookCountByStatusAndSuid(Integer uid,int status);
	Book getPassAuditBookByBookcrossingId(String bookcrossingId);
	List<Book> getPassAuditBooksByNameHasPage(String bookname,int searchType,int pageNum,int pageSize);
	int getPassAuditBooksCountByName(String bookname,int searchType);
	List<Book> getPassAuditBooksByCategoryHasPage(Integer cid,int pageNum,int pageSize);
	int getPassAuditBooksCountByCategory(Integer cid);
	Book getPassAuditBookByBid(Integer bid);
	int getDestineOrBorrowPassAuditBookCountByUid(Integer uid);
	void destinePassAuditBook(Integer bid,Integer uid);
	void cancelDestineBook(Integer bid);
	List<Book> getPassAuditBooksByCuidAndNuid(Integer uid);
	void confirmObtainBook(Book needUpdateBook,DriftRecord needUpdateDriftRecord,boolean bookIsFirstDrift);

}
