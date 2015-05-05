<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="documentimport" scope="session" class="fr.paris.lutece.plugins.documentimport.web.DocumentimportJspBean" />
<%= documentimport.getSummryImportFile(request)%>
<%@ include file="../../AdminFooter.jsp" %>