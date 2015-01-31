{
<%@ page language="java" contentType="text/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:choose>
	<c:when test="${inserted == true }">
		"price" : "<fmt:formatNumber value="${price}" type="CURRENCY" groupingUsed="true" />",
		"price_number" : "${price}"
	</c:when>
	<c:otherwise>
		"price" : "0"
		"price_number" : "0"
	</c:otherwise>
</c:choose>
}