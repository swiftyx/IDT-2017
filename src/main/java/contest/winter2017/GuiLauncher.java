package contest.winter2017;

import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class GuiLauncher {
	private static Gui gui;

	public static void main(String[] args) {
		Main.gui = true;
		gui = new Gui();
		gui.setVisible();
		gui.initScrollView();
	}

	public static JLabel getTextArea() {
		return gui.getTextArea();
	}

	public static JScrollPane getScrollPane() {
		return gui.getScrollPane();
	}
}
