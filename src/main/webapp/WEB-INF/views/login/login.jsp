<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:choose>
	<c:when test="${logined == true }">
		<script>alert('환영합니다 ${sessionScope.userName} 님');location.href="../index.do";</script>	
	</c:when>
	<c:otherwise>
		<script>alert('아이디 혹은 패스워드가 잘못되었습니다.');location.href="./loginForm.do";</script>	
	</c:otherwise>
</c:choose>