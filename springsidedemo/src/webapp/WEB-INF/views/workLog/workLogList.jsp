<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="bos" uri="/WEB-INF/tags/bos-tags.tld" %>
<html>
<head>
	<title>日志管理</title>
</head>
<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<div class="row">
		<div class="span5 offset6">
			<form class="form-search" action="#">
				<label>日志类型：</label> 
				<bos:select id="search_EQ_type" dictionaryType="work_log_type" value="${param.search_EQ_type}" all="true"></bos:select>
				<button type="submit" class="btn" id="search_btn">Search</button>
		    </form>
	    </div>
	</div>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th width="100px">日志类型</th>
		<th>日志内容</th>
		<th width="150px">创建时间</th>
		<th width="50px">管理</th></tr></thead>
		<tbody>
		<c:forEach items="${workLogs.content}" var="workLog">
			<tr>
				<td><bos:fmt dictionaryType="work_log_type" value="${workLog.type}"/></td>
				<td><a href="${ctx}/workLog/update/${workLog.id}">${workLog.content}</a></td>
				<td>${workLog.created}</td>
				<td><a href="${ctx}/workLog/delete/${workLog.id}" 
				onclick="if(!window.confirm('确认删除？')) return false;">删除</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
	<tags:pagination page="${workLogs}" paginationSize="5"/>
	<div><a class="btn" href="${ctx}/workLog/create">创建日志</a></div>
</body>
</html>
