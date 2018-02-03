package editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import editor.csv.CsvCtrler;
import editor.csv.CsvInfo;

@SuppressWarnings("serial")
public class MainForm extends JFrame {
	
	final private String titlePostfix = "CSV Editor (By: Ren Haotian)";
	final private int maxColsNum = 2048;
	final private int defaultRowsNum = 32;
	final private int defaultColsNum = 16;
	
	private CsvCtrler csvCtrler;
	private boolean csvTableSaved = true;
	private int delColsNum = 0;
	
	// The fileChooser is used when open and save file from dialog.
	private JFileChooser fileChooser = new JFileChooser() {
		{
			setFileFilter(new FileFilter() {
				public String getDescription(){
					return "CSV File";
				}
				public boolean accept(File file){
					return file.getName().endsWith(".csv");
				}
			});
		}
	};
	
	// Initialize the tool bar.
	private AbstractAction openAction = new AbstractAction("Open") {
		{
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			openActionFunc();
		}
	};
	private AbstractAction saveAction = new AbstractAction("Save") {
		{
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			saveActionFunc();
		}
	};
	private AbstractAction saveAsAction = new AbstractAction("SaveAs") {
		{
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			saveAsActionFunc();
		}
	};
	private AbstractAction newRowAction = new AbstractAction("New Row") {
		@Override
		public void actionPerformed(ActionEvent e) {
			newRowActionFunc();
		}
	};
	private AbstractAction newColAction = new AbstractAction("New Column") {
		@Override
		public void actionPerformed(ActionEvent e) {
			newColActionFunc();
		}
	};
	private AbstractAction delRowAction = new AbstractAction("Delete Row") {
		@Override
		public void actionPerformed(ActionEvent e) {
			delRowActionFunc();
		}
	};
	private AbstractAction delColAction = new AbstractAction("Delete Column") {
		@Override
		public void actionPerformed(ActionEvent e) {
			delColActionFunc();
		}
	};
	private AbstractAction showAuthorAction = new AbstractAction("About") {
		@Override
		public void actionPerformed(ActionEvent e) {
			final String message = "CSV Editor\n"
					+ "Spread Sheet Project (for Java Application)\n"
					+ "By: REN Haotian (3150104714@zju.edu.cn)\n\n"
					+ "Open Source Dependencies for CSV Reader & Writer:\n"
					+ "License: Apache License, Version 2.0\n"
					+ "(http://www.apache.org/licenses/LICENSE-2.0.txt)\n"
					+ "opencsv-4.1 (http://opencsv.sourceforge.net/)\n"
					+ "commons-beanutils-1.9.3 (http://commons.apache.org/proper/commons-beanutils/)\n"
					+ "commons-lang3-3.7 (http://commons.apache.org/proper/commons-lang/)\n"
					+ "commons-text-1.2 (http://commons.apache.org/proper/commons-text/)\n";
			JOptionPane.showMessageDialog(null, message, "About", JOptionPane.PLAIN_MESSAGE);
		}
	};
	private JToolBar toolBar = new JToolBar() {
		{
			add(openAction);
			add(saveAction);
			add(saveAsAction);
			addSeparator();
			add(newRowAction);
			add(newColAction);
			add(delRowAction);
			add(delColAction);
			addSeparator();
			add(showAuthorAction);
		}
	};
	
	// The table and the pane which contains it:
	private JTable csvTable = new JTable(defaultRowsNum, maxColsNum) {
		{
			setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			for (int i = maxColsNum - 1; i >= defaultColsNum; --i) {
				removeColumn(getColumnModel().getColumn(i));
			}
			getModel().addTableModelListener(new TableModelListener() {
				@Override
				public void tableChanged(TableModelEvent e) {
					csvTableSaved = false;
				}
			});
		}
	};
	private JScrollPane scrollPane = new JScrollPane(csvTable);
	
	public MainForm() {
		// Register close event:
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (!csvTableSaved) {
					int choice = JOptionPane.showConfirmDialog(null, "File has been modifided, save changes?","Save Changes?", JOptionPane.YES_NO_CANCEL_OPTION);
					switch (choice) {
					case JOptionPane.YES_OPTION:
						saveActionFunc();
						break;
					case JOptionPane.NO_OPTION:
						break;
					case JOptionPane.CANCEL_OPTION:
					case JOptionPane.CLOSED_OPTION:
						return;
					}
				}
				System.exit(0);
			}
		});
		// Initialize the size from screen size.
		Dimension srceenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(srceenSize.width * 31 / 50, srceenSize.height * 31 / 50);
		setResizable(true);
		add(toolBar, BorderLayout.PAGE_START); // Add tool bar.
		add(scrollPane); // Add table pane.
		setTitle(titlePostfix);
		setVisible(true);
	}
	 
	private void openActionFunc() {
		if (!csvTableSaved) {
			int choice = JOptionPane.showConfirmDialog(null, "File has been modifided, save changes?", "Save Changes?", JOptionPane.YES_NO_CANCEL_OPTION);
			switch (choice) {
			case JOptionPane.YES_OPTION:
				saveActionFunc();
				break;
			case JOptionPane.NO_OPTION:
				break;
			case JOptionPane.CANCEL_OPTION:
			case JOptionPane.CLOSED_OPTION:
				return;
			}
		}
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String filePath = fileChooser.getSelectedFile().getAbsolutePath();
			try {
				csvCtrler = new CsvCtrler(filePath);
				showTableByCsvInfo(csvCtrler.read());
			} catch (FileNotFoundException e) {
				JOptionPane.showConfirmDialog(null, "File Not Found.", "Open File Error", JOptionPane.CLOSED_OPTION);
				return;
			} catch (IOException e) {
				JOptionPane.showConfirmDialog(null, "Cannot read the file in CSV format.", "Read File Error", JOptionPane.CLOSED_OPTION);
				return;
			} catch (Exception e) {
				return;
			}
			setTitle(fileChooser.getSelectedFile().getName() + " - " + titlePostfix);
			csvTableSaved = true;
		}
	}
	
	private void saveActionFunc() {
		if (csvCtrler == null) {
			saveAsActionFunc();
		} else {
			try {
				csvCtrler.write(getCsvInfoFromTable());
			} catch (IOException e) {  
				JOptionPane.showConfirmDialog(null, "Cannot write the file.", "Write File Error", JOptionPane.CLOSED_OPTION);
				return;
			} catch (Exception e) {
				return;
			}
			csvTableSaved = true;
		}
	}
	
	private void saveAsActionFunc() {
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			String filePath = fileChooser.getSelectedFile().getAbsolutePath();
			csvCtrler = new CsvCtrler(filePath);
			saveActionFunc();
			setTitle(fileChooser.getSelectedFile().getName() + " - " + titlePostfix);
		}
	}
	
	private void newRowActionFunc() {
		((DefaultTableModel)csvTable.getModel()).insertRow(csvTable.getRowCount(), new String[]{});
		csvTableSaved = false;
	}
	
	private void newColActionFunc() {
		csvTable.addColumn(new TableColumn(csvTable.getColumnCount() + delColsNum));
		csvTableSaved = false;
	}
	
	private void delRowActionFunc() {
		if (csvTable.getRowCount() == 0) {
			return;
		}
		int delRow = csvTable.getSelectedRow();
		if (delRow < 0) {
			delRow = csvTable.getRowCount() - 1;
		}
		((DefaultTableModel)csvTable.getModel()).removeRow(delRow);
		csvTableSaved = false;
	}
	
	private void delColActionFunc() {
		if (csvTable.getColumnCount() == 0) {
			return;
		}
		int delCol = csvTable.getSelectedColumn();
		if (delCol < 0) {
			delCol = csvTable.getColumnCount() - 1;
		}
		csvTable.removeColumn(csvTable.getColumnModel().getColumn(delCol));
		++delColsNum;
		csvTableSaved = false;
	}
	
	private void showTableByCsvInfo(CsvInfo csvContent) {
		remove(scrollPane);
		int rowNum = csvContent.getRowNum();
		int colNum = csvContent.getColNum();
		if (rowNum ==  0 || colNum == 0) {
			csvTable = new JTable(1, maxColsNum);
			for (int i = maxColsNum - 1; i >= 1; --i) {
				csvTable.removeColumn(csvTable.getColumnModel().getColumn(i));
			}
		} else {
			csvTable = new JTable(rowNum, maxColsNum);
			for (int i = maxColsNum - 1; i >= colNum; --i) {
				csvTable.removeColumn(csvTable.getColumnModel().getColumn(i));
			}
			List<String[]> csvData = csvContent.getCsvContent();
			for (int i = 0; i < rowNum; ++i) {
				String[] csvRow = csvData.get(i);
				for (int j = 0; j < csvRow.length; ++j) {
					csvTable.setValueAt(csvRow[j], i, j);
				}
			}
		}
		csvTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		csvTable.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				csvTableSaved = false;
			}
		});
		scrollPane = new JScrollPane(csvTable);
		add(scrollPane);
		validate();
	}
	
	private CsvInfo getCsvInfoFromTable() {
		ArrayList<String[]> stringList = new ArrayList<String[]>();
		int rowNum = csvTable.getRowCount();
		int colNum = csvTable.getColumnCount();
		for (int i = 0; i < rowNum; ++i) {
			String[] row = new String[colNum];
			for (int j = 0; j < colNum; ++j) {
				row[j] = (String)csvTable.getValueAt(i, j);
			}
			stringList.add(row);
		}
		return new CsvInfo(stringList);
	}
	
}
