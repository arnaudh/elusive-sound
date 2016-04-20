package org.elusive.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public abstract class ReadTabsDirectory {

	private static File dir = new File("tabs");

	@Parameters
	public static Collection<Object[]> getFiles() {
		Collection<Object[]> params = new ArrayList<Object[]>();
		for (File f : dir.listFiles()) {
			if (f.isFile()) {
				Object[] arr = new Object[] { f };
				params.add(arr);
			}
		}
		return params;
	}

	protected File file;
	public ReadTabsDirectory(File file) {
		this.file = file;
	}
}
