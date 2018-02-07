package util;

import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Future on 2/7/18.
 */
public class TextAreaOutputStream extends OutputStream {

// *************************************************************************************************
// INSTANCE MEMBERS
// *************************************************************************************************

    private byte[] oneByte;                                                    // array for write(int val);
    private Appender appender;                                                   // most recent action

    public TextAreaOutputStream(JTextArea textArea) {
        this(textArea, 1000);
    }

    public TextAreaOutputStream(JTextArea textArea, int maxLines) {
//        if (maxLines < 1) {
//            throw new IllegalArgumentException("TextAreaOutputStream maximum lines must be positive (value=" + maxLines + ")");
//        }
        oneByte = new byte[1];
        appender = new Appender(textArea, maxLines);
    }

    static private String bytesToString(byte[] ba, int str, int len) {
        try {
            return new String(ba, str, len, "UTF-8");
        } catch (UnsupportedEncodingException thr) {
            return new String(ba, str, len);
        } // all JVMs are required to support UTF-8
    }

    /**
     * Clear the current console text area.
     */
    public synchronized void clear() {
        if (appender != null) {
            appender.clear();
        }
    }

    public synchronized void close() {
        appender = null;
    }

    public synchronized void flush() {
    }

    public synchronized void write(int val) {
        oneByte[0] = (byte) val;
        write(oneByte, 0, 1);
    }

    public synchronized void write(byte[] ba) {
        write(ba, 0, ba.length);
    }

    public synchronized void write(byte[] ba, int str, int len) {
        if (appender != null) {
            appender.append(bytesToString(ba, str, len));
        }
    }


    static class Appender implements Runnable {
        static private final String EOL1 = "\n";
        static private final String EOL2 = System.getProperty("line.separator", EOL1);
        private final JTextArea textArea;
        private final int maxLines;                                                   // maximum lines allowed in text area
        //        private final LinkedList<Integer> lengths;                                    // length of lines within text area
        private final List<String> values;                                            // values waiting to be appended
        private int curLength;                                                        // length of current line
        private boolean clear;
        private boolean queue;

        Appender(JTextArea textArea, int maxLines) {
            this.textArea = textArea;
            this.maxLines = maxLines;
//            lengths = new LinkedList<>();
            values = new ArrayList<>();

            curLength = 0;
            clear = false;
            queue = true;
        }

        synchronized void append(String val) {
            values.add(val);
            if (queue) {
                queue = false;
                EventQueue.invokeLater(this);
            }
        }

        synchronized void clear() {
            clear = true;
            curLength = 0;
//            lengths.clear();
            values.clear();
            if (queue) {
                queue = false;
                EventQueue.invokeLater(this);
            }
        }

        // MUST BE THE ONLY METHOD THAT TOUCHES textArea!
        public synchronized void run() {
            if (clear) {
                textArea.setText("");
            }
            for (String val : values) {
                curLength += val.length();
                if (val.endsWith(EOL1) || val.endsWith(EOL2)) {
//                    if (lengths.size() >= maxLines) {
//                        textArea.replaceRange("", 0, lengths.removeFirst());
//                    }
//                    lengths.addLast(curLength);
                    curLength = 0;
                }
                textArea.append(val);
            }
            values.clear();
            clear = false;
            queue = true;
        }
    }

}