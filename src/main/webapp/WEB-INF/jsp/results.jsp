<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Results"/>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>
<div class="card">
  <h2><c:out value="${election.title}"/> — Results</h2>
  <c:choose>
    <c:when test="${hidden}">
      <div class="alert info">Voting is still in progress. Results will be published once the election is closed.</div>
    </c:when>
    <c:otherwise>
      <c:if test="${election.votingOpen}"><div class="alert info">Live count (visible to admin only while voting is open).</div></c:if>
      <c:if test="${not empty winner}">
        <div class="alert success">
          <c:choose>
            <c:when test="${tie}">🤝 The top positions are currently tied.</c:when>
            <c:otherwise>🏆 <b><c:out value="${winner.name}"/></b> (<c:out value="${winner.party}"/>) is leading.</c:otherwise>
          </c:choose>
        </div>
      </c:if>
      <table>
        <thead><tr><th>#</th><th>Candidate</th><th>Party</th><th>Votes</th><th style="width:30%">Share</th></tr></thead>
        <tbody>
          <c:forEach var="r" items="${rows}" varStatus="st">
            <tr>
              <td>${st.count}</td>
              <td><c:out value="${r.candidate.name}"/></td>
              <td><c:out value="${r.candidate.party}"/></td>
              <td><b>${r.votes}</b></td>
              <td>
                <div class="bar"><span style="width:${r.percent}%"></span></div>
                <small class="muted"><fmt:formatNumber value="${r.percent}" maxFractionDigits="1"/>%</small>
              </td>
            </tr>
          </c:forEach>
          <c:if test="${empty rows}"><tr><td colspan="5" class="muted">No candidates.</td></tr></c:if>
        </tbody>
      </table>
      <p class="muted">Total votes cast: <b>${totalVotes}</b></p>
    </c:otherwise>
  </c:choose>
</div>
<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
