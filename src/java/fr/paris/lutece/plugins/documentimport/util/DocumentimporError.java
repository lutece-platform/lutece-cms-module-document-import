package fr.paris.lutece.plugins.documentimport.util;

public class DocumentimporError {

	
	   private int _nCountLine;
	   private int _nCountLineFailure;
	   private StringBuffer _strError;
	   private StringBuffer _strWarning;
	   private int _nwarning;
	   
	   
	   /**
	     * Count line
	     * @return count line
	     */
	    public int getCountLine(  )
	    {
	        return _nCountLine;
	    }

	    /**
	     * Count line
	     * @param nCountLine count line
	     */
	    public void setCountLine( int nCountLine )
	    {
	        _nCountLine = nCountLine;
	    }
	    /**
	     * Count warning
	     * @return count line
	     */
	    public int getCountWarning(  )
	    {
	        return _nwarning;
	    }

	    /**
	     * Count warning
	     * @param nCountLine count line
	     */
	    public void setCountWarning( int nCountWarning )
	    {
	    	_nwarning = nCountWarning;
	    }

	    /**
	     * Count line failure
	     * @return count line failure
	     */
	    public int getCountLineFailure(  )
	    {
	        return _nCountLineFailure;
	    }

	    /**
	     * Count line failure
	     * @param nCountLineFailure count line failure
	     */
	    public void setCountLineFailure( int nCountLineFailure )
	    {
	        _nCountLineFailure = nCountLineFailure;
	    }
	    
	    /**
	     * Error
	     * @return error
	     */
	    public StringBuffer getError(  )
	    {
	        return _strError;
	    }

	    /**
	     * Error
	     * @param strError error
	     */
	    public void setError( StringBuffer strError )
	    {
	        _strError = strError;
	    }
	    /**
	     * Warning
	     * @return warning
	     */
	    public StringBuffer getWarning(  )
	    {
	        return _strWarning;
	    }

	    /**
	     * Warning
	     * @param strWarnong warning
	     */
	    public void setWarning( StringBuffer strWarning )
	    {
	        _strWarning = strWarning;
	    }
}
