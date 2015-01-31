<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${!updated}">
	<script type='text/javascript'>alert('정보가 정상적으로 수정되었습니다.');location.href='${pageContext.request.contextPath}/index.do';</script>
</c:if>
<c:if test="${updated}">
	<script type='text/javascript'>alert('정보수정에 실패하였습니다.');location.href='${pageContext.request.contextPath}/index.do';</script>
</c:if>