package WorldSimulator.Plants;

import WorldSimulator.Organism;
import WorldSimulator.OrganismTypes.Plant;
import WorldSimulator.World;

import java.util.ArrayList;

public class Sosnowsky extends Plant
{
    public Sosnowsky(int x, int y,int age, World world)
    {
        super(x,y,age,world,World.OrganismType.SosnowskysHogweed);
        sowChance *= 2;
        strength = 10;
        initiative = 0;
    }
    @Override
    public boolean action()
    {
        ArrayList<Integer> possibleMovements=new ArrayList<Integer>();
        if (getValidAdjacentCells(x, y, false, possibleMovements))
        {
            for (int direction : possibleMovements)
            {
                if (!world.checkCell(x + world.getDirX(direction), y + world.getDirY(direction)))
                {
                    Organism temp = (world.getCreature(x + world.getDirX(direction), y + world.getDirY(direction)));
                    if (temp.isAnimalCheck()&&temp.getId()!=World.OrganismType.CyberSheep)
                        world.removeOrganism(temp,false);
                }
            }
        }
        int breed = rand.nextInt() % sowChance;
        if (breed == 0)
            multiply();
        return true;
    }
    @Override
    public boolean collisionDefence(int attackerX, int attackerY)
    {
        Organism temp = (world.getCreature(attackerX, attackerY));
        if (temp.getId() == World.OrganismType.CyberSheep)
            return true;
        else
            world.removeOrganism(temp,false);
        return false;
    }
}
