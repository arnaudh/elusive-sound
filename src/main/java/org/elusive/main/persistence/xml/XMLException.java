/***********************************************************************************************
 * Egis
 * (C)opyright 2011 Ipsosenso - Tous droits réservés.
 * 
 * Code réalisé par Ipsosenso.
 *
 * @date 27 mai 2011 @author Arnaud -  XMLException.java
 ***********************************************************************************************/

package org.elusive.main.persistence.xml;

import java.io.File;

/**
 * @author Arnaud
 * 
 */
public class XMLException extends Exception {

	private File file;
	private String xmlLu;

	/**
	 * 
	 */
	public XMLException(Throwable throwable, String message, String xmlLu) {
		super(message, throwable);
		this.xmlLu = xmlLu;
	}

	public void setFilePath(File file) {
		this.file = file;
	}

	public File getFilePath() {
		return file;
	}

	public String getXmlLu() {
		return xmlLu;
	}

	public String toString() {
		String s = "\n";
		s += getMessage();
		s += '\n';
		if (file != null) {
			s += "******************** FILE ********************\n";
			s += file + "\n";
		}
		s += "******************** XML LU ********************\n";
		s += xmlLu + "\n";
		s += "**************************************************\n";
		return s;
	}

}
