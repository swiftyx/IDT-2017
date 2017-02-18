package contest.winter2017;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

/**
 * ID Swing UI.
 */
public class Gui {
	private JFrame frame;

	private JPanel mainPanel;

	private JPanel controlPanel;
	private JPanel logoPanel;

	private static final double BUTTON_WEIGHT = 20.0;
	private static final double LABEL_WEIGHT = 20.0;

	private static final int BUTTON_LEFT = 10;

	private static final Color BUTTON_COLOR = new Color(131, 204, 255);

	private JTextField jacocoAgentJarPathText;
	private JTextField jacocoOutputPathText;
	private JTextField jarToTestPathText;
	private JTextField timeGoalText;
	private JTextField bbTestsText;

	private JButton jacocoAgentJarPathChooseButton;
	private JButton jacocoOutputPathChooserButton;
	private JButton jarToTestPathChooserButton;

	private JFileChooser jacocoAgentJarPathChooser;
	private JFileChooser jacocoOutputPathChooser;
	private JFileChooser jarToTestPathChooser;

	private JCheckBox toolChainChk;

	private List<JTextField> texts;

	private JButton resetButton;
	private JButton runButton;

	private JLabel outputTa;
	private JScrollPane scrollPane;

	public Gui() {
		configureLookAndFeel();
		frame = new JFrame("IDT Tester");
		frame.setSize(1000, 740);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setAppIcon(frame);

		controlPanel = new JPanel();

		frame.add(getMainPanel());
		placeComponents();

		texts = new ArrayList<JTextField>();
		texts.add(jacocoAgentJarPathText);
		texts.add(jacocoOutputPathText);
		texts.add(jarToTestPathText);
		texts.add(timeGoalText);
		texts.add(bbTestsText);

		setupEvents();
	}

	private void configureLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void setAppIcon(JFrame frame) {
		// Icon src: https://idtus.com/wp-content/themes/idt/icon2.png
		ImageIcon icon = new ImageIcon(Gui.class.getResource("/icon/icon.png"));
		Image image = icon.getImage();
		frame.setIconImage(image);
		if (SystemUtils.IS_OS_MAC_OSX) {
			try {
				Class NSApplication = Class.forName("com.apple.eawt.Application");
				Method sharedApplication = NSApplication.getMethod("getApplication");
				Object shared = sharedApplication.invoke(NSApplication);
				Method setApplicationIconImage = NSApplication.getMethod("setDockIconImage", Image.class);
				setApplicationIconImage.invoke(shared, image);
			} catch (Exception e) {
			}
		}
	}

	// Test values for test.
	@SuppressWarnings("unused")
	private void setValues() {
		jacocoAgentJarPathText.setText("/idt_contest/jacoco/lib/jacocoagent.jar");
		jacocoOutputPathText.setText("/idt_contest/jacoco");
		jarToTestPathText.setText("/idt_contest/jars/TesterTypeCheck.jar");
		bbTestsText.setText("10");
		timeGoalText.setText("1");
	}

	private void placeComponents() {
		controlPanel.setLayout(new GridBagLayout());
		JLabel jarToTestPathLabel = new JLabel("Jar to Test");
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridy = 0;
		gridBagConstraints11.weightx = LABEL_WEIGHT;
		gridBagConstraints11.anchor = GridBagConstraints.WEST;
		gridBagConstraints11.insets = new Insets(0, 5, 5, 0);

		jarToTestPathText = new JTextField();
		jarToTestPathText.setBorder(javax.swing.BorderFactory.createLineBorder(BUTTON_COLOR));

		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.gridx = 1;
		gridBagConstraints12.gridy = 0;
		gridBagConstraints12.weightx = 100.0;
		gridBagConstraints12.fill = GridBagConstraints.BOTH;
		gridBagConstraints12.insets = new Insets(0, 0, 5, 0);

		GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
		gridBagConstraints13.gridx = 2;
		gridBagConstraints13.gridy = 0;
		gridBagConstraints13.weightx = BUTTON_WEIGHT;
		gridBagConstraints13.anchor = GridBagConstraints.WEST;
		gridBagConstraints13.gridwidth = 2;
		gridBagConstraints13.insets = new Insets(0, BUTTON_LEFT, 5, 0);

		JLabel jacocoOutputPathLabel = new JLabel("Jacoco Output Directory");
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.gridy = 1;
		gridBagConstraints21.weightx = LABEL_WEIGHT;
		gridBagConstraints21.anchor = GridBagConstraints.WEST;
		gridBagConstraints21.insets = new Insets(5, 5, 5, 0);

		jacocoOutputPathText = new JTextField(20);
		jacocoOutputPathText.setBorder(javax.swing.BorderFactory.createLineBorder(BUTTON_COLOR));
		GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
		gridBagConstraints22.gridx = 1;
		gridBagConstraints22.gridy = 1;
		gridBagConstraints22.weightx = 100.0;
		gridBagConstraints22.fill = GridBagConstraints.BOTH;
		gridBagConstraints22.insets = new Insets(5, 0, 5, 0);

		GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
		gridBagConstraints23.gridx = 2;
		gridBagConstraints23.gridy = 1;
		gridBagConstraints23.weightx = BUTTON_WEIGHT;
		gridBagConstraints23.anchor = GridBagConstraints.WEST;
		gridBagConstraints23.gridwidth = 2;
		gridBagConstraints23.insets = new Insets(5, BUTTON_LEFT, 5, 0);

		JLabel jacocoAgentJarPathLabel = new JLabel("Jacoco Agent Jar");

		GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
		gridBagConstraints31.gridx = 0;
		gridBagConstraints31.gridy = 2;
		gridBagConstraints31.weightx = LABEL_WEIGHT;
		gridBagConstraints31.anchor = GridBagConstraints.WEST;
		gridBagConstraints31.insets = new Insets(5, 5, 5, 0);

		jacocoAgentJarPathText = new JTextField();
		jacocoAgentJarPathText.setBorder(javax.swing.BorderFactory.createLineBorder(BUTTON_COLOR));
		GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
		gridBagConstraints32.gridx = 1;
		gridBagConstraints32.gridy = 2;
		gridBagConstraints32.weightx = 100.0;
		gridBagConstraints32.fill = GridBagConstraints.BOTH;
		gridBagConstraints32.insets = new Insets(5, 0, 5, 0);

		GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
		gridBagConstraints33.gridx = 2;
		gridBagConstraints33.gridy = 2;
		gridBagConstraints33.weightx = BUTTON_WEIGHT;
		gridBagConstraints33.anchor = GridBagConstraints.WEST;
		gridBagConstraints33.gridwidth = 2;
		gridBagConstraints33.insets = new Insets(5, BUTTON_LEFT, 5, 0);

		JLabel bbTestsPathLabel = new JLabel("Number of Test Iterations");
		GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
		gridBagConstraints41.gridx = 0;
		gridBagConstraints41.gridy = 3;
		gridBagConstraints41.weightx = LABEL_WEIGHT;
		gridBagConstraints41.anchor = GridBagConstraints.WEST;
		gridBagConstraints41.insets = new Insets(5, 5, 5, 0);

		bbTestsText = new JTextField("1000");
		bbTestsText.setBorder(javax.swing.BorderFactory.createLineBorder(BUTTON_COLOR));
		GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
		gridBagConstraints42.gridx = 1;
		gridBagConstraints42.gridy = 3;
		gridBagConstraints42.ipadx = 55;
		gridBagConstraints42.anchor = GridBagConstraints.WEST;
		gridBagConstraints42.insets = new Insets(5, 0, 5, 0);
		gridBagConstraints42.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints42.ipady = 5;

		JLabel timeGoalPathLabel = new JLabel("Test Time Goal (minutes)");
		GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
		gridBagConstraints51.gridx = 0;
		gridBagConstraints51.gridy = 4;
		gridBagConstraints51.weightx = LABEL_WEIGHT;
		gridBagConstraints51.anchor = GridBagConstraints.WEST;
		gridBagConstraints51.insets = new Insets(5, 5, 5, 0);

		timeGoalText = new JTextField("5");
		timeGoalText.setBorder(javax.swing.BorderFactory.createLineBorder(BUTTON_COLOR));
		GridBagConstraints gridBagConstraints52 = new GridBagConstraints();
		gridBagConstraints52.gridx = 1;
		gridBagConstraints52.gridy = 4;
		gridBagConstraints52.ipadx = 55;
		gridBagConstraints52.anchor = GridBagConstraints.WEST;
		gridBagConstraints52.insets = new Insets(5, 0, 5, 0);
		gridBagConstraints52.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints52.ipady = 5;

		JLabel toolChainLabel = new JLabel("YAML only Output");
		GridBagConstraints gridBagConstraints61 = new GridBagConstraints();

		gridBagConstraints61.gridx = 0;
		gridBagConstraints61.gridy = 5;
		gridBagConstraints61.weightx = LABEL_WEIGHT;
		gridBagConstraints61.anchor = GridBagConstraints.WEST;
		gridBagConstraints61.insets = new Insets(5, 5, 5, 0);

		ImageIcon checkOff = new ImageIcon(getClass().getResource("/icon/checkbox_off_blue.png"));
		ImageIcon checkOn = new ImageIcon(getClass().getResource("/icon/checkbox_on_blue.png"));
		toolChainChk = new JCheckBox("", checkOff);
		toolChainChk.setOpaque(false);
		toolChainChk.setSelectedIcon(checkOn);
		GridBagConstraints gridBagConstraints62 = new GridBagConstraints();
		gridBagConstraints62.gridx = 1;
		gridBagConstraints62.gridy = 5;
		gridBagConstraints62.anchor = GridBagConstraints.WEST;
		gridBagConstraints62.insets = new Insets(5, -7, 5, 0);

		runButton = new WButton("Run", Color.WHITE, BUTTON_COLOR);
		GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
		gridBagConstraints71.gridx = 1;
		gridBagConstraints71.gridy = 6;
		gridBagConstraints71.weightx = BUTTON_WEIGHT;
		gridBagConstraints71.anchor = GridBagConstraints.EAST;
		gridBagConstraints71.insets = new Insets(5, 0, 5, 0);
		gridBagConstraints71.ipadx = 8;

		resetButton = new WButton("Reset");
		GridBagConstraints gridBagConstraints72 = new GridBagConstraints();
		gridBagConstraints72.gridx = 2;
		gridBagConstraints72.gridy = 6;
		gridBagConstraints72.weightx = BUTTON_WEIGHT;
		gridBagConstraints72.anchor = GridBagConstraints.WEST;
		gridBagConstraints72.insets = new Insets(5, 0, 5, 0);

		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.gridx = 0;
		gridBagConstraints8.gridy = 7;
		gridBagConstraints8.weightx = 100.0;
		gridBagConstraints8.weighty = 100.0;
		gridBagConstraints8.fill = GridBagConstraints.BOTH;
		gridBagConstraints8.gridwidth = 4;
		gridBagConstraints8.insets = new Insets(5, 5, 5, 5);

		outputTa = new JLabel();
		outputTa.setFont(new Font("Dialog", Font.PLAIN, 14));
		outputTa.setOpaque(true);
		outputTa.setVerticalAlignment(SwingConstants.BOTTOM);
		outputTa.setBackground(Color.white);

		scrollPane = new JScrollPane(outputTa);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		controlPanel.add(jarToTestPathLabel, gridBagConstraints11);
		controlPanel.add(jarToTestPathText, gridBagConstraints12);
		controlPanel.add(getJarToTestPathChooserButton(), gridBagConstraints13);

		controlPanel.add(jacocoOutputPathLabel, gridBagConstraints21);
		controlPanel.add(jacocoOutputPathText, gridBagConstraints22);
		controlPanel.add(getJacocoOutputPathChooserButton(), gridBagConstraints23);

		controlPanel.add(jacocoAgentJarPathLabel, gridBagConstraints31);
		controlPanel.add(jacocoAgentJarPathText, gridBagConstraints32);
		controlPanel.add(getJacocoAgentJarPathChooseButton(), gridBagConstraints33);

		controlPanel.add(bbTestsPathLabel, gridBagConstraints41);
		controlPanel.add(bbTestsText, gridBagConstraints42);

		controlPanel.add(timeGoalPathLabel, gridBagConstraints51);
		controlPanel.add(timeGoalText, gridBagConstraints52);

		controlPanel.add(toolChainLabel, gridBagConstraints61);
		controlPanel.add(toolChainChk, gridBagConstraints62);

		controlPanel.add(runButton, gridBagConstraints71);
		controlPanel.add(resetButton, gridBagConstraints72);

		controlPanel.add(scrollPane, gridBagConstraints8);

	}

	private void setupEvents() {
		resetButton.addActionListener(e -> {
			for (JTextField text : texts)
				text.setText("");
			bbTestsText.setText("1000");
			timeGoalText.setText("5");
			toolChainChk.setSelected(false);
			outputTa.setText("");
			enableAll(true);
		});

		runButton.addActionListener(e -> {

			String tips = "";
			if (StringUtils.isBlank(jarToTestPathText.getText())) {
				tips = "Jar to Test is null";
			} else if (StringUtils.isBlank(jacocoOutputPathText.getText())) {
				tips = "Jacoco Output Directory is null";
			} else if (StringUtils.isBlank(jacocoAgentJarPathText.getText())) {
				tips = "Jacoco Agent Jar is null";
			}

			if (StringUtils.isNotBlank(tips)) {
				outputTa.setText(tips);
				outputTa.setVerticalAlignment(SwingConstants.TOP);
				outputTa.update(outputTa.getGraphics());

				scrollPane.update(scrollPane.getGraphics());

				return;
			} else {
				outputTa.setVerticalAlignment(SwingConstants.BOTTOM);
			}

			enableAll(false);
			outputTa.setText("");
			outputTa.update(outputTa.getGraphics());

			if (!StdoutWrapper.sb.toString().equals("<html>")) {
				StdoutWrapper.sb = new StringBuffer("<html>");

				scrollPane.setPreferredSize(new Dimension(300, 100));
				scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());

				scrollPane.getViewport().remove(outputTa);

				outputTa = new JLabel();
				outputTa.setFont(new Font("Dialog", Font.PLAIN, 14));
				outputTa.setBounds(scrollPane.getX(), scrollPane.getY(), scrollPane.getWidth(), scrollPane.getHeight());
				outputTa.setVerticalAlignment(SwingConstants.BOTTOM);
				outputTa.setOpaque(true);
				outputTa.setBackground(Color.WHITE);
				outputTa.setPreferredSize(new Dimension(scrollPane.getWidth(), scrollPane.getHeight()));

				scrollPane.getViewport().add(outputTa);

				outputTa.update(outputTa.getGraphics());
				scrollPane.update(scrollPane.getGraphics());
			}

			new Thread(() -> {
				Main.runMain(jacocoAgentJarPathText.getText().trim(), jacocoOutputPathText.getText().trim(),
						jarToTestPathText.getText().trim(), bbTestsText.getText().trim(), timeGoalText.getText().trim(),
						toolChainChk.isSelected());
				enableAll(true);
			}).start();
		});
	}

	private JFileChooser getJacocoAgentJarPathChooser() {
		if (jacocoAgentJarPathChooser == null) {
			jacocoAgentJarPathChooser = new JFileChooser(getRootDirectory());
			jacocoAgentJarPathChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		}
		return jacocoAgentJarPathChooser;
	}

	private JFileChooser getJacocoOutputPathChooser() {
		if (jacocoOutputPathChooser == null) {
			jacocoOutputPathChooser = new JFileChooser(getRootDirectory());
			jacocoOutputPathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		return jacocoOutputPathChooser;
	}

	private JFileChooser getJarToTestPathChooser() {
		if (jarToTestPathChooser == null) {
			jarToTestPathChooser = new JFileChooser(getRootDirectory());
			jarToTestPathChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		}
		return jarToTestPathChooser;
	}

	private File getRootDirectory() {
		String preferredPath = null, alternativePath = SystemUtils.USER_HOME;
		if (SystemUtils.IS_OS_WINDOWS) {
			preferredPath = "c:\\idt_contest";
			alternativePath = "c:\\";
		} else if (SystemUtils.IS_OS_MAC_OSX) {
			preferredPath = "/idt_contest";
		}

		try {
			File file = new File(preferredPath);
			if (file.exists())
				return file;
		} catch (Exception e) {
		}
		return new File(alternativePath);
	}

	private ActionListener set(JFileChooser fileChooser, JTextField textField) {
		return (e) -> {
			int state = fileChooser.showDialog(frame, "Choose");
			if (state == JFileChooser.APPROVE_OPTION) {
				String filePath = fileChooser.getSelectedFile().getPath();
				textField.setText(filePath);
			}
		};
	}

	private JButton getJacocoAgentJarPathChooseButton() {
		if (jacocoAgentJarPathChooseButton == null) {
			jacocoAgentJarPathChooseButton = new WButton("Browse");
			jacocoAgentJarPathChooseButton
					.addActionListener(set(getJacocoAgentJarPathChooser(), jacocoAgentJarPathText));
		}
		return jacocoAgentJarPathChooseButton;

	}

	private JButton getJacocoOutputPathChooserButton() {
		if (jacocoOutputPathChooserButton == null) {
			jacocoOutputPathChooserButton = new WButton("Browse");
			jacocoOutputPathChooserButton.addActionListener(set(getJacocoOutputPathChooser(), jacocoOutputPathText));
		}
		return jacocoOutputPathChooserButton;
	}

	private JButton getJarToTestPathChooserButton() {
		if (jarToTestPathChooserButton == null) {
			jarToTestPathChooserButton = new WButton("Browse");
			jarToTestPathChooserButton.addActionListener(set(getJarToTestPathChooser(), jarToTestPathText));
		}
		return jarToTestPathChooserButton;
	}

	private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			ImageIcon icon = new ImageIcon(getClass().getResource("/icon/logo.png"));
			Image img = icon.getImage();

			logoPanel = new JPanel() {
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
				}
			};

			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 100.0;
			gridBagConstraints1.weighty = 15.0;
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.insets = new Insets(25, 0, 0, 0);
			gridBagConstraints1.ipady = 50;

			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.weightx = 100.0;
			gridBagConstraints2.weighty = 100.0;
			gridBagConstraints2.fill = GridBagConstraints.BOTH;

			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(controlPanel, gridBagConstraints2);
			mainPanel.add(logoPanel, gridBagConstraints1);
		}
		return mainPanel;
	}

	private void enableAll(boolean b) {
		for (JTextField text : texts)
			text.setEnabled(b);
		toolChainChk.setEnabled(b);
		getJarToTestPathChooserButton().setEnabled(b);
		getJacocoAgentJarPathChooseButton().setEnabled(b);
		getJacocoOutputPathChooserButton().setEnabled(b);
		resetButton.setEnabled(b);
		runButton.setEnabled(b);
	}

	public JLabel getTextArea() {
		return outputTa;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setVisible() {
		frame.setVisible(true);
	}

	public void initScrollView() {
		outputTa.setPreferredSize(new Dimension(scrollPane.getWidth(), scrollPane.getHeight() + 1));
	}

	private class WButton extends JButton {
		private Color moveColor = BUTTON_COLOR;
		private Color boderColor = BUTTON_COLOR;
		private Color backColor = BUTTON_COLOR;
		private Color foregroundColor = Color.WHITE;

		public WButton(String text) {
			super(text);
			init();
		}

		public WButton(String text, Color color, Color foregroundColor) {
			super(text);
			moveColor = color;
			boderColor = color;
			backColor = color;
			this.foregroundColor = foregroundColor;
			init();
		}

		@Override
		public Dimension getPreferredSize() {
			int textW = StringUtils.isEmpty(getText()) ? 0
					: getFontMetrics(getFont()).stringWidth(getText()) + getInsets().left + getInsets().right;
			int textH = StringUtils.isEmpty(getText()) ? 0 : getFontMetrics(getFont()).getHeight();

			int w = Math.max(super.getPreferredSize().width, textW);
			int h = Math.max(super.getPreferredSize().height, textH);

			if (StringUtils.isEmpty(getText())) {
				int var = Math.min(w, h);
				w = var;
				h = var;
			}

			return new Dimension(w, h);
		}

		@Override
		public Dimension getMinimumSize() {
			int w = Math.min(getPreferredSize().width, super.getMinimumSize().width);
			int h = Math.min(getPreferredSize().height, super.getMinimumSize().height);
			return new Dimension(w, h);
		}

		@Override
		public Dimension getMaximumSize() {
			int w = Math.max(getPreferredSize().width, super.getMaximumSize().width);
			int h = Math.max(getPreferredSize().height, super.getMaximumSize().height);
			return new Dimension(w, h);
		}

		private int getTextWidth() {
			int w = StringUtils.isEmpty(getText()) ? 0 : getFontMetrics(getFont()).stringWidth(getText());
			return getWidth() < w ? 0 : w;
		}

		private void init() {
			this.setForeground(foregroundColor);
			this.setBackground(backColor);
			this.setBoderColor(boderColor);
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					setBackground(moveColor);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					setBackground(backColor);
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					setBackground(backColor.darker());
					super.mouseClicked(e);
				}

				@Override
				public void mousePressed(MouseEvent e) {
					setBackground(backColor.darker());
				}
			});
		}

		public void setBoderColor(Color boderColor) {
			this.boderColor = boderColor;
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			FontMetrics fontMetrics = getFontMetrics(getFont());
			int x = (getWidth() - getTextWidth() + 1) / 2;
			int y = (getHeight() - fontMetrics.getHeight() + 1) / 2 + fontMetrics.getAscent()
					+ fontMetrics.getLeading();

			Color borderC = boderColor;

			g2.setColor(getBackground());
			g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 5, 5);
			g2.setColor(borderC);
			g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 5, 5);

			g2.setColor(getForeground());

			if (StringUtils.isNotEmpty(getText())) {
				g2.setFont(getFont());
				g2.drawString(getText(), x, y);
			}
			g2.dispose();
		}
	}
}