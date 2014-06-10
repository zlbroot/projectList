<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="bos" uri="/WEB-INF/tags/bos-tags.tld" %>
<html>
<head>
	<title>日志管理</title>
</head>

<body>
	<form id="inputForm" action="${ctx}/workLog/${action}" method="post" class="form-horizontal">
		<input type="hidden" name="id" value="${workLog.id}"/>
		<fieldset>
			<legend><small>管理日志</small></legend>
			<div class="control-group">
				<label for="workLog_title" class="control-label">日志类型:</label>
				<div class="controls">
					<bos:select id="type" dictionaryType="work_log_type" value="${workLog.type}"></bos:select> 
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">日志内容:</label>
				<div class="controls">
					<textarea id="content" name="content"  class="form-control" rows="4" style="width:300px;">${workLog.content}</textarea>
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
			$("#workLog_title").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate();
		});
	</script>
</body>
</html>
