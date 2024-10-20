import javax.swing.*;

import View.FramePrincipal;
import View.TelaCadastroAlunos;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;


public class App {
    public static void main(String args[]){
        // Definindo o tema FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());

        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(FramePrincipal::new);
    }
}
