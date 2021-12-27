package WorldSimulator;

import javax.swing.*;
import java.awt.*;

public class customFrame extends JFrame
{
    public customFrame(String name)
    {
        super(name);
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
    }
}
