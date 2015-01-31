<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:choose>
	<c:when test="${inserted == true }">
		<script>alert('상풍이 정상적으로 등록되었습니다.');location.href="./auctionArticleList.do";</script>	
	</c:when>
	<c:otherwise>
		<script>alert('상풍 등룩 중 문제가 발생하였습니다.');location.href="./insertAuctionForm.do";</script>	
	</c:otherwise>
</c:choose>