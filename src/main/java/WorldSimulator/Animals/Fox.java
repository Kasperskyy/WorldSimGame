
package WorldSimulator.Animals;

import WorldSimulator.OrganismTypes.Animal;
import WorldSimulator.World;

import java.util.ArrayList;
import java.util.Collections;

public class Fox extends Animal
{
	public Fox(int x, int y, int age, World world)
	{
    	super(x,y,age,world,World.OrganismType.Fox);
		strength = 3;
		initiative = 7;
	}
	private void sniff(ArrayList<Integer> possibleMovements)
	{
		for (int i = 0; i < possibleMovements.size(); i++)
		{
			int temp = possibleMovements.get(i);
			if (!world.checkCell(x+ world.getDirX(temp),y+ world.getDirY(temp)))
			{
				if ((world.getCreature(x+ world.getDirX(temp),y+ world.getDirY(temp)).getStrength() > strength)&&(world.getCreature(x+ world.getDirX(temp),y+ world.getDirY(temp)).getId()!=id))
				{
          	    	Collections.swap(possibleMovements,i,possibleMovements.size()-1);
					possibleMovements.remove(possibleMovements.size() - 1);
					i--;
				}
			}
		}
	}
	@Override
	public boolean action()
	{
		int newX = x, newY = y;
		ArrayList<Integer> possibleMovements=new ArrayList<Integer>();
		if (getValidAdjacentCells(x, y, false, possibleMovements))
		{
			sniff(possibleMovements);
			if (possibleMovements.size() != 0)
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
		}
		return true;
	}
}
