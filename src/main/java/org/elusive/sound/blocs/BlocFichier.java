package org.elusive.sound.blocs;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.elusive.main.persistence.ResourceManager;
import org.elusive.sound.genetics.Geneticable;
import org.elusive.sound.tools.SoundFileReader;
import org.elusive.ui.exception.FileNotFoundManager;
import org.elusive.ui.fenetre.ElusivePanel;
import org.elusive.ui.grille.Grid;
import org.elusive.ui.tools.EditableLabel;
import org.elusive.ui.tools.EditablePanelListener;

public class BlocFichier extends Bloc {

	private File fichier = null;


	public BlocFichier() {
//		fichier = new File("../sounds/clic.wav");
	}

	public BlocFichier(String path) {
		this(new File(path));
	}

	public BlocFichier(File file) {
		fichier = file.getAbsoluteFile();
	}

	private transient float[] dataCache;

	public float[] generateData() {
		if (dataCache != null) {
			return dataCache;
		}
		
		if( !fichier.exists() ){
			File file = ResourceManager.toResourceFile(fichier.getName());
			if( file.exists() ){
				System.out.println("MISSING RESOURCE "+fichier+" FOUND in the resource folder");
				fichier = file;
			}
		}

		SoundFileReader reader = new SoundFileReader(fichier);
		reader.addProgressListener(this);
		
		try {
			reader.read();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
			return errorCase(e);
		} catch (FileNotFoundException e) {
			FileNotFoundManager man = new FileNotFoundManager(fichier);
			File newFile = man.manage();
			if (newFile == null) { // euh... tu veux pas relier à un son ?
				return errorCase(e);
			} else {
				this.fichier = newFile;
				return generateData(); // récursion (non infinie) possible si le fichier
				// spécifié par l'utilisateur n'existe
				// plus :p
			}
		} catch (IOException e) { //IO exceptions other than FileNotFound
			e.printStackTrace();
			return errorCase(e);
		}

		float[] leftData = reader.getLeftData();
		end = leftData.length;
		
		dataCache = leftData;
		return leftData;
	}
	
	public float[] errorCase(Exception ex) {
		errorGeneratingData = ex;
		return new float[44100];
	}

	/*
	 * public AudioFormat getFormat() { if (format == null) { AudioInputStream
	 * audioInputStream = null; try { audioInputStream =
	 * AudioSystem.getAudioInputStream(fichier); } catch
	 * (UnsupportedAudioFileException e) { e.printStackTrace(); } catch
	 * (IOException e) { e.printStackTrace(); } format =
	 * audioInputStream.getFormat(); } return format; }
	 */

	@Override
	public int resize(int s, int e) {
		//C'est mooooort, on resize pas !
		return dataCache.length;
//		int ret = super.resize(s, e);
//		if (start < 0) {
//			start = 0;
//			return 0;
//		}
//		if (end > nbFrames) {
//			end = (int) nbFrames;
//		}
//		dataCache = null;
//		return ret;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("BlocFichier[");
		sb.append("fichier = " + fichier);
		sb.append(']');
		return sb.toString();
	}

	@Override
	protected ElusivePanel createElusivePanel() {
		ElusivePanel panel = new ElusivePanel("File");

		if (errorGeneratingData == null) {

			String fileName = fichier.getName();
			int dotPosition = fileName.lastIndexOf(".");
			final String extension;
			final String nameWithoutExtension;
			if (dotPosition != -1) {
			    extension = fileName.substring(dotPosition);
			    nameWithoutExtension = fileName.substring(0, dotPosition);
			}else{
				extension = "";
				nameWithoutExtension = fileName;
			}

			EditableLabel panelLabel = new EditableLabel(new EditablePanelListener() {
				@Override
				public boolean textChanged(String text) {
					System.out.println("la = "+text);
					File f = new File(fichier.getParentFile().getAbsolutePath() + File.separator + text + extension);
					if (f.exists()) {
						int response = JOptionPane.showConfirmDialog(null, "The file " + f.getName() + " already exists.", "File Conflict", JOptionPane.YES_OPTION, JOptionPane.ERROR_MESSAGE);
						return false;
					}
					boolean renamed = fichier.renameTo(f);
					if (!renamed) {
						System.err.println("BlocFichier : rename didn't work");
						return false;
					}
					fichier = f; // So that next rename works... don't ask why

					return true;
				}
			}, nameWithoutExtension);
			panel.add(panelLabel);

		} else {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			errorGeneratingData.printStackTrace(pw);
			String str = sw.toString(); // stack trace as a string
			str = "<html>"+str.replace("\n", "<br>")+"</html>";
			
			JLabel label = new JLabel(str);
			label.setForeground(Color.red);
			panel.add(label);
		}

		return panel;
	}

	public File getFichier() {
		return fichier;
	}

	public void setFichier(File fichier) {
		this.fichier = fichier;
	}

	@Override
	public Geneticable mutate(double strength) {
		return new BlocFichier(fichier);
	}

}
