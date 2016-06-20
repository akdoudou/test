package fr.Ak_doudou.Jeu.main;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Variable {

	public static TeamAssault t1;
	public static void init()
	{
		t1 = new TeamAssault("red", new Location(Jeu.w, -48, 4, -14), 4, new Location(Bukkit.getWorld("test"), -52, 4, -14));
		t1.registerChest(new Location(Jeu.w, -50, 4, -11));
		t1.registerChest(new Location(Jeu.w, -52, 4, -11));
		t1.registerChest(new Location(Jeu.w, -52, 4, -17));
		t1.registerChest(new Location(Jeu.w, -50, 4, -17));
	}
}
