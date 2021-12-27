package WorldSimulator.Worlds;

import WorldSimulator.Main.WorldSim;
import WorldSimulator.World;
import java.util.HashMap;


public class SquareWorld extends World//abstraction with another hexagonal world in mind. Sadly, I did not have time to create the hexagonal map
{
      public SquareWorld(WorldSim simulator)
      {
            super(simulator);
            directionX= new HashMap<Integer,Integer>();
            directionY= new HashMap<Integer,Integer>();
            directionX.put(direction.North.ordinal(),0);
            directionX.put(direction.West.ordinal(),-1);
            directionX.put(direction.East.ordinal(),1);
            directionX.put(direction.South.ordinal(),0);
            directionY.put(direction.North.ordinal(),-1);
            directionY.put(direction.West.ordinal(),0);
            directionY.put(direction.East.ordinal(),0);
            directionY.put(direction.South.ordinal(),1);
            dirCount=directionX.size();
      }
    
}
