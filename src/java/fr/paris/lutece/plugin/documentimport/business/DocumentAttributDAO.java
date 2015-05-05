package fr.paris.lutece.plugin.documentimport.business;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.util.sql.DAOUtil;

public final class DocumentAttributDAO implements IDocumentAttributDAO {

	private static final String SQL_QUERY_SELECT_ATTRIBUTES_OF_DOCUMENT_TYPE = " SELECT a.id_document_attr" +
	        " FROM document_type_attr a" + " WHERE a.document_type_attr_name = ?" + " AND a.code_document_type= ?";
	
	private static final String SQL_QUERY_SELEC_VALUE_ATTRIBUTE = " SELECT d.text_value" +
	        " FROM document_content d" + " WHERE d.id_document_attr = ?";
	
	@Override
	public int findIdDocumentAttributs(String documentTypeAttrName,
			String codeDocumentType) {
			
		   int id= -1;
		   DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ATTRIBUTES_OF_DOCUMENT_TYPE );
	       daoUtil.setString( 1, documentTypeAttrName );
	       daoUtil.setString( 2, codeDocumentType );
	       daoUtil.executeQuery(  );
	       if ( daoUtil.next(  ) )
	        {
	    	   id = daoUtil.getInt( 1 );
	        }
	       
	       daoUtil.free(  );
	       
	       return id;
	}

	@Override
	public List<String> findValueAttributs(int idDocumentAttribut) {
		
		 List<String> listAttributs = new ArrayList<String>();
		 DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELEC_VALUE_ATTRIBUTE );
	     daoUtil.setInt( 1, idDocumentAttribut );
	     
	       daoUtil.executeQuery(  );
	       while ( daoUtil.next(  ) )
	        {
	    	   listAttributs.add(daoUtil.getString( 1 ));
	        }
	      
	       daoUtil.free(  );
	       
		return listAttributs;
	}
	

}
