<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div id="header">
	<div id="title">
	    <h2><a href="${ctx}">林小宝工作台</a>
	    <shiro:user>
			<div class="btn-group pull-right">
				<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
					<i class="icon-user"></i> <shiro:principal property="name"/>
					<span class="caret"></span>
				</a>
				<ul class="dropdown-menu">
					<shiro:hasRole name="admin">
						<li><a href="${ctx}/admin/user">Admin Users</a></li>
						<li class="divider"></li>
					</shiro:hasRole>
					<li><a href="${ctx}/profile">Edit Profile</a></li>
					<li><a href="${ctx}/logout">Logout</a></li>
				</ul>
			</div>
		</shiro:user>
		</h2>
	</div>
	<shiro:hasRole name="user">
		<ul class="nav nav-tabs">
		  <li class="active"><a href="${ctx}/index">首页</a></li>
		  <li><a href="${ctx}/workLog">工作日志</a></li>
		  <li><a href="${ctx}/task">任务管理</a></li>
		  <li><a href="${ctx}/params">参数管理</a></li>
		  <li><a href="${ctx}/sms">发送短信</a></li>
		</ul>
	</shiro:hasRole>
</div>