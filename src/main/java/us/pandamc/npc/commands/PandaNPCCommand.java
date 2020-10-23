package us.pandamc.npc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.pandamc.npc.npc.NPC;
import us.pandamc.npc.utils.CC;

public class PandaNPCCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings[0].equalsIgnoreCase("create")) {
            if (strings.length == 2) {
                Player player = (Player) commandSender;
                String npcName = strings[1];
                NPC.getNpcs().put(npcName, new NPC(npcName, "&rTest", player.getLocation(), null, null, null, null, null, player.getLocation().getYaw(), player.getLocation().getYaw(), player.getLocation().getPitch(), null));
                player.sendMessage(CC.translate("&aNPC Create successfully."));
            }
            commandSender.sendMessage(CC.translate("&7&m--------------------------"));
            commandSender.sendMessage(CC.translate("&3&LPandaNPC Command"));
            commandSender.sendMessage(CC.translate(""));
            commandSender.sendMessage(CC.translate("&7- &b/" + s + " create {name}"));
            commandSender.sendMessage(CC.translate("&7&m--------------------------"));
        }
        return true;
    }
}
