package com.lusheng.bookcrossing.action.manager;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.lusheng.bookcrossing.model.Category;
import com.lusheng.bookcrossing.service.BookService;
import com.lusheng.bookcrossing.service.CategoryService;
import com.lusheng.bookcrossing.uitls.ParameterUtils;
import com.lusheng.bookcrossing.uitls.TextUtils;

@RequestMapping("manager")
@Controller
public class ManageCategoryAction implements IManagerAction {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private BookService bookService;

	@RequestMapping("/listCategories")
	public String listCategories(HttpServletRequest request) {
		List<Category> categories = categoryService.getFirstLevelCategories();
		for (Category category : categories) {
			category.getSubCategories()
					.addAll(categoryService.getSecondLevelCategories(category
							.getCid()));
		}
		request.setAttribute("categories", categories);
		return BASE_PATH + "/listCategories";
	}

	@RequestMapping("/addFirstLevelCategory")
	public void addFirstLevelCategory(HttpServletRequest request,
			PrintWriter out) {
		String cname = ParameterUtils.getTrimString(request, "cname");
		if (TextUtils.isEmpty(cname)) {
			out.write(ERROR_CODE);
			out.flush();
			return;
		}
		try {
			categoryService.addFirstLevelCategory(cname);
			out.write(SUCCESS_CODE);
		} catch (Exception e) {
			out.write(ERROR_CODE);
		}
		out.flush();
	}

	@RequestMapping("/addSecondLevelCategory")
	public void addSecondLevelCategory(HttpServletRequest request,
			PrintWriter out) {
		String cname = ParameterUtils.getTrimString(request, "cname");
		if (TextUtils.isEmpty(cname)) {
			out.write(ERROR_CODE);
			out.flush();
			return;
		}
		int pid;
		try {
			pid = ParameterUtils.getInt(request, "pid");
		} catch (Exception e) {
			out.write(ERROR_CODE);
			out.flush();
			return;
		}
		try {
			categoryService.addSecondLevelCategory(cname, pid);
			out.write(SUCCESS_CODE);
		} catch (Exception e) {
			out.write(ERROR_CODE);
		}
		out.flush();
	}

	@RequestMapping("/deleteFirstLevelCategory")
	public void deleteFirstLevelCategory(HttpServletRequest request,
			PrintWriter out) {
		int cid;
		try {
			cid = ParameterUtils.getInt(request, "cid");
		} catch (Exception e) {
			out.write(ERROR_CODE);
			out.flush();
			return;
		}
		List<Category> categories = categoryService
				.getSecondLevelCategories(cid);
		if (categories != null && categories.size() > 0) {
			out.write("3");// 有子级分类，不能删除
			out.flush();
			return;
		}
		try {
			categoryService.deleteCategory(cid);
			out.write(SUCCESS_CODE);
		} catch (Exception e) {
			out.write(ERROR_CODE);
		}
		out.flush();
	}

	@RequestMapping("/deleteSecondLevelCategory")
	public void deleteSecondLevelCategory(HttpServletRequest request,
			PrintWriter out) {
		int cid;
		try {
			cid = ParameterUtils.getInt(request, "cid");
		} catch (Exception e) {
			out.write(ERROR_CODE);
			out.flush();
			return;
		}
		int count=bookService.getPassAuditBooksCountByCategory(cid);
		if(count>0){
			out.write("3");//该分类下含有图书，不能删除该分类
			out.flush();
			return;
		}
		try {
			categoryService.deleteCategory(cid);
			out.write(SUCCESS_CODE);
		} catch (Exception e) {
			out.write(ERROR_CODE);
		}
		out.flush();
	}

	@RequestMapping("/updateFirstLevelCategory")
	public void updateFirstLevelCategory(HttpServletRequest request,
			PrintWriter out) {
		String cname = ParameterUtils.getTrimString(request, "cname");
		if (TextUtils.isEmpty(cname)) {
			out.write(ERROR_CODE);
			out.flush();
			return;
		}
		int cid;
		try {
			cid = ParameterUtils.getInt(request, "cid");
		} catch (Exception e) {
			out.write(ERROR_CODE);
			out.flush();
			return;
		}
		try {
			categoryService.updateFirstLevelCategory(cname, cid);
			out.write(SUCCESS_CODE);
		} catch (Exception e) {
			out.write(ERROR_CODE);
		}
		out.flush();
	}

	@RequestMapping("/updateSecondLevelCategory")
	public void updateSecondLevelCategory(HttpServletRequest request,
			PrintWriter out) {
		String cname = ParameterUtils.getTrimString(request, "cname");
		if (TextUtils.isEmpty(cname)) {
			out.write(ERROR_CODE);
			out.flush();
			return;
		}
		int cid;
		int pid;
		try {
			cid = ParameterUtils.getInt(request, "cid");
			pid = ParameterUtils.getInt(request, "pid");
		} catch (Exception e) {
			out.write(ERROR_CODE);
			out.flush();
			return;
		}
		try {
			categoryService.updateSecondLevelCategory(cname, pid, cid);
			out.write(SUCCESS_CODE);
		} catch (Exception e) {
			out.write(ERROR_CODE);
		}
		out.flush();
	}
	@RequestMapping("/getSecondLevelCategories")
	@ResponseBody
	public void getSecondLevelCategories(HttpServletRequest request,PrintWriter out){
		int pid;
		try {
			pid=ParameterUtils.getInt(request, "pid");
		} catch (Exception e) {
			out.write(ERROR_CODE);
			out.flush();
			return;
		}
		List<Category> subCategories = categoryService.getSecondLevelCategories(pid);
		out.write(new Gson().toJson(subCategories));
		out.flush();
	}
}
