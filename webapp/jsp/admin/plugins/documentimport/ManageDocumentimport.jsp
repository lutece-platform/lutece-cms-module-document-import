<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="managedocumentimportexport" scope="session" class="fr.paris.lutece.plugins.documentimport.web.ManageDocumentimportJspBean" />

<% managedocumentimportexport.init( request, managedocumentimportexport.RIGHT_MANAGEDOCUMENTIMPORT ); %>
<%= managedocumentimportexport.getManageDocumentimportexportHome ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
