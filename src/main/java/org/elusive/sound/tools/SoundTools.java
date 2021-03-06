package org.elusive.sound.tools;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.TargetDataLine;

import org.elusive.sound.blocs.BlocFichier;

public class SoundTools {

	private static float MAX = -Short.MIN_VALUE;
	
	public static void writeDataToFile( float[] data, File file){
		int n = data.length;
		byte[] audioData = new byte[n * 2];
		
		
		for( int i = 0; i < n; i++){
			short sample = (short) (data[i] * MAX);
			audioData[2*i] = (byte) sample;
			audioData[2*i+1] = (byte) (sample >>> 8);
		}
		System.out.println("SoundTools.writeDataToFile() byte length : "+audioData.length);
		
	      //Get an input stream on the byte array
	      // containing the data
	      InputStream byteArrayInputStream =
	                        new ByteArrayInputStream(
	                                      audioData);

	      //Get the required audio format
	      AudioFormat audioFormat = MyFormats.mono();

	      //Get an audio input stream from the
	      // ByteArrayInputStream
	      AudioInputStream audioInputStream = new AudioInputStream(
	                    byteArrayInputStream,
	                    audioFormat,
	                    audioData.length/audioFormat.
	                                 getFrameSize());
		try{
	          AudioSystem.write(
	                    audioInputStream,
	                    AudioFileFormat.Type.WAVE,
	                    file);
	        }catch (Exception e) {
	          e.printStackTrace();
	          System.exit(0);
	        }//end catch

	}
	
	private static final int MASK = 0xff;

	public static byte[] floatToByteArray(float f){
		int i = Float.floatToIntBits(f);
		return intToByteArray(i);
	}
	
	public static byte[] intToByteArray(int param){
		byte[] result = new byte[4];
		for(int i = 0; i < 4; i++){
			int offset = (3 - i) * 8;
			result[i] = (byte) ((param >> offset ) & MASK);
		}
		return result;		
	}
	
	
	public static void main(String[] args) {
		
		File file = new File("test.wav");
		BlocFichier b = new BlocFichier(file);
		float[] data = b.generateData();
		
		System.out.println("data size : "+data.length);

//		for(int i = 0; i < 10; i++){
//			System.out.println("data["+i+"] = "+data[i]);
//		}
		
		file = new File("testOut.wav");
		writeDataToFile(data, file);
		b = new BlocFichier(file);
		data = b.generateData();
		
		System.out.println("data size : "+data.length);

//		for(int i = 0; i < 10; i++){
//			System.out.println("data["+i+"] = "+data[i]);
//		}
		
//
//		byte b1 = data[2 * n];
//		byte b2 = data[2 * n + 1];
//		int i1 = (b1) & 0xFF;
//		int i2 = ((b2) << 8);
//		leftData[n] = ((short) (i1 + i2)) / (MAX);
		
	}

	
	public static void printLineStatus( String info, TargetDataLine line ){
		System.out.println("###### "+info+" ######");
		System.out.println("IS OPEN ="+line.isOpen());
		System.out.println("IS ACTIVE  =" + line.isActive());
		System.out.println("IS RUNNING ="+line.isRunning());
		System.out.println("###########################");
	}
}
