package fr.paris.lutece.plugins.documentimport.util;

/**
 * 
 * DocumentimportException
 *
 */
public class DocumentimportException extends Exception{
	
		private static final long serialVersionUID = 6610609149888544149L;
	    private String _strTitleField;
	    private String _strErrorMessage;
	    private boolean _bMandatoryError;

	    /**
	     * Creates a new DocumentimportException
	     * @param strTitleField The title of the filed that caused the error
	     * @param strErrorMessage The error message
	     */
	    public DocumentimportException( String strTitleField, String strErrorMessage )
	    {
	        _strTitleField = strTitleField;
	        _strErrorMessage = strErrorMessage;
	        _bMandatoryError = false;
	    }

	    /**
	     * Creates a new DocumentimportException
	     * @param strTitleField The title of the filed that caused the error
	     */
	    public DocumentimportException( String strTitleField )
	    {
	        _strTitleField = strTitleField;
	        _bMandatoryError = true;
	    }

	    /**
	     * return true if the error is a mandatory error
	     * @return true if the error is a mandatory error
	     */
	    public boolean isMandatoryError(  )
	    {
	        return _bMandatoryError;
	    }

	    /**
	     * set true if the error is a mandatory error
	     * @param mandatoryError true if the error is a mandatory error
	     */
	    public void setMandatoryError( boolean mandatoryError )
	    {
	        _bMandatoryError = mandatoryError;
	    }

	    /**
	     * Gets the error Message
	     * @return the error Message
	     */
	    public String getErrorMessage(  )
	    {
	        return _strErrorMessage;
	    }

	    /**
	     * set the error message
	     * @param errorMessage the erroer message
	     */
	    public void setErrorMessage( String errorMessage )
	    {
	        _strErrorMessage = errorMessage;
	    }

	    /**
	     *
	     * @return the title of the field
	     */
	    public String getTitleField(  )
	    {
	        return _strTitleField;
	    }

	    /**
	     * set the title of the field
	     * @param titleField the title of the field
	     */
	    public void setTitleField( String titleField )
	    {
	        _strTitleField = titleField;
	    }

}
