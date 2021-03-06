/***********************************************************************************************
 * Egis
 * (C)opyright 2011 Ipsosenso - Tous droits réservés.
 * 
 * Code réalisé par Ipsosenso.
 *
 * @date 27 mai 2011 @author Arnaud -  TestEffetFichier.java
 ***********************************************************************************************/

package org.elusive.xml.effets;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.elusive.main.persistence.xml.XMLException;
import org.elusive.main.persistence.xml.XmlTools;
import org.elusive.sound.effets.Effet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


/**
 * @author Arnaud
 * 
 */
@RunWith(Parameterized.class)
public class TestEffet {

	@Parameters
	public static Collection<Object[]> getBlocClass() {
		Collection<Object[]> params = new ArrayList<Object[]>();
		for (Class<? extends Effet> c : Effet.liste) {
			params.add(new Object[] { c });
		}
		return params;
	}
	
	private Class<? extends Effet> c;
	private Effet b;

	/**
	 * 
	 */
	public TestEffet(Class<? extends Effet> c) {
		this.c = c;
	}
	
	@Test
	public void testInstance() throws InstantiationException, IllegalAccessException{
		b = c.newInstance();		
	}
	
	@Test
	public void testSave() throws InstantiationException, IllegalAccessException{
		testInstance();
		save();
	}
	
	private String save(){
		return XmlTools.toXML(b);		
	}
	
	@Test
	public void testReSave() throws InstantiationException, IllegalAccessException, XMLException{
		testInstance();
		String xml1 = save();
		Object obj = XmlTools.fromXML(xml1);
		String xml2 = XmlTools.toXML(obj);
		
		assertEquals(xml1, xml2);
	}
	
	

}
