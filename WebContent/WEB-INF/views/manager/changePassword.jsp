<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
    <style type="text/css">
    span#error,span#success{
			display: block;
			width: 100%;
			text-align: center;
			margin: 10px;
    }
    span#error{
    	color: red;
    }
    span#success{
    	color:green;
    }
    form{
    	width: 60%;
    	margin: 0px auto;
    }
    
    </style>
    <script type="text/javascript">
    	function check(){
    		$("#success").text("");
    		var oldPwd=$.trim($("#oldPwdId").val());
    		if(oldPwd==""){
    			$("#error").text("原始密码不能为空");
    			return false;
    		}
    		var newPwd=$.trim($("#newPwdId").val());
    		if(newPwd==""){
    			$("#error").text("新密码不能为空");
    			return false;
    		}
    		if(newPwd.length<6){
    			$("#error").text("新密码长度不能低于6位");
    			return false;
    		}
    		var reNewPwd=$.trim($("#reNewPwdId").val());
    		if(newPwd!=reNewPwd){
    			$("#error").text("新密码与确认密码不同");
    			return false;
    		}
    		return true;
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
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">管理用户<span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="${pageContext.request.contextPath}/manager/lookUsers">查看用户</a></li>
                <li ><a href="${pageContext.request.contextPath}/manager/addUser">添加用户</a></li>
              </ul>
            </li>
             <li ><a href="${pageContext.request.contextPath}/manager/listCategories">管理分类</a></li>
             <li class="active"><a href="${pageContext.request.contextPath}/manager/changePassword">修改密码</a></li>
          </ul>
         <ul class="nav navbar-nav navbar-right">
            <li><a>账号：${sessionScope.manager.accountNumber }</a></li>
            <li><a href="${pageContext.request.contextPath}/manager/logout">退出</a></li>
          </ul>
        </div>
      </div>
    </nav>

    <div class="container theme-showcase" role="main">
			<form  action="${pageContext.request.contextPath}/manager/changePassword" method="post" onsubmit="return check();">
			   <span id="error">${requestScope.error }</span>
			   <span id="success">${requestScope.success }</span>
			  <div class="form-group">
			    <label for="oldPwdId">请输入原始密码</label>
			    <input type="password" class="form-control" id="oldPwdId" name="oldPwd" placeholder="原始密码">
			  </div>
			  <div class="form-group">
			    <label for="newPwdId">请输入新密码</label>
			    <input type="password" class="form-control" id="newPwdId" name="newPwd" placeholder="新密码">
			  </div>
			  <div class="form-group">
			    <label for="reNewPwdId">请再次输入新密码</label>
			    <input type="password" class="form-control" id="reNewPwdId" placeholder="确认密码">
			  </div>
			  <button type="reset" class="btn btn-primary">重置</button>
			  <button type="submit" class="btn btn-primary">确定</button>
			</form>
    </div> <!-- /container -->


    <script src="${pageContext.request.contextPath}/bootstrap3/js/docs.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="${pageContext.request.contextPath}/bootstrap3/js/ie10-viewport-bug-workaround.js"></script>
  </body>
  </html>

