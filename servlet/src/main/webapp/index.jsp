<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>提交表单数据</title>
</head>
<body bgcolor="#FFFFFF">
<h1 align="center"><b>欢迎登录系统</b></h1>
<form action="GetPostData" method="post">
<p></p>
<table width="52%" broder="2" align="center">
<tr bgcolor="#FFFFCC">
	<td align="center" width="43%"><div align="center">用户名：</div></td>
	<td width="57%"><div align="left">
		<input type="text" name="username">
	</div></td>
</tr>
<tr bgcolor="#CCFF99">
	<td align="center" width="43%"><div align="center">密码：</div></td>
	<td width="57%"><div align="left">
		<input type="password" name="password">
	</div></td>
</tr>
<tr bgcolor="#FFFFCC">
	<td align="center" width="43%"><div align="center">你想访问的网址：</div></td>
	<td width="57%"><div align="left">
		<input type="text" name="url">
	</div></td>
</tr>
</table>
<p align="center">
<input type="reset" name="Reset" value="重置">
<input type="submit" name="Submit" value="提交">
</p>
</form>
</body>
</html>