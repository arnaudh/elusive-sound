package org.elusive.main.persistence;

import java.io.File;

import org.elusive.main.tools.IOtools;

public class ResourceManager {
	
	private static File resourcesDir;
	public static File getResourcesDir(){
		if( resourcesDir == null ){
			// need intern temp directory
			File dir = IOtools.getTemporaryFolder();
			resourcesDir = new File(dir.getAbsolutePath() + File.separator + "Elusive");
		}
		if (!resourcesDir.isDirectory()) {
			boolean b = resourcesDir.mkdir();
			if (!b) {
				System.err.println("Fenetre : couldn't mkdir the directory : " + resourcesDir);
				return null;
			}
		}
		return resourcesDir;
	}

	// Called when project opened
	public static void setProjectFile(File savedFile) {
		String parent = savedFile.getParent();
		String res = parent + File.separator + "resources";
		resourcesDir = new File(res);
		if( !resourcesDir.exists() ){
			System.err.println("ResourcesDir doesn't exist");
		}
		System.out.println("ResourceManager.setProjectFile("+savedFile+") : resources updated to "+resourcesDir);
	}
	
	public static File toResourceFile(String filename){
		return new File(resourcesDir.getPath() +File.separator + filename);
	}

}
