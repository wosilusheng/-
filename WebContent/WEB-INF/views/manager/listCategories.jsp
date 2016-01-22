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
    	//添加一级分类
    	function addFirstLevelCategoryCheck(node){
    		var cname=$.trim($("#cnameId").val());
    		if(cname==""){
    			alert("分类名不能为空");
    			return false;
    		}
    		var url=$(node).attr("action")+"?cname="+encodeURIComponent(cname);
    		$.get(url,null,function(data){
	    		if(data=="1"){
	    			alert("添加分类"+cname+"成功");
	    		}else{
	    			alert("添加分类"+cname+"失败");
	    		}
	    		$('#addFirstLevelCategoryId').modal('hide');
	    		if(data=="1"){
	    			window.location.reload();
	    		}
	    	});
    		return false;
    	}
    	//显示修改一级分类的模态框
    	function showUpdateFirstLevelCategoryModel(node){
    		var cid=$(node).attr("cid");
    		var cname=$(node).attr("cname");
    		$('#updateFirstLevelCategoryId').modal('show');
    		$('#cidUflcId').val(cid);
    		$("#cnameUflcId").val(cname);
    		return false;
    	}
    	//修改一级分类
    	function updateFirstLevelCategory(node){
    		var cname=$.trim($("#cnameUflcId").val());
    		if(cname==""){
    			alert("分类名不能为空");
    			return false;
    		}
    		var cid=$('#cidUflcId').val();
    		var url=$(node).attr("action")+"?cname="+encodeURIComponent(cname)+"&cid="+encodeURIComponent(cid);
    		$.get(url,null,function(data){
	    		if(data=="1"){
	    			alert("修改成功");
	    		}else{
	    			alert("修改失败");
	    		}
	    		$('#updateFirstLevelCategoryId').modal('hide');
	    		if(data=="1"){
	    			window.location.reload();
	    		}
	    	});
    		return false;
    	}
    	//显示修改二级分类的模态框
    	function showUpdateSecondLevelCategoryModel(node){
    		var cid=$(node).attr("cid");
    		var cname=$(node).attr("cname");
    		var pid=$(node).attr("pid");
    		$('#updateSecondLevelCategoryId').modal('show');
    		$('#updateSecondLevelCategoryId').find("option[value='"+pid+"']").attr("selected","selected");
    		$('#cidUslcId').val(cid);
    		$("#cnameUslcId").val(cname);
    		return false;
    	}
    	//修改二级分类
    	function updateSecondLevelCategory(node){
    		var cname=$.trim($("#cnameUslcId").val());
    		if(cname==""){
    			alert("分类名不能为空");
    			return false;
    		}
    		var cid=$('#cidUslcId').val();
    		var pid=$(node).find("select").val();
    		var url=$(node).attr("action")+"?cname="+encodeURIComponent(cname)+"&cid="+encodeURIComponent(cid)+"&pid="+encodeURIComponent(pid);
    		$.get(url,null,function(data){
	    		if(data=="1"){
	    			alert("修改成功");
	    		}else{
	    			alert("修改失败");
	    		}
	    		$('#updateSecondLevelCategoryId').modal('hide');
	    		if(data=="1"){
	    			window.location.reload();
	    		}
	    	});
    		return false;
    	}
    	//显示添加二级分类的模态框
    	function showAddSecondLevelCategoryModel(node){
    		var pid=$(node).attr("pid");
    		$('#addSecondLevelCategoryId').modal('show');
    		$("#cnameAslcId").val("");
    		$('#addSecondLevelCategoryId').find("option[value='"+pid+"']").attr("selected","selected");
    		return false;
    	}
    	//添加二级分类
    	function addSecondLevelCategory(node){
    		var cname=$.trim($("#cnameAslcId").val());
    		if(cname==""){
    			alert("分类名不能为空");
    			return false;
    		}
    		var pid=$(node).find("select").val();
    		var url=$(node).attr("action")+"?cname="+encodeURIComponent(cname)+"&pid="+encodeURIComponent(pid);
    		$.get(url,null,function(data){
	    		if(data=="1"){
	    			alert("添加分类"+cname+"成功");
	    		}else{
	    			alert("添加分类"+cname+"失败");
	    		}
	    		$('#addSecondLevelCategoryId').modal('hide');
	    		if(data=="1"){
	    			window.location.reload();
	    		}
	    	});
    		return false;
    	}
    	//删除一级分类
    	function deleteFirstLevelCategory(node){
    		var cname=$(node).attr("cname");
    		if(!confirm("确定要删除一级分类"+cname+"吗？")){
    			return false;
    		}
    		var cid=$(node).attr("cid");
    		var url=$(node).attr("href");
    		var args={"cid":cid};
    		$.post(url,args,function(data){
	    		if(data=="1"){
	    			alert("删除一级分类"+cname+"成功");
	    			$(node).parent().parent().remove();
	    		}else if(data=="3"){
	    			alert("一级分类"+cname+"下存在二级分类，不能删除");
	    		}else{
	    			alert("删除一级分类"+cname+"失败");
	    		}
	    	});
    		return false;
    	}
    	//删除二级分类
    	function deleteSecondLevelCategory(node){
    		var cname=$(node).attr("cname");
    		if(!confirm("确定要删除二级分类"+cname+"吗？")){
    			return false;
    		}
    		var cid=$(node).attr("cid");
    		var url=$(node).attr("href");
    		var args={"cid":cid};
    		$.post(url,args,function(data){
	    		if(data=="1"){
	    			alert("删除二级分类"+cname+"成功");
	    			$(node).parent().parent().remove();
	    		}else if(data=="3"){
	    			alert("二级分类"+cname+"下存在漂书，不能删除");
	    		}else{
	    			alert("删除二级分类"+cname+"失败");
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
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">管理用户<span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="${pageContext.request.contextPath}/manager/lookUsers">查看用户</a></li>
                <li ><a href="${pageContext.request.contextPath}/manager/addUser">添加用户</a></li>
              </ul>
            </li>
             <li class="active"><a href="${pageContext.request.contextPath}/manager/listCategories">管理分类</a></li>
             <li ><a href="${pageContext.request.contextPath}/manager/changePassword">修改密码</a></li>
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
	    <h1>分类列表</h1>
	    <button class="btn btn-primary" data-toggle="modal" data-target="#addFirstLevelCategoryId">添加一级分类</button><br><br>
				<c:if test="${empty requestScope.categories }">
					<h2>还没有任何分类信息</h2>
				</c:if>
				<c:if test="${!empty requestScope.categories }">
				<table class="table table-bordered table-condensed">
		    		<tr class="info" align="center">
		    			<td>分类名称</td>
		    			<td>操作</td>
		    		</tr>
					<c:forEach items="${requestScope.categories }" var="category">
						<tr class="success">
							<td >${category.cname}</td>
							<td  align="right">
								<a href="#" onclick="return showAddSecondLevelCategoryModel(this);" 
								pid="${category.cid}">添加二级分类</a>
								&nbsp;<a href="#" onclick="return showUpdateFirstLevelCategoryModel(this);" 
								cid="${category.cid}" cname="${category.cname}">修改</a>
								&nbsp;<a href="${pageContext.request.contextPath}/manager/deleteFirstLevelCategory" 
								cid="${category.cid}" cname="${category.cname}" onclick="return deleteFirstLevelCategory(this);">删除</a>
							</td>
						</tr>
						<c:if test="${!empty category.subCategories }">
							<c:forEach items="${category.subCategories }" var="subCategory">
								<tr align="right">
									<td>${subCategory.cname}</td>
									<td>
									<a href="#" onclick="return showUpdateSecondLevelCategoryModel(this);" 
									cid="${subCategory.cid}" cname="${subCategory.cname}" pid="${category.cid }">修改</a>
									&nbsp;
									<a href="${pageContext.request.contextPath}/manager/deleteSecondLevelCategory" 
									cid="${subCategory.cid}" cname="${subCategory.cname}" onclick="return deleteSecondLevelCategory(this);">删除</a>
									</td>
								</tr>
							</c:forEach>
						</c:if>
					</c:forEach>
					</table>
				</c:if>
	    </center>
    </div> <!-- /container -->
    <!-- Button trigger modal -->
<!-- 添加一级分类 -->
<div class="modal fade" id="addFirstLevelCategoryId" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">添加一级分类</h4>
      </div>
      <div class="modal-body">
      	<form action="${pageContext.request.contextPath}/manager/addFirstLevelCategory" method="post" onsubmit="return addFirstLevelCategoryCheck(this);">
		  <div class="form-group">
		    <label for="cnameId">请输入分类名称</label>
		    <input type="text" class="form-control" id="cnameId" name="cname" placeholder="分类名称">
		  </div>
		  <button type="submit" class="btn btn-default" >确定</button>
		</form>
      </div>
    </div>
  </div>
</div>

<!-- 添加二级分类 -->
<div class="modal fade" id="addSecondLevelCategoryId" tabindex="-1" role="dialog" aria-labelledby="myModalLabel1">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel1">添加二级分类</h4>
      </div>
      <div class="modal-body">
      	<form action="${pageContext.request.contextPath}/manager/addSecondLevelCategory" method="post" onsubmit="return addSecondLevelCategory(this);">
		  <div class="form-group">
		    <label for="cnameAslcId">请输入分类名称</label>
		    <input type="text" class="form-control" id="cnameAslcId" name="cname" placeholder="分类名称">
		  </div>
		  <label >选择一级分类</label>
		  <select class="form-control" name="pid">
		  		<c:forEach items="${requestScope.categories }" var="category">
		  			<option value="${category.cid }">${category.cname }</option>
		  		</c:forEach>
			</select>
		  <button type="submit" class="btn btn-default" >确定</button>
		</form>
      </div>
    </div>
  </div>
</div>
<!-- 修改一级分类 -->
<div class="modal fade" id="updateFirstLevelCategoryId" tabindex="-1" role="dialog" aria-labelledby="myModalLabel2">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel2">修改一级分类</h4>
      </div>
      <div class="modal-body">
      	<form action="${pageContext.request.contextPath}/manager/updateFirstLevelCategory" method="post" onsubmit="return updateFirstLevelCategory(this);">
		  <div class="form-group">
		    <label for="cnameUflcId">分类名称</label>
		    <input type="text" class="form-control" id="cnameUflcId" name="cname" >
		  </div>
		  <input type="hidden"  id="cidUflcId" name="cid" >
		  <button type="submit" class="btn btn-default" >确定</button>
		</form>
      </div>
    </div>
  </div>
</div>

<!-- 修改二级分类 -->
<div class="modal fade" id="updateSecondLevelCategoryId" tabindex="-1" role="dialog" aria-labelledby="myModalLabel3">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel3">修改二级分类</h4>
      </div>
      <div class="modal-body">
      	<form action="${pageContext.request.contextPath}/manager/updateSecondLevelCategory" method="post" onsubmit="return updateSecondLevelCategory(this);">
		  <div class="form-group">
		    <label for="cnameUslcId">分类名称</label>
		    <input type="text" class="form-control" id="cnameUslcId" name="cname" >
		  </div>
		  <label >选择一级分类</label>
		  <select class="form-control" name="pid">
		  		<c:forEach items="${requestScope.categories }" var="category">
		  			<option value="${category.cid }">${category.cname }</option>
		  		</c:forEach>
			</select>
			<input type="hidden"  id="cidUslcId" name="cid" >
		  <button type="submit" class="btn btn-default" >确定</button>
		</form>
      </div>
    </div>
  </div>
</div>

    <script src="${pageContext.request.contextPath}/bootstrap3/js/docs.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="${pageContext.request.contextPath}/bootstrap3/js/ie10-viewport-bug-workaround.js"></script>
  </body>
  </html>

