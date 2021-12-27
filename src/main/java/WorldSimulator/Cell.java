package WorldSimulator;


import WorldSimulator.Animals.*;
import WorldSimulator.Plants.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Cell extends JButton
{
	private World world;
	private int x,y;
	private  Organism currentOrg;
	public Cell(int x, int y,World world)
	{
		this.world=world;
		this.x=x;
		this.y=y;
		currentOrg = null;
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(currentOrg==null)
				{
					int creatureId;
					String idString= JOptionPane.showInputDialog("Please choose organism to add: \n0-Wolf\n1-Sheep\n2-Fox\n3-Turtle\n4-Antelope\n5-Cyber Sheep\n6-Grass\n7-Sow Thistle\n8-Guarana\n9-Belladonna\n10-Sosnowsky's Hogweed\n11-Human(only if no humans on field)");
					try{
						 creatureId= Integer.parseInt(idString);
					}catch (NumberFormatException ex) {
						creatureId=-1;//no such organism
					}
					if(creatureId>0 && creatureId < World.OrganismType.values().length)
					createLife(creatureId);
				}
			}
		});
	}
	public boolean occupied()
	{
		if (currentOrg == null)
			return false;
		else
			return true;
	}
	public void setCell(Organism newOrg)
	{
		currentOrg = newOrg;
		if(newOrg.id== World.OrganismType.Human)
			setBackground(Color.GREEN);
		setIcon(new ImageIcon(newOrg.world.iconMap.get(newOrg.id.ordinal())));
	}
	public void clearCell()
	{
		setBackground(Color.LIGHT_GRAY);
		currentOrg = null;
		setIcon(null);
	}
	private void createLife(int id)
	{
		Organism temp;
		switch (World.OrganismType.values()[id]) {
			case Wolf:
				temp = new Wolf(x, y, 0, world);
				break;
			case Sheep:
				temp = new Sheep(x, y, 0, world);
				break;
			case Fox:
				temp = new Fox(x, y, 0, world);
				break;
			case Turtle:
				temp = new Turtle(x, y, 0, world);
				break;
			case Antelope:
				temp = new Antelope(x, y, 0, world);
				break;
			case CyberSheep:
				temp = new CyberSheep(x, y, 0, world);
				break;
			case Grass:
				temp = new Grass(x, y, 0, world);
				break;
			case SowThistle:
				temp = new SowThistle(x, y, 0, world);
				break;
			case Guarana:
				temp = new Guarana(x, y, 0, world);
				break;
			case Belladonna:
				temp = new Belladonna(x, y, 0, world);
				break;
			case SosnowskysHogweed:
				temp = new Sosnowsky(x, y, 0, world);
				break;
			case Human:
				if(!world.checkHumanExistence())
					temp = new Human(x, y, 0, world);
				break;
		}
	}
	public Organism getCell()
	{
		return currentOrg;
	}
}
