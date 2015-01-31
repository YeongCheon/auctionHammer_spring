<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${!inserted}">
	<script>alert("댓글작성 성공!");</script>
</c:if>
<c:if test="${inserted}">
	<script>alert("댓글작성 실패...");</script>
</c:if>

<script>location.href="./articleDetail.do?article_id="+"${article_id}";</script>