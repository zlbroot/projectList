<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="bos" uri="/WEB-INF/tags/bos-tags.tld" %>
<html>
<head>
	<title>参数管理-操作</title>
</head>

<body>
	<form id="inputForm" action="${ctx}/params/${action}" method="post" class="form-horizontal">
		<input type="hidden" name="id" value="${entity.id}"/>
		<fieldset>
			<legend><small>参数管理</small></legend>
			<div class="control-group">
				<label for="pid" class="control-label">PID:</label>
				<div class="controls">
					<input  name="pid" value="${entity.pid}" />
				</div>
			</div>	
			<div class="control-group">
				<label for="type" class="control-label">参数类型:</label>
				<div class="controls">
					<input id="type"  name="type" value="${entity.type}" />
				</div>
			</div>	
			
			<div class="control-group">
				<label for="key" class="control-label">参数键:</label>
				<div class="controls">
					<input id="key"  name="k" value="${entity.k}" />
				</div>
			</div>	
			
			<div class="control-group">
				<label for="value" class="control-label">参数值:</label>
				<div class="controls">
					<input  name="v" id="value" value="${entity.v}" />
				</div>
			</div>	
			
			<div class="control-group">
				<label for="description" class="control-label">描述:</label>
				<div class="controls">
					<textarea id="description" name="description"  class="form-control" rows="4" style="width:300px;">${entity.description}</textarea>
				</div>
			</div>	
			<div class="form-actions">
				<input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;	
				<input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
			</div>
		</fieldset>
	</form>
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#pid").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate();
		});
	</script>
</body>
</html>
