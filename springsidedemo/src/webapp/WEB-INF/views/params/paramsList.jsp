<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="bos" uri="/WEB-INF/tags/bos-tags.tld" %>
<html>
<head>
	<title>参数管理</title>
</head>
<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<div class="row">
		<div class="span5 offset6">
			<form class="form-search" action="#">
				<label>参数类型：</label> 
				<input name="search_EQ_type"  value="${param.search_EQ_type}"/>
				<button type="submit" class="btn" id="search_btn">Search</button>
		    </form>
	    </div>
	</div>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th width="20px">PID</th><th width="100px">参数类型</th>
		<th>参数键</th>
		<th>参数值</th>
		<th>创建时间</th>
		<th>描述</th>
		<th width="50px">管理</th></tr></thead>
		<tbody>
		<c:forEach items="${datas.content}" var="tt">
			<tr>
				<td>${tt.pid}</td>
				<td>${tt.type}</td>
				<td><a href="${ctx}/params/update/${tt.id}">${tt.k}</a></td>
				<td><a href="${ctx}/params/update/${tt.id}">${tt.v}</a></td>
				<td>${tt.created}</td>
				<td>${tt.description}</td>
				<td><a href="${ctx}/params/delete/${tt.id}" 
				onclick="if(!window.confirm('确认删除？')) return false;">删除</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<tags:pagination page="${datas}" paginationSize="5"/>
	<div><a class="btn" href="${ctx}/params/create">创建参数</a></div>
</body>
</html>
