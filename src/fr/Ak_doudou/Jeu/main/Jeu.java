package fr.Ak_doudou.Jeu.main;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import fr.Ak_doudou.APIMiniJeu.main.Kit;
import fr.Ak_doudou.GestionMenu.main.GestionMenu;
import fr.Ak_doudou.GestionMenu.main.MenuCustom;

public class Jeu extends JavaPlugin implements Listener{
	
	static GameAsault game = new GameAsault();
	static World w;
	public void onEnable()
	{
		getLogger().log(Level.INFO, "Chargement du plugin Jeu");
		Bukkit.getPluginManager().registerEvents(this, this);
		Kit k = new Kit();
		w = Bukkit.getWorld("test");
		k.addItemInventory(new ItemStack(Material.STONE_SWORD));
		game.setDefaultKit(k);
		TeamAssault t = new TeamAssault("red", new Location(Bukkit.getWorld("test"), -48, 4, -14), 4, new Location(Bukkit.getWorld("test"), -52, 4, -14));
		t.registerChest(new Location(w, -50, 4, -11));
		t.registerChest(new Location(w, -52, 4, -11));
		t.registerChest(new Location(w, -52, 4, -17));
		t.registerChest(new Location(w, -50, 4, -17));

		Shop s = new Shop(new Location(w, -24, 5, -15));
		MenuCustom menu = GestionMenu.createMenu("test");
		menu.addElem(0, ChatColor.GOLD + "Bond", "buy Bond mcvfmkldfxkj563gfasdhuil", Material.FEATHER, 0);
		s.addVente("Guerrier", menu);
		t.registerShop(s);

		game.registerTeam(t);
		t = new TeamAssault("blue", new Location(Bukkit.getWorld("test"), -30, 4, -14), 4, new Location(Bukkit.getWorld("test"), -26, 4, -14));
		t.registerChest(new Location(w, -28, 4, -18));
		t.registerChest(new Location(w, -26, 4, -18));
		t.registerChest(new Location(w, -28, 4, -10));
		t.registerChest(new Location(w, -26, 4, -10));
		
		s = new Shop(new Location(w, -24, 5, -15));
		menu = GestionMenu.createMenu("test");
		menu.addElem(0, ChatColor.GOLD + "Bond", "buy Bond mcvfmkldfxkj563gfasdhuil", Material.FEATHER, 0);
		s.addVente("Guerrier", menu);
		t.registerShop(s);
		
		game.registerTeam(t);

		game.registerItems(new Item(ChatColor.GOLD + "Bond", Material.FEATHER, 30, 3));
		
		game.addAP(new AvantPoste(new Location(w, -45, 4, -14)));
		game.addAP(new AvantPoste(new Location(w, -39, 4, -14)));
		game.addAP(new AvantPoste(new Location(w, -33, 4, -14)));
		
		Classes c = new Classes("Guerrier");
		game.registerClasses(c);
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
               game.loop();
            }
        }, 0L, 20L);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		PlayerAssault p = new PlayerAssault(e.getPlayer().getUniqueId(), game.getDefaultKit());
		game.registerPlayer(p);
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e)
	{
		game.deletePlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerBreak(BlockBreakEvent e)
	{
		if (e.getBlock().getType().equals(Material.EMERALD_BLOCK))
		{
			e.setCancelled(game.breakNexusLife(e.getPlayer().getUniqueId(), e.getBlock().getLocation()));
		}
		if (e.getBlock().getType().equals(Material.GOLD_BLOCK))
		{
			e.setCancelled(game.isBlockAP(e.getPlayer(), e.getBlock()));
		}
	}
	
	@EventHandler
	public void onOpenChest(InventoryOpenEvent e)
	{
		if (e.getInventory().getType().equals(InventoryType.CHEST))
		{
			e.setCancelled(game.openChest((Player) e.getPlayer(), e.getInventory().getLocation(), e.getInventory()));
		}
	}
	
	@EventHandler
	public void onFight(EntityDamageByEntityEvent e)
	{
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player)
			if (game.isOnSameTeam(e.getEntity().getUniqueId(), e.getDamager().getUniqueId()))
				e.setCancelled(true);
	}
	
	@EventHandler
	public void onOpenShop(PlayerInteractAtEntityEvent e)
	{
		Location loc = new Location(w, (int)e.getRightClicked().getLocation().getX(), (int)e.getRightClicked().getLocation().getY(), (int)e.getRightClicked().getLocation().getZ());
		if (game.openShop(loc, ((PlayerAssault) game.getPlayerAPI(e.getPlayer().getUniqueId()))))
		{
	//		e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerTakeDamage(EntityDamageEvent e)
	{
		if (e.getEntity() instanceof Player)
		{
			Player p = (Player) e.getEntity();
			if (p.getHealth() - e.getDamage() <= 0)
			{
				e.setCancelled(true);
				((PlayerAssault)game.getPlayerAPI(p.getUniqueId())).die();;
			}
		}
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		if (label.equals("start"))
		{
			game.start();
			start();
		}
		if (label.equals("setteam"))
		{
			game.setTeamToPlayer(args[1], game.getPlayerAPI(Bukkit.getPlayer(args[0]).getUniqueId()));
		}
		if (label.equals("classe"))
		{
			game.setClasseToPlayer((PlayerAssault)game.getPlayerAPI(Bukkit.getPlayer(args[0]).getUniqueId()), args[1]);
		}
		if (label.equals("test"))
		{
		//	Location loc = new Location(w, -24, 5, -15);
		//	game.openShop(loc, ((PlayerAssault) game.getPlayerAPI(((Player)sender).getUniqueId())));
	//		GestionMenu.openMenu(Bukkit.getPlayerExact("Ak_doudou"), Shop.categorie.get("Guerrier"));
		}
		if (label.equals("buy"))
		{
			if (sender instanceof Player)
			{
				if (args[1].equals("mcvfmkldfxkj563gfasdhuil"))
				{
					achat(((Player)sender), args[0]);
				}
			}
		}
		return true;
	}	

	public void achat(Player p, String nom)
	{
		if (nom.equals("Bond"))
		{
			int count = 0;
			Inventory inv = p.getInventory();
			for (int i = 0; i < inv.getSize(); i++) {
				if (inv.getItem(i) != null && inv.getItem(i).getType().equals(Material.DOUBLE_PLANT))
					count += inv.getItem(i).getAmount();
			}
			if (count >= game.getItems(ChatColor.GOLD + "Bond").getPrice())
			{
				count = game.getItems(ChatColor.GOLD + "Bond").getPrice();
				((PlayerAssault)game.getPlayerAPI(p.getUniqueId())).addItem(game.getItems(ChatColor.GOLD + "Bond"));
				for (int i = 0; i < inv.getSize(); i++) {
					if (inv.getItem(i) != null && inv.getItem(i).getType().equals(Material.DOUBLE_PLANT))
					{
						if (count - inv.getItem(i).getAmount() > 0)
						{
							count -= inv.getItem(i).getAmount();
							inv.clear(i);
						}	
						else	
						{
							ItemStack it = inv.getItem(i);
							it.setAmount(it.getAmount() - count);
							inv.setItem(i, it);
							count = 0;
						}
					}
					if (count <= 0)
						break;
				}
			}
			else
				p.sendMessage("Ta pas assez de tune batard casse toi !");
		}
	}
	
	public void start()
	{
		
	}
}
