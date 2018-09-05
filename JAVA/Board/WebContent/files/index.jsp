<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage = "error.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<body>

	<h1 align="center">게시판</h1>
	<hr />
	<div align="center">
		<form name="loginform" method = "POST" action="control.jsp">
		<input type=hidden name="action" value="insert" />
			<table>
				<tbody>
					<tr>
						<td>아이디</td>
						<td><input type="text" name="id" /></td>
					</tr>
					<tr>
						<td>비밀번호</td>
						<td><input type="text" name="password" /></td>
					</tr>
					<tr>
						<td><input type="submit" value="회원가입" /></td>
						<td><input type="submit" value="로그인" /></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>

</body>
</html>