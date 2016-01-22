package com.lusheng.bookcrossing.action.manager;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lusheng.bookcrossing.model.User;
import com.lusheng.bookcrossing.service.UserService;
import com.lusheng.bookcrossing.uitls.TextUtils;

@RequestMapping("manager")
@Controller
public class ManageUserAction implements IManagerAction {
	private static final int PAGE_SIZE = 10;
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/addUser", method = RequestMethod.GET)
	public String goAddUser() {
		return BASE_PATH + "/addUser";
	}

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public String addUser(HttpServletRequest request) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (TextUtils.isEmpty(username)) {
			request.setAttribute(ERROR, "用户名不能为空");
			return BASE_PATH + "/addUser";
		}
		if (TextUtils.isEmpty(password)) {
			request.setAttribute(ERROR, "密码不能为空");
			return BASE_PATH + "/addUser";
		}
		if (password.length() < 6) {
			request.setAttribute(ERROR, "密码长度不能低于6位");
			return BASE_PATH + "/addUser";
		}
		if (userService.getUserByName(username) != null) {
			request.setAttribute(ERROR, "用户名" + username + "已被占用");
			return BASE_PATH + "/addUser";
		}
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		userService.addUser(user);
		request.setAttribute(SUCCESS, "用户" + username + "添加成功");
		return BASE_PATH + "/addUser";
	}

	@RequestMapping("/lookUsers")
	public String lookUsers(HttpServletRequest request) {
		String pageNumStr = request.getParameter("pageNum");
		int pageNum = 1;
		if (!TextUtils.isEmpty(pageNumStr)) {
			try {
				pageNum = Integer.parseInt(pageNumStr);// 防止浏览器端在Url后面输入非数字的pageNum
			} catch (Exception e) {
			}
		}
		String username = request.getParameter("username");
		String searchTypeStr = request.getParameter("searchType");
		int searchType = User.ACCURATE_SEARCH_TPYE;
		if (!TextUtils.isEmpty(searchTypeStr)) {
			try {
				searchType = Integer.parseInt(searchTypeStr);
			} catch (Exception e) {
			}
		}
		List<User> users = null;
		int userCount;
		if (TextUtils.isEmpty(username)) {
			users = userService.getUsersByPage(pageNum, PAGE_SIZE);
			userCount = userService.getUserCount();
		} else {
			users = userService.getUsersByName(pageNum, PAGE_SIZE, username,
					searchType);
			userCount = userService.getUserCountByName(username, searchType);
		}
		request.setAttribute("users", wrapperUsers(users));
		int pageCount = (userCount - 1) / PAGE_SIZE + 1;
		request.setAttribute("pageCount", pageCount);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("username", username);
		request.setAttribute("searchType", searchType);
		return BASE_PATH + "/lookUsers";
	}

	private List<User> wrapperUsers(List<User> users) {
		if (users == null || users.size() == 0) {
			return null;
		}
		for (User user : users) {
			if (user.getEmailAddress() == null) {
				user.setEmailAddress("未填写");
			}
			if (user.getSex() == null) {
				user.setSex("保密");
			}
			if (user.getPhoneNumber() == null) {
				user.setPhoneNumber("未填写");
			}
			if (user.getSignature() == null) {
				user.setSignature("这个人很懒，什么也没留下~");
			}
		}
		return users;
	}

	@RequestMapping("/deleteUser")
	public void deleteUser(HttpServletRequest request, PrintWriter out) {
		String uidStr = request.getParameter("uid");
		int uid = 0;
		try {
			uid = Integer.parseInt(uidStr);
		} catch (Exception e) {
			out.write(ERROR_CODE);
			out.flush();
			return;
		}
		try {
			userService.deleteUser(uid);
			out.write(SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			out.write(ERROR_CODE);
		}
		out.flush();
	}

	@RequestMapping("/resetUserPwd")
	public void resetUserPwd(HttpServletRequest request, PrintWriter out) {
		String uidStr = request.getParameter("uid");
		int uid = 0;
		try {
			uid = Integer.parseInt(uidStr);
		} catch (Exception e) {
			out.write(ERROR_CODE);
			out.flush();
			return;
		}
		try {
			userService.resetPassword(uid);
			out.write(SUCCESS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			out.write(ERROR_CODE);
		}
		out.flush();
	}
}
