<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Login"/>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>
<div class="card auth">
  <h2>Login</h2>
  <c:if test="${param.error != null}"><div class="alert error">Invalid email or password.</div></c:if>
  <c:if test="${param.logout != null}"><div class="alert info">You have been logged out.</div></c:if>
  <c:if test="${param.registered != null}"><div class="alert success">Registration successful. Please log in.</div></c:if>
  <form action="${pageContext.request.contextPath}/login" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <label>Email</label>
    <input type="email" name="username" required autofocus>
    <label>Password</label>
    <input type="password" name="password" required>
    <div style="margin-top:20px"><button class="btn" type="submit">Login</button>
      <a class="btn btn-light" href="${pageContext.request.contextPath}/register">Create account</a></div>
  </form>
</div>
<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
