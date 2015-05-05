package fr.paris.lutece.plugin.documentimport.business;

import java.util.List;

public interface IDocumentAttributDAO {
	
	/**
	 * 
	 * @param documentTypeAttrName
	 * @param codeDocumentType
	 * @return
	 */
	int findIdDocumentAttributs(String documentTypeAttrName, String codeDocumentType);
	
	
	/**
	 * return attributs value
	 * @param idDocumentAttribut
	 * @return
	 */
	List<String> findValueAttributs(int idDocumentAttribut); 

}
