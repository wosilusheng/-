package com.lusheng.bookcrossing.uitls;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {
	private static final String FLAG_KEY = "flag";
	private static final String DATA_KEY = "data";
	public static final String ERROR_CODE_KEY = "errorCode";
	public static final String ERROR_STR_KEY = "errorStr";
	private static  Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	public static String getSuccessMsg(Object data) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put(FLAG_KEY, true);
		if (data != null) {
			map.put(DATA_KEY, data);
		}
		return gson.toJson(map);
	}

	public static String getErrorMsg(int errorCode, String errorStr) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put(FLAG_KEY, false);
		map.put(ERROR_CODE_KEY, errorCode);
		map.put(ERROR_STR_KEY, errorStr);
		return gson.toJson(map);
	}
	public static String getServerBugErrorMsg(){
		return getErrorMsg(Config.SERVER_BUG_ERROR_CODE, Config.SERVER_BUG_ERROR_STR);
	}
}
