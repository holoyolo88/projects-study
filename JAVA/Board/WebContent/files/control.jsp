<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import = "source.*" errorPage = "error.jsp"%>
    
<% request.setCharacterEncoding("UTF-8"); %>

<jsp:useBean id="post" class = "Board.source.Post" />
<jsp:setProperty name ="post" property = "*" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Control</title>
</head>
<body>
<%  
String action = request.getParameter("action");
if(action.equals("list")){
	
}
else if(action.equals("insert")){
	
}
else if (action.equals("edit")){
	
}
else if(action.equals("delete")){
	
}
else if(action.equals("update")){
	
}
%>

</body>
</html>