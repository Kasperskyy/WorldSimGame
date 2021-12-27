package WorldSimulator;
import WorldSimulator.Animals.Human;
import WorldSimulator.Main.WorldSim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
public abstract class World
{
    protected int width=60,height=30,turnCounter=0,dirCount;
    protected Cell[][] board;
    protected ArrayList<Organism> organisms;
    protected ArrayList<Organism> turnQueue;
    protected HashMap<Integer,Integer> directionX;
    protected HashMap<Integer,Integer> directionY;
    protected HashMap<Integer,String> iconMap;
    protected WorldSim simulator;
    protected boolean humanTurn;
    protected boolean humanLives;
    protected enum direction
    {
        North,South,East,West,SouthEast,SouthWest// additional directions included for hexagonal map. I sadly didn't have time to implement the hexagonal map
    }
    public enum OrganismType
    {
		Wolf, Sheep, Fox, Turtle, Antelope, CyberSheep, Grass, SowThistle, Guarana, Belladonna, SosnowskysHogweed, Human
	}
    protected World(WorldSim givenSim)
    {
        simulator=givenSim;
        board=new Cell[height][width];
        for(int i=0;i<height;i++)
        {
            for(int j=0;j<width;j++)
            {
                board[i][j]=new Cell(j,i,this);
            }
        }
        organisms=new ArrayList<Organism>();
        turnQueue=new ArrayList<Organism>();
        initIcons();
    }
    private void initIcons()
    {
        iconMap=new HashMap<Integer,String>();
        iconMap.put(OrganismType.CyberSheep.ordinal(),"xSheep.png");
        iconMap.put(OrganismType.Antelope.ordinal(),"antelope.png");
        iconMap.put(OrganismType.Belladonna.ordinal(),"belladonna.png");
        iconMap.put(OrganismType.Fox.ordinal(),"fox.png");
        iconMap.put(OrganismType.Grass.ordinal(),"grass.png");
        iconMap.put(OrganismType.Guarana.ordinal(),"guarana.png");
        iconMap.put(OrganismType.Human.ordinal(),"human.png");
        iconMap.put(OrganismType.Sheep.ordinal(),"sheep.png");
        iconMap.put(OrganismType.SosnowskysHogweed.ordinal(),"sosnowsky.png");
        iconMap.put(OrganismType.SowThistle.ordinal(),"sowThistle.png");
        iconMap.put(OrganismType.Turtle.ordinal(),"turtle.png");
        iconMap.put(OrganismType.Wolf.ordinal(),"wolf.png");
    }
	protected void heapify(int i)
    {
        int left = (i * 2) + 1, right = (i * 2) + 2, maxps;
	    if (left < turnQueue.size()     &&      ((turnQueue.get(left).getInit() > turnQueue.get(i).getInit())    ||    ((turnQueue.get(left).getInit() == turnQueue.get(i).getInit()) && (turnQueue.get(left).getAge() > turnQueue.get(i).getAge()))))
	    {
	        maxps = left;
	    }
	    else
	    {
	        maxps = i;
	    }
	    if (right < turnQueue.size() &&      ((turnQueue.get(right).getInit() > turnQueue.get(maxps).getInit())   ||   ((turnQueue.get(right).getInit() == turnQueue.get(maxps).getInit()) && (turnQueue.get(right).getAge() > turnQueue.get(maxps).getAge()))))
	    {
	        maxps = right;
	    }
	    if (maxps != i)
	        {
	            Collections.swap(turnQueue,i,maxps);
	        	heapify(maxps);
	        }
        }
	public void buildHeap()
    {
        turnQueue=new ArrayList<Organism>(organisms);
	    for (int i = (organisms.size()-1) / 2; i >= 0; i--)
	    {
	        heapify(i);
	    }
    }
	public void removeHeapHead()
    {
        Collections.swap(turnQueue,0,turnQueue.size()-1);
        turnQueue.remove(turnQueue.size() - 1);
        heapify(0);
    }
	protected void removeFromQueue(int index)
    {
        Collections.swap(turnQueue,index,turnQueue.size()-1);
	    turnQueue.remove(turnQueue.size() - 1);
	    for (int i = (turnQueue.size() - 1) / 2; i >= 0; i--)
        {
		    heapify(i);
        }
    }
	public void clear()
        {
            simulator.clearAnnouncementText();
            for (int i=0;i<organisms.size();)
                removeOrganism(organisms.get(i),true);
        }
	public void setTurnCounter(int value)
    {
                    turnCounter=value;
    }
	public void makeTurn()
    {
	    for (int i = 0; i < turnQueue.size();)
	    {
	        if(turnQueue.get(0).id==OrganismType.Human)
            {
                humanTurn=true;
                Human temp = (Human)turnQueue.get(0);
                getSimulator().setCoolDownLabel("YOUR TURN! Your strength: " + temp.getStrength() + ". Your cooldown: " + temp.getCoolDown() + " (0= Potion Available)");
                return;
            }
            turnQueue.get(0).yearOlder();
		    if (!turnQueue.get(0).action())
		    {
		    	removeOrganism(turnQueue.get(0),false);
		    }
		    else
		    	removeHeapHead();
	    }
        buildHeap();
    }
	public void addOrganism(Organism newOrg)
    {
        organisms.add(newOrg);
        if (newOrg.id==OrganismType.Human)
            humanLives=true;
        board[newOrg.getY()][newOrg.getX()].setCell(newOrg);
    }
	public void removeOrganism(Organism newOrg,boolean cleaning)
    {
        if (newOrg.id==OrganismType.Human)
            humanLives=false;
        if(!cleaning)
        {
            String temp = newOrg.getId() + ((newOrg.isAnimalCheck())?" has been slain at coordinates X: ":" has been consumed at coordinates X:  ") + (newOrg.getX()) + ", Y: " + (newOrg.getY()) + "." ;
            simulator.addAnnouncement(temp);
        }
	    int index = turnQueue.indexOf(newOrg);
	    if(index!=-1)
		    removeFromQueue(index);
	    index = organisms.indexOf(newOrg);
        if(index==-1) {
            System.out.println("WA");
        }
	    board[newOrg.getY()][newOrg.getX()].clearCell();
	    organisms.remove(index);
    }
	public boolean validMove(int x, int y)
    {
        if ((x < width && y < height) && (x >= 0 && y >= 0))
            return true;
        else
            return false;
    }
	public boolean checkCell(int x, int y)
    {
        if (board[y][x].occupied())
            return false;
        else
            return true;
    }
	public void moveOrganism(int x, int y, int newX, int newY)
    {
        Organism temp = board[y][x].getCell();
	    board[y][x].clearCell();
	    board[newY][newX].setCell(temp);
    }
	public Organism getCreature(int x, int y)
    {
            return board[y][x].getCell();
    }
	public int worldScan(OrganismType id, ArrayList<Organism> foundObjects)
        {
            
	    int counter = 0;
	    for (int i = 0; i < organisms.size(); i++)
	    {
	    	if (organisms.get(i).getId() == id)
		    {
		    	counter++;
		    	foundObjects.add(organisms.get(i));
		    }
	    }
          return counter;
        }
	public int getWidth()
    {
        return width;
    }
	public int getHeight()
    {
        return height;
    }
	public int getTurnCounter()
    {
        return turnCounter;
    }
	public int getDirX(int  direction)
    {
        return directionX.get(direction);
    }
	public int getDirY(int direction)
    {
        return directionY.get(direction);
    }
    public Cell getCell(int x, int y)
    {
        return board[y][x];
    }
    public ArrayList<Organism> getOrganisms(){return organisms;}
    public WorldSim getSimulator()
    {
        return simulator;
    }
    public boolean needHumanInput()
    {
        return humanTurn;
    }
    public void setHumanTurn(boolean turn)
    {
        humanTurn=turn;
    }
    public boolean checkHumanExistence(){return humanLives;}
    public Organism getHuman()
    {
        return turnQueue.get(0);
    }
}
