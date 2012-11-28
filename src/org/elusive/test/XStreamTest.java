package org.elusive.test;

import java.io.File;

import org.elusive.main.persistence.xml.XMLException;
import org.elusive.main.persistence.xml.XmlTools;
import org.elusive.sound.enveloppes.Enveloppe;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamTest {

	/**
	 * @param args
	 * @throws XMLException 
	 */
	public static void main(String[] args) throws XMLException {
		// TODO Auto-generated method stub
		Object fromXML = XmlTools.fromXML("<f>110</f>");
		System.out.println(fromXML);
	}
	
	//readResolve : pour faire des choses après initialisation des attributs d'un objet
	
	public XStreamTest() {
		XStream xstream = new XStream(new DomDriver());
//		xstream.alias("synth", SynthetiseurFM.class);

		File file = new File("sauvegardes/enveloppes/test.fq");

		Enveloppe env = Enveloppe.createEnveloppeADSR();
		XmlTools.toFile( env, file);

		/*
		OperateurFM ope = new OperateurFM(100, 440);
		OperateurFM ope2 = new OperateurFM(100, 10);
		ope.add(ope2);
		SynthetiseurFM synth = new SynthetiseurFM(ope );
		
		String xml = xstream.toXML(synth);
		
		System.out.println(xml);
		
		SynthetiseurFM synth2 = (SynthetiseurFM)xstream.fromXML(xml);
		
		System.out.println("*******************");
		System.out.println("*******************");

		
		String xml2 = xstream.toXML(synth2);
		System.out.println(xml2);
		

		System.out.println("*******************");
		System.out.println("*******************");
		
		
		System.out.println("EQUALS = "+xml.equals(xml2));

		System.out.println("PANEL = "+synth2.getPanelBloc());
		
		 */
		
	}

}
