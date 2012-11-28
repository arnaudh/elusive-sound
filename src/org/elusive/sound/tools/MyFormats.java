package org.elusive.sound.tools;

import javax.sound.sampled.AudioFormat;

public class MyFormats {

	static float sampleRate = 44100;
    static int sampleSizeInBits = 16;
    static int channels = 1;
    static boolean signed = true;
    static boolean bigEndian = true;

    public static AudioFormat mono(){
    	return new AudioFormat(sampleRate, 
  	  	      sampleSizeInBits, 1, signed, !bigEndian);
    }
    
    public static AudioFormat out(){
    	return new AudioFormat(sampleRate, 
  	  	      16, 1, signed, !bigEndian);
    }
    
    public static AudioFormat stereo(){
    	return new AudioFormat(sampleRate, 
  	  	      sampleSizeInBits, 2, signed, !bigEndian);
    }
    

}
