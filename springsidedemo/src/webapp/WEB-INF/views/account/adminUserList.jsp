<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ taglib prefix="bos" uri="/WEB-INF/tags/bos-tags.tld" %>
<html>
<head>
	<title>用户管理</title>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>登录名</th><th>用户名</th>
		<th>状态</th><th>角色</th>
		<th>注册时间</th><th>描述</th><th>管理</th></tr></thead>
		<tbody>
		<c:forEach items="${users}" var="user">
			<tr>
				<td><a href="${ctx}/admin/user/update/${user.id}">${user.loginName}</a></td>
				<td>${user.cnName}</td>
				<td><bos:fmt dictionaryType="user_status" value="${user.userStatus}"/></td>
				<td>${user.roles}</td>
				<td><fmt:formatDate value="${user.registerDate}" pattern="yyyy年MM月dd日  HH时mm分ss秒" /></td>
				<td>${user.description}</td>
				<td><a href="${ctx}/admin/user/delete/${user.id}" 
					onclick="if(!window.confirm('确认删除？')) return false;">删除</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>
