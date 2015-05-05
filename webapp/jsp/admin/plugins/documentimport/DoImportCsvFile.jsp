<%@ page errorPage="../../ErrorPage.jsp" %>


<jsp:useBean id="documentimport" scope="session" class="fr.paris.lutece.plugins.documentimport.web.DocumentimportJspBean" />
<% documentimport.init( request, documentimport.RIGHT_MANAGEDOCUMENTIMPORT );
response.sendRedirect( documentimport.doImportDocument(request) );
%>
