package org.elusive.main.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;

public class IOtools {

	public static String readFile(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			StringBuilder sb = new StringBuilder();
			String ligne;
			while ((ligne = br.readLine()) != null) {
				sb.append(ligne);
				sb.append('\n');
			}
			br.close();
			isr.close();
			fis.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void writeToFile(File file, String str) {
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(str);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void copyfile(File sourceFile, File destFile) {
		try {
			if (!destFile.exists()) {
				destFile.createNewFile();
			}
			FileChannel source = null;
			FileChannel destination = null;
			try {
				source = new FileInputStream(sourceFile).getChannel();
				destination = new FileOutputStream(destFile).getChannel();
				long count = 0;
				long size = source.size();
				while ((count += destination.transferFrom(source, 0, size
						- count)) < size)
					;
			} finally {
				if (source != null) {
					source.close();
				}
				if (destination != null) {
					destination.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File copyFileInDirectory(File file, File directory)
			throws IOException {
		String fileName = file.getName();
		String name = fileName;
		String extension = "";
		int dotPosition = fileName.lastIndexOf(".");
		if (dotPosition != -1) {
			extension = fileName.substring(dotPosition);
			name = fileName.substring(0, dotPosition);
		}
		File ressourceFile = IOtools.createTempFile(name, extension, directory);
		IOtools.copyfile(file, ressourceFile);
		return ressourceFile;
	}

	public static String getFileExtension(File file) {
		String fileName = file.getName();
		int dotPosition = fileName.lastIndexOf(".");
		String extension = "";
		if (dotPosition != -1) {
			extension = fileName.substring(dotPosition);
		}
		return extension;
	}

	public static File createTempFile(String prefix, String suffix,
			File directory) throws IOException {
		File ret = null;
		// try the name directly (File.createTempFile doesn't do that)
		File f = new File(directory.getAbsolutePath() + File.separator + prefix
				+ suffix);
		if (f.exists()) {
			ret = File.createTempFile(prefix + " - ", suffix, directory);
		} else {
			ret = f;
		}
		System.out.println("IOtools.createTemporaryFile() - file created : "
				+ ret);
		return ret;
	}

	public static File getTemporaryFolder() {
		return new File(System.getProperty("java.io.tmpdir"));
	}
}
