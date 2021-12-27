package WorldSimulator.Animals;
import WorldSimulator.OrganismTypes.Animal;
import WorldSimulator.World;

import java.awt.event.KeyEvent;


public class Human extends Animal
{
    private int coolDown;
    private int nextKey;
    public Human(int x, int y,int age, World world)
    {
        super(x,y,age,world,World.OrganismType.Human);
        strength = 5;
        initiative = 4;
    }
    public void useAbility()
    {
        coolDown = 10;
        strength +=5;
    }
    private void decreaseCoolDown()
    {
        if (coolDown>5)
            strength--;
        coolDown--;
    }
    public void setCoolDown(int val)
    {
        coolDown=val;
    }
    public int getCoolDown()
    {
        return coolDown;
    }
    @Override
    public boolean action()
    {
        if (coolDown != 0)
            decreaseCoolDown();
        int newX = x, newY = y;
        switch(nextKey)
        {
            case KeyEvent.VK_SPACE:
                break;
            case KeyEvent.VK_UP:
                newY--;
                break;
            case KeyEvent.VK_DOWN:
                newY++;
                break;
            case KeyEvent.VK_LEFT:
                newX--;
                break;
            case KeyEvent.VK_RIGHT:
                newX++;
                break;
        }
        if ((newX == x) && (newY == y))
            return true;
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
    public boolean validateNextMove(int nextKey)
    {
        this.nextKey=nextKey;
        switch(nextKey)
        {
            case KeyEvent.VK_SPACE:
                return true;
            case KeyEvent.VK_5:
            case KeyEvent.VK_NUMPAD5:
                if(coolDown==0)
                {
                    useAbility();
                    world.getSimulator().setCoolDownLabel("YOUR TURN! Your strength: " + strength + ". Your cooldown: " + coolDown + " (0= Potion Available)");
                }
                return false;
            case  KeyEvent.VK_UP:
                if (y-1<0)
                    return false;
                break;
            case  KeyEvent.VK_DOWN:
                if (y+1>=world.getHeight())
                    return false;
                break;
            case  KeyEvent.VK_LEFT:
                if (x-1<0)
                    return false;
                break;
            case  KeyEvent.VK_RIGHT:
                if (x+1>=world.getWidth())
                    return false;
                break;
            default:
                return false;
        }
        return true;
    }
}
