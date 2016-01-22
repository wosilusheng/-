<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="http://v3.bootcss.com/favicon.ico">

    <title>主页</title>

	<link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap3/css/bootstrap.min.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap3/css/bootstrap-theme.min.css">
	<script src="${pageContext.request.contextPath}/bootstrap3/js/jquery-1.11.2.min.js"></script>
	<script src="${pageContext.request.contextPath}/bootstrap3/js/bootstrap.min.js"></script>
    <!-- Custom styles for this template -->
    <link href="${pageContext.request.contextPath}/bootstrap3/css/theme.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="${pageContext.request.contextPath}/bootstrap3/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script type="text/javascript">
    	function check(){
    		if($.trim($("#usernameId").val())==""){
    			alert("用户名不能为空");
    			return false;
    		}
    		return true;
    	}
    	function resetUserPwd(node){
    		if(!confirm("确定要重置用户"+$(node).attr("username")+"的密码吗？")){
    			return false;
    		}
    		var url=$(node).attr("href");
    		var args={"uid":$(node).attr("uid")};
    		$.post(url,args,function(data){
	    		if(data=="1"){
	    			alert("重置用户"+$(node).attr("username")+"的密码成功");
	    		}else{
	    			alert("重置用户"+$(node).attr("username")+"的密码失败");
	    		}
	    	});
    		return false;
    	}
    	function deleteUser(node){
    		if(!confirm("确定要删除用户"+$(node).attr("username")+"吗？")){
    			return false;
    		}
    		var url=$(node).attr("href");
    		var args={"uid":$(node).attr("uid")};
    		$.post(url,args,function(data){
	    		if(data=="1"){
	    			alert("删除用户"+$(node).attr("username")+"成功");
	    			$(node).parent().parent().remove();
	    		}else{
	    			alert("删除用户"+$(node).attr("username")+"成功");
	    		}
	    	});
    		return false;
    	}
    </script>
  </head>

  <body role="document">

    <!-- Fixed navbar -->
       <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" >图书漂流服务平台</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-left">
            <li ><a href="${pageContext.request.contextPath}/manager/home">主页</a></li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">管理漂书<span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="${pageContext.request.contextPath}/manager/auditBook">审核漂书</a></li>
                <li><a href="${pageContext.request.contextPath}/manager/searchBook">搜索漂书</a></li>
                <li><a href="${pageContext.request.contextPath}/manager/listBook">查看漂书</a></li>
              </ul>
            </li>
            <li class="dropdown active">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">管理用户<span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li class="active"><a href="${pageContext.request.contextPath}/manager/lookUsers">查看用户</a></li>
                <li><a href="${pageContext.request.contextPath}/manager/addUser">添加用户</a></li>
              </ul>
            </li>
             <li><a href="${pageContext.request.contextPath}/manager/listCategories">管理分类</a></li>
             <li><a href="${pageContext.request.contextPath}/manager/changePassword">修改密码</a></li>
          </ul>
         <ul class="nav navbar-nav navbar-right">
            <li><a>账号：${sessionScope.manager.accountNumber }</a></li>
            <li><a href="${pageContext.request.contextPath}/manager/logout">退出</a></li>
          </ul>
        </div>
      </div>
    </nav>

    <div class="container theme-showcase" role="main">
    		<center>
    			<form class="form-inline" action="${pageContext.request.contextPath}/manager/lookUsers" method="post" onsubmit="return check();">
				  <div class="form-group">
				    <label for="usernameId">请输入用户名</label>
				    <input type="text" class="form-control" id="usernameId" name="username" value="${requestScope.username }">
				  </div>
				  <select class="form-control" name="searchType">
					  <option value="1">精确搜索</option>
					  <c:if test="${requestScope.searchType == 2}">
					  	<option value="2" selected="selected">模糊搜索</option>
					  </c:if>
					  <c:if test="${requestScope.searchType != 2}">
					  	<option value="2" >模糊搜索</option>
					  </c:if>
				   </select>
				  <button type="submit" class="btn btn-primary" >搜索</button>
				</form>
				<br><br>
			<c:if test="${empty requestScope.users }">
					 <c:if test="${empty requestScope.username}">
					  	<h1>还没有任何用户</h1>
					 </c:if>
					  <c:if test="${! empty requestScope.username}">
					  	<h1>没有符合条件的用户存在</h1>
					 </c:if>
			</c:if>
			<c:if test="${!empty requestScope.users }">
					<table class="table table-striped">
				    	<tr>
				    		<th>用户名</th>
				    		<th>邮箱地址</th>
				    		<th>电话号码</th>
				    		<th>性别</th>
				    		<th>个性签名</th>
				    		<th>等级</th>
				    		<th>积分数</th>
				    		<th>重置密码</th>
				    		<th>删除</th>
				    	</tr>
				    	<c:forEach items="${requestScope.users }" var="user">
				    		<tr>
				    			<td>${user.username}</td>
				    			<td>${user.emailAddress}</td>
				    			<td>${user.phoneNumber}</td>
				    			<td>${user.sex}</td>
				    			<td>${user.signature}</td>
				    			<td>${user.level}</td>
				    			<td>${user.points}</td>
				    			<td><a href="${pageContext.request.contextPath}/manager/resetUserPwd" username="${user.username}" uid="${user.uid}" onclick="return resetUserPwd(this);">重置密码</a></td>
				    			<td><a href="${pageContext.request.contextPath}/manager/deleteUser"  username="${user.username}" uid="${user.uid}" onclick="return deleteUser(this);">删除</a></td>
				    		</tr>
				    	</c:forEach>
			    	</table>
			    <c:if test="${requestScope.pageNum >1}">
			    	<c:if test="${empty requestScope.username || empty requestScope.searchType}">
			    		<a href="${pageContext.request.contextPath}/manager/lookUsers?pageNum=${requestScope.pageNum-1}">上一页</a>
			    	</c:if>
			    	<c:if test="${!empty requestScope.username && !empty requestScope.searchType}">
			    		<a href="${pageContext.request.contextPath}/manager/lookUsers?pageNum=${requestScope.pageNum-1}&username=${requestScope.usename}&searchType=${requestScope.searchType}">上一页</a>
			    	</c:if>
			    </c:if>
					&nbsp;&nbsp;第${requestScope.pageNum }页&nbsp;&nbsp;
					<c:if test="${requestScope.pageNum < requestScope.pageCount}">
						<c:if test="${empty requestScope.username || empty requestScope.searchType}">
				    		<a href="${pageContext.request.contextPath}/manager/lookUsers?pageNum=${requestScope.pageNum+1}">下一页</a>
				    	</c:if>
				    	<c:if test="${!empty requestScope.username && !empty requestScope.searchType}">
				    		<a href="${pageContext.request.contextPath}/manager/lookUsers?pageNum=${requestScope.pageNum+1}&username=${requestScope.username}&searchType=${requestScope.searchType}">下一页</a>
				    	</c:if>
					&nbsp;&nbsp;
					</c:if>
					共${requestScope.pageCount}页
			</c:if>
			</center>
    </div> <!-- /container -->


    <script src="${pageContext.request.contextPath}/bootstrap3/js/docs.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="${pageContext.request.contextPath}/bootstrap3/js/ie10-viewport-bug-workaround.js"></script>
  </body>
  </html>

