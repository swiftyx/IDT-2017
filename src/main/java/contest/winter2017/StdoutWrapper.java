package contest.winter2017;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

public class StdoutWrapper {
    private static boolean toolChain = false;
    private static FileWriter fw = null;
    
    private static int maxSize = 1000;

    private static FontMetrics fm = null;
    private static int charWidth= -1;
    private static int charHeight=-1;
    protected static StringBuffer sb = new StringBuffer("<html>");
    private static List<String> lineList = new ArrayList<String>();
    private static int lineCount = 0;
    private static final int CACHE_OUTPUT_LINECOUNT = 50;
    private static final int REFRESH_OUTPUT_LINECOUNT = 15;
	
    private static void print(String line, boolean isYaml) {
        if (Main.gui) {
        		if(StringUtils.isBlank(line)){
        			return;
        		}
        		
        		line = StringEscapeUtils.escapeHtml3(line);
        		lineList.add(line);
        		lineCount ++;
        		buildOutputString(line);
    		
			JScrollPane scrollPane=GuiLauncher.getScrollPane();
			JLabel ta = (JLabel)scrollPane.getViewport().getView();
			
			getCharPixels(ta);
			getMaxHorizontalPiexls(line);
			refreshPanel(ta, scrollPane, isYaml);
		} else {
			System.out.print(line);
		}

        if (!Config.i().isSaveStdout()) return;

        try {
            fw.write(line);
            fw.flush();
        } catch (IOException ex) {
            if (fw != null) {
                try { fw.close(); } 
                catch (IOException ex2) { /* Ingore  */ }
            }
            throw new RuntimeException(ex);
        }
    }

    public static void refreshPanel(JLabel ta, JScrollPane scrollPane, boolean isYaml){
        if(lineCount % REFRESH_OUTPUT_LINECOUNT == 0 || isYaml){
			ta.setText(sb.toString()+"</html>");
			ta.setBounds((int)ta.getBounds().getX(), (int)ta.getBounds().getY(), maxSize, (int)(ta.getBounds().getHeight()+charHeight));
			ta.setPreferredSize(new Dimension(maxSize, (int)ta.getPreferredSize().getHeight()+charHeight));
			scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
			scrollPane.update(scrollPane.getGraphics());
		}
    }
    
    public static void getMaxHorizontalPiexls(String line){
        if(line.length()*charWidth>maxSize){
			maxSize=line.length()*charWidth;
		}
    }
    
    public static void getCharPixels(JLabel ta){
        if(fm == null){
			fm =ta.getFontMetrics(ta.getFont());
			charWidth=fm.charWidth('A');
			charHeight=fm.getHeight();
		}
    }
    
    public static void buildOutputString(String line){
        if(lineCount >= CACHE_OUTPUT_LINECOUNT){
			sb = new StringBuffer("<html>");
			for(int i = lineCount - CACHE_OUTPUT_LINECOUNT;i < lineCount;i ++){
				sb = sb.append(lineList.get(i)).append("<br>");
			}
		}else{
			sb = sb.append(line).append("<br>");
		}
    }
    
    public static void printYamlf(String format, Object... args) {
        String line = String.format(format, args);
        print(line, true);
    }
    
    public static void printf(String format, Object... args) {
        if (toolChain) {
            return;
        }
        String line = String.format(format, args);
        print(line, false);
    }

    public static void println(String line) {
        if (toolChain) {
            return;
        }
        print(line + "\n", false);
    }
    
    public static void setToolChain(boolean toolChain) {
        StdoutWrapper.toolChain = toolChain;
    }

    public static void init() {
        if (!Config.i().isSaveStdout()) return;

        String ts = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss").format(new Date());
        String filename = "idt-" + ts + ".out";
        try {
            if (fw != null) {
                fw.flush();
                fw.close();
            }
            fw = new FileWriter(filename);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
