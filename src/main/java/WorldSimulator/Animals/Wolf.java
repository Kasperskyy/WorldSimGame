
package WorldSimulator.Animals;

import WorldSimulator.OrganismTypes.Animal;
import WorldSimulator.Organism;
import WorldSimulator.World;

public class Wolf extends Animal
{
	public  Wolf(int x, int y,int age, World world)
	{
    	super(x,y,age,world,World.OrganismType.Wolf);
		strength = 9;
		initiative = 5;
	}
}
