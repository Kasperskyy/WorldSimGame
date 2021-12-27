
package WorldSimulator.OrganismTypes;

import WorldSimulator.Organism;
import WorldSimulator.World;

public abstract class Plant extends Organism
{
    protected int sowChance=10;
    protected Plant(int x, int y, int age, World world,World.OrganismType id)
    {
        super(x,y,age,world,id);
        isAnimal = false;        
    }
    @Override
    public boolean action()
    {
        int breed = rand.nextInt() % sowChance;
	    if (breed == 0)
	        multiply();
	    return true;
    }
}
