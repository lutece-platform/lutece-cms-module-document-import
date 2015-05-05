/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.documentimport.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.pligins.documentimport.service.DocumentimportService;
import fr.paris.lutece.plugins.document.business.DocumentType;
import fr.paris.lutece.plugins.document.business.DocumentTypeHome;
import fr.paris.lutece.plugins.document.business.spaces.DocumentSpace;
import fr.paris.lutece.plugins.document.business.spaces.DocumentSpaceHome;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;


/**
 * ManageDocumentimportexport JSP Bean abstract class for JSP Bean
 */
public  class ManageDocumentimportJspBean extends PluginAdminPageJspBean
{
	    // Right
	    public static final String RIGHT_MANAGEDOCUMENTIMPORT = "DOCUMENTIMPORT_MANAGEMENT";
	    protected static final String PARAMETER_PAGE_INDEX = "page_index";
	    protected static final String MARK_PAGINATOR = "paginator";
	    protected static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
	
	    //MARKER 
	    private static final String MARK_DOCUMENT_TYPES_LIST= "document_types_list";
	    private static final String MARK_DOCUMENT_SPACES_LIST= "document_spaces_list";
	    
	    //Variables
	    protected int _nDefaultItemsPerPage;
	    protected String _strCurrentPageIndex;
	    protected int _nItemsPerPage;
	    private static final String TEMPLATE_MANAGE_IMPORT ="admin/plugins/documentimport/managedocumentimport_tabs.html";
    
	    /**
	     * get form to import file
	     * @param request
	     * @return view of formulaire
	     */
	   public String getManageDocumentimportexportHome ( HttpServletRequest request ){
		   
		   Map<String, Object> model = new HashMap<String, Object>(  );
		   
		   Collection<DocumentSpace> docspace=DocumentSpaceHome.findAll(  );
		   for(DocumentSpace dcsp: docspace){
			   
			   if(dcsp.getResourceTypeCode().contains("")){
				   
				   
			   }
			   
			}
		   Collection<DocumentType> dc= DocumentTypeHome.findAll(  ) ;
		   
		   model.put( MARK_DOCUMENT_TYPES_LIST, dc);
		   
		   
		   model.put( MARK_DOCUMENT_SPACES_LIST,  docspace);
	
	       HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_IMPORT, getLocale(  ), model );
	
	       return getAdminPage( templateList.getHtml(  ) );
   }
    
    
}
