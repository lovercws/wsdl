<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css"
	href="./themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="./themes/icon.css">
<script type="text/javascript" src="./js/jquery.min.js"></script>
<script type="text/javascript" src="./js/jquery.easyui.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$.ajax({
	    type:'post',
	    url:'WsdlLoadServlet?type=load&name=1',
	    async: false, 
	    dataType:"JSON",
	    success:function(data){
	    	alert(data);
	    }
	});
});
</script>
</head>
<body>
	<h2>Tree Lines</h2>
	<p>This sample shows how to show tree lines.</p>
	<div style="margin: 10px 0;"></div>
	<div class="easyui-panel" style="padding: 5px">
		<ul class="easyui-tree"
			data-options="url:'WsdlLoadServlet?type=load',method:'get',animate:true,lines:true"></ul>
	</div>
</body>
</html>