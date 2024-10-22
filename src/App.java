import javax.swing.*;
import java.awt.*;

import View.FramePrincipal;
import com.formdev.flatlaf.FlatLightLaf;

public class App {
    public static void main(String args[]){
        // Definindo o tema do FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("TabbedPane.background", new Color(74, 143, 211));
            UIManager.put("Panel.background", new Color(124, 173, 223));

        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(FramePrincipal::new);
    }
}
