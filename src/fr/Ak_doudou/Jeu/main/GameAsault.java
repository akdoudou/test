package fr.Ak_doudou.Jeu.main;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.Ak_doudou.APIMiniJeu.main.MiniJeu;

public class GameAsault extends MiniJeu{

	private List<AvantPoste> lap = new LinkedList<AvantPoste>();
	private List<Classes>classes = new LinkedList<Classes>();
	private List<Item>items = new LinkedList<Item>();
	
	public boolean breakNexusLife(UUID p, Location loc)
	{
		TeamAssault team;
		if ((team = isBlockNexus(loc)) != null)
		{
			Bukkit.broadcastMessage(team.getColor());
			if (team.hasPlayer(p))
				return true;
		}
		team.loseLife();
		return false;
	}
	
	public void loop()
	{
		if (this.running)
		{
			for (int i = 0; i < lap.size(); i++) {
				lap.get(i).run();
			}
		}
	}
	
	public boolean isBlockAP(Player p, Block b)
	{
		for (int i = 0; i < lap.size(); i++) {
			if (lap.get(i).breakBlock(getPlayerAPI(p.getUniqueId()), b))
				return true;
		}
		return false;
	}
	
	public TeamAssault isBlockNexus(Location loc)
	{
		for (int i = 0; i < teams.size(); i++) {
			if (((TeamAssault) teams.get(i)).isBlockLife(loc))
				return (TeamAssault) teams.get(i);
		}
		return null;
	}
	
	public void addAP(AvantPoste ap)
	{
		lap.add(ap);
	}
	
	public void start()
	{
		super.start();
		for (int i = 0; i < lap.size(); i++) {
			lap.get(i).start();
		}
	}

	public boolean openChest(Player p, Location loc, Inventory inv) {
		boolean ret = false;
		for (int i = 0; i < teams.size(); i++) {
			if (((TeamAssault) teams.get(i)).openChest(this.getPlayerAPI(p.getUniqueId()), loc, inv))
					ret = true;
		}
		return ret;
	}
	
	public boolean openShop(Location loc, PlayerAssault p)
	{
		for (int i = 0; i < teams.size(); i++) {
			if (teams.get(i).hasPlayer(p.getUUID()))
			{
				if (((TeamAssault) teams.get(i)).openShop(p, loc))
					return true;
			}
		}
		return false;
	}
	
	public void registerClasses(Classes c)
	{
		classes.add(c);
	}
	
	public void registerItems(Item i)
	{
		items.add(i);
	}
	
	public Item getItems(String name)
	{
		for (int j = 0; j < items.size(); j++) {
			if (items.get(j).getName().equals(name))
				return items.get(j);
		}
		return null;
	}
	public void setClasseToPlayer(PlayerAssault p, String classe)
	{
		for (int i = 0; i < classes.size(); i++) {
			if (classes.get(i).getName().equals(classe))
			{
				p.setClasse(classes.get(i));
				p.getPlayer().sendMessage("classe set");
			}
		}
	}
}
