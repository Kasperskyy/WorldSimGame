package WorldSimulator.Plants;

import WorldSimulator.OrganismTypes.Plant;
import WorldSimulator.World;

public class SowThistle extends Plant
{
    public SowThistle(int x, int y, int age, World world)
    {
        super(x,y,age,world,World.OrganismType.SowThistle);
        strength = 0;
        initiative = 0;
    }
    @Override
    public boolean action()
    {
        for (int i = 0; i < 3; i++)
        {
            int breed = rand.nextInt() % sowChance;
            if (breed == 0)
                multiply();
        }
        return true;
    }
}
