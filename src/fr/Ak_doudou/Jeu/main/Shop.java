package fr.Ak_doudou.Jeu.main;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import fr.Ak_doudou.GestionMenu.main.GestionMenu;
import fr.Ak_doudou.GestionMenu.main.MenuCustom;

public class Shop {

	private Location loc;
	public HashMap<String, MenuCustom>categorie = new HashMap<String, MenuCustom>();
	
	public Shop(Location l)
	{
		loc = l;
	}
	
	public void addVente(String classe, MenuCustom menu)
	{
		if (categorie.containsKey(classe))
		{
			menu.addElem(0, ChatColor.GOLD + "Bond", null, Material.FEATHER, 0);
			categorie.put(classe, menu);
		}
		else
		{
			categorie.put(classe, menu);
		}
	}
	
	public boolean openShop(PlayerAssault p, Location l)
	{
		if (l.equals(loc))
		{
			if (categorie.containsKey(p.getClasse().getName()))
			{
				Bukkit.broadcastMessage("passe");
				GestionMenu.openMenu(p.getPlayer(), categorie.get(p.getClasse().getName()));
				return true;
			}
		}
		return false;
	}
}
