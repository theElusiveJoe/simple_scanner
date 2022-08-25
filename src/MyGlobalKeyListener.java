import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.awt.Desktop;
import java.net.URI;

public class MyGlobalKeyListener extends SwingWorker<Void, String> implements NativeKeyListener {
    String link = "";
    ArrayList<String> links = new ArrayList<String>();
    DefaultListModel<String> lm;

    MyGlobalKeyListener(DefaultListModel<String> lm) {
        this.lm = lm;
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        System.out.print(e.getKeyChar());
        System.out.print(" -> ");
        System.out.println(e.getRawCode());
        // 65293 - for linux
        if (e.getRawCode() == 65293) {
            System.out.println(link);
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(link));
                    links.add(link);
                    lm.addElement(link);
                }
            } catch (IOException | URISyntaxException ioException) {
                ioException.printStackTrace();
            }
            link = "";
        } else if (e.getRawCode() < 200) {
            link += e.getKeyChar();
        }
    }

    @Override
    protected Void doInBackground() {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
        return null;
    }
}
