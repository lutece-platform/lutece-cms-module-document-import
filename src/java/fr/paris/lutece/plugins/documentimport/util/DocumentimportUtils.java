package fr.paris.lutece.plugins.documentimport.util;

import fr.paris.lutece.portal.service.util.AppLogService;

public final class DocumentimportUtils {

	public static final String EMPTY_STRING = "";

	// PARAMETERS
    public static final String PARAMETER_ID_TYPE_DOCUMENT = "id_type_document";
	
    
    private static final String REGEX_ID = "^[\\d]+$";
    /**
     * convert a string to int
     *
     * @param strParameter
     *            the string parameter to convert
     * @return the conversion
     */
    public static int convertStringToInt( String strParameter )
    {
        int nIdParameter = -1;

        try
        {
            if ( strParameter != null )
            {
                String strTrimedParameter = strParameter.trim(  );

                if ( strTrimedParameter.matches( REGEX_ID ) )
                {
                    nIdParameter = Integer.parseInt( strTrimedParameter );
                }
            }
        }
        catch ( NumberFormatException ne )
        {
            AppLogService.error( ne );
        }

        return nIdParameter;
    }

}
