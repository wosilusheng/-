package com.lusheng.bookcrossing.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.lusheng.bookcrossing.uitls.TextUtils;
/**
 * 处理中文乱码
 * @author lusheng
 *
 */
public class EncodeFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		response.setCharacterEncoding("utf-8");
		request=new HttpServletRequestWrapper((HttpServletRequest) request){
			@Override
			public String getParameter(String name) {
				System.out.println(name+"---->原始编码："+super.getParameter(name));
				try {
					if(super.getParameter(name)!=null){
						System.out.println(name+"---->处理后编码："+new String(super.getParameter(name).getBytes("iso8859-1"),"utf-8"));
						return new String(super.getParameter(name).getBytes("iso8859-1"),"utf-8");
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return super.getParameter(name);
			}
		};
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

}
