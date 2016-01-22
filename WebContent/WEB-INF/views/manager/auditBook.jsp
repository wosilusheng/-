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
    	function showModel(node){
    		var bid=$(node).attr("bid");
    		var bookname=$(node).attr("bookname");
    		$("#bidId").val(bid);
    		$("#myModalLabel").text("评审漂书"+bookname);
    		$("#passId").hide();
			$("#nopassId").hide();
			$("#statusId").val("-1")
			$("#firstLevelId").val("-1");
			$("#secondLevelId").val("-1");
			$("#nopassReasonId").val("");
    		$("#myModal").modal('show');
    		return false;
    	}
    	function auditChange(node){
    		if($(node).val()==1){
    			$("#passId").hide();
    			$("#nopassId").show();
    		}else if($(node).val()==2){
    			$("#nopassId").hide();
    			$("#passId").show();
    		}
    	}
    	function auditBook(node){
    		var passUrl=$(node).attr("passUrl");
    		var status=$("#statusId").val();
    		var bid=$("#bidId").val();
    		if(status==-1){
    			alert("请选择审核结果");
    			return false;
    		}else if(status==1){//选择了"不通过"
    			var nopassReason=$.trim($("#nopassReasonId").val());
    			if(nopassReason==""){
    				alert("不通过的原因不能为空");
        			return false;
    			}
    			var url=$(node).attr("nopassUrl")+"?nopassReason="+encodeURIComponent(nopassReason)+"&bid="+encodeURIComponent(bid);
        		$.post(url,null,function(data){
    	    		if(data=="1"){
    	    			alert("操作成功");
    	    			$("a[bid='"+bid+"']").parent().parent().remove();
    	    		}else{
    	    			alert("操作失败");
    	    		}
    	    		$('#myModal').modal('hide');
    	    	});
    		}else{
    			var cid=$("#secondLevelId").val();
    			if(cid==-1){
    				alert("请选择漂书所属分类");
    				return false;
    			}
    			var url=$(node).attr("passUrl")+"?cid="+encodeURIComponent(cid)+"&bid="+encodeURIComponent(bid);
        		$.get(url,null,function(data){
    	    		if(data=="1"){
    	    			alert("操作成功");
    	    			$("a[bid='"+bid+"']").parent().parent().remove();
    	    		}else{
    	    			alert("操作失败");
    	    		}
    	    		$('#myModal').modal('hide');
    	    	});
    		}
    		return false;
    	}
    	
    	function firstLevelChange(node){
    		var url=$(node).attr("url");
    		if($(node).val()!=-1){
    			var args={"pid":$(node).val()};
    			$.post(url,args,function(data){
    	    		if(data=="0"){
    	    			alert("获取二级分类失败");
    	    		}else{
    	    			data=eval(data);
    	    			$("#secondLevelId").empty().append("<option value='-1'>请选择二级分类</option>");
    	    			if(data.length==0){
    	    				alert("该一级分类下没有二级分类");
    	    			}else{
    	    				for(var i=0;i<data.length;i++){
    	    					$("#secondLevelId").append("<option value='"+data[i].cid+"'>"+data[i].cname+"</option>");
    	    				}
    	    			}
    	    		}
    	    	});
    		}else{
    			$("#secondLevelId").empty().append("<option value='-1'>请选择二级分类</option>");
    		}
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
                <li class="active"><a href="${pageContext.request.contextPath}/manager/auditBook">审核漂书</a></li>
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
    	<center>
		<form class="form-inline" action="${pageContext.request.contextPath}/manager/auditBook" method="post">
		  <label>请选择审核状态</label>
		  <select class="form-control" name="status">
			  <option value="0">未审核的漂书</option>
			  <c:if test="${requestScope.status == 1 }">
			  	 <option value="1"  selected="selected">未通过审核的漂书</option>
			  </c:if>
			  <c:if test="${requestScope.status != 1 }">
			  	 <option value="1">未通过审核的漂书</option>
			  </c:if>
			   <c:if test="${requestScope.status == 2 }">
			  	 <option value="2" selected="selected">通过审核的漂书</option>
			  </c:if>
			   <c:if test="${requestScope.status != 2 }">
			  	 <option value="2">通过审核的漂书</option>
			  </c:if>
			</select>
		  <button type="submit" class="btn btn-primary">查看</button>
		</form>
		<br>
		<c:if test="${empty requestScope.books }">
			<c:choose>
				<c:when test="${requestScope.status == 0}"><h1>没有未审核的漂书</h1></c:when>
				<c:when test="${requestScope.status == 1}"><h1>没有未通过审核的漂书</h1></c:when>
				<c:when test="${requestScope.status == 2}"><h1>没有通过审核的漂书</h1></c:when>
				<c:otherwise>数据为空</c:otherwise>
			</c:choose>
		</c:if>
		<c:if test="${!empty requestScope.books }">
			<table class="table table-striped table-condensed">
				<tr>
					<th>图片</th>
					<th>漂书号</th>
					<th>书名</th>
					<th>作者</th>
					<th>出版社</th>
					<th>出版时间</th>
					<th>分享时间</th>
					<th>分享者</th>
					<c:if test="${requestScope.status == 0 }"><th>评审</th></c:if>
					<c:if test="${requestScope.status == 1 }"><th>不通过的原因</th></c:if>
					<c:if test="${requestScope.status == 2 }"><th>所属分类</th></c:if>
					<c:if test="${requestScope.status == 2 }"><th>详细信息</th></c:if>
				</tr>
				<c:forEach items="${requestScope.books }" var="book">
					<tr>
						<td><img class="img-responsive" src="${pageContext.request.contextPath}${book.imagePath }"></td>
						<td>${book.bookcrossingId }</td>
						<td>${book.bookname }</td>
						<td>${book.author }</td>
						<td>${book.press }</td>
						<td>${book.publishTime }</td>
						<td>${book.shareTime }</td>
						<td>${book.shareUser.username }</td>
						<c:if test="${requestScope.status == 0 }"><td><a href="#" onclick="return showModel(this);" bid="${book.bid }" bookname="${book.bookname }">评审</a></td></c:if>
						<c:if test="${requestScope.status == 1 }"><td>${book.nopassReason }</td></c:if>
						<c:if test="${requestScope.status == 2 }"><td>${book.category.cname }</td></c:if>
						<c:if test="${requestScope.status == 2 }"><td><a href="${pageContext.request.contextPath}/manager/bookInfo?bid=${book.bid}">详细信息</a></td></c:if>
					</tr>
				</c:forEach>
			</table>
			 <c:if test="${requestScope.pageNum >1}">
			    	<a href="${pageContext.request.contextPath}/manager/auditBook?pageNum=${requestScope.pageNum-1}&status=${requestScope.status}">上一页</a>
			    </c:if>
					&nbsp;&nbsp;第${requestScope.pageNum }页&nbsp;&nbsp;
					<c:if test="${requestScope.pageNum < requestScope.pageCount}">
				    		<a href="${pageContext.request.contextPath}/manager/auditBook?pageNum=${requestScope.pageNum+1}&status=${requestScope.status}">下一页</a>
					&nbsp;&nbsp;
					</c:if>
					共${requestScope.pageCount}页
		</c:if>
      </center>
    </div> 

		<!-- Modal -->
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="myModalLabel">评审</h4>
		      </div>
		      <div class="modal-body">
		      	<form passUrl="${pageContext.request.contextPath}/manager/auditPass" 
		      	nopassUrl="${pageContext.request.contextPath}/manager/auditNoPass" onsubmit="return auditBook(this);">
		      			<input type="hidden" name="bid" id="bidId"/>
					    <label >审核结果</label>
					    <select class="form-control" name="status" id="statusId" onchange="auditChange(this)">
					    	<option value="-1">请选择审核结果</option>
					    	<option value="1">不通过</option>
					    	<option value="2">通过</option>
					    </select>
					    <div id="nopassId">
					    <label >审核不通过的原因</label>
					    <textarea class="form-control" rows="3" name="nopassReason" id="nopassReasonId"></textarea>
					    </div>
					    <div id="passId">
					    <label >一级分类</label>
					    <select class="form-control" id="firstLevelId" onchange="firstLevelChange(this);" url="${pageContext.request.contextPath}/manager/getSecondLevelCategories">
					    	<option value="-1">请选择一级分类</option>
					    	<c:forEach items="${requestScope.categories}" var="category">
					    		<option value="${category.cid }">${category.cname }</option>
					    	</c:forEach>
					    </select>
					    <label >二级分类</label>
					    <select class="form-control" name="cid" id="secondLevelId">
					    	<option value="-1">请选择二级分类</option>
					    </select>
					    </div>
					  <button type="submit" class="btn btn-default">确定</button>
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