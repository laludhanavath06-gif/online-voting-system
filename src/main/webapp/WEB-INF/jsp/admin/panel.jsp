<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Admin Panel"/>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>
<div class="stats">
  <div class="stat"><b>${totalVoters}</b>Registered Voters</div>
  <div class="stat"><b>${totalVoted}</b>Votes Cast</div>
  <div class="stat"><b>${turnout}%</b>Turnout</div>
  <div class="stat"><b>${candidates.size()}</b>Candidates</div>
</div>

<div class="card">
  <div class="row">
    <div style="flex:1">
      <h2 style="margin:0"><c:out value="${election.title}"/></h2>
      <p class="muted" style="margin:4px 0 0">Status:
        <c:choose>
          <c:when test="${election.votingOpen}"><span class="badge open">OPEN</span></c:when>
          <c:otherwise><span class="badge closed">CLOSED</span></c:otherwise>
        </c:choose>
      </p>
    </div>
    <form action="${pageContext.request.contextPath}/admin/election/toggle" method="post">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
      <button class="btn ${election.votingOpen ? 'btn-danger' : 'btn-ok'}" type="submit">
        ${election.votingOpen ? 'Close Voting & Publish Results' : 'Open Voting'}
      </button>
    </form>
  </div>
</div>

<div class="card">
  <h2>Candidate Management</h2>
  <form action="${pageContext.request.contextPath}/admin/candidates" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <div class="row">
      <div style="flex:1"><label>Name</label><input type="text" name="name" required></div>
      <div style="flex:1"><label>Party</label><input type="text" name="party" required></div>
    </div>
    <label>Manifesto (optional)</label>
    <textarea name="manifesto" rows="2" maxlength="500"></textarea>
    <div style="margin-top:14px"><button class="btn" type="submit">Add Candidate</button></div>
  </form>
  <table style="margin-top:20px">
    <thead><tr><th>Name</th><th>Party</th><th></th></tr></thead>
    <tbody>
      <c:forEach var="c" items="${candidates}">
        <tr>
          <td><c:out value="${c.name}"/></td>
          <td><c:out value="${c.party}"/></td>
          <td>
            <form class="inline" action="${pageContext.request.contextPath}/admin/candidates/${c.id}/delete" method="post"
                  onsubmit="return confirm('Remove this candidate?')">
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
              <button class="btn btn-sm btn-danger" type="submit">Delete</button>
            </form>
          </td>
        </tr>
      </c:forEach>
      <c:if test="${empty candidates}"><tr><td colspan="3" class="muted">No candidates yet.</td></tr></c:if>
    </tbody>
  </table>
</div>

<div class="card">
  <h2>Registered Voters</h2>
  <table>
    <thead><tr><th>Name</th><th>Email</th><th>Voter ID</th><th>Voted?</th></tr></thead>
    <tbody>
      <c:forEach var="v" items="${voters}">
        <tr>
          <td><c:out value="${v.fullName}"/></td>
          <td><c:out value="${v.email}"/></td>
          <td><c:out value="${v.voterId}"/></td>
          <td><span class="badge ${v.hasVoted ? 'yes' : 'no'}">${v.hasVoted ? 'Yes' : 'No'}</span></td>
        </tr>
      </c:forEach>
      <c:if test="${empty voters}"><tr><td colspan="4" class="muted">No voters registered yet.</td></tr></c:if>
    </tbody>
  </table>
  <p class="muted" style="margin-bottom:0">Ballots are stored separately from voter accounts' "voted" flag; the admin panel never shows who voted for whom.</p>
</div>
<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
