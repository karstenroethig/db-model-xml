package karstenroethig.db.plugin.utils;

import org.apache.commons.lang3.StringUtils;


public class TextUtils {

    /**
     * Convert line breaks to html <code>&lt;br&gt;</code> tag.
     *
     * @param   s  the String to convert
     *
     * @return  the converted string
     */
    public static final String br( String s ) {
        s = noNull( s );

        StringBuffer str = new StringBuffer();

        for( int i = 0; i < s.length(); i++ ) {

            if( s.charAt( i ) == '\n' ) {
                str.append( "<br/>" );
            }

            str.append( s.charAt( i ) );
        }

        return str.toString();
    }

    /**
     * Escape html entity characters and high characters (eg "curvy" Word quotes).
     *
     * <p>Note this method can also be used to encode XML</p>
     */
    public static final String htmlEncode( String s ) {
        return htmlEncode( s, true );
    }

    /**
     * Escape html entity characters and high characters (eg "curvy" Word quotes). Note this method can also be used to
     * encode XML.
     *
     * @param   s                   the String to escape.
     * @param   encodeSpecialChars  if true high characters will be encode other wise not.
     *
     * @return  the escaped string
     */
    public static final String htmlEncode( String s, boolean encodeSpecialChars ) {
        s = noNull( s );

        StringBuffer str = new StringBuffer();

        for( int j = 0; j < s.length(); j++ ) {
            char c = s.charAt( j );

            // encode standard ASCII characters into HTML entities where needed
            if( c < '\200' ) {

                switch( c ) {

                    case '"' :
                        str.append( "&quot;" );

                        break;

                    case '&' :
                        str.append( "&amp;" );

                        break;

                    case '<' :
                        str.append( "&lt;" );

                        break;

                    case '>' :
                        str.append( "&gt;" );

                        break;

                    case '§' :
                        str.append( "&sect;" );

                        break;

                    default :
                        str.append( c );
                }
            }
            // encode 'ugly' characters (ie Word "curvy" quotes etc)
            else if( encodeSpecialChars && ( c < '\377' ) ) {
                String hexChars = "0123456789ABCDEF";
                int a = c % 16;
                int b = ( c - a ) / 16;
                String hex = "" + hexChars.charAt( b ) + hexChars.charAt( a );
                str.append( "&#x" + hex + ";" );
            }
            //add other characters back in - to handle charactersets
            //other than ascii
            else {
                str.append( c );
            }
        }

        return str.toString();
    }

    /**
     * Finds all leading spaces on each line and replaces it with an HTML space (&amp;nbsp;).
     *
     * @param   s  string containing text to replaced with &amp;nbsp;
     *
     * @return  the new string
     */
    public static final String leadingSpaces( String s ) {
        s = noNull( s );

        StringBuffer str = new StringBuffer();
        boolean justAfterLineBreak = true;

        for( int i = 0; i < s.length(); i++ ) {

            if( justAfterLineBreak ) {

                if( s.charAt( i ) == ' ' ) {
                    str.append( "&nbsp;" );
                } else {
                    str.append( s.charAt( i ) );
                    justAfterLineBreak = false;
                }
            } else {

                if( s.charAt( i ) == '\n' ) {
                    justAfterLineBreak = true;
                }

                str.append( s.charAt( i ) );
            }
        }

        return str.toString();
    }

    /**
     * Converts plain text to html code.
     *
     * <ul>
     * <li>escapes appropriate characters</li>
     * <li>puts in line breaks</li>
     * </ul>
     *
     * @param   str  - String containing the plain text.
     *
     * @return  the escaped string
     */
    public static final String plainTextToHtml( String str ) {
        return plainTextToHtml( str, true );
    }

    /**
     * Converts plain text to html code.
     *
     * <ul>
     * <li>escapes appropriate characters</li>
     * <li>puts in line breaks</li>
     * </ul>
     *
     * @param   str                 - String containing the plain text.
     * @param   encodeSpecialChars  - if true high characters will be encode other wise not.
     *
     * @return  the escaped string
     */
    public static final String plainTextToHtml( String str, boolean encodeSpecialChars ) {
        str = noNull( str );

        //First, convert all the special chars...
        str = htmlEncode( str, encodeSpecialChars );

        //Convert all leading whitespaces
        str = leadingSpaces( str );

        //Then convert all line breaks...
        str = br( str );

        return str;
    }

    public static final String noNull( String string ) {
        return noNull( string, StringUtils.EMPTY );
    }

    public static final String noNull( String string, String defaultString ) {

        if( StringUtils.isNotBlank( string ) ) {
            return string;
        }

        return defaultString;
    }
    
    public String shorten( String string, int length ) {
    	
    	if( StringUtils.isBlank( string ) ) {
    		return StringUtils.EMPTY;
    	}
    	
    	string = StringUtils.replace( string, "\n", " " );
    	
    	if( string.length() > length ) {
    		return StringUtils.substring( string, 0, length - 3 ) +  "...";
    	}
    	
    	return string;
    }

}
