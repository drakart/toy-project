<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
#find_wrapper { 
    border: 1px solid black;
    padding: 5px 20px;
    position: absolute;
    top: 50%;
    left: 50%;
    width: 450px; height: 250px;
    margin-left: -220px;
    margin-top : -170px;
    
    display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
} 
h1{
	font-size: 25px;
	padding-bottom: 20px;
}
.form{
	width: 300px;
}
.form > div{
	background-color:lightgray;
	display: flex;
	justify-content: center;
	padding-bottom: 7px;
	align-items: center;
	min-height: 100px;
}

label{
	flex: 1;
	text-align: left;
}
button{
	float: none;
	padding: 3px;
	background:lightblue;
	border: none; 
	display: inline;
	height: 50px;width: 150px;
}
input {
	padding: 5px;
}
</style>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
</head>
<body>

<h1 style="text-align: center;">PCLASS TOY PROJECT</h1>


<c:if test="${empty authentication}">
	<h2><a href="/member/login-form">login</a></h2>
	<h2><a href="/member/join-form">회원가입</a></h2>
</c:if>

<c:if test="${not empty authentication}">
	<h2>${authentication.userId}님 안녕?</h2>
	<h2><a href="/member/logout">logout</a></h2>
	<h2><a href="/member/mypage">마이페이지</a></h2>
	<h2><a href="/admin/member/member-list">멤버리스트</a></h2>
	<h2><a href="/board/board-form">게시판 작성</a></h2>
</c:if>

</body>
</html>