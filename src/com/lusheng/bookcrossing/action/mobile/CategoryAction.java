package com.lusheng.bookcrossing.action.mobile;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lusheng.bookcrossing.model.Category;
import com.lusheng.bookcrossing.service.CategoryService;
import com.lusheng.bookcrossing.uitls.JsonUtils;

@Controller
@RequestMapping("mobile")
public class CategoryAction {
	@Autowired
	private CategoryService categoryService;

	@ResponseBody
	@RequestMapping("/listCategories")
	public String listCategories(HttpServletRequest request) {
		List<Category> categories = categoryService.getFirstLevelCategories();
		for (Category category : categories) {
			category.getSubCategories().addAll(
					wrapCategories(categoryService
							.getSecondLevelCategories(category.getCid())));
		}
		return JsonUtils.getSuccessMsg(categories);
	}

	private List<Category> wrapCategories(List<Category> categories) {
		for (Category category : categories) {
			category.setParent(null);
			category.setSubCategories(null);
		}
		return categories;
	}
}
