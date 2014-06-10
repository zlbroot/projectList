<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
	<title>首页</title>
	<script type="text/javascript">
		function queryCard(loginName){
			$.post("${ctx}/api/v1/params/szt",{'loginName':loginName},function(data){
				if(0 == data.status){
					alert("卡号 ： " + data.data.cardno + "    截止："+data.data.expiryDate+"    余额："+data.data.balance+" 元");
				}
			},"json");
		};
	</script>
</head>
<body>
	<h3><a href="${ctx}/task">任务管理</a></h3>
	<h3><a href="###" onclick="queryCard('zenglb');">查询深圳通余额</a></h3>
</body>
</html>