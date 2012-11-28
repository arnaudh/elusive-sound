package org.elusive.tab;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.elusive.main.tools.IOtools;
import org.elusive.sound.melody.tabs.Couplet;
import org.elusive.sound.melody.tabs.GuitarTab;
import org.elusive.util.ReadTabsDirectory;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

public class TabCompare extends ReadTabsDirectory {
	

	private String filePrefix;
	private String fileContent;
	private String tabString;
	private String tabString2;

	public TabCompare(File file) {
		super(file);
	}

	@Test
	public void testSameAsReadTab() {
		System.out.println("file : " + file);
		filePrefix = "\n ******* " + file + " *******\n\n";

		fileContent = IOtools.readFile(file);

		GuitarTab tab = new GuitarTab(fileContent);
		tabString = tab.toString();
		System.out.println(tabString);
		GuitarTab tab2 = new GuitarTab(GuitarTab.CoupletsToString(tab.getCouplets()));
		tabString2 = tab2.toString();
		
		assertEquals(tab.getCouplets().size(), tab2.getCouplets().size());
		
		for( int i = 0; i < tab.getCouplets().size(); i++){
			assertEquals(tab.getCouplets().get(i).size(), tab2.getCouplets().get(i).size());
		}

//		assertEquals(filePrefix + tabString, filePrefix + tabString2);

	}

	// @Test
	// public void testSameAsOriginalTab(){
	// testSameAsReadTab();
	// //quasi-impossible ! (juste pour voir quels notations de tablatures j'ai
	// zappé...)
	// assertEquals(filePrefix+tabString, filePrefix+fileContent);
	// }

}
