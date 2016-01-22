package com.lusheng.bookcrossing.action.mobile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.lusheng.bookcrossing.model.PointsChangeRecord;
import com.lusheng.bookcrossing.model.User;
import com.lusheng.bookcrossing.service.PointsChangeRecordService;
import com.lusheng.bookcrossing.service.UserService;
import com.lusheng.bookcrossing.uitls.Config;
import com.lusheng.bookcrossing.uitls.JsonUtils;
import com.lusheng.bookcrossing.uitls.ParameterUtils;
import com.lusheng.bookcrossing.uitls.TextUtils;
import com.lusheng.bookcrossing.uitls.UUIDUtils;

@Controller
@RequestMapping("mobile")
public class UserAction {
	@Autowired
	private UserService userService;
	@Autowired
	private PointsChangeRecordService pointsChangeRecordService;
	@ResponseBody
	@RequestMapping("/register")
	public String register(HttpServletRequest request) {
		String username=ParameterUtils.getTrimString(request, "username");
		String password=ParameterUtils.getTrimString(request, "password");
		if(TextUtils.isEmpty(username)){
			return JsonUtils.getErrorMsg(Config.EMPTY_PRAMETERS_ERROR_CODE, "用户名不能为空");
		}
		if(TextUtils.isEmpty(password)){
			return JsonUtils.getErrorMsg(Config.EMPTY_PRAMETERS_ERROR_CODE, "密码不能为空");
		}
		if(password.length()<6){
			return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "密码长度不能低于6位");
		}
		if(userService.getUserByName(username)!=null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "用户名已存在");
		}
		try {
			userService.register(username, password);
			return JsonUtils.getSuccessMsg(null);
		} catch (Exception e) {
			return JsonUtils.getServerBugErrorMsg();
		}
	}
	@ResponseBody
	@RequestMapping("/login")
	public String login(HttpServletRequest request,HttpServletResponse response){
		String username=ParameterUtils.getTrimString(request, "username");
		String password=ParameterUtils.getTrimString(request, "password");
		if(TextUtils.isEmpty(username)){
			return JsonUtils.getErrorMsg(Config.EMPTY_PRAMETERS_ERROR_CODE, "用户名不能为空");
		}
		if(TextUtils.isEmpty(password)){
			return JsonUtils.getErrorMsg(Config.EMPTY_PRAMETERS_ERROR_CODE, "密码不能为空");
		}
		User user=userService.login(username, password);
		if(user==null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "用户名或密码错误");
		}else{
			return JsonUtils.getSuccessMsg(wrapUser(user));
		}
	}
	@ResponseBody
	@RequestMapping("/lookMyInfo")
	public String lookMyInfo(HttpServletRequest request){
		Integer uid=(Integer) request.getAttribute("uid");
		User user=userService.getUserByUid(uid);
		return JsonUtils.getSuccessMsg(wrapUser(user));
	}
	
	@ResponseBody
	@RequestMapping("/lookUserInfo")
	public String lookUserInfo(HttpServletRequest request){
		int uid=ParameterUtils.getIntNoCareException(request, "uid", -1);
		if(uid==-1){
			return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "用户Id不合法");
		}
		User user=userService.getUserByUid(uid);
		if(user==null){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "该用户不存在");
		}
		user.setPassword(null);
		user.setToken(null);
		return JsonUtils.getSuccessMsg(wrapUser(user));
	}
	
	@ResponseBody
	@RequestMapping("/lookMyPointsChangeRecord")
	public String lookMyPointsChangeRecord(HttpServletRequest request){
		Integer uid=(Integer) request.getAttribute("uid");
		int pageNum=ParameterUtils.getIntNoCareException(request, "pageNum", 1);
		int pageSize=ParameterUtils.getIntNoCareException(request, "pageSize", 10);
		pageSize=pageSize==0?10:pageSize;
		List<PointsChangeRecord> pointsChangeRecords=pointsChangeRecordService.getPointsChangeRecordsByUidHasPage(uid, pageNum, pageSize);
		int count=pointsChangeRecordService.getPointsChangeRecordsCountByUid(uid);
		int pageCount=(count-1)/pageSize+1;
		Map<String,Object> map=new LinkedHashMap<String, Object>();
		map.put("pageNum", pageNum);
		map.put("pageCount", pageCount);
		map.put("pointsChangeRecords", wrapperPointsChangeRecords(pointsChangeRecords));
		return JsonUtils.getSuccessMsg(map);
	}
	@ResponseBody
	@RequestMapping("/changePassword")
	public String changePassword(HttpServletRequest request){
		String oldPwd=ParameterUtils.getTrimString(request, "oldPwd");
		if(TextUtils.isEmpty(oldPwd)){
			return JsonUtils.getErrorMsg(Config.EMPTY_PRAMETERS_ERROR_CODE, "原密码不能为空");
		}
		String newPwd=ParameterUtils.getTrimString(request, "newPwd");
		if(TextUtils.isEmpty(newPwd)){
			return JsonUtils.getErrorMsg(Config.EMPTY_PRAMETERS_ERROR_CODE, "新密码不能为空");
		}
		if(newPwd.length()<6){
			return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "新密码不能低于6位");
		}
		Integer uid=(Integer) request.getAttribute("uid");
		User user=userService.getUserByUid(uid);
		if(!TextUtils.equals(user.getPassword(),oldPwd)){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "原密码错误");
		}
		try {
			userService.changePassword(uid, newPwd);
			return JsonUtils.getSuccessMsg(null);
		} catch (Exception e) {
			return JsonUtils.getServerBugErrorMsg();
		}
	}
	@ResponseBody
	@RequestMapping("/changeMyInfo")
	public String changeMyInfo(HttpServletRequest request){
		Integer uid=(Integer) request.getAttribute("uid");
		User user=userService.getUserByUid(uid);
		boolean hasChange=false;
		String emailAddress=ParameterUtils.getTrimString(request, "emailAddress");
		if(!TextUtils.isEmpty(emailAddress)){
			Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
			Matcher matcher = pattern.matcher(emailAddress);
			if(matcher.matches()){
				user.setEmailAddress(emailAddress);
			}else{
				return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "不合法的邮箱地址");
			}
			hasChange=true;
		}
		String phoneNumber=ParameterUtils.getTrimString(request, "phoneNumber");
		if(!TextUtils.isEmpty(phoneNumber)){
			Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
		    Matcher matcher = regex.matcher(phoneNumber);
		    if(matcher.matches()){
				user.setPhoneNumber(phoneNumber);
			}else{
				return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "不合法的手机号码");
			}
		    hasChange=true;
		}
		String sex=ParameterUtils.getTrimString(request, "sex");
		if(!TextUtils.isEmpty(sex)){
			if(TextUtils.equals("男", sex)||TextUtils.equals("女", sex)){
				user.setSex(sex);
			}else{
				user.setSex(null);
			}
			hasChange=true;
		}
		String signature=ParameterUtils.getTrimString(request, "signature");
		if(!TextUtils.isEmpty(signature)){
			user.setSignature(signature);
			hasChange=true;
		}
		if(!hasChange){
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "没有提供要改变的信息");
		}
		try {
			userService.updateEntity(user);
			return JsonUtils.getSuccessMsg(wrapUser(user));
		} catch (Exception e) {
			return JsonUtils.getServerBugErrorMsg();
		}
	}
	@ResponseBody
	@RequestMapping("/changeMyAvatar")
	public String changeMyAvatar(HttpServletRequest request){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Integer uid=(Integer) multipartRequest.getAttribute("uid");
		MultipartFile file = multipartRequest.getFile("file");
		if(file==null||file.getSize()==0){
			return JsonUtils.getErrorMsg(Config.EMPTY_PRAMETERS_ERROR_CODE, "文件不能为空");
		}
		String filename=file.getOriginalFilename().toLowerCase();
		if(!(filename.endsWith(".jpg")||filename.endsWith(".png"))){
			return JsonUtils.getErrorMsg(Config.INVALID_PRAMETERS_ERROR_CODE, "文件格式不对");
		}
		User user=userService.getEntity(uid);
		String lastAvatarPath=user.getAvatarPath();
		String avatarPath=Config.AVATAR_BASE_PATH+UUIDUtils.randomUUID()+filename.substring(filename.lastIndexOf("."));
		String path=request.getServletContext().getRealPath(avatarPath);
		InputStream is=null;
		FileOutputStream fos=null;
		try {
			is=file.getInputStream();
			fos=new FileOutputStream(new File(path));
			int len=0;
			byte[] buff=new byte[1024];
			while((len=is.read(buff))!=-1){
				fos.write(buff, 0, len);
			}
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtils.getErrorMsg(Config.OTHER_ERROR_CODE, "文件传输错误");
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		user.setAvatarPath(avatarPath);
		try {
			userService.updateEntity(user);
			if(lastAvatarPath!=null){
				File lastFile=new File(request.getServletContext().getRealPath(lastAvatarPath));
				if(lastFile.exists()){
					lastFile.delete();
				}
			}
			return JsonUtils.getSuccessMsg(wrapUser(user));
		} catch (Exception e) {
			return JsonUtils.getServerBugErrorMsg();
		}
	}
	private List<PointsChangeRecord> wrapperPointsChangeRecords(List<PointsChangeRecord> pointsChangeRecords){
		for(PointsChangeRecord pointsChangeRecord:pointsChangeRecords){
			pointsChangeRecord.setUser(null);
			pointsChangeRecord.setPcrid(null);
		}
		return pointsChangeRecords;
	}
	private User wrapUser(User user) {
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
		return user;
	}
}
