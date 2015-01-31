<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${inserted}}">
	<script>alert("글작성 성공!");<script>
</c:if>
<c:if test="${!inserted}">
	<script>alert("글작성 실패...");</script>
</c:if>

<script>location.href="./articleList.do";</script>