package WorldSimulator.Plants;

import WorldSimulator.OrganismTypes.Plant;
import WorldSimulator.World;

public class Grass extends Plant
{
    public Grass(int x, int y, int age, World world)
    {
        super(x,y,age,world,WorldSimulator.World.OrganismType.Grass);
        strength=0;
        initiative=0;
    }
}
