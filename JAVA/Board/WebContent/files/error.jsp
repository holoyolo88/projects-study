<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error</title>
</head>
<body>
<div>
Error
<hr/>
<%=out.println("An Error is Occurred. Please Try Again.")
%>
<%=exception %>
</div>
</body>
</html>