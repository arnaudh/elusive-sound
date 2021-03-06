/***********************************************************************************************
 * Egis
 * (C)opyright 2011 Ipsosenso - Tous droits réservés.
 * 
 * Code réalisé par Ipsosenso.
 *
 * @date 27 mai 2011 @author Arnaud -  TestBlocFichier.java
 ***********************************************************************************************/

package org.elusive.xml.blocs;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.elusive.main.persistence.xml.XMLException;
import org.elusive.main.persistence.xml.XmlTools;
import org.elusive.sound.blocs.Bloc;
import org.elusive.util.XmlTestTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.*;

/**
 * @author Arnaud
 * 
 */
@RunWith(Parameterized.class)
public class TestBloc {

	@Parameters
	public static Collection<Object[]> getBlocClass() {
		Collection<Object[]> params = new ArrayList<Object[]>();
		for (Class<? extends Bloc> c : Bloc.liste) {
			params.add(new Object[] { c });
		}
		return params;
	}
	
	private Class<? extends Bloc> c;
	private Bloc b;

	/**
	 * 
	 */
	public TestBloc(Class<? extends Bloc> c) {
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
	
	@Test
	public void testDoesntContainPaths() throws InstantiationException, IllegalAccessException{
		testInstance();
		String str = save();
		boolean bool = XmlTestTools.containsProjectPaths(str);
		if( XmlTestTools.containsProjectPaths(str) ){
			fail("XML contains paths for "+c+", xml :\n"+str);
		}
	}
	
	
	@Test
	public void testReSave() throws InstantiationException, IllegalAccessException, XMLException{
		testInstance();
		String xml1 = save();
		Object obj = XmlTools.fromXML(xml1);
		String xml2 = XmlTools.toXML(obj);

		assertEquals(xml1, xml2);
	}
	
	
	

	private String save(){
		return XmlTools.toXML(b);		
	}

}
