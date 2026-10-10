<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Cast Your Vote"/>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>
<div class="card">
  <div class="row">
    <div style="flex:1">
      <h2 style="margin:0"><c:out value="${election.title}"/></h2>
      <p class="muted" style="margin:4px 0 0">Welcome, <b><c:out value="${me.fullName}"/></b> (Voter ID: <c:out value="${me.voterId}"/>)</p>
    </div>
    <c:choose>
      <c:when test="${election.votingOpen}"><span class="badge open">VOTING OPEN</span></c:when>
      <c:otherwise><span class="badge closed">VOTING CLOSED</span></c:otherwise>
    </c:choose>
  </div>
</div>

<c:choose>
  <c:when test="${me.hasVoted}">
    <div class="alert success">✔ You have already voted. Thank you for participating!</div>
    <a class="btn" href="${pageContext.request.contextPath}/results">View Results</a>
  </c:when>
  <c:when test="${not election.votingOpen}">
    <div class="alert info">Voting is not open right now. Please check back later.</div>
  </c:when>
  <c:otherwise>
    <div class="card">
      <h2>Candidates</h2>
      <c:if test="${empty candidates}"><p class="muted">No candidates available.</p></c:if>
      <c:forEach var="c" items="${candidates}">
        <div class="cand">
          <div class="info">
            <h3><c:out value="${c.name}"/></h3>
            <span class="badge no"><c:out value="${c.party}"/></span>
            <c:if test="${not empty c.manifesto}"><p><c:out value="${c.manifesto}"/></p></c:if>
          </div>
          <form action="${pageContext.request.contextPath}/vote" method="post"
                onsubmit="return confirm('Cast your vote? This cannot be undone.')">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <input type="hidden" name="candidateId" value="${c.id}">
            <button class="btn btn-ok" type="submit">Vote</button>
          </form>
        </div>
      </c:forEach>
    </div>
  </c:otherwise>
</c:choose>
<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
