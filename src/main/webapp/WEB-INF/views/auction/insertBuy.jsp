<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:choose>
	<c:when test="${inserted}">
		<script>alert('구매 완료!!!');location.href="./auctionArticleList.do";</script>	
	</c:when>
	<c:otherwise>
		<script>alert('구매실패!!');location.href="./insertAuctionForm.do";</script>	
	</c:otherwise>
</c:choose>