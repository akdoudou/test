package fr.Ak_doudou.Jeu.main;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import fr.Ak_doudou.APIMiniJeu.main.Kit;
import fr.Ak_doudou.APIMiniJeu.main.PlayerAPI;

public class PlayerAssault extends PlayerAPI {

	private int money = 0;
	private Classes classe;
	private List<Item>inv = new LinkedList<Item>();
	private List<Integer>durability = new LinkedList<Integer>();
	private int nbDie;
	
	public PlayerAssault(UUID uuid, Kit defaultkit) {
		super(uuid, defaultkit);
		nbDie = 0;
	}

	public void giveMoney(int value){money += value;}
	public int getMoney(){return money;}
	public void setClasse(Classes c){classe = c;}
	public int getNbDie() { return nbDie;}
	public Classes getClasse(){return classe;}
	
	public void addItem(Item it)
	{
		inv.add(it);
		durability.add(0);
		this.getPlayer().getInventory().addItem(it.getItemStack());
	}
	
	public void die()
	{
		for (int i = 0; i < inv.size(); i++) {
			if (inv.get(i).getDurability() == durability.get(i))
			{
				inv.remove(i);
				durability.remove(i);
				i = 0;
			}
			else
			{
				durability.add(i, durability.get(i) + 1);
			}
		}
		this.getPlayer().getInventory().clear();
		for (int i = 0; i < inv.size(); i++) {
			this.getPlayer().getInventory().addItem(inv.get(i).getItemStack());
		}
		this.teleportLocDepart();
		this.getPlayer().setHealth(20);
	}
	
}
