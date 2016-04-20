package org.elusive.ui.dndjtable;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.plaf.basic.BasicTableUI.MouseInputHandler;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.elusive.sound.blocs.frequenceur.Frequencable;

public class DragDropRowTableUI extends BasicTableUI {
	
	private List<DragDropRowListener> listeners;
	
	private boolean draggingRow = false;
	private int startDragPoint;
	private int dyOffset;
	
	public DragDropRowTableUI() {
		listeners = new ArrayList<DragDropRowListener>();
	}

	protected MouseInputListener createMouseInputListener() {
        return new DragDropRowMouseInputHandler();
    }
    
    public void paint(Graphics g, JComponent c) {
    	super.paint(g, c);
    	
    	if (draggingRow) {
    		g.setColor(table.getParent().getBackground());
			Rectangle cellRect = table.getCellRect(table.getSelectedRow(), 0, false);
    		g.copyArea(cellRect.x, cellRect.y, table.getWidth(), table.getRowHeight(), cellRect.x, dyOffset);
    		
    		if (dyOffset < 0) {
    			g.fillRect(cellRect.x, cellRect.y + (table.getRowHeight() + dyOffset), table.getWidth(), (dyOffset * -1));
    		} else {
    			g.fillRect(cellRect.x, cellRect.y, table.getWidth(), dyOffset);
    		}
    	}
    }
    
    class DragDropRowMouseInputHandler extends MouseInputHandler {
    	
        public void mousePressed(MouseEvent e) {
        	super.mousePressed(e);
        	startDragPoint = (int)e.getPoint().getY();
        }
        
        public void mouseDragged(MouseEvent e) {
        	int fromRow = table.getSelectedRow();
        	
        	if (fromRow >= 0) {
	        	draggingRow = true;
	        	        	
	        	int rowHeight = table.getRowHeight();
	        	int middleOfSelectedRow = (rowHeight * fromRow) + (rowHeight / 2);
	        	
	        	int toRow = -1;
	        	int yMousePoint = (int)e.getPoint().getY();
	        	        	
	        	if (yMousePoint < (middleOfSelectedRow - rowHeight)) {
	        		// Move row up
	        		toRow = fromRow - 1;
	        	} else if (yMousePoint > (middleOfSelectedRow + rowHeight)) {
	        		// Move row down
	        		toRow = fromRow + 1;
	        	}
	        	
	        	
	        	if (toRow >= 0 && toRow < table.getRowCount()) {
	        		TableModel model = table.getModel();
	        		
		    		for (int i = 0; i < model.getColumnCount(); i++) {
		    			Object fromValue = model.getValueAt(fromRow, i);
		    			Object toValue = model.getValueAt(toRow, i);
		    			
		    			model.setValueAt(toValue, fromRow, i);
		    			model.setValueAt(fromValue, toRow, i);
		    		}
		    		table.setRowSelectionInterval(toRow, toRow);
		    		startDragPoint = yMousePoint;
	        	}
	        	
	        	dyOffset = (startDragPoint - yMousePoint) * -1;
	        	table.repaint();
        	}
        }
        
        public void mouseReleased(MouseEvent e){
        	super.mouseReleased(e);
        	
        	draggingRow = false;
        	table.repaint();

        	for(DragDropRowListener lis : listeners){
        		lis.rowMoved();
        	}
        }
        
    }

    public void addDragDropRowListener(DragDropRowListener listener){
    	if( listener != null ){
    		listeners.add(listener);
    	}
    }
    
//    public static void main(String[] args) {
//
//		Object[][] data = new Integer[10][1];
//		for (int i = 0, n = 10; i < n; i++) {
//			data[i][0] = Integer.valueOf(i);
//		}
//		final DefaultTableModel dm = new DefaultTableModel(data, new String[] { "" }) {
//			public boolean isCellEditable(int rowIndex, int mColIndex) {
//				return false;
//			}
//		};
//		JTable table = new JTable(dm);
//		table.setUI(new DragDropRowTableUI(new DragDropRowListener() {
//			@Override
//			public void rowMoved() {
//				System.out.println("DragDropRowTableUI.main(...).new DragDropRowListener() {...}.rowMoved()");
//			}
//		}));
//		
//		JFrame frame = new JFrame();
//		frame.getContentPane().add(table);
//		frame.pack();
//		frame.setVisible(true);
//		
//	}
}