
package WorldSimulator.Animals;

import WorldSimulator.OrganismTypes.Animal;
import WorldSimulator.Organism;
import WorldSimulator.World;

import java.util.ArrayList;

public class Antelope extends Animal
{
    public  Antelope(int x, int y, int age, World world)
	{
        super(x,y,age,world,World.OrganismType.Antelope);
		strength = 4;
		initiative = 4;
	}
    @Override
	protected boolean action()
	{
		int newX = x, newY = y;
		ArrayList<Integer> possibleMovements=new ArrayList<Integer>();
		if (getValidAdjacentCells(x, y, false, possibleMovements))
		{
        	int dir = rand.nextInt(possibleMovements.size());
            newX += world.getDirX(possibleMovements.get(dir));
            newY += world.getDirY(possibleMovements.get(dir));
		}
		else
			return true;
		possibleMovements.clear();
		if (getValidAdjacentCells(newX, newY, false, possibleMovements))
		{
            int dir = rand.nextInt(possibleMovements.size());	
            newX += world.getDirX(possibleMovements.get(dir));
            newY += world.getDirY(possibleMovements.get(dir));
			if (world.checkCell(newX, newY)||(x==newX&&y==newY))
			{
				world.moveOrganism(x, y, newX, newY);
				x = newX;
				y = newY;
				return true;
			}
			else
			{
				int escapes = rand.nextInt() % 2;
				possibleMovements.clear();
				if ((escapes==1)&& getValidAdjacentCells(newX, newY, true, possibleMovements))
				{
					//changeCoordinates(&newX, &newY, possibleMovements);
                	dir = rand.nextInt(possibleMovements.size());
                    newX += world.getDirX(possibleMovements.get(dir));
                    newY += world.getDirY(possibleMovements.get(dir));
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
	protected boolean collisionDefence(int attackerX, int attackerY)
	{
		Organism opponent = world.getCreature(attackerX, attackerY);
		ArrayList<Integer>possibleMovements=new ArrayList<Integer>();
		int escapes = rand.nextInt() % 2,newX=x,newY=y;
		if ((escapes==1) && getValidAdjacentCells(newX, newY, true, possibleMovements))
		{
        	int dir = rand.nextInt(possibleMovements.size());
        	newX += world.getDirX(possibleMovements.get(dir));
        	newY += world.getDirY(possibleMovements.get(dir));
			world.moveOrganism(x, y, newX, newY);
			world.moveOrganism(attackerX, attackerY, x, y);
			opponent.setX(x);
			opponent.setY(y);
			x = newX;
			y = newY;
			return false;
		}
		else
			return true;
	}
}
