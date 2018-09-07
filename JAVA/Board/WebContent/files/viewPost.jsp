<%@ page language="java" contentType="text/html; 
    pageEncoding="UTF-8" errorPage = "error.jsp import = "java.util.*, Board.source.Post" %>
<jsp:useBean id = "posts" scope="request" class=java.util.ArrayList"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>view posts</title>
</head>
<body>
<h1 align="center">게시판</h1>
	<hr />
	<div>
	<% for(Post prevpost : (ArrayList<Post>)posts) %>{
	<%=prevpost.getpost() %>
	<%= "by"+prevpost.getId() %>}
	</div>



</body>
</html>