package com.lusheng.bookcrossing.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lusheng.bookcrossing.service.UserService;
import com.lusheng.bookcrossing.uitls.Config;
import com.lusheng.bookcrossing.uitls.JsonUtils;
import com.lusheng.bookcrossing.uitls.ParameterUtils;
import com.lusheng.bookcrossing.uitls.TextUtils;

public class LoginInterceptor implements HandlerInterceptor {
	private List<String> ignorePaths;
	@Autowired
	private UserService userService;
	public LoginInterceptor() {
		ignorePaths = new ArrayList<String>();
		ignorePaths.add("/mobile/register");
		ignorePaths.add("/mobile/login");
		ignorePaths.add("/mobile/lookUserInfo");
		ignorePaths.add("/mobile/lookReviewByBid");
		ignorePaths.add("/mobile/searchBookByBookcrossingId");
		ignorePaths.add("/mobile/searchBookByBookname");
		ignorePaths.add("/mobile/listCategories");
		ignorePaths.add("/mobile/listBookByCid");
		ignorePaths.add("/mobile/lookDriftRecordByBid");
	}
	/**
	 * 这个方法在业务处理器处理请求之前被调用，在该 方法中对用户 请求 request 进行处理。 如果程序员决定该拦截器对
	 * 请求进行拦截处理后还要调用其他的拦截器，或者是业务处理器去 进行处理， 则返回true；如果程序员决定不需要再调用 其他的组件
	 * 去处理请求，则返回false。
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String servletPath = request.getServletPath();
		if (servletPath.startsWith("/manager")
				&& !servletPath.startsWith("/manager/login")
				&& request.getSession().getAttribute("manager") == null) {
			request.setAttribute("error", "您还没有登录");
			request.getRequestDispatcher("/manager/login.jsp").forward(request,
					response);
			return false;
		}
		if (servletPath.startsWith("/mobile")
				&& !ignorePaths.contains(servletPath)) {
			String token = ParameterUtils.getTrimString(request, "token");
			response.setContentType("text/html;charset=UTF-8");
			if (TextUtils.isEmpty(token)) {
				writeErrorMsgToClient(response, Config.EMPTY_PRAMETERS_ERROR_CODE, "token不能为空");
				return false;
			}
			Integer uid = userService.getUidByToken(token);
			if (uid == null) {
				writeErrorMsgToClient(response, Config.WRONG_TOKEN_ERROR_CODE, "错误的token");
				return false;
			}
			request.setAttribute("uid", uid);
		}
		return true;
	}
	private void writeErrorMsgToClient(HttpServletResponse response,int errorCode,String errorStr) throws IOException{
		PrintWriter out = response.getWriter();
		out.write(JsonUtils.getErrorMsg(errorCode, errorStr));
		out.flush();
		out.close();
	}

	/**
	 * 这个方法在业务处理器处理完请求后，但 是DispatcherServlet 向客户端返回响应前被调用，在该方法中对
	 * 用户请求request进行处理。
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 这个方法在 DispatcherServlet 完全处理完请 求后被调用，可以在该方法中进行一些资源清理的操作。
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
					throws Exception {

	}

}
