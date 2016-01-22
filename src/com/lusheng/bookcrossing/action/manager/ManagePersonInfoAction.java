package com.lusheng.bookcrossing.action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lusheng.bookcrossing.model.Manager;
import com.lusheng.bookcrossing.service.ManagerService;
import com.lusheng.bookcrossing.uitls.ParameterUtils;
import com.lusheng.bookcrossing.uitls.TextUtils;

@RequestMapping("manager")
@Controller
public class ManagePersonInfoAction implements IManagerAction{
	@Autowired
	private ManagerService managerService;
	@RequestMapping("/login")
	public String login(HttpServletRequest request) {
		String accountNumber = ParameterUtils.getTrimString(request, "accountNumber");
		String password = ParameterUtils.getTrimString(request, "password");
		if (TextUtils.isEmpty(accountNumber)) {
			request.setAttribute(ERROR, "账号不能为空");
			return "forward:/manager/login.jsp";
		}
		if (TextUtils.isEmpty(password)) {
			request.setAttribute(ERROR, "密码不能为空");
			return "forward:/manager/login.jsp";
		}
		Manager manager = managerService.login(accountNumber, password);
		if (manager != null) {
			request.getSession().setAttribute("manager", manager);
			return BASE_PATH + "/home";
		} else {
			request.setAttribute(ERROR, "账号或密码不正确");
			return "forward:/manager/login.jsp";
		}
	}

	@RequestMapping("/home")
	public String goHome() {
		return BASE_PATH + "/home";
	}

	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/manager/login.jsp";
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public String goChangePassword() {
		return BASE_PATH + "/changePassword";
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public String changePassword(HttpServletRequest request) {
		String oldPwd = ParameterUtils.getTrimString(request, "oldPwd");
		String newPwd = ParameterUtils.getTrimString(request, "newPwd");
		if (TextUtils.isEmpty(oldPwd)) {
			request.setAttribute(ERROR, "原始密码不能为空");
			return BASE_PATH + "/changePassword";
		}
		if (TextUtils.isEmpty(newPwd)) {
			request.setAttribute(ERROR, "新密码不能为空");
			return BASE_PATH + "/changePassword";
		}
		Manager manager = (Manager) request.getSession()
				.getAttribute("manager");
		if (!TextUtils.equals(manager.getPassword(), oldPwd)) {
			request.setAttribute(ERROR, "原始密码错误");
			return BASE_PATH + "/changePassword";
		}
		if (TextUtils.equals(oldPwd, newPwd)) {
			request.setAttribute(ERROR, "新密码不能与原始密码相同");
			return BASE_PATH + "/changePassword";
		}
		try {
			managerService.changePassword(manager.getMid(), newPwd);
			request.setAttribute(SUCCESS, "密码修改成功");
			manager.setPassword(newPwd);
		} catch (Exception e) {
			request.setAttribute(ERROR, "密码修改失败");
		}
		return BASE_PATH + "/changePassword";
	}
}
