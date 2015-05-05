package fr.paris.lutece.plugin.documentimport.business;

import java.util.List;

import fr.paris.lutece.portal.service.spring.SpringContextService;

public final class DocumentAttributHome {

	
	
	 // Static variable pointed at the DAO instance
    private static IDocumentAttributDAO _dao = SpringContextService.getBean( "documentimport.documentAttributDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DocumentAttributHome(  )
    {
    }
    
    
    public static int findIdDocumentAttributs(String documentTypeAttrName , String codeDocumentType)
    {
        return _dao.findIdDocumentAttributs(documentTypeAttrName, codeDocumentType);
    }
    
    
    public static List<String> findIdDocumentAttributs(int idDocumentAttribut)
    {
        return _dao.findValueAttributs(idDocumentAttribut);
    }
    
}
