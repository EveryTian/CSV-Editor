package editor;

import javax.swing.SwingUtilities;

public class Main {

	public static void main(String[] args) throws Exception {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new MainForm();
			}
		});
	}

}
