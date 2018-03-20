package thomi100.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Joiner;

public class TimberMain extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		
		Bukkit.getPluginManager().registerEvents(this, this);
		
		System.out.println("[Timber] The plugin (v" + getDescription().getVersion() + ") was enabled!");
		loadMessages();
		
	}
	
	@Override
	public void onDisable() {		
		System.out.println("[Timber] The plugin was disabled!");
	}
	
	private HashMap<String, String> messages = new HashMap<String, String>();
	public ArrayList<String> enabledWorlds = new ArrayList<String>();
	
	public boolean spread = false;
	public boolean allowCreative = false;
	public boolean justWithAxe = true;
	public boolean destroyLeaves = true;
	
	public int overload = 25;
	
	public void loadMessages() {
		HashMap<String, Object> setNew = new HashMap<String, Object>();
		messages.clear();
		enabledWorlds.clear();
		
		this.reloadConfig();
		if(getConfig().getString("prefix") != null) {
			messages.put("prefix", getConfig().getString("prefix").replace("&", "§"));
		} else {
			setNew.put("prefix", "&7[&eTimber&7] ");
			messages.put("prefix", "§7[§eTimber§7] ");
		}
		
		if(getConfig().getString("reloadDone") != null) {
			messages.put("reloadDone", getConfig().getString("reloadDone").replace("&", "§"));
		} else {
			setNew.put("reloadDone", "&aYou've reloaded the plugin!");
			messages.put("reloadDone", "§aYou've reloaded the plugin!");
		}
		
		if(getConfig().getString("noPermissions") != null) {
			messages.put("noPermissions", getConfig().getString("noPermissions").replace("&", "§"));
		} else {
			setNew.put("noPermissions", "&cYou don't have eneught permissions for that.");
			messages.put("noPermissions", "§cYou don't have eneught permissions for that.");
		}
		
		if(getConfig().getString("command") != null) {
			messages.put("command", getConfig().getString("command").replace("&", "§"));
		} else {
			setNew.put("command", "&cError: Try using &e/timber <reload/settings>");
			messages.put("command", "§cError: Try using §e/timber <reload/settings>");
		}
		
		if(getConfig().getString("spread") != null) {
			spread = getConfig().getString("spread").equalsIgnoreCase("true");
		} else {
			setNew.put("spread", "false");
			spread = false;
		}
		
		if(getConfig().getString("allowCreative") != null) {
			allowCreative = getConfig().getString("allowCreative").equalsIgnoreCase("true");
		} else {
			setNew.put("allowCreative", "false");
			allowCreative = false;
		}
		
		if(getConfig().getString("justWithAxe") != null) {
			justWithAxe = getConfig().getString("justWithAxe").equalsIgnoreCase("true");
		} else {
			setNew.put("justWithAxe", "true");
			justWithAxe = true;
		}
		
		if(getConfig().getString("overload") != null) {
			overload = getConfig().getInt("overload");
		} else {
			setNew.put("overload", 50);
			overload = 50;
		}
		
		if(getConfig().getString("destroyLeaves") != null) {
			destroyLeaves = getConfig().getString("destroyLeaves").equalsIgnoreCase("true");
		} else {
			setNew.put("destroyLeaves", "true");
			destroyLeaves = true;
		}

		if(getConfig().getStringList("enabledWorlds") != null && !getConfig().getStringList("enabledWorlds").isEmpty()) {
			enabledWorlds.addAll(getConfig().getStringList("enabledWorlds"));
		} else {
			for(World wld : Bukkit.getWorlds()) {
				enabledWorlds.add(wld.getName());
			}
			setNew.put("enabledWorlds", enabledWorlds);
		}
		if(getConfig().getString("apple-chance") == null) {
			setNew.put("apple-chance", 200);
		}
		
		
		if(!setNew.isEmpty()) {
			for(String s : setNew.keySet()) {
				getConfig().set(s, setNew.get(s));
			}
			System.out.println("[Timber] Added " + setNew.size() + " new messages or settings to the config. Maybe you want to change them.");
			saveConfig();
		} else {
			System.out.println("[Timber] Your messages and settings in the configuration were up to date.");
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("timber")) {
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("reload")) {
					if(sender.hasPermission("timber.reload") || sender.hasPermission("Timber.*") || !(sender instanceof Player)) {
						loadMessages();
						sender.sendMessage(messages.get("prefix") + messages.get("reloadDone"));
						return true;
					}
					sender.sendMessage(messages.get("prefix") + messages.get("noPermissions"));
					return true;
				} else if(args[0].equalsIgnoreCase("settings")) {
					if(sender.hasPermission("timber.settings") || sender.hasPermission("Timber.*") || !(sender instanceof Player)) {
						sender.sendMessage(messages.get("prefix") + "§aEnabled worlds§7: §e" + Joiner.on("§7, §e").join(enabledWorlds));
						sender.sendMessage(messages.get("prefix") + "§aEnabled sperad§7: §e" + spread);
						sender.sendMessage(messages.get("prefix") + "§aAllow creative§7: §e" + allowCreative);
						sender.sendMessage(messages.get("prefix") + "§aJust with axe§7: §e" + justWithAxe);
						sender.sendMessage(messages.get("prefix") + "§aDestroy leaves§7: §e" + destroyLeaves);
						sender.sendMessage(messages.get("prefix") + "§aOverload§7: §e" + overload);
						sender.sendMessage(messages.get("prefix") + "§aApple chance§7: §e1 of " + getConfig().getString("apple-chance"));
						return true;
					}
					sender.sendMessage(messages.get("prefix") + messages.get("noPermissions"));
					return true;
				}
				sender.sendMessage(messages.get("prefix") + messages.get("command"));
				return true;
			}
			sender.sendMessage(messages.get("prefix") + messages.get("command"));
			return true;
		}
		return false;
	}
		
	public HashMap<Integer, Integer> load = new HashMap<Integer, Integer>();
	
	public void addNr(int task) {
		int amount = load.get(task);
		load.remove(task);
		load.put(task, amount + 1);
	}

	public int getRandom(int lower, int upper) {
		Random random = new Random();
		return random.nextInt((upper - lower) + 1) + lower;
	}
	
	public void breakNext(Block b, int task) {
		if(b.getType().equals(Material.LOG) || b.getType().equals(Material.LOG_2) || (b.getType().toString().contains("LEAVES") && destroyLeaves)) {
			if(load.get(task) < overload) {
				addNr(task);
				if(b.getType().equals(Material.LOG) || b.getType().equals(Material.LOG_2)) {
					for(ItemStack it : b.getDrops()) {
						b.getLocation().getWorld().dropItem(b.getLocation(), it);
					}
				    b.getLocation().getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.LOG);
				} else {
				    b.getLocation().getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.LEAVES);
				    int chance = 200;
		    		if(getConfig().getString("apple-chance") != null) chance = getConfig().getInt("apple-chance");
				    if(getRandom(1, chance) == 1) {
				    	b.getLocation().getWorld().dropItem(b.getLocation(), new ItemStack(Material.APPLE, 1));
				    }
				}
				b.setType(Material.AIR);
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run() {
						breakNext(b.getRelative(BlockFace.UP), task);
						breakNext(b.getRelative(BlockFace.DOWN), task);
						if(spread || (destroyLeaves && b.getRelative(BlockFace.NORTH).getType().toString().contains("LEAVES"))) breakNext(b.getRelative(BlockFace.NORTH), task);
						if(spread || (destroyLeaves && b.getRelative(BlockFace.WEST).getType().toString().contains("LEAVES"))) breakNext(b.getRelative(BlockFace.WEST), task);
						if(spread || (destroyLeaves && b.getRelative(BlockFace.SOUTH).getType().toString().contains("LEAVES"))) breakNext(b.getRelative(BlockFace.SOUTH), task);
						if(spread || (destroyLeaves && b.getRelative(BlockFace.EAST).getType().toString().contains("LEAVES"))) breakNext(b.getRelative(BlockFace.EAST), task);
					}
				}, 2);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(e.getBlock().getType().equals(Material.LOG) || e.getBlock().getType().equals(Material.LOG_2)) {
			Player p = ((Player) e.getPlayer());
			boolean isWorld = enabledWorlds.contains(p.getLocation().getWorld().getName());
			boolean hasPermission = p.hasPermission("Timber.use") || p.hasPermission("Timber.*");
			boolean hasNearBlocksUp = (e.getBlock().getRelative(BlockFace.UP).getType().equals(Material.LOG) || e.getBlock().getRelative(BlockFace.UP).getType().equals(Material.LOG_2) || e.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.LOG) || e.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.LOG_2));
			boolean hasNearBlocksNW = (e.getBlock().getRelative(BlockFace.NORTH).getType().equals(Material.LOG) || e.getBlock().getRelative(BlockFace.NORTH).getType().equals(Material.LOG_2) || e.getBlock().getRelative(BlockFace.WEST).getType().equals(Material.LOG) || e.getBlock().getRelative(BlockFace.WEST).getType().equals(Material.LOG_2));
			boolean hasNearBlocksSE = (e.getBlock().getRelative(BlockFace.SOUTH).getType().equals(Material.LOG) || e.getBlock().getRelative(BlockFace.SOUTH).getType().equals(Material.LOG_2) || e.getBlock().getRelative(BlockFace.EAST).getType().equals(Material.LOG) || e.getBlock().getRelative(BlockFace.EAST).getType().equals(Material.LOG_2));
			boolean isAxe = false;
			isAxe = (p.getInventory().getItem(p.getInventory().getHeldItemSlot()) != null && p.getInventory().getItem(p.getInventory().getHeldItemSlot()).getType() != Material.AIR && p.getInventory().getItem(p.getInventory().getHeldItemSlot()).getAmount() > 0 && p.getItemInHand().getType().toString().contains("_AXE"));
			if(isWorld && hasPermission) {
				if(p.getGameMode().equals(GameMode.SURVIVAL) || (p.getGameMode().equals(GameMode.CREATIVE) && allowCreative)) {
					if(!justWithAxe || isAxe) {
						if(hasNearBlocksUp) {
							int task = getRandom(0, 999999999);
							while(load.containsKey(task)) task = getRandom(0, 999999999);
							load.put(task, 0);
							breakNext(e.getBlock(), task);
						} else {
							if((hasNearBlocksNW || hasNearBlocksSE) && spread) {
								int task = getRandom(0, 999999999);
								while(load.containsKey(task)) task = getRandom(0, 999999999);
								load.put(task, 0);
								breakNext(e.getBlock(), task);
							}
						}
					}
				}
			}
		}
	}

}
