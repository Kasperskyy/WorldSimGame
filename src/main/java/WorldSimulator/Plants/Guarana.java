package WorldSimulator.Plants;

import WorldSimulator.Organism;
import WorldSimulator.OrganismTypes.Plant;
import WorldSimulator.World;

public class Guarana extends Plant
{
    public Guarana(int x, int y,int age, World world)
    {
        super(x,y,age,world,World.OrganismType.Guarana);
        strength = 0;
        initiative = 0;
    }
    @Override
    public boolean collisionDefence(int attackerX, int attackerY)
    {
        Organism temp = (world.getCreature(attackerX, attackerY));
        temp.setStrength(temp.getStrength() + 3);
        return true;
    }
}
