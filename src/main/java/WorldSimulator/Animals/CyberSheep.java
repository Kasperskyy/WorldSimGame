package WorldSimulator.Animals;
import WorldSimulator.OrganismTypes.Animal;
import WorldSimulator.Organism;
import WorldSimulator.World;

import java.lang.Math;
import java.util.ArrayList;

public class CyberSheep extends Animal
{
    public CyberSheep(int x, int y, int age, World world)
    {
        super(x,y,age,world,World.OrganismType.CyberSheep);
        strength = 11;
        initiative = 4;
    }
    public boolean action()
    {
        int newX = x, newY = y;
        ArrayList<Organism> sosnowskyList=new ArrayList<Organism>();
        int count = world.worldScan(World.OrganismType.SosnowskysHogweed, sosnowskyList);
        if (count > 0)	{
            int minIndex = 0,minDistance = world.getWidth() * world.getHeight(), distance = 0;
            for (int i = 0; i < count; i++)
            {
                distance = cyberSniff(x, y, sosnowskyList.get(i).getX(), sosnowskyList.get(i).getY());
                if (distance < minDistance)
                {
                    minDistance = distance;
                    minIndex = i;
                }
            }
            int targetX = sosnowskyList.get(minIndex).getX();
            int targetY = sosnowskyList.get(minIndex).getY();
            if (targetX < x)
                newX--;
            else if (targetX > x)
                newX++;
            else if (targetY < y)
                newY--;
            else if (targetY > y)
                newY++;
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
        else
        {
            ArrayList<Integer> possibleMovements= new ArrayList<Integer>();
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
            return true;
        }
    }
    private int cyberSniff(int x, int y, int nX, int nY)
    {
        return (Math.abs(x - nX) + Math.abs(y - nY));
    }

}
