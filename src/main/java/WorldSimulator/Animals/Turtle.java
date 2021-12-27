
package WorldSimulator.Animals;

import WorldSimulator.OrganismTypes.Animal;
import WorldSimulator.Organism;
import WorldSimulator.World;

import java.util.ArrayList;

public class Turtle extends Animal
{
	public Turtle(int x, int y, int age, World world)
	{
    	super(x,y,age,world,World.OrganismType.Turtle);
		strength = 2;
		initiative = 1;
	}
    @Override  
    public boolean action()
	{
		int move = rand.nextInt() % 4;
		if (move == 3)
		{
			int newX = x, newY = y;
			ArrayList<Integer> possibleMovements=new ArrayList<Integer>();
			if (getValidAdjacentCells(x, y, false, possibleMovements))
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
    @Override
    public boolean collisionDefence(int attackerX, int attackerY)
	{
		Organism opponent = world.getCreature(attackerX, attackerY);
		if (opponent.getStrength() < 5)
			return false;
		else
			return true;
	}
}
