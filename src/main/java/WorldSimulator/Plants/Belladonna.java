package WorldSimulator.Plants;

import WorldSimulator.Organism;
import WorldSimulator.OrganismTypes.Plant;
import WorldSimulator.World;

public class Belladonna extends Plant
{
    public Belladonna(int x, int y,int age, World world)
    {
        super(x,y,age, world,World.OrganismType.Belladonna);
        sowChance *= 2;
        strength = 99;
        initiative = 0;
    }
    @Override
    public boolean collisionDefence(int attackerX, int attackerY)
    {
        boolean toDelete = false;
        Organism temp = (world.getCreature(attackerX, attackerY));
        if (temp.getStrength() > strength)
            toDelete = true;
        world.removeOrganism(temp,false);
        if(toDelete)
            world.removeOrganism(this,false);
        return false;
    }
}
