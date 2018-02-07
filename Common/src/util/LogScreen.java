package util;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

/**
 * Created by Future on 2/8/18.
 */
public class LogScreen extends JFrame{
    private JTextArea textArea;
    private TextAreaOutputStream textAreaOutputStream;
    private PrintStream printStream;
    private JScrollPane scrollPane;

    public LogScreen() {
        this.textArea = new JTextArea();
        this.textAreaOutputStream = new TextAreaOutputStream(textArea);
        this.printStream = new PrintStream(textAreaOutputStream);
        this.scrollPane = new JScrollPane(textArea);
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );
        System.setErr(printStream);
        System.setOut(printStream);
    }

    public void addComponents() {
        this.add(new JLabel(" Server Log"), BorderLayout.NORTH);
        this.add(scrollPane);
    }

    public void showScreen() {
        this.addComponents();

        this.pack();
        this.setSize(1000, 600);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
