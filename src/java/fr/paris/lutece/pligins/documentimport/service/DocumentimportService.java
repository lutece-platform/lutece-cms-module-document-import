package fr.paris.lutece.pligins.documentimport.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.DocumentHome;
import fr.paris.lutece.plugins.document.business.DocumentType;
import fr.paris.lutece.plugins.document.business.DocumentTypeHome;
import fr.paris.lutece.plugins.document.business.attributes.DocumentAttribute;
import fr.paris.lutece.plugins.document.business.category.Category;
import fr.paris.lutece.plugins.document.business.category.CategoryHome;
import fr.paris.lutece.plugins.document.business.spaces.DocumentSpace;
import fr.paris.lutece.plugins.document.business.spaces.DocumentSpaceHome;
import fr.paris.lutece.plugins.document.service.AttributeManager;
import fr.paris.lutece.plugins.document.service.AttributeService;
import fr.paris.lutece.plugins.document.service.DocumentException;
import fr.paris.lutece.plugins.document.service.DocumentService;
import fr.paris.lutece.plugins.document.service.metadata.MetadataHandler;
import fr.paris.lutece.plugins.document.service.spaces.SpaceResourceIdService;
import fr.paris.lutece.plugins.document.utils.IntegerUtils;
import fr.paris.lutece.plugins.documentimport.util.DocumentImport;
import fr.paris.lutece.plugins.documentimport.util.DocumentimporError;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.string.StringUtil;

public class DocumentimportService {


	// Variables declarations
	
	 private static final String PROPERTY_WARNING_LINE = "documentimport.import_document.warning.line";
	 private static final String PROPERTY_ERROR_LINE = "documentimport.import_document.error.line";
	 
	 private static final String MESSAGE_ERROR_CSV_MANDATORY_FIELD= "documentimport.message.mandatory.required";
	 private static final String MESSAGE_INVALID_DATEBEGIN = "documentimport.invalid.date.begin";
	 private static final String MESSAGE_INVALID_DATEEND = "documentimport.invalid.date.end";
	 private static final String MESSAGE_INVALID_DATE_BEFORE_70 = "documentimport.invalid.date.before";
	 private static final String MESSAGE_ERROR_DATEEND_BEFORE_DATEBEGIN= "documentimport.invalid.date.end.befor.begin";
	 private static final String MESSAGE_WARNING_CSV_ILLEGAL_CHARACTER= "documentimport.warning.illegal.char";
	 
	 
	 private static final String ERROR= "error";
	 private static DocumentimportService _singleton = new DocumentimportService();
	
	    
    /** Creates a new instance of DocumentimportService */
    private DocumentimportService(  )
    {
      //  _managerEventListeners = SpringContextService.getBean( "document.documentEventListernersManager" );
    }

	/**
     * Return the data of a document object
     * @param mRequest The MultipartHttpServletRequest
     * @param document The document object
     * @param locale The locale
     * @return data of document object
     */
    public  String  getDocumentData(HttpServletRequest mRequest, Document document,  HashMap<String,String> valueAttribute, DocumentimporError _docError,Locale locale, AdminUser user )
    {
        String strDocumentTitle = valueAttribute.get(DocumentImport.PARAMETER_TITLE);
        String strDocumentSummary = valueAttribute.get(DocumentImport.PARAMETER_SUMMARY);
        String strDocumentComment = valueAttribute.get(DocumentImport.PARAMAETER_COMMENT);
        String strDateValidityBegin = valueAttribute.get(DocumentImport.PARAMAETER_DATE_VALID_BEGIN);
        String strDateValidityEnd = valueAttribute.get(DocumentImport.PARAMETER_DATE_VALID_END);
        String strMailingListId = valueAttribute.get(DocumentImport.PARAMETER_DATE_MAILING_LIST);
        int nMailingListId = IntegerUtils.convert( strMailingListId, 0 );
        String strPageTemplateDocumentId = valueAttribute.get(DocumentImport.PARAMETER_TEMPLATE_DOCUMENT_ID);
        int nPageTemplateDocumentId = IntegerUtils.convert( strPageTemplateDocumentId, 0 );
        //String[] arrayCategory = mRequest.getParameterValues( PARAMETER_CATEGORY );
        String[] arrayCategory = null;

        // Check for mandatory value
        if ( StringUtils.isBlank( strDocumentSummary ) )
        {
         _docError.getWarning(  ).append( I18nService.getLocalizedString( PROPERTY_WARNING_LINE, locale ) );
       	 _docError.getWarning(  ).append( _docError.getCountLine(  ) );
       	 _docError.getWarning(  ).append( " > " );
       	 _docError.getWarning(  )
                         .append( DocumentImport.PARAMETER_TITLE +" :"+  I18nService.getLocalizedString(MESSAGE_ERROR_CSV_MANDATORY_FIELD  ,
                       		  locale) );
       	 _docError.getWarning(  ).append( "<br/>" );
       	 _docError.setCountWarning( _docError.getCountWarning() + 1 );
       	 
       //	 return "error";
        }
        if (StringUtils.isBlank( strDocumentSummary ) )
        {
         _docError.getWarning(  ).append( I18nService.getLocalizedString( PROPERTY_WARNING_LINE, locale ) );
       	 _docError.getWarning(  ).append( _docError.getCountLine(  ) );
       	 _docError.getWarning(  ).append( " > " );
       	 _docError.getWarning(  )
                         .append( DocumentImport.PARAMETER_SUMMARY  + " :" +  I18nService.getLocalizedString(MESSAGE_ERROR_CSV_MANDATORY_FIELD  ,
                       		  locale  ));
       	 _docError.getWarning(  ).append( "<br/>" );
       	 _docError.setCountWarning( _docError.getCountWarning() + 1 );
       	 
       //	 return "error";
        }

        // Check for illegal character character
        if ( StringUtil.containsHtmlSpecialCharacters( strDocumentTitle ) )
        {
        	 _docError.getWarning(  ).append( I18nService.getLocalizedString( PROPERTY_WARNING_LINE, locale ) );
          	 _docError.getWarning(  ).append( _docError.getCountLine(  ) );
          	 _docError.getWarning(  ).append( " > " );
          	 _docError.getWarning(  )
                            .append(  DocumentImport.PARAMETER_TITLE + " :" + I18nService.getLocalizedString(MESSAGE_WARNING_CSV_ILLEGAL_CHARACTER  ,
                          		  locale) );
          	 _docError.getWarning(  ).append( "<br/>" );
          	 _docError.setCountWarning( _docError.getCountWarning() + 1 );
          	 
        }
     // Check for illegal character character
        if ( StringUtil.containsHtmlSpecialCharacters( strDocumentSummary ) )
        {
        	 _docError.getWarning(  ).append( I18nService.getLocalizedString( PROPERTY_WARNING_LINE, locale ) );
          	 _docError.getWarning(  ).append( _docError.getCountLine(  ) );
          	 _docError.getWarning(  ).append( " > " );
          	 _docError.getWarning(  )
                            .append( DocumentImport.PARAMETER_SUMMARY + " :" + I18nService.getLocalizedString( MESSAGE_WARNING_CSV_ILLEGAL_CHARACTER  ,
                          		  locale)  );
          	 _docError.getWarning(  ).append( "<br/>" );
          	_docError.setCountWarning( _docError.getCountWarning() + 1 );
        }

        DocumentType documentType = DocumentTypeHome.findByPrimaryKey( document.getCodeDocumentType(  ) );
        List<DocumentAttribute> listAttributes = documentType.getAttributes(  );

        for ( DocumentAttribute attribute : listAttributes )
        {
            String strAdminMessage = setAttribute( attribute, document, valueAttribute,_docError,locale );

            if ( strAdminMessage != null )
            {
                return ERROR;
            }
        }

        Timestamp dateValidityBegin = null;
        Timestamp dateValidityEnd = null;

        if ( ( strDateValidityBegin != null ) && !strDateValidityBegin.equals( "" ) )
        {
            Date dateBegin = DateUtil.formatDateLongYear( strDateValidityBegin, locale );

            if ( ( dateBegin == null ) )
            {
            	 _docError.getWarning(  ).append( I18nService.getLocalizedString( PROPERTY_WARNING_LINE, locale ) );
              	 _docError.getWarning(  ).append( _docError.getCountLine(  ) );
              	 _docError.getWarning(  ).append( " > " );
              	 _docError.getWarning(  )
                                .append( DocumentImport.PARAMAETER_DATE_VALID_BEGIN + " :" + I18nService.getLocalizedString( MESSAGE_INVALID_DATEBEGIN  ,
                              		  locale) );
              	_docError.getWarning(  ).append( "<br/>" );
              	_docError.setCountWarning( _docError.getCountWarning() + 1 );
               
            }

            dateValidityBegin = new Timestamp( dateBegin.getTime(  ) );

            if ( dateValidityBegin.before( new Timestamp( 0 ) ) )
            {
            	 _docError.getWarning(  ).append( I18nService.getLocalizedString( PROPERTY_WARNING_LINE, locale ) );
              	 _docError.getWarning(  ).append( _docError.getCountLine(  ) );
              	 _docError.getWarning(  ).append( " > " );
              	 _docError.getWarning(  )
                                .append( DocumentImport.PARAMAETER_DATE_VALID_BEGIN + " :" +  I18nService.getLocalizedString( MESSAGE_INVALID_DATE_BEFORE_70  ,
                              		  locale) );
              	_docError.getWarning(  ).append( "<br/>" );
              	_docError.setCountWarning( _docError.getCountWarning() + 1 );
              	
            }
        }

        if ( ( strDateValidityEnd != null ) && !strDateValidityEnd.equals( "" ) )
        {
            Date dateEnd = DateUtil.formatDateLongYear( strDateValidityEnd, locale );

            if ( ( dateEnd == null ) )
            {
            	 _docError.getWarning(  ).append( I18nService.getLocalizedString( PROPERTY_WARNING_LINE, locale ) );
              	 _docError.getWarning(  ).append( _docError.getCountLine(  ) );
              	 _docError.getWarning(  ).append( " > " );
              	 _docError.getWarning(  )
                                .append( DocumentImport.PARAMETER_DATE_VALID_END+ " :" + I18nService.getLocalizedString( MESSAGE_INVALID_DATEEND  ,
                              		  locale) );
              	_docError.getWarning(  ).append( "<br/>" );
              	_docError.setCountWarning( _docError.getCountWarning() + 1 );
              	
            }

            dateValidityEnd = new Timestamp( dateEnd.getTime(  ) );

            if ( dateValidityEnd.before( new Timestamp( 0 ) ) )
            {
            	 _docError.getWarning(  ).append( I18nService.getLocalizedString( PROPERTY_WARNING_LINE, locale ) );
              	 _docError.getWarning(  ).append( _docError.getCountLine(  ) );
              	 _docError.getWarning(  ).append( " > " );
              	 _docError.getWarning(  )
                                .append( DocumentImport.PARAMETER_DATE_VALID_END + " :" + I18nService.getLocalizedString( MESSAGE_INVALID_DATE_BEFORE_70  ,
                              		  locale)   );
              	_docError.getWarning(  ).append( "<br/>" );
              	_docError.setCountWarning( _docError.getCountWarning() + 1 );
              
            }
        }

        //validate period (dateEnd > dateBegin )
        if ( ( dateValidityBegin != null ) && ( dateValidityEnd != null ) )
        {
            if ( dateValidityEnd.before( dateValidityBegin ) )
            {
            	 _docError.getWarning(  ).append( I18nService.getLocalizedString( PROPERTY_WARNING_LINE, locale ) );
              	 _docError.getWarning(  ).append( _docError.getCountLine(  ) );
              	 _docError.getWarning(  ).append( " > " );
              	 _docError.getWarning(  )
                                .append( DocumentImport.PARAMETER_DATE_VALID_END + ", "+  DocumentImport.PARAMAETER_DATE_VALID_BEGIN + " :" +  I18nService.getLocalizedString(
                                		MESSAGE_ERROR_DATEEND_BEFORE_DATEBEGIN  ,
                              		  locale) );
              	_docError.getWarning(  ).append( "<br/>" );
              	_docError.setCountWarning( _docError.getCountWarning() + 1 );
            	
            }
        }

        document.setTitle( strDocumentTitle );
        document.setSummary( strDocumentSummary );
        document.setComment( strDocumentComment );
        document.setDateValidityBegin( dateValidityBegin );
        document.setDateValidityEnd( dateValidityEnd );
        document.setMailingListId( nMailingListId );
        document.setPageTemplateDocumentId( nPageTemplateDocumentId );

        MetadataHandler hMetadata = documentType.metadataHandler(  );

        if ( hMetadata != null )
        {
            document.setXmlMetadata( hMetadata.getXmlMetadata( mRequest.getParameterMap(  ) ) );
        }

        document.setAttributes( listAttributes );

        //Categories
        List<Category> listCategories = new ArrayList<Category>(  );

        if ( arrayCategory != null )
        {
            for ( String strIdCategory : arrayCategory )
            {
                listCategories.add( CategoryHome.find( IntegerUtils.convert( strIdCategory ) ) );
            }
        }

        document.setCategories( listCategories );

        try {
			this.createDocument(document, user);
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return null; // No error
    }
    
    /**
     * Update the specify attribute with the mRequest parameters
     *
     * @param attribute The {@link DocumentAttribute} to update
     * @param document The {@link Document}
     * @param mRequest The multipart http request
     * @param locale The locale
     * @return an admin message if error or null else
     */
    private  String setAttribute( DocumentAttribute attribute, Document document, HashMap<String,String> valueAttribute , DocumentimporError _docError,
        Locale locale )
    {
        String strParameterStringValue = valueAttribute.get(attribute.getName());
       // FileItem fileParameterBinaryValue = mRequest.getFile( attribute.getCode(  ) );
       // String strIsUpdatable = mRequest.getParameter( PARAMETER_ATTRIBUTE_UPDATE + attribute.getCode(  ) );
       // String strToResize = mRequest.getParameter( attribute.getCode(  ) + PARAMETER_CROPPABLE );
       // boolean bIsUpdatable = ( ( strIsUpdatable == null ) || strIsUpdatable.equals( "" ) ) ? false : true;
       // boolean bToResize = ( ( strToResize == null ) || strToResize.equals( "" ) ) ? false : true;

        if ( strParameterStringValue != null ) // If the field is a string
        {
            // Check for mandatory value
            if ( attribute.isRequired(  ) && strParameterStringValue.trim(  ).equals( "" ) )
            {
            	
            	 _docError.getWarning(  ).append( I18nService.getLocalizedString( PROPERTY_WARNING_LINE, locale ) );
            	 _docError.getWarning(  ).append( _docError.getCountLine(  ) );
            	 _docError.getWarning(  ).append( " > " );
            	 _docError.getWarning(  )
                              .append( attribute.getName(  )  + " :" +  I18nService.getLocalizedString( MESSAGE_ERROR_CSV_MANDATORY_FIELD  ,
                            		  locale));
            	 _docError.getWarning(  ).append( "<br/>" );
            	 _docError.setCountWarning(_docError.getCountWarning( ) + 1 );
                return null;
            }

            // Check for specific attribute validation
            AttributeManager manager = AttributeService.getInstance(  ).getManager( attribute.getCodeAttributeType(  ) );
            String strValidationErrorMessage = manager.validateValue( attribute.getId(  ), strParameterStringValue,
                    locale );

            if ( strValidationErrorMessage != null )
            {
	                
	             _docError.getError().append( I18nService.getLocalizedString( PROPERTY_ERROR_LINE, locale ) );
	           	 _docError.getError(  ).append( _docError.getCountLine(  ) );
	           	 _docError.getError(  ).append( " > " );
	           	 _docError.getError(  ).append( attribute.getName(  )+": " + strValidationErrorMessage );
	           	 _docError.getError(  ).append( "<br/>" );
	           	 
	           	_docError.setCountLineFailure( _docError.getCountLineFailure(  ) + 1 );
	               
	           	 return ERROR;
	            	
            }

            attribute.setTextValue( strParameterStringValue );
        }
    /*    else if ( fileParameterBinaryValue != null ) // If the field is a file
        {
            attribute.setBinary( true );

            String strContentType = fileParameterBinaryValue.getContentType(  );
            byte[] bytes = fileParameterBinaryValue.get(  );
            String strFileName = fileParameterBinaryValue.getName(  );
            String strExtension = FilenameUtils.getExtension( strFileName );

            AttributeManager manager = AttributeService.getInstance(  ).getManager( attribute.getCodeAttributeType(  ) );

            if ( !bIsUpdatable )
            {
                // there is no new value then take the old file value
                DocumentAttribute oldAttribute = document.getAttribute( attribute.getCode(  ) );

                if ( ( oldAttribute != null ) && ( oldAttribute.getBinaryValue(  ) != null ) &&
                        ( oldAttribute.getBinaryValue(  ).length > 0 ) )
                {
                    bytes = oldAttribute.getBinaryValue(  );
                    strContentType = oldAttribute.getValueContentType(  );
                    strFileName = oldAttribute.getTextValue(  );
                    strExtension = FilenameUtils.getExtension( strFileName );
                }
            }

            List<AttributeTypeParameter> parameters = manager.getExtraParametersValues( locale, attribute.getId(  ) );

            String extensionList = StringUtils.EMPTY;

            if ( CollectionUtils.isNotEmpty( parameters ) &&
                    CollectionUtils.isNotEmpty( parameters.get( 0 ).getValueList(  ) ) )
            {
                extensionList = parameters.get( 0 ).getValueList(  ).get( 0 );
            }

            // Check for mandatory value
            if ( attribute.isRequired(  ) && ( ( bytes == null ) || ( bytes.length == 0 ) ) )
            {
                return AdminMessageService.getMessageUrl( mRequest, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }
            else if ( StringUtils.isNotBlank( extensionList ) && !extensionList.contains( strExtension ) )
            {
                Object[] params = new Object[2];
                params[0] = attribute.getName(  );
                params[1] = extensionList;

                return AdminMessageService.getMessageUrl( mRequest, MESSAGE_EXTENSION_ERROR, params,
                    AdminMessage.TYPE_STOP );
            }

            // Check for specific attribute validation
            String strValidationErrorMessage = manager.validateValue( attribute.getId(  ), strFileName, locale );

            if ( strValidationErrorMessage != null )
            {
                String[] listArguments = { attribute.getName(  ), strValidationErrorMessage };

                return AdminMessageService.getMessageUrl( mRequest, MESSAGE_ATTRIBUTE_VALIDATION_ERROR, listArguments,
                    AdminMessage.TYPE_STOP );
            }

       /*     if ( bToResize && !ArrayUtils.isEmpty( bytes ) )
            {
                // Resize image
                String strWidth = mRequest.getParameter( attribute.getCode(  ) + PARAMETER_WIDTH );

                if ( StringUtils.isBlank( strWidth ) || !StringUtils.isNumeric( strWidth ) )
                {
                    String[] listArguments = 
                        {
                            attribute.getName(  ),
                            I18nService.getLocalizedString( MESSAGE_ATTRIBUTE_WIDTH_ERROR, mRequest.getLocale(  ) )
                        };

                    return AdminMessageService.getMessageUrl( mRequest, MESSAGE_ATTRIBUTE_VALIDATION_ERROR,
                        listArguments, AdminMessage.TYPE_STOP );
                }

                try
                {
                    bytes = ImageUtils.resizeImage( bytes, Integer.valueOf( strWidth ) );
                }
                catch ( IOException e )
                {
                    return AdminMessageService.getMessageUrl( mRequest, MESSAGE_ATTRIBUTE_RESIZE_ERROR,
                        AdminMessage.TYPE_STOP );
                }
            }

       //     attribute.setBinaryValue( bytes );
            attribute.setValueContentType( strContentType );
            attribute.setTextValue( strFileName );
        }*/

        return null;
    }

    
    /**
     * Create a new document
     *
     * @param document The document
     * @param user The user doing the action
     * @throws DocumentException raise when error occurs in event or rule
     */
    public void createDocument( Document document, AdminUser user)
        throws DocumentException
    {
    	DocumentService.getInstance().createDocument(document, user);
       /* document.setId( DocumentHome.newPrimaryKey(  ) );
        document.setDateCreation( new Timestamp( new java.util.Date(  ).getTime(  ) ) );
        document.setDateModification( new Timestamp( new java.util.Date(  ).getTime(  ) ) );
        
        DocumentHome.create( document );*/
    }
    
    /**
     * Get the unique instance of the service
     *
     * @return The unique instance
     */
    public static DocumentimportService getInstance(  )
    {
        return _singleton;
    }
    
    /**
     * get user space 
     * @param user
     * @return documents space
     */
    public Collection<DocumentSpace> getUserSpaces( AdminUser user )
    {
        Collection<DocumentSpace> listSpaces = DocumentSpaceHome.findAll(  );
        listSpaces = RBACService.getAuthorizedCollection( listSpaces, SpaceResourceIdService.PERMISSION_VIEW, user );

        return listSpaces;
    }
    
    
    /**
     * get identifiant of the space
     * @param nameSpaceParent
     * @param nameSpaceFils
     * @return ID
     */
    public int getIdSpace(String nameSpaceParent, String nameSpaceFils){
    	
    	
    	if(nameSpaceParent != null || nameSpaceFils!= null ){
    		String ident= getIdSpaceParent(nameSpaceParent);
    		if(ident == null){
    			return -1;
    		}
	    	List<DocumentSpace> docSpace= DocumentSpaceHome.findChilds(Integer.parseInt(ident));
	    	
	    	for(DocumentSpace item: docSpace ){
	    		
	    		if(item.getName().equals(nameSpaceFils)) return item.getId();
	    	}
    	}
    	return -1;
    }
    
    /**
     * get parent identifiant of the space
     * @param nameSpace
     * @return ID
     */
    private String getIdSpaceParent(String nameSpace){
    	
    	ReferenceList listSpace= DocumentSpaceHome.getDocumentSpaceList(  );
    	
    	for(ReferenceItem item: listSpace ){
    		
    		if(item.getName().equals(nameSpace)) return item.getCode();
    	}
    	
    	return null;
    }
}
