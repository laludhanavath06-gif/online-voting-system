<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Voter Registration"/>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>
<div class="card auth">
  <h2>Voter Registration</h2>
  <form action="${pageContext.request.contextPath}/register" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <label>Full Name</label>
    <input type="text" name="fullName" value="<c:out value='${fullName}'/>" required>
    <label>Email</label>
    <input type="email" name="email" value="<c:out value='${email}'/>" required>
    <label>Voter ID</label>
    <input type="text" name="voterId" value="<c:out value='${voterId}'/>" placeholder="e.g. ABC1234567" required>
    <label>Date of Birth</label>
    <input type="date" name="dateOfBirth" required>
    <label>Password</label>
    <input type="password" name="password" minlength="6" required>
    <label>Confirm Password</label>
    <input type="password" name="confirmPassword" minlength="6" required>
    <div style="margin-top:20px"><button class="btn" type="submit">Register</button>
      <a class="btn btn-light" href="${pageContext.request.contextPath}/login">Already registered?</a></div>
  </form>
</div>
<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
