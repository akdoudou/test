package fr.Ak_doudou.Jeu.main;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.Ak_doudou.APIMiniJeu.main.PlayerAPI;
import fr.Ak_doudou.APIMiniJeu.main.Team;

public class TeamAssault extends Team{

	private Location nexus;
	private List<Location> Life = new LinkedList<Location>();
	private int life;
	private List<Location> chest = new LinkedList<Location>();
	private HashMap<Location, Inventory> locChest = new HashMap<Location, Inventory>();
	private ItemStack piece;
	private List<Shop> lshop = new LinkedList<Shop>();
	
	public TeamAssault(String c, Location n, int l, Location s) {
		super(c, s);
		nexus = n;
		life = l;
		Life.add(new Location(nexus.getWorld(), nexus.getX(), nexus.getY(), nexus.getZ() + 1));
		Life.add(new Location(nexus.getWorld(), nexus.getX(), nexus.getY(), nexus.getZ() - 1));
		Life.add(new Location(nexus.getWorld(), nexus.getX() + 1, nexus.getY(), nexus.getZ()));
		Life.add(new Location(nexus.getWorld(), nexus.getX() - 1, nexus.getY(), nexus.getZ()));
		piece = new ItemStack(Material.DOUBLE_PLANT);
		ItemMeta meta = piece.getItemMeta();
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		piece.setItemMeta(meta);
	}

	public void start()
	{
		for (int i = 0; i < Life.size(); i++) {
			nexus.getWorld().getBlockAt(Life.get(i)).setType(Material.EMERALD_BLOCK);
		}
		for (int i = 0; i < chest.size(); i++) {
			if (i + 1 > this.nbPlayer)
				break;
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "setblock " + chest.get(i).getX() + " " + chest.get(i).getY() + " " + chest.get(i).getZ() + " chest 0 replace {CustomName:" + this.players.get(i).getPlayer().getDisplayName() + "}");
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "setblock " + chest.get(i).getX() + " " + (chest.get(i).getY() + 1) + " " + chest.get(i).getZ() + " minecraft:skull 1 replace {ExtraType:"+ this.players.get(i).getPlayer().getDisplayName() +",SkullType:3,Rot:4}");
		}
	}
	
	public void registerChest(Location loc)
	{
		chest.add(loc);
	}
	
	public void loseLife()
	{
		if (Jeu.game.isRunning())
		{
			life--;
			if (life == 0)
				Jeu.game.stop();
		}
	}
	
	public boolean isBlockLife(Location loc)
	{
		if (Life.contains(loc))
			return true;
		return false;
	}
	
	public void giveMoney(int value)
	{
		for (int i = 0; i < this.players.size(); i++) {
			((PlayerAssault) players.get(i)).giveMoney(value);
			if (locChest.get(chest.get(i)) != null)
				locChest.get(chest.get(i)).addItem(piece);
		}
	}
	
	public boolean openChest(PlayerAPI p, Location loc, Inventory inv)
	{
		boolean ret = false;
		for (int i = 0; i < chest.size(); i++) {
			if (chest.get(i).equals(loc))
			{
				ret = true;
				if (i == getPlayerId(p))
				{
					locChest.put(loc, inv);
					return false;
				}
			}
		}
		return ret;
	}
	
	public void registerShop(Shop s)
	{
		lshop.add(s);
	}
	
	public boolean openShop(PlayerAssault p, Location loc)
	{
		for (int i = 0; i < lshop.size(); i++) {
			if (lshop.get(i).openShop(p, loc))
				return true;
		}
		return false;
	}
}
