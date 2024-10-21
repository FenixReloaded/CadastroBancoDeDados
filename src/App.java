import javax.swing.*;

import View.FramePrincipal;
import View.TelaCadastroAlunos;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import java.awt.*;


public class App {
    public static void main(String args[]){
        // Definindo o tema FlatLaf
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
