package fr.Ak_doudou.Jeu.main;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Item {

	private String name;
	private ItemStack it;
	private int price;
	private int durability;
	public Item(String n, Material mat, int p, int d)
	{
		name = n;
		it = new ItemStack(mat);
		ItemMeta meta = it.getItemMeta();
		meta.setDisplayName(n);
		it.setItemMeta(meta);
		price = p;
		durability = d;
	}
	
	public int getPrice() { return price;}
	public int getDurability() { return durability;}
	public String getName() { return name;}
	public ItemStack getItemStack() { return it;};
}
