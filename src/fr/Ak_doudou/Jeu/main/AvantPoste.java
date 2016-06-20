package fr.Ak_doudou.Jeu.main;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.Ak_doudou.APIMiniJeu.main.PlayerAPI;

public class AvantPoste {

	private Location loc;
	private TeamAssault team;
	private int value;
	private int timeMoney;
	private int countTime;
	public AvantPoste(Location l)
	{
		loc = l;
		value = 1;
		timeMoney = 6;
		countTime = 6;
		team = null;
	}
	
	public Location getLocation() { return loc; }
	public int getValue() { return value;}
	
	public boolean breakBlock(PlayerAPI p, Block b)
	{
		if (b.getLocation().equals(loc))
		{
			if (team == null || !team.hasPlayer(p.getUUID()))
			{
				System.out.println("teamSet");
				team = (TeamAssault) p.getTeam();
			}
			return true;
		}
		return false;
	}
	
	public void start()
	{
		loc.getWorld().getBlockAt(loc).setType(Material.GOLD_BLOCK);
	}
	
	public void run ()
	{
		if (countTime >= timeMoney)
		{
			if (team != null)
				team.giveMoney(value);
			else if (team == null)
				countTime = 0;
		}
		else
			countTime++;
	}
}
