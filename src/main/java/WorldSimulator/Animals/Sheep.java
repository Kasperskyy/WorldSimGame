
package WorldSimulator.Animals;

import WorldSimulator.OrganismTypes.Animal;
import WorldSimulator.World;

public class Sheep extends Animal
{
	public Sheep(int x, int y,int age, World world)
	{
    	super(x,y,age,world,World.OrganismType.Sheep);
		strength = 4;
		initiative = 4;
	}
}
