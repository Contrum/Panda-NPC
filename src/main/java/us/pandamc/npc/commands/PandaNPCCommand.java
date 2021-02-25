package us.pandamc.npc.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.pandamc.npc.npc.NPC;
import us.pandamc.npc.npc.select.NPCSelect;
import us.pandamc.npc.utils.CC;
import us.pandamc.npc.utils.Utils;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class PandaNPCCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate("&4No Console."));
            return true;
        }


        Player player = (Player) sender;

        if(!player.hasPermission("pandanpc.use")){
            player.sendMessage(CC.translate("&cYou don't have permission to do that."));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(CC.translate("&7&m--------------------------"));
            sender.sendMessage(CC.translate("&3&LPandaNPC Command"));
            sender.sendMessage(CC.translate(""));
            sender.sendMessage(CC.translate("&7- &b/" + label + "&7 create <name>"));
            sender.sendMessage(CC.translate("&7- &b/" + label + "&7 setdisplayname <name> (displayname)"));
            sender.sendMessage(CC.translate("&7- &b/" + label + "&7 tphere <name>"));
            sender.sendMessage(CC.translate("&7- &b/" + label + "&7 teleport <name>"));
            sender.sendMessage(CC.translate("&7- &b/" + label + "&7 setarmor <name>"));
            sender.sendMessage(CC.translate("&7- &b/" + label + "&7 addcommand <name> (command)"));
            sender.sendMessage(CC.translate("&7- &b/" + label + "&7 removecommand <name> (number)"));
            sender.sendMessage(CC.translate("&7- &b/" + label + "&7 listcommands <name>"));
            sender.sendMessage(CC.translate("&7- &b/" + label + "&7 setskin <name> (skiname)"));
            sender.sendMessage(CC.translate("&7- &b/" + label + "&7 itemselector"));
            sender.sendMessage(CC.translate("&7- &b/" + label + "&7 delete <name>"));
            sender.sendMessage(CC.translate("&7&m--------------------------"));
        }

        if (!(args.length == 0)) {
            if (args[0].equalsIgnoreCase("create")) {
                if (!(args.length == 2)) {
                    player.sendMessage(CC.translate("&cPlease usage: /" + label + " create {name}"));
                    return true;
                }
                String npcName = args[1];
                NPC npc = new NPC(npcName);
                npc.setDisplayName(npcName);
                Location location = player.getLocation();
                npc.setLocation(location);
                npc.setYaw(location.getYaw());
                npc.setPitch(location.getPitch());
                npc.setHeadYaw(location.getYaw());
                npc.save();
                npc.spawnAll();
                NPC.getNpcs().put(npcName, npc);
                player.sendMessage(CC.translate("&aNPC Created correctly"));
            }else if (args[0].equalsIgnoreCase("setdisplayname")) {

                if(NPCSelect.getSelectedNPC(player) != null){
                    if (!(args.length <= 2)) {
                        player.sendMessage(CC.translate("&cPlease usage: /" + label + " setdisplayname (displayname)"));
                        return true;
                    }
                    String displayName = args[1];
                    NPC npc = NPCSelect.getSelectedNPC(player);
                    if(npc == null){
                        player.sendMessage(CC.translate("&cNpc not found."));
                        return false;
                    }
                    npc.setDisplayName(CC.translate(displayName));
                    npc.destroyAll();
                    npc.spawnAll();
                    npc.save();
                    player.sendMessage(CC.translate("&aNPC set display name correctly"));
                    return true;
                }

                if (!(args.length <= 3)) {
                    player.sendMessage(CC.translate("&cPlease usage: /" + label + " setdisplayname (name) (displayname)"));
                    return true;
                }
                String npcName = args[1];
                String displayName = args[2];
                NPC npc = NPC.getByName(npcName);
                if(npc == null){
                    player.sendMessage(CC.translate("&cNpc not found."));
                    return false;
                }
                npc.setDisplayName(CC.translate(displayName));
                npc.destroyAll();
                npc.spawnAll();
                npc.save();
                player.sendMessage(CC.translate("&aNPC set display name correctly"));
            }else if (args[0].equalsIgnoreCase("setlocation")
                || args[0].equalsIgnoreCase("tphere")
                || args[0].equalsIgnoreCase("teleporthere")
                || args[0].equalsIgnoreCase("move")
            ) {
                if(NPCSelect.getSelectedNPC(player) != null){
                    NPC npc = NPCSelect.getSelectedNPC(player);
                    if(npc == null){
                        player.sendMessage(CC.translate("&cNpc not found."));
                        return false;
                    }
                    Location location = player.getLocation();
                    npc.setLocation(location);
                    npc.setYaw(location.getYaw());
                    npc.setPitch(location.getPitch());
                    npc.setHeadYaw(location.getYaw());
                    npc.destroyAll();
                    npc.spawnAll();
                    npc.save();
                    player.sendMessage(CC.translate("&aNPC set location correctly"));
                    return true;
                }
                if (!(args.length <= 2)) {
                    player.sendMessage(CC.translate("&cPlease usage: /" + label + " setlocation (name)"));
                    return true;
                }
                String npcName = args[1];
                NPC npc = NPC.getByName(npcName);
                if(npc == null){
                    player.sendMessage(CC.translate("&cNpc not found."));
                    return false;
                }
                Location location = player.getLocation();
                npc.setLocation(location);
                npc.setYaw(location.getYaw());
                npc.setPitch(location.getPitch());
                npc.setHeadYaw(location.getYaw());
                npc.destroyAll();
                npc.spawnAll();
                npc.save();
                player.sendMessage(CC.translate("&aNPC set location correctly"));
            }else if (args[0].equalsIgnoreCase("setarmor")) {
                if(NPCSelect.getSelectedNPC(player) != null){
                    NPC npc = NPCSelect.getSelectedNPC(player);
                    if(npc == null){
                        player.sendMessage(CC.translate("&cNpc not found."));
                        return false;
                    }
                    npc.setHelmet(player.getInventory().getHelmet());
                    npc.setChest(player.getInventory().getChestplate());
                    npc.setLegs(player.getInventory().getLeggings());
                    npc.setBoots(player.getInventory().getBoots());
                    npc.setHand(player.getInventory().getItemInHand());
                    npc.destroyAll();
                    npc.spawnAll();
                    npc.save();
                    player.sendMessage(CC.translate("&aNPC set armor correctly"));
                    return true;
                }
                if (!(args.length <= 2)) {
                    player.sendMessage(CC.translate("&cPlease usage: /" + label + " setarmor (name)"));
                    return true;
                }
                String npcName = args[1];
                NPC npc = NPC.getByName(npcName);
                if(npc == null){
                    player.sendMessage(CC.translate("&cNpc not found."));
                    return false;
                }
                npc.setHelmet(player.getInventory().getHelmet());
                npc.setChest(player.getInventory().getChestplate());
                npc.setLegs(player.getInventory().getLeggings());
                npc.setBoots(player.getInventory().getBoots());
                npc.setHand(player.getInventory().getItemInHand());
                npc.destroyAll();
                npc.spawnAll();
                npc.save();
                player.sendMessage(CC.translate("&aNPC set armor correctly"));
            }else if (args[0].equalsIgnoreCase("addcommand")) {
                if(NPCSelect.getSelectedNPC(player) != null){
                    if (args.length < 2) {
                        player.sendMessage(CC.translate("&cPlease usage: /" + label + " addcommand (command)"));
                        return true;
                    }
                    String[] newArray = Arrays.copyOfRange(args, 1, args.length);
                    String commandNPC = String.join(" ", newArray);
                    NPC npc = NPCSelect.getSelectedNPC(player);
                    if(npc == null){
                        player.sendMessage(CC.translate("&cNpc not found."));
                        return false;
                    }
                    npc.getCommands().add(commandNPC);
                    npc.save();
                    player.sendMessage(CC.translate("&aNPC add command correctly"));
                    return true;
                }
                if (args.length < 4) {
                    player.sendMessage(CC.translate("&cPlease usage: /" + label + " addcommand (name) (command)"));
                    return true;
                }
                String npcName = args[1];
                String[] newArray = Arrays.copyOfRange(args, 2, args.length);
                String commandNPC = String.join(" ", newArray);
                NPC npc = NPC.getByName(npcName);
                if(npc == null){
                    player.sendMessage(CC.translate("&cNpc not found."));
                    return false;
                }
                npc.getCommands().add(commandNPC);
                npc.save();
                player.sendMessage(CC.translate("&aNPC add command correctly"));
            }else if (args[0].equalsIgnoreCase("removecommand")) {
                if(NPCSelect.getSelectedNPC(player) != null){
                    if (args.length < 2) {
                        player.sendMessage(CC.translate("&cPlease usage: /" + label + " addcommand (command)"));
                        return true;
                    }
                    int number = Integer.parseInt(args[1]);
                    number = number - 1;
                    NPC npc = NPCSelect.getSelectedNPC(player);
                    if(npc == null){
                        player.sendMessage(CC.translate("&cNpc not found."));
                        return false;
                    }
                    if(number > npc.getCommands().size()){
                        player.sendMessage(ChatColor.RED + "NPC Command not found");
                        return true;
                    }
                    npc.getCommands().remove(number);
                    npc.save();
                    player.sendMessage(CC.translate("&aNPC remove command correctly"));
                    return true;
                }
                if (args.length < 3) {
                    player.sendMessage(CC.translate("&cPlease usage: /" + label + " removecommand (name) (number)"));
                    return true;
                }
                String npcName = args[1];
                int number = Integer.parseInt(args[2]);
                number = number - 1;
                NPC npc = NPC.getByName(npcName);
                if(npc == null){
                    player.sendMessage(CC.translate("&cNpc not found."));
                    return false;
                }
                if(number > npc.getCommands().size()){
                    player.sendMessage(ChatColor.RED + "NPC Command not found");
                    return true;
                }
                npc.getCommands().remove(number);
                npc.save();
                player.sendMessage(CC.translate("&aNPC remove command correctly"));
            }else if (args[0].equalsIgnoreCase("listcommands")) {
                if(NPCSelect.getSelectedNPC(player) != null){
                    NPC npc = NPCSelect.getSelectedNPC(player);
                    if(npc == null){
                        player.sendMessage(CC.translate("&cNpc not found."));
                        return false;
                    }
                    sender.sendMessage(CC.translate("&3&lNPC &f&l" + npc.getName() + "&3&l Command List"));
                    player.sendMessage(CC.MENU_BAR);
                    if(npc.getCommands().isEmpty()){
                        player.sendMessage(CC.translate("&cNo commands added"));
                        player.sendMessage(CC.MENU_BAR);
                        return true;
                    }
                    npc.getCommands().forEach(commandNPC -> player.sendMessage(CC.translate("&7- &b" + commandNPC)));
                    player.sendMessage(CC.MENU_BAR);
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(CC.translate("&cPlease usage: /" + label + " listcommands (name)"));
                    return true;
                }
                String npcName = args[1];
                NPC npc = NPC.getByName(npcName);
                if(npc == null){
                    player.sendMessage(CC.translate("&cNpc not found."));
                    return false;
                }
                AtomicInteger i = new AtomicInteger(1);
                sender.sendMessage(CC.translate("&3&lNPC &f&l" + npcName + "&3&l Command List"));
                player.sendMessage(CC.MENU_BAR);
                if(npc.getCommands().isEmpty()){
                    player.sendMessage(CC.translate("&cNo commands added"));
                    player.sendMessage(CC.MENU_BAR);
                }
                npc.getCommands().forEach(commandNPC -> player.sendMessage(CC.translate(i.getAndIncrement() + " &7- &b" + commandNPC)));
                player.sendMessage(CC.MENU_BAR);
            }else if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage(CC.translate("&3&lPandaNPC List"));
                player.sendMessage(CC.MENU_BAR);
                NPC.getNpcs().values().forEach(npc -> player.sendMessage(CC.translate("&7- &b" + npc.getName())));
                player.sendMessage(CC.MENU_BAR);
            }else if (args[0].equalsIgnoreCase("delete")) {
                if(NPCSelect.getSelectedNPC(player) != null){
                    NPC npc = NPCSelect.getSelectedNPC(player);
                    if(npc == null){
                        player.sendMessage(CC.translate("&cNpc not found."));
                        return false;
                    }
                    npc.delete();
                    npc.destroyAll();
                    player.sendMessage(CC.translate("&aNPC deleted"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(CC.translate("&cPlease usage: /" + label + " delete (name)"));
                    return true;
                }
                String npcName = args[1];
                NPC npc = NPC.getByName(npcName);
                if(npc == null){
                    player.sendMessage(CC.translate("&cNpc not found."));
                    return false;
                }
                npc.delete();
                npc.destroyAll();
                player.sendMessage(CC.translate("&aNPC deleted"));
            }else if (args[0].equalsIgnoreCase("setskin")) {
                if(NPCSelect.getSelectedNPC(player) != null){
                    if (args.length < 2) {
                        player.sendMessage(CC.translate("&cPlease usage: /" + label + " setskin (skinname)"));
                        return true;
                    }
                    NPC npc = NPCSelect.getSelectedNPC(player);
                    String nameSkin = args[1];
                    if(npc == null){
                        player.sendMessage(CC.translate("&cNpc not found."));
                        return false;
                    }
                    String[] skin = Utils.getFromName(nameSkin);
                    if(skin == null){
                        player.sendMessage(ChatColor.RED + "Skin can only be configured with premium players.");
                        return false;
                    }
                    npc.setTexture(skin[0]);
                    npc.setSignature(skin[1]);
                    npc.destroyAll();
                    npc.spawnAll();
                    npc.save();
                    player.sendMessage(CC.translate("&aNPC set skin correctly"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(CC.translate("&cPlease usage: /" + label + " setskin (name) (skinname)"));
                    return true;
                }
                String npcName = args[1];
                String nameSkin = args[2];
                NPC npc = NPC.getByName(npcName);
                if(npc == null){
                    player.sendMessage(CC.translate("&cNpc not found."));
                    return false;
                }
                String[] skin = Utils.getFromName(nameSkin);
                if(skin == null){
                    player.sendMessage(ChatColor.RED + "Skin can only be configured with premium players.");
                    return false;
                }
                npc.setTexture(skin[0]);
                npc.setSignature(skin[1]);
                npc.destroyAll();
                npc.spawnAll();
                npc.save();
                player.sendMessage(CC.translate("&aNPC set skin correctly"));
            }else if (args[0].equalsIgnoreCase("itemselector")) {
                player.getInventory().addItem(NPCSelect.selectItem());
                player.sendMessage(CC.translate("&aYou've received npc selected item."));
            }else if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {
                if(NPCSelect.getSelectedNPC(player) != null){
                    NPC npc = NPCSelect.getSelectedNPC(player);
                    player.teleport(npc.getLocation());
                    player.sendMessage(CC.translate("&aYou've teleported to &f" + npc.getName() + "&a."));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(CC.translate("&cPlease usage: /" + label + " teleport (name)"));
                    return true;
                }
                String npcName = args[1];
                NPC npc = NPC.getByName(npcName);
                if(npc == null){
                    player.sendMessage(CC.translate("&cNpc not found."));
                    return false;
                }
                player.teleport(npc.getLocation());
                player.sendMessage(CC.translate("&aYou've teleported to &f" + npc.getName() + "&a."));
            }else{
                sender.sendMessage(CC.translate("&cArgument not found. You can use: "));
                sender.sendMessage(CC.translate(""));
                sender.sendMessage(CC.translate("&7&m--------------------------"));
                sender.sendMessage(CC.translate("&3&LPandaNPC Command"));
                sender.sendMessage(CC.translate(""));
                sender.sendMessage(CC.translate("&7- &b/" + label + " create (name)"));
                sender.sendMessage(CC.translate("&7- &b/" + label + " setdisplayname (name) (displayname)"));
                sender.sendMessage(CC.translate("&7- &b/" + label + " setlocation (name)"));
                sender.sendMessage(CC.translate("&7- &b/" + label + " teleport (name)"));
                sender.sendMessage(CC.translate("&7- &b/" + label + " setarmor (name)"));
                sender.sendMessage(CC.translate("&7- &b/" + label + " addcommand (name) (command)"));
                sender.sendMessage(CC.translate("&7- &b/" + label + " removecommand (name) (number)"));
                sender.sendMessage(CC.translate("&7- &b/" + label + " listcommands (name)"));
                sender.sendMessage(CC.translate("&7- &b/" + label + " setskin (name) (skiname)"));
                sender.sendMessage(CC.translate("&7- &b/" + label + " itemselector"));
                sender.sendMessage(CC.translate("&7- &b/" + label + " delete (name)"));
                sender.sendMessage(CC.translate("&7&m--------------------------"));
            }
        }
        return true;
    }
}
