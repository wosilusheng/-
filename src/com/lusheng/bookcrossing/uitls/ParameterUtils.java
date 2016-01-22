package com.lusheng.bookcrossing.uitls;

import javax.servlet.http.HttpServletRequest;

public class ParameterUtils {
	/**
	 * 等到int类型的参数，不关心异常
	 * @param request
	 * @param key
	 * @param defaultVaule
	 * @return
	 */
	public static int getIntNoCareException(HttpServletRequest request,String key,int defaultVaule){
		String valueStr= request.getParameter(key);
		if(valueStr!=null){
			valueStr=valueStr.trim();
		}
		try {
			return Integer.parseInt(valueStr);
		} catch (Exception e) {
			return defaultVaule;
		}
	}
	public static int getInt(HttpServletRequest request,String key){
		String valueStr= request.getParameter(key);
		if(valueStr!=null){
			valueStr=valueStr.trim();
		}
		return Integer.parseInt(valueStr);
	}
	public static long getLong(HttpServletRequest request,String key){
		String valueStr= request.getParameter(key);
		if(valueStr!=null){
			valueStr=valueStr.trim();
		}
		return Long.parseLong(valueStr);
	}
	
	public static long getLongNoCareException(HttpServletRequest request,String key,long defaultVaule){
		String valueStr= request.getParameter(key);
		if(valueStr!=null){
			valueStr=valueStr.trim();
		}
		try {
			return Long.parseLong(valueStr);
		} catch (Exception e) {
			return defaultVaule;
		}
	}

	public static String getTrimString(HttpServletRequest request,String key){
		String value=request.getParameter(key);
		if(value!=null){
			value.trim();
		}
		return value;
	}
	
}
