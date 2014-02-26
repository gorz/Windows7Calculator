import javax.swing.*;
import java.awt.*;

/**
 * Created by gorz on 24.02.14.
 */
public class Text extends JTextField {

    Font MFont;
    boolean isMemoryFlagVisible;

    public Text() {
        super();
        MFont =  new Font(getFont().getFontName(), Font.BOLD, 13);
        isMemoryFlagVisible = false;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(isMemoryFlagVisible) {
            g.setFont(MFont);
            g.drawString("M", 5, getHeight() - 7);
        }
    }

    public void setMemoryFlagVisible(boolean f) {
        isMemoryFlagVisible = f;
        repaint();
    }
}
