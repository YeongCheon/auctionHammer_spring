<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.owasp.esapi.ESAPI"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${result == true}">
	<script type='text/javascript'>alert('회원가입이 정상적으로 완료되었습니다.환영합니다 \\n고갱님');location.href='${pageContext.request.contextPath}/index.do';</script>
</c:if>
<c:if test="${result == false}">
	<script type='text/javascript'>alert('회원가입에 실패하었습니다.');location.href='${pageContext.request.contextPath}/index.do';</script>
</c:if>