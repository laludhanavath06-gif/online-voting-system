<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Home"/>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>
<div class="card hero">
  <h1>Secure Online Elections</h1>
  <p>Register with your Voter ID, log in securely and cast your vote once. Results are published as soon as the election closes.</p>
  <c:choose>
    <c:when test="${not empty pageContext.request.userPrincipal}">
      <a class="btn" href="${pageContext.request.contextPath}/dashboard">Go to Dashboard</a>
    </c:when>
    <c:otherwise>
      <a class="btn" href="${pageContext.request.contextPath}/register">Register as Voter</a>
      <a class="btn btn-light" href="${pageContext.request.contextPath}/login">Login</a>
    </c:otherwise>
  </c:choose>
</div>
<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
