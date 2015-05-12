package fr.paris.lutece.plugins.documentimport.web;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import au.com.bytecode.opencsv.CSVReader;
import fr.paris.lutece.pligins.documentimport.service.DocumentimportService;
import fr.paris.lutece.plugin.documentimport.business.DocumentAttributHome;
import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.DocumentType;
import fr.paris.lutece.plugins.document.business.DocumentTypeHome;
import fr.paris.lutece.plugins.document.business.attributes.DocumentAttribute;
import fr.paris.lutece.plugins.document.business.spaces.DocumentSpaceHome;
import fr.paris.lutece.plugins.document.utils.IntegerUtils;
import fr.paris.lutece.plugins.documentimport.util.DocumentImport;
import fr.paris.lutece.plugins.documentimport.util.DocumentimporError;
import fr.paris.lutece.plugins.documentimport.util.DocumentimportUtils;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.fileupload.FileUploadService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.filesystem.FileSystemUtil;
import fr.paris.lutece.util.html.HtmlTemplate;


/**
 * Class of the StandaloneAppJspBean import document.
 */
public class DocumentimportJspBean extends PluginAdminPageJspBean{


	private static final long serialVersionUID = 1L;

	// Right
    public static final String RIGHT_MANAGEDOCUMENTIMPORT = "DOCUMENTIMPORT_MANAGEMENT";
    protected static final String PARAMETER_PAGE_INDEX = "page_index";
    protected static final String MARK_PAGINATOR = "paginator";
    protected static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_STR_ERROR = "str_error";
    private static final String MARK_STR_WARNING = "str_warning";
    private static final String MARK_STR_ERROR_COUNT_LINE = "str_nbr_error";
    private static final String MARK_STR_COUNT_LINE = "str_nbr_line";
    private static final String MARK_STR_WARNING_COUNT_LINE = "str_nbr_warning";
    
    //Variables
    protected int _nDefaultItemsPerPage;
    protected String _strCurrentPageIndex;
    protected int _nItemsPerPage;
    
	//Parameters
	 private static final String PARAMETER_FILE_IMPORT = "file_import";
	 private static final String PARAMETER_CODE_DOCUMENT = "codeDocument";
	 private static final String PARAMETER_SPACE_DOCUMENT = "spaceDocument";
	 private static final String PARAMETER_ID_TYPE_DOCUMENT = DocumentimportUtils.PARAMETER_ID_TYPE_DOCUMENT;
	  
	 private static final String FIELD_FILE_IMPORT = "documentimport.import_document_record.label_file";
	 
	// Messages (I18n keys)
	 private static final String MESSAGE_MANDATORY_FIELD = "documentimport.message.mandatory.field";
	 private static final String MESSAGE_ERROR_CSV_FILE_IMPORT = "documentimport.message.error_csv_file_import";
	 private static final String MESSAGE_ERROR_CSV_NUMBER_SEPARATOR = "documentimport.message.error.number.separartor";
	 private static final String MESSAGE_ERROR_CSV_HEAD = "documentimport.message.error.csv.head";
	 private static final String MESSAGE_ERROR_SEPACE = "documentimport.message.space.error";
	 private static final String MESSAGE_ERROR_DUPLICATE_DOC = "documentimport.message.duplicate.document";
	//Properties
	 private static final String PROPERTY_IMPORT_CSV_DELIMITER = "documentimport.import.csv.delimiter";
	 private static final String PROPERTY_ERROR_LINE = "documentimport.import_document.error.line";
	 
	// Misc
	 private static final String CONSTANT_EXTENSION_CSV_FILE = ".csv";
	 private static final String CONSTANT_MIME_TYPE_CSV = "application/csv";
	 private static final String CONSTANT_MIME_TYPE_TEXT_CSV = "text/csv";
	 private static final String CONSTANT_MIME_TYPE_OCTETSTREAM = "application/octet-stream";
	 
	 // JSP URL
	 private static final String  JSP_SUMMRY_IMPORT = "jsp/admin/plugins/documentimport/SummryImportCsv.jsp"; 
	 private static final String TEMPLATE_SUMMRY_IMPORT ="admin/plugins/documentimport/summrydocumentimport.html";
	
	 //session filed
	 public DocumentimporError documentError= new DocumentimporError();
	 
	 
	 
	 /** get view of summry 
	  * get the summry of import	
	  * @param request
	  * @return summry
	  */
	 public String getSummryImportFile( HttpServletRequest request )
	    {
	 		Map<String, Object> model = new HashMap<String, Object>(  );
	 		
	 		if(documentError.getError() != null || documentError.getWarning() != null){
			        model.put( MARK_STR_ERROR, documentError.getError(  ).toString(  ) );
			        model.put( MARK_STR_WARNING, documentError.getWarning().toString(  ) );
			        model.put( MARK_STR_COUNT_LINE, documentError.getCountLine() );
			        model.put( MARK_STR_ERROR_COUNT_LINE, documentError.getCountLineFailure() );
			        model.put( MARK_STR_WARNING_COUNT_LINE, documentError.getCountWarning());
	        }
	 		
	 		
	        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_SUMMRY_IMPORT, getLocale(  ), model );

	        return getAdminPage( templateList.getHtml(  ) );
	    }
	 
		/**
		    * Import Document
		    * @param request the Http Request
		    * @throws AccessDeniedException the {@link AccessDeniedException}
		    * @return The URL to go after performing the action
		 */
		 
	    public String doImportDocument( HttpServletRequest request )
	        throws AccessDeniedException
	    {
	    	
	    	List<String> headCsvFile= new ArrayList<String>();
	    	boolean firstLine= true;
	    	
	    	String strCodeDocument = request.getParameter( PARAMETER_CODE_DOCUMENT );
	    	String _strSpaceId = request.getParameter( PARAMETER_SPACE_DOCUMENT );
	    	
	        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	        FileItem fileItem = multipartRequest.getFile( PARAMETER_FILE_IMPORT );
	        String strMimeType = FileSystemUtil.getMIMEType( FileUploadService.getFileNameOnly( fileItem ) );

	        if ( ( fileItem == null ) || ( fileItem.getName(  ) == null ) ||
	        		DocumentimportUtils.EMPTY_STRING.equals( fileItem.getName(  ) ) )
	        {
	            
	            return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, AdminMessage.TYPE_STOP );
	        }

	        if ( ( !strMimeType.equals( CONSTANT_MIME_TYPE_CSV ) && !strMimeType.equals( CONSTANT_MIME_TYPE_OCTETSTREAM ) &&
	                !strMimeType.equals( CONSTANT_MIME_TYPE_TEXT_CSV ) ) ||
	                !fileItem.getName(  ).toLowerCase(  ).endsWith( CONSTANT_EXTENSION_CSV_FILE ) )
	        {
	        	
	            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_CSV_FILE_IMPORT, new String[] { "ddd" },AdminMessage.TYPE_ERROR );
	        }
	        
	        String strIdTypeDocument = request.getParameter( PARAMETER_ID_TYPE_DOCUMENT );
	        int nIdTypeDocument = DocumentimportUtils.convertStringToInt( strIdTypeDocument );
	      

	        Character strCsvSeparator = AppPropertiesService.getProperty( PROPERTY_IMPORT_CSV_DELIMITER ).charAt( 0 );
	        documentError.setError( new StringBuffer(  ) );
	        documentError.setWarning( new StringBuffer(  ) );
	        
	        int idDocumentAttribut= DocumentAttributHome.findIdDocumentAttributs(DocumentImport.IDENTIFIANT, strCodeDocument);
	        List<String> attributValue= DocumentAttributHome.findIdDocumentAttributs(idDocumentAttribut);
	        
	        
	        try
	        {
	            InputStreamReader inputStreamReader = new InputStreamReader( fileItem.getInputStream(  ) );
	            CSVReader csvReader = new CSVReader( inputStreamReader, strCsvSeparator, '\"' );

	            String[] nextLine;

	            documentError.setCountLine( 0 );
	            documentError.setCountLineFailure( 0 );
	            documentError.setCountWarning( 0 );
	            String[] requiredAttributs= getRequiredAttribut(strCodeDocument);
	            
	            while ( ( nextLine = csvReader.readNext(  ) ) != null )
	            {
	            	documentError.setCountLine( documentError.getCountLine(  ) + 1 );
	            	HashMap<String,String> valueCSVfile= new HashMap<String,String> ();

	                if ( nextLine.length < requiredAttributs.length )
	                {
	                	documentError.getError(  ).append( I18nService.getLocalizedString( PROPERTY_ERROR_LINE, getLocale(  ) ) );
	                	documentError.getError(  ).append( documentError.getCountLine(  ) );
	                	documentError.getError(  ).append( " > " );
	                	documentError.getError(  )
	                                 .append( I18nService.getLocalizedString( MESSAGE_ERROR_CSV_NUMBER_SEPARATOR,
	                            getLocale(  ) ) );
	                	documentError.getError(  ).append( "<br/>" );
	                	documentError.setCountLineFailure( documentError.getCountLineFailure(  ) + 1 );
	                	
	                	//return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_CSV_NUMBER_SEPARATOR, AdminMessage.TYPE_STOP );
	                }
	                
	               
	                else
	                {
	                	Document document = new Document(  );
	                	document.setCodeDocumentType(strCodeDocument);
	                	
	                    document.setStateId( 1 );
	                    document.setCreatorId( getUser(  ).getUserId(  ) );
	                	
	                	if( firstLine){
	                		
	            	    	for (String element: nextLine){
	            	    		
	            	    		headCsvFile.add(element.trim());
	            	    	}
	                		String attributFail= checkHead( requiredAttributs, headCsvFile, request );
	                		if(attributFail != null){
	                			
	    	    				documentError.getError(  ).append( I18nService.getLocalizedString( PROPERTY_ERROR_LINE, getLocale(  ) ) );
	    	                	documentError.getError(  ).append( documentError.getCountLine(  ) );
	    	                	documentError.getError(  ).append( " > " );
	    	                	documentError.getError(  )
	    	                                 .append( I18nService.getLocalizedString( MESSAGE_ERROR_CSV_NUMBER_SEPARATOR,
	    	                            getLocale(  ) ) );
	    	                	documentError.getError(  ).append( "<br/>" );
	    	                	documentError.setCountLineFailure( documentError.getCountLineFailure(  ) + 1 );
	    	                	
	    	                	 Object[] param = { attributFail };
	    	                	return AdminMessageService.getMessageUrl( request,  MESSAGE_ERROR_CSV_HEAD, param,AdminMessage.TYPE_STOP );
	                		} 		
	                		
	                		firstLine= false;
	                		
	                	}else if ( nextLine.length != headCsvFile.size() ){
	                		
		                	documentError.getError(  ).append( I18nService.getLocalizedString( PROPERTY_ERROR_LINE, getLocale(  ) ) );
		                	documentError.getError(  ).append( documentError.getCountLine(  ) );
		                	documentError.getError(  ).append( " > " );
		                	documentError.getError(  )
		                                 .append( I18nService.getLocalizedString( MESSAGE_ERROR_CSV_NUMBER_SEPARATOR,
		                            getLocale(  ) ) );
		                	documentError.getError(  ).append( "<br/>" );
		                	documentError.setCountLineFailure( documentError.getCountLineFailure(  ) + 1 );
		                	
		                	//return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_CSV_NUMBER_SEPARATOR, AdminMessage.TYPE_STOP );
		                }
	                	
	                	
	                	else{
	   

	                   /* try
	                    {*/
	                        for ( int i = 0; i < nextLine.length; i++ )
	                        {
	                        	valueCSVfile.put(headCsvFile.get(i), nextLine[i]);
	                        	
	                        }
	                        
	                        int idSpace= DocumentimportService.getInstance().getIdSpace(valueCSVfile.get(DocumentImport.ARRONDISSEMENT), valueCSVfile.get(DocumentImport.THEMATIQUE));
	                       
	                        
	                        
	                        if( idSpace == -1){
	                        	documentError.getError(  ).append( I18nService.getLocalizedString( PROPERTY_ERROR_LINE, getLocale(  ) ) );
			                	documentError.getError(  ).append( documentError.getCountLine(  ) );
			                	documentError.getError(  ).append( " > " );
			                	documentError.getError(  )
			                                 .append(I18nService.getLocalizedString( MESSAGE_ERROR_SEPACE, getLocale(  ) ));
			                	documentError.getError(  ).append( "<br/>" );
			                	documentError.setCountLineFailure( documentError.getCountLineFailure(  ) + 1 );
		                        
	                        }else if(checkExistanceDoc(attributValue, valueCSVfile.get(DocumentImport.IDENTIFIANT) )){
	                        	documentError.getError(  ).append( I18nService.getLocalizedString( PROPERTY_ERROR_LINE, getLocale(  ) ) );
			                	documentError.getError(  ).append( documentError.getCountLine(  ) );
			                	documentError.getError(  ).append( " > " );
			                	documentError.getError(  )
			                                 .append( I18nService.getLocalizedString(MESSAGE_ERROR_DUPLICATE_DOC, getLocale(  ) ));
			                	documentError.getError(  ).append( "<br/>" );
			                	documentError.setCountLineFailure( documentError.getCountLineFailure(  ) + 1 );
	                        
	                		}else{
	                        	document.setSpaceId( idSpace );                      
		                        String succes= DocumentimportService.getInstance().getDocumentData(request, document, valueCSVfile, documentError,getLocale(  ), getUser( ));
		                        
		                        if(succes == null){
		                        	attributValue.add(valueCSVfile.get(DocumentImport.IDENTIFIANT));
		                        }
	                        }
	                    }    
	                    }

	        	        /*
	                    catch ( DocumentimportException error )
	                    {
	                    	documentError.getError(  ).append( I18nService.getLocalizedString( PROPERTY_LINE, getLocale(  ) ) );
	                    	documentError.getError(  ).append( documentError.getCountLine(  ) );
	                    	documentError.getError(  ).append( " > " );

	                        if ( error.isMandatoryError(  ) )
	                        {
	                            Object[] tabRequiredFields = { error.getTitleField(  ) };
	                            documentError.getError(  )
	                                         .append( I18nService.getLocalizedString( 
	                                        		 MESSAGE_DOCUMENT_ERROR_MANDATORY_FIELD, tabRequiredFields, getLocale(  ) ) );
	                        }
	                        else
	                        {
	                            Object[] tabRequiredFields = { error.getTitleField(  ), error.getErrorMessage(  ) };
	                            documentError.getError(  )
	                                         .append( I18nService.getLocalizedString( MESSAGE_DIRECTORY_ERROR,
	                                    tabRequiredFields, getLocale(  ) ) );
	                        }

	                        documentError.getError(  ).append( "<br/>" );
	                        documentError.setCountLineFailure( documentError.getCountLineFailure(  ) + 1 );
	                    }*/
	                 
	                }
	           
	        }
	    
	        
	        
	        catch ( IOException e )
	        {
	            AppLogService.error( e );
	        }
	        
	     
	        return  getJspSummryImport(request);
	    }

	    
	    /**
	     * get attributs required
	     * @param codeDocumentType
	     * @return array of atributs
	     */
    
	    private String [] getRequiredAttribut(String codeDocumentType){
	    	
	    	  
	    	 DocumentType documentType = DocumentTypeHome.findByPrimaryKey( codeDocumentType );
	         List<DocumentAttribute> listAttributes = documentType.getAttributes(  );
	         int countRequiredAttribut= 0; 
	         List<String> attributs = new ArrayList<String>();
	         
	         attributs.add(DocumentImport.PARAMETER_TITLE);
	         attributs.add(DocumentImport.PARAMETER_SUMMARY);
	          
	          for ( DocumentAttribute attribute : listAttributes )
	          {
	             
	              if  ( attribute.isRequired(  )) 
	              {
	            	  countRequiredAttribut= countRequiredAttribut +1;
	            	  attributs.add(attribute.getName());
	              }
	          }
	         
	          String[] stockArr = new String[attributs.size()];
	          int i= 0;
	          for(Object s : attributs.toArray()){
	        	  stockArr[i]=(String) s;
	          	  i= i+1;
	          }
	          
	    	return stockArr ;
	    }
	    
	    
	    /**
	     * check head csvfile
	     * @param requiredAttributs
	     * @param head
	     * @param request
	     * @return 
	     */
	    private String checkHead(String [] requiredAttributs, List<String> head, HttpServletRequest request){
	    	
	    
	    	for (String attribut: requiredAttributs){
	    		
	    		if (!head.contains(attribut)) {
	    			
	    			return attribut;
	                	
	    		}
	    	}
	    	return null;
	    }
	    
	    
	    /**
	     * return url of the jsp manage doc
	     * @param request The HTTP request
	     * @return url of the jsp manage doc
	     */
	    private String getJspSummryImport( HttpServletRequest request)
	    {
	    	
	        return AppPathService.getBaseUrl( request ) + JSP_SUMMRY_IMPORT;
	    }
	    
	    
	    /**
	     * checks whether the document exists in the database
	     * @param attributValue
	     * @param valueAttribut
	     * @return bool
	     */
	    private Boolean checkExistanceDoc(List<String> attributValue, String valueAttribut ){
		  
           if(attributValue.contains(valueAttribut))return true;
           else return false;
           
           
	   }
	    
}
