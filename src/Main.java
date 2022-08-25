import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import org.jnativehook.GlobalScreen;

import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

public final class
Main {
    JButton b1, b2, b3;
    JLabel t;
    DefaultListModel<String> lm;
    JList<String> l;
    MyGlobalKeyListener glk;

    Main() {
        JFrame frame = new JFrame();

        // Creating Button
        b1 = new JButton("Начать сканирование");
        b1.setBounds(50, 50, 300, 30);
        StartScanner ss = new StartScanner();
        b1.addActionListener(ss);
        frame.add(b1);

        b2 = new JButton("Окончить сканирование");
        b2.setBounds(50, 100, 300, 30);
        EndScanner es = new EndScanner();
        b2.addActionListener(es);
        b2.setEnabled(false);
        frame.add(b2);

        b3 = new JButton("Скопировать");
        b3.setBounds(50, 500, 300, 30);
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Enumeration<String> en = lm.elements();
                String s = "";
                while (en.hasMoreElements()) {
                    s += en.nextElement() + "\n";
                }
                StringSelection stringSelection = new StringSelection(s);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        });
        frame.add(b3);


        t = new JLabel("сканирование не идет");
        t.setBounds(50, 0, 300, 30);
        frame.add(t);


        lm = new DefaultListModel<String>();
        l = new JList<String>(lm);
        l.setBounds(50, 150, 300, 300);
        frame.add(l);

        // Setting Frame size. This is the window size
        frame.setSize(500, 500);

        frame.setLayout(null);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        glk = new MyGlobalKeyListener(lm);
        glk.doInBackground();
    }

    public class StartScanner implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            glk.link = "";
            GlobalScreen.removeNativeKeyListener(glk);
            GlobalScreen.addNativeKeyListener(glk);
            t.setText("сканирование в процессе");
            b1.setEnabled(false);
            b2.setEnabled(true);
        }
    }

    public class EndScanner implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            GlobalScreen.removeNativeKeyListener(glk);
            t.setText("сканирование не идет");
            b1.setEnabled(true);
            b2.setEnabled(false);
            lm.clear();
        }
    }


    public static void main(String[] args) {
        new Main();
    }
}