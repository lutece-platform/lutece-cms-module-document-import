package fr.paris.lutece.plugins.documentimport.util;

import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class DocumentImport {
	

	// Variables declarations
		  public static final String PARAMETER_ID_DOCUMENT= "_nIdDocument";
		  public static final String PARAMETER_SUMMARY ="__strSummary";
		  public static final String PARAMAETER_COMMENT= "_strComment";
		  public static final String PARAMAETER_DATE_VALID_BEGIN= "_dateValidityBegin";
		  public static final String PARAMETER_DATE_VALID_END= "_dateValidityEnd";
		  public static final String PARAMETER_DATE_MAILING_LIST="_strMailingListId";
		  public static final String PARAMETER_TITLE = "title";
		  public static final String PARAMETER_DESCRIPTION = "description";
		  public static final String PARAMETER_TEMPLATE_DOCUMENT_ID = "_nIdPageTemplateDocument";
		  
		  public static final String IDENTIFIANT = AppPropertiesService.getProperty( "documentimport.identifiant" );
		  public static final String ARRONDISSEMENT = AppPropertiesService.getProperty( "documentimport.arrondissement" );
		  public static final String THEMATIQUE = AppPropertiesService.getProperty( "documentimport.thematique" );
	
	
}
