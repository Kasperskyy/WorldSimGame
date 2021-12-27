
package WorldSimulator;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Random;
public abstract class Organism 
{
    protected int x,y,initiative,strength,age=0;
    protected int breedChance = 1;//100% default success rate
    protected boolean isAnimal;
    protected World world;	
    protected World.OrganismType id;
    protected Random rand = new Random();
	protected abstract boolean action();
	protected Organism(int x, int y, int age, World world, World.OrganismType id)
    {
    	this.id=id;
    	this.age = age;
		this.x = x;
		this.y = y;
		this.world = world;
		this.world.addOrganism(this);
    }
    protected boolean getValidAdjacentCells(int x, int y,boolean reqEmpty, ArrayList<Integer> cells)
	{
		if (reqEmpty)
		{
			for (int i = 0; i < world.dirCount; i++)
			{
				if(world.validMove(x+ world.getDirX(i),y+ world.getDirY(i)) && world.checkCell(x+ world.getDirX(i),y+ world.getDirY(i)))
					cells.add(i);
			}
		}
		else
		{
			for (int i = 0; i < world.dirCount; i++)
			{
				if (world.validMove(x + world.getDirX(i), y + world.getDirY(i)))
					cells.add(i);
			}
		}
		if (cells.isEmpty())
			return false;
		else;
			return true;
	}
	protected boolean collisionOffence(int defenderX, int defenderY)
	{
		Organism opponent = world.getCreature(defenderX, defenderY);
		if (opponent.getId() == id)
		{
			multiply();
			return true;
		}
		if (opponent.collisionDefence(x, y))
		{
			String temp =  id + ((opponent.isAnimal) ? " is attacking " : " is attempting to eat ")+ opponent.id+ " at coordinates X: "+(defenderX)+ ", Y: "+ (defenderY) + ". " ;
			world.simulator.addAnnouncement(temp);
			if (strength >= opponent.getStrength())
			{
				world.removeOrganism(opponent,false);
				world.moveOrganism(x, y, defenderX, defenderY);
				x = defenderX;
				y = defenderY;
				return true;
			}
			else
			{
				return false;
			}
		}
		else return true;
	}
	protected boolean collisionDefence(int attackerX, int attackerY)
{
	return true;
}
	protected void multiply()
	{
		if (rand.nextInt() % breedChance == 0)
		{
			int newX = x, newY = y;
            ArrayList<Integer> possibleMovements =new ArrayList<Integer>();
			if (getValidAdjacentCells(x, y, true, possibleMovements))
			{
            	int dir = rand.nextInt(possibleMovements.size());
            	newX += world.getDirX(possibleMovements.get(dir));
            	newY += world.getDirY(possibleMovements.get(dir));
				Organism newOrganism=null;
				String temp =   id + ((isAnimal) ? " has given birth at coordinates X: " : " has successfully grown from a seed at X:  ") + (newX) + ", Y: " + (newY) + "." ;
				world.simulator.addAnnouncement(temp);
				spawnInstance(newX, newY,0, world);
			}
		}
	}
	protected void spawnInstance(int x, int y, int age, World world)
	{
		try
		{
			Constructor<? extends Organism> creator = getClass().getDeclaredConstructor(int.class, int.class, int.class, World.class);
			Organism newOrg = creator.newInstance(x,y,0,world);
		}

		catch(Exception except)
		{

		}
	}
	public int getAge()
	{
		return age;
	}
	public int getInit()
	{
		return initiative;
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public int getStrength()
	{
		return strength;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public void setY(int y)
	{
		this.y = y;
	}
	public World.OrganismType getId()
	{
		return id;
	}
	public void setStrength(int strength)
	{
		this.strength = strength;
	}
	public boolean isAnimalCheck()
	{
		return isAnimal;
	}
	public void yearOlder()
	{
		age++;
	}
}
