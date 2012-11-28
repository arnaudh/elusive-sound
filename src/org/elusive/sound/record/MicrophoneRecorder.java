package org.elusive.sound.record;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.TargetDataLine;

import org.elusive.ui.action.MyAction;
import org.elusive.ui.grille.Grid;
import org.elusive.ui.resources.Icons;

public class MicrophoneRecorder {

	private AudioFormat audioFormat;
	private TargetDataLine targetDataLine;
	private File audioFile;

	private boolean recording = false;
	
	//Actions
	private MyAction recordAction;
	private MyAction stopRecordAction;
	
	//Listeners
	private List<MicrophoneRecorderListener> listeners = new ArrayList<MicrophoneRecorderListener>();

	public MicrophoneRecorder(MicrophoneRecorderListener lis) {
		addMicrophoneRecorderListener(lis);
		recordAction = new MyAction("Record", null, "Record", Icons.RECORD_ICON) {
			@Override
			public void actionPerformed(ActionEvent e) {
				record();
			}
		};
		stopRecordAction = new MyAction("Stop recording", null, "Stop recording", Icons.CLOSE_ICON) {
			@Override
			public void actionPerformed(ActionEvent e) {
				stopRecord();
			}
		};
	}

	public void addMicrophoneRecorderListener(MicrophoneRecorderListener lis) {
		listeners.add(lis);
	}

	class CaptureThread extends Thread {

		public void run() {
			AudioFormat audioFormat = getAudioFormat();
			try {
				targetDataLine.open(audioFormat);
				targetDataLine.start();
				AudioSystem.write(new AudioInputStream(targetDataLine), AudioFileFormat.Type.WAVE, audioFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (MicrophoneRecorderListener lis : listeners) {
				lis.soundRecorded(audioFile);
			}
		}
	}
	
	public void record() {
		recording = true;
	    try{
	        //Get things set up for capture
	        audioFormat = getAudioFormat();
	        DataLine.Info dataLineInfo =
	                            new DataLine.Info(
	                              TargetDataLine.class,
	                              audioFormat);
	        targetDataLine = (TargetDataLine)
	                 AudioSystem.getLine(dataLineInfo);
			targetDataLine.addLineListener(new LineListener() {
				@Override
				public void update(LineEvent event) {
//					System.out.println("**LINE LISTENER : "+event+" // frame "+event.getFramePosition());
					if( event.getType() == LineEvent.Type.START && recording){
						for( MicrophoneRecorderListener lis : listeners ){
							lis.startedRecording();
						}
					}else if( event.getType() == LineEvent.Type.CLOSE ){
					}
				}
			});

	        //Create a thread to capture the microphone
	        // data into an audio file and start the
	        // thread running.  It will run until the
	        // Stop button is clicked.  This method
	        // will return after starting the thread.
	        new CaptureThread().start();
	      }catch (Exception e) {
	        e.printStackTrace();
	        System.exit(0);
	      }//end catch
	}
	
	public void recordOnto( File file ){
		audioFile = file;
		record();
	}

	public void stopRecord() {
		recording = false;
		targetDataLine.stop();
		targetDataLine.close();
	}

	public boolean isRecording() {
		return targetDataLine.isActive();
	}

	private AudioFormat getAudioFormat() {
		float sampleRate = Grid.FRAMES_PER_SECOND;
		// 8000,11025,16000,22050,44100
		int sampleSizeInBits = 16;
		// 8,16
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}// end getAudioFormat

	public MyAction getRecordAction() {
		return recordAction;
	}

	public MyAction getStopRecordAction() {
		return stopRecordAction;
	}
	
}
