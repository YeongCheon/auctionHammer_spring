<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<<c:if test="${isOk}">
	<script>alert('인증 성공');location.href='./modifyStep02.do';</script>
</c:if>
<<c:if test="${!isOk}">
	<script>alert('인증 실패');location.href='./modifyStep01.do';</script>
</c:if>
	
