package org.elusive.sound.play;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.elusive.ui.fenetre.Fenetre;



public class DataPlayer {
	
	
	//Sound
	private SourceDataLine sourceDataLine;

	public DataPlayer(AudioFormat audioFormat){
//		System.out.println("new DataPlayer("+audioFormat+")");
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		try {
//			System.out.println("DataPlayer.DataPlayer()");
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
//			System.out.println("DataPlayer.DataPlayer1()");
			sourceDataLine.open();
//			System.out.println("DataPlayer.DataPlayer()2");
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public int play(byte[] data){
		if( !sourceDataLine.isRunning() ){
			sourceDataLine.start();
		}
		int numByteRead = sourceDataLine.write(data, 0, data.length);
		return numByteRead;
	}


	public void play(float[] data) {
		play(data, 0, data.length);
	}
	

	public void play(float[] dataToPlay, int debut, int taille) {
		byte[] dada = new byte[2*taille];
		for(int b = 0; b < taille; b++){
			int val = (int) ((dataToPlay[b+debut])*(-Short.MIN_VALUE));
			byte b1 = (byte)( val >> 8);
			byte b2 = (byte)( (val << 8) >> 8 );
			/*
			byte b3 = (byte)( (val << 16) >> 24 );
			byte b4 = (byte)( (val << 24) >> 24 );
			dada[4*b+2] = b3;
			dada[4*b+3] = b4;
			*/
			dada[2*b] = b2;
			dada[2*b+1] = b1;
			if( Math.random() < 0.1 ){
				//System.out.println("data["+(debut+b)+"] = "+val+", b1 = "+Main.byteToHex(b1)+", b2 = "+Main.byteToHex(b2));
			}
		}
		play(dada);
		
	}


	public void stop() {
		sourceDataLine.stop();
	}


	public void flush() {
		sourceDataLine.flush();
	}
	
	
}
