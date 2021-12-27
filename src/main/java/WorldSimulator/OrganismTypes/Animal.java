package WorldSimulator.OrganismTypes;

import WorldSimulator.Organism;
import WorldSimulator.World;

import java.util.ArrayList;

public abstract class Animal extends Organism
{
    protected Animal(int x, int  y, int age, World world, World.OrganismType id)
	{
    	super(x,y,age,world,id);
		isAnimal = true;
	}
    @Override
	protected boolean action()
	{
		Integer newX=x, newY=y;
		ArrayList<Integer> possibleMovements=new ArrayList<Integer>();
		if (getValidAdjacentCells(x, y,false,possibleMovements))
		{
            int dir = rand.nextInt(possibleMovements.size());	
            	newX += world.getDirX(possibleMovements.get(dir));
            	newY += world.getDirY(possibleMovements.get(dir));
			if (world.checkCell(newX, newY))
			{
				world.moveOrganism(x, y, newX, newY);
				x = newX;
				y = newY;
				return true;
			}
			else
			{
				return collisionOffence(newX, newY);
			}
		}
		return true;
	}
}
