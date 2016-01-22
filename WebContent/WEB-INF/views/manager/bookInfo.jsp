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
    function change(node){
		var url=$(node).attr("url");
		if($(node).val()!=-1){
			var args={"pid":$(node).val()};
			$.post(url,args,function(data){
	    		if(data=="0"){
	    			alert("获取二级分类失败");
	    		}else{
	    			data=eval(data);
	    			$("#cidId").empty().append("<option value='-1'>请选择二级分类</option>");
	    			if(data.length==0){
	    				alert("该一级分类下没有二级分类");
	    			}else{
	    				for(var i=0;i<data.length;i++){
	    					$("#cidId").append("<option value='"+data[i].cid+"'>"+data[i].cname+"</option>");
	    				}
	    			}
	    		}
	    	});
		}else{
			$("#cidId").empty().append("<option value='-1'>请选择二级分类</option>");
		}
	}
	function check(node){
		var cid=$("#cidId").val();
		if(cid==-1){
			alert("请选择二级分类");
			return false;
		}
		var bid=$(node).attr("bid");
		var args={"cid":cid,"bid":bid};
		var url=$(node).attr("action");
		var cname=$("#cidId").find("option[value='"+cid+"']").text();
		$.post(url,args,function(data){
    		if(data=="1"){
    			alert("操作成功");
    			$("#cnameId").text(cname);
    		}else{
    			alert("操作失败");
    		}
    	});
		return false;
	}
	function goback(){
		history.back();
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
             <li class="dropdown active">
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
                <li><a href="${pageContext.request.contextPath}/manager/addUser">添加用户</a></li>
              </ul>
            </li>
             <li ><a href="${pageContext.request.contextPath}/manager/listCategories">管理分类</a></li>
             <li ><a href="${pageContext.request.contextPath}/manager/changePassword">修改密码</a></li>
          </ul>
         <ul class="nav navbar-nav navbar-right">
            <li><a >账号：${sessionScope.manager.accountNumber }</a></li>
            <li><a href="${pageContext.request.contextPath}/manager/logout">退出</a></li>
          </ul>
        </div>
      </div>
    </nav>

    <div class="container theme-showcase" role="main">
    	<button class="btn btn-primary" onclick="goback();">返回</button>
    	<center>
		<c:if test="${empty requestScope.book || empty requestScope.book.category}">
			<h1>不存在符合条件的漂书</h1>
		</c:if>
		<c:if test="${(!empty requestScope.book)&&(!empty requestScope.book.category)}">
			<img src="${pageContext.request.contextPath}${book.imagePath}" class="img-responsive" >
			书名：${book.bookname }<br>
			作者：${book.author }<br>
			出版社：${book.press }<br>
			出版时间：${book.publishTime }<br>
			分享时间：${book.shareTime }<br>
			分享者：${book.shareUser.username }<br>
			当前借阅者：${book.currUser.username }<br>
			请求漂书者：${book.nextUser.username }<br>
			所属分类：<span id="cnameId">${book.category.cname }</span><br>
			<hr>
			<form class="form-inline" action="${pageContext.request.contextPath}/manager/changeCategory" 
			bid="${requestScope.book.bid }" onsubmit="return check(this);">
				<label>修改分类：</label>
			   <label>一级分类</label>
				  <select class="form-control"  id="pidId" onchange="change(this)" 
				  	 url="${pageContext.request.contextPath}/manager/getSecondLevelCategories">
					   <option value="-1">请选择一级分类</option>
					 	<c:forEach items="${requestScope.categories }" var="category">
					 		<option value="${category.cid }">${category.cname }</option>
					 	</c:forEach>
					</select>
					<label>二级分类</label>
				  <select class="form-control" name="cid" id="cidId">
					   <option value="-1">请选择二级分类</option>
					</select>
			  <button type="submit" class="btn btn-primary">修改</button>
			</form>
		</c:if>
      	</center>
    </div> <!-- /container -->


    <script src="${pageContext.request.contextPath}/bootstrap3/js/docs.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="${pageContext.request.contextPath}/bootstrap3/js/ie10-viewport-bug-workaround.js"></script>
  

	</body>
</html>