package WorldSimulator.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import WorldSimulator.*;
import WorldSimulator.Animals.*;
import WorldSimulator.Plants.*;
import WorldSimulator.Worlds.*;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;




public class WorldSim
{
    World theWorld;
    private final JPanel mainPanel= new JPanel(new BorderLayout(0,0));
    private JPanel worldBoard;
    private JTextArea Announcements;
    private Label turnLabel = new Label();
    private Label coolDownLabel = new Label();
    public static customFrame frame;

    public static void main(String[] args) throws IOException {
        WorldSim simulator = new WorldSim();
        frame = new customFrame("World Simulator, Kacper Krzeminski 180229");
        frame.setVisible(true);
        frame.add(simulator.mainPanel);
        load("default.txt",simulator.theWorld);
    }
    public WorldSim()
    {
        theWorld=new SquareWorld(this);
        initGui();
    }
    public final void initGui()
    {
        turnLabel.setFocusable(false);
        coolDownLabel.setFocusable(false);
        mainPanel.setBorder(new EmptyBorder(0,0,0,0));
        JToolBar tools = new JToolBar();
        //tools.setMinimumSize(new Dimension(0,300));
        tools.setFloatable(false);
        tools.setFocusable(false);
        mainPanel.add(tools, BorderLayout.PAGE_START);
        JButton temp = new JButton("Restart");
        temp.setFocusable(false);
        temp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    load("default.txt",theWorld);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                turnLabel.setText("Turn " + theWorld.getTurnCounter()+". ");
                coolDownLabel.setText("");
                theWorld.setHumanTurn(false);
            }
        });
        tools.add(temp);
        temp = new JButton("Save");
        temp.setFocusable(false);
        temp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    save(theWorld);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        tools.add(temp);
        temp = new JButton("Load");
        temp.setFocusable(false);
        temp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    load("save.txt",theWorld);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                theWorld.setHumanTurn(false);
                coolDownLabel.setText("");
                turnLabel.setText("Turn " + theWorld.getTurnCounter()+". ");
            }
        });
        tools.add(temp);
        temp = new JButton("Start Next Turn");
        temp.setFocusable(false);
        temp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(!theWorld.needHumanInput())
                {
                    theWorld.buildHeap();
                    theWorld.setTurnCounter(theWorld.getTurnCounter()+1);
                    theWorld.makeTurn();
                    turnLabel.setText("Turn " + theWorld.getTurnCounter()+". ");
                    Announcements.setCaretPosition(Announcements.getDocument().getLength());
                }
            }
        });
        tools.add(temp);
        tools.addSeparator(new Dimension(300,0));
        temp = new JButton("Show Controls");
        temp.setFocusable(false);
        temp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"Up arrow - Move up. \n Down arrow - move down. \n Left arrow - move left. \n Right arrow - move right.\n 5 - use magic potion\nSpace- Skip human turn");

            }
        });
        tools.add(temp);
        tools.addSeparator();
        turnLabel.setText("Turn " + theWorld.getTurnCounter()+". ");
        tools.add(turnLabel);
        tools.addSeparator();
        tools.add(coolDownLabel);


        Announcements = new JTextArea(5,20);
        clearAnnouncementText();
        Announcements.setVisible(true);
        Announcements.setEditable(false);
        Announcements.setFocusable(false);


        JScrollPane AnnouncementBox = new JScrollPane(Announcements);
        AnnouncementBox.setFocusable(false);
        AnnouncementBox.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        AnnouncementBox.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainPanel.add(AnnouncementBox,BorderLayout.SOUTH);


        worldBoard= new JPanel(new GridLayout(theWorld.getHeight(), theWorld.getWidth()));
        worldBoard.setBorder((new LineBorder(Color.BLACK)));
        worldBoard.setFocusable(false);
        mainPanel.add(worldBoard);
        Insets buttonMargin = new Insets(0,0,0,0);
        for(int i=0;i<theWorld.getHeight();i++)
        {
            for(int j = 0;j<theWorld.getWidth();j++)
            {
                Cell givenCell = theWorld.getCell(j,i);
                givenCell.setMargin(buttonMargin);
                givenCell.setBackground(Color.LIGHT_GRAY);
                worldBoard.add(givenCell);
            }
        }
        mainPanel.setFocusable(true);
        InputMap im =mainPanel.getInputMap(mainPanel.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = mainPanel.getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "Space");
        am.put("Space", new humanAction(KeyEvent.VK_SPACE));
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Up");
        am.put("Up", new humanAction(KeyEvent.VK_UP));
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Down");
        am.put("Down", new humanAction(KeyEvent.VK_DOWN));
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Left");
        am.put("Left", new humanAction(KeyEvent.VK_LEFT));
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Right");
        am.put("Right", new humanAction(KeyEvent.VK_RIGHT));
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_5, 0), "5");
        am.put("5", new humanAction(KeyEvent.VK_5));
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD5, 0), "Num5");
        am.put("Num5", new humanAction(KeyEvent.VK_NUMPAD5));
    }
    protected class humanAction extends AbstractAction{
        private int pressedKey;
        humanAction(int pressedKey)
        {
            super();
            this.pressedKey=pressedKey;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if(theWorld.needHumanInput())
            {
                Human theHuman=(Human)theWorld.getHuman();
                if(theHuman.validateNextMove(pressedKey))
                {
                    theHuman.yearOlder();
                    if (!theHuman.action())
                    {
                        theWorld.removeOrganism(theHuman,false);
                    }
                    else
                        theWorld.removeHeapHead();
                    theWorld.makeTurn();
                    theWorld.setHumanTurn(false);
                    setCoolDownLabel("");
                }
            }
        }
    }
    public void setCoolDownLabel(String text)
    {
        coolDownLabel.setText(text);
    }
    public void clearAnnouncementText()
    {
        Announcements.setText("Game Log: \n");
    }
    public void addAnnouncement(String newAnnouncement)
    {
        Announcements.append("Turn " + theWorld.getTurnCounter()+ ": " + newAnnouncement + "\n");
    }

    private static void load(String name,World world) throws IOException {
        world.clear();
        File text = new File(name);
        Scanner file = new Scanner(text);
        Organism temp;
        int orgCount, type, x, y, age, strength, coolDown, turnCounter;
        orgCount= file.nextInt();
        turnCounter= file.nextInt();
        world.setTurnCounter(turnCounter);
        for (int i = 0; i < orgCount; i++)
        {
            type=file.nextInt();
            x=file.nextInt();
            y=file.nextInt();
            age=file.nextInt();
            strength=file.nextInt();
            switch (World.OrganismType.values()[type])
            {
                case Wolf:
                    temp= new Wolf(x, y, age, world);
                    ((Wolf)temp).setStrength(strength);
                    break;
                case Sheep:
                    temp = new Sheep(x, y, age, world);
                    ((Sheep)temp).setStrength(strength);
                    break;
                case Fox:
                    temp = new Fox(x, y, age, world);
                    ((Fox)temp).setStrength(strength);
                    break;
                case Turtle:
                    temp = new Turtle(x, y, age, world);
                    ((Turtle)temp).setStrength(strength);
                    break;
                case Antelope:
                    temp = new Antelope(x, y, age, world);
                    ((Antelope)temp).setStrength(strength);
                    break;
                case CyberSheep:
                    temp = new CyberSheep(x, y, age, world);
                    ((CyberSheep)temp).setStrength(strength);
                    break;
                case Grass:
                    temp = new Grass(x, y, age, world);
                    ((Grass)temp).setStrength(strength);
                    break;
                case SowThistle:
                    temp = new SowThistle(x, y, age, world);
                    ((SowThistle)temp).setStrength(strength);
                    break;
                case Guarana:
                    temp = new Guarana(x, y, age, world);
                    ((Guarana)temp).setStrength(strength);
                    break;
                case Belladonna:
                    temp = new Belladonna(x, y, age, world);
                    ((Belladonna)temp).setStrength(strength);
                    break;
                case SosnowskysHogweed:
                    temp = new Sosnowsky(x, y, age, world);
                    ((Sosnowsky)temp).setStrength(strength);
                    break;
                case Human:
                    temp = new Human(x, y, age, world);
                    coolDown=file.nextInt();
                    ((Human)temp).setCoolDown(coolDown);
                    (temp).setStrength(strength);
                    break;
            }
        }
        file.close();
    }
    private static void save( World world) throws IOException {
        FileWriter file= new FileWriter("save.txt");
        file.write(world.getOrganisms().size()+" ");
        file.write(world.getTurnCounter()+"\n");
        for (Organism v : world.getOrganisms())
        {
            file.write(v.getId().ordinal()+ " ");
            file.write(v.getX() + " ");
            file.write(v.getY() + " ");
            file.write(v.getAge() + " ");
            file.write(v.getStrength() + " ");
            if (v.getId()== World.OrganismType.Human)
            {
                int cd = ((Human)v).getCoolDown();
                file.write(cd + " ");
            }
            file.write("\n");
        }
        file.close();
    }
}
