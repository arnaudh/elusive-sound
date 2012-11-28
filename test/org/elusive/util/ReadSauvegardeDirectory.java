package org.elusive.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public abstract class ReadSauvegardeDirectory {

	private static File dir = new File("sauvegardes");

	@Parameters
	public static Collection<Object[]> getFiles() {
		Collection<Object[]> params = new ArrayList<Object[]>();
		for (File f : dir.listFiles()) {
			if (f.isFile() && f.getName().endsWith(".elu")) {
				Object[] arr = new Object[] { f };
				params.add(arr);
			}
		}
		return params;
	}

	protected File file;
	public ReadSauvegardeDirectory(File file) {
		this.file = file;
	}
}
