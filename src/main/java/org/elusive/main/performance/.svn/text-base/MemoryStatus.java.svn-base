package org.elusive.main.performance;


import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

import org.elusive.main.historique.History;
import org.elusive.sound.blocs.Bloc;
import org.elusive.sound.blocs.BlocFichier;




public class MemoryStatus implements Runnable {

	private JProgressBar bar = new JProgressBar(JProgressBar.VERTICAL);
	private JProgressBar bar2 = new JProgressBar(JProgressBar.VERTICAL);
	private boolean affiche = false;
	private static long free;
	private static long max;
	private static long total;
	private static long old_free;
	private static long old_max;
	private static long old_total;
	
	
	public MemoryStatus(JFrame mem){
		Container con = mem.getContentPane();
		mem.setMinimumSize(new Dimension(10, 200));
		con.add(bar);
		bar.addMouseListener(new MouseListener() {			
			public void mouseReleased(MouseEvent arg0) {
			}
			public void mousePressed(MouseEvent arg0) {
			}			
			public void mouseExited(MouseEvent arg0) {
			}			
			public void mouseEntered(MouseEvent arg0) {
			}			
			public void mouseClicked(MouseEvent arg0) {
				affiche = ! affiche;
			}
		});
	}
	
	public void run() {
		/*
		ArrayList<Bloc> blocs = new ArrayList<Bloc>();
		ArrayList<byte[]> datas = new ArrayList<byte[]>();
		*/
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Runtime r = Runtime.getRuntime();
			old_free = free;
			old_max = max;
			old_total = total;
			free = r.freeMemory();
			max = r.maxMemory();
			total = r.totalMemory();
			if( affiche && (old_total != total) ){
				System.out.println("*************** "+this+" *************");
			}
			int val = (int) (total * 100 / max);
			bar.setValue(val);
			val = (int) (free * 100 / max);
			bar2.setValue(val);
		}

	}

	public static long getFree() {
		return free;
	}

	public static long getMax() {
		return max;
	}

	public static long getTotal() {
		return total;
	}

	/**
	 * @return
	 */
	public static String getStatus() {
		return "MemoryStatus[free="+free+", max="+max+", total="+total+"]";
	}


}
