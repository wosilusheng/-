package com.lusheng.bookcrossing.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lusheng.bookcrossing.dao.BaseDao;
import com.lusheng.bookcrossing.model.Book;
import com.lusheng.bookcrossing.model.DriftRecord;
import com.lusheng.bookcrossing.service.BookService;
import com.lusheng.bookcrossing.service.UserService;

@Service("bookService")
public class BookServiceImpl extends BaseServiceImpl<Book> implements
		BookService {
	@Resource(name = "driftRecordDao")
	private BaseDao<DriftRecord> driftRecordDao;
	@Autowired
	private UserService  userService;
	@Resource(name = "bookDao")
	@Override
	public void setDao(BaseDao<Book> dao) {
		super.setDao(dao);
	}

	@Override
	public void auditPass(Integer bid, Integer cid) {
		String hql = "update Book b set b.category.cid=?,b.status=? where b.bid=?";
		this.batchEntityByHQL(hql, cid, Book.PASS_STATUS, bid);
	}

	@Override
	public void auditNoPass(Integer bid, String nopassReason) {
		String hql = "update Book b set b.nopassReason=?,b.status=? where b.bid=?";
		this.batchEntityByHQL(hql, nopassReason, Book.NO_PASS_STATUS, bid);
	}

	@Override
	public void changeCategory(Integer bid, Integer cid) {
		String hql = "update Book b set b.category.cid=? where b.bid=?";
		this.batchEntityByHQL(hql, cid, bid);
	}

	@Override
	public List<Book> getBooksByStatusHasPage(int status, int pageNum,
			int pageSize) {
		String hql = "from Book b where b.status=?";
		return this.findEntityHasPageByHQL(hql, (pageNum - 1) * pageSize,
				pageSize, status);
	}

	@Override
	public int getBookCountByStatus(int status) {
		String hql = "select count(*) from Book b where b.status=?";
		return (int) (long) this.uniqueResult(hql, status);
	}

	@Override
	public List<Book> getBooksByStatusAndSuidHasPage(Integer uid, int status,
			int pageNum, int pageSize) {
		String hql = "from Book b where b.shareUser.uid=? and b.status=?";
		return this.findEntityHasPageByHQL(hql, (pageNum - 1) * pageSize,
				pageSize, uid, status);
	}

	@Override
	public int getBookCountByStatusAndSuid(Integer uid, int status) {
		String hql = "select count(*) from Book b where b.shareUser.uid=? and b.status=?";
		return (int) (long) this.uniqueResult(hql, uid, status);
	}

	@Override
	public Book getPassAuditBookByBookcrossingId(String bookcrossingId) {
		String hql = "from Book b where b.bookcrossingId=? and b.status=?";
		return (Book) this.uniqueResult(hql, bookcrossingId, Book.PASS_STATUS);
	}

	@Override
	public List<Book> getPassAuditBooksByNameHasPage(String bookname,
			int searchType, int pageNum, int pageSize) {
		String hql;
		if (searchType == Book.ACCURATE_SEARCH_TPYE) {
			hql = "from Book b where b.bookname=? and b.status=?";
			return this.findEntityHasPageByHQL(hql, (pageNum - 1) * pageSize,
					pageSize, bookname, Book.PASS_STATUS);
		} else {
			hql = "from Book b where b.bookname like ? and b.status=?";
			return this.findEntityHasPageByHQL(hql, (pageNum - 1) * pageSize,
					pageSize, "%" + bookname + "%", Book.PASS_STATUS);
		}
	}

	@Override
	public int getPassAuditBooksCountByName(String bookname, int searchType) {
		String hql;
		if (searchType == Book.ACCURATE_SEARCH_TPYE) {
			hql = "select count(*) from Book b where b.bookname=? and b.status=?";
			return (int) (long) this.uniqueResult(hql, bookname,
					Book.PASS_STATUS);
		} else {
			hql = "select count(*) from Book b where b.bookname like ? and b.status=?";
			return (int) (long) this.uniqueResult(hql, "%" + bookname + "%",
					Book.PASS_STATUS);
		}
	}

	@Override
	public List<Book> getPassAuditBooksByCategoryHasPage(Integer cid,
			int pageNum, int pageSize) {
		String hql = "from Book b where b.category.cid=? and b.status=?";
		return this.findEntityHasPageByHQL(hql, (pageNum - 1) * pageSize,
				pageSize, cid, Book.PASS_STATUS);
	}

	@Override
	public int getPassAuditBooksCountByCategory(Integer cid) {
		String hql = "select count(*) from Book b where b.category.cid=? and b.status=?";
		return (int) (long) this.uniqueResult(hql, cid, Book.PASS_STATUS);
	}

	@Override
	public Book getPassAuditBookByBid(Integer bid) {
		String hql = "from Book b where b.bid=? and b.status=?";
		return (Book) this.uniqueResult(hql, bid, Book.PASS_STATUS);
	}

	@Override
	public int getDestineOrBorrowPassAuditBookCountByUid(Integer uid) {
		String hql = "select count(*)  from Book b where (b.currUser.uid=? or b.nextUser.uid=?) and b.status=?";
		return (int) (long) this.uniqueResult(hql, uid, uid, Book.PASS_STATUS);
	}

	@Override
	public void destinePassAuditBook(Integer bid, Integer uid) {
		String hql = "update Book b set b.nextUser.uid=? where b.bid=? and b.status=?";
		this.batchEntityByHQL(hql, uid, bid, Book.PASS_STATUS);
	}

	@Override
	public void cancelDestineBook(Integer bid) {
		Book book = this.getEntity(bid);
		book.setNextUser(null);
		this.updateEntity(book);
	}

	@Override
	public List<Book> getPassAuditBooksByCuidAndNuid(Integer uid) {
		String hql = "from Book b where (b.currUser.uid=? or b.nextUser.uid=?) and b.status=?";
		return this.findEntityByHQL(hql, uid, uid, Book.PASS_STATUS);
	}

	@Override
	public void confirmObtainBook(Book needUpdateBook,
			DriftRecord needUpdateDriftRecord, boolean bookIsFirstDrift) {
		this.updateEntity(needUpdateBook);
		driftRecordDao.updateEntity(needUpdateDriftRecord);
		if(bookIsFirstDrift&&needUpdateBook!=null){
			userService.changePointsByUid(needUpdateBook.getShareUser().getUid(), 300, "分享的漂书"+needUpdateBook.getBookname()+"第一次漂流成功");
		}
	}
}
