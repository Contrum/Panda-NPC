package us.pandamc.npc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.pandamc.npc.PandaNPC;
import us.pandamc.npc.npc.NPC;
import us.pandamc.npc.utils.CC;

public class PandaNPCCommand extends Command {

    public PandaNPCCommand() {
        super("pandanpc");
        this.setPermission("panda.command.npc");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(CC.translate("&4No Console."));
            return true;
        }

        if (strings.length == 0) {
            commandSender.sendMessage(CC.translate("&7&m--------------------------"));
            commandSender.sendMessage(CC.translate("&3&LPandaNPC Command"));
            commandSender.sendMessage(CC.translate(""));
            commandSender.sendMessage(CC.translate("&7- &b/" + s + " create {name}"));
            commandSender.sendMessage(CC.translate("&7&m--------------------------"));
        }

        Player player = (Player) commandSender;
        if (!(strings.length == 0)) {
            if (strings[0].equalsIgnoreCase("create")) {
                if (!(strings.length == 2)) {
                    player.sendMessage(CC.translate("&cPlease usage: /" + s + " create {name}"));
                    return true;
                }
                String npcName = strings[1];
                NPC npc = new NPC(npcName, npcName, player.getLocation());
                npc.save();
                PandaNPC.get().getPackets().spawn(npc, player);
                player.sendMessage(CC.translate("&aNPC Created correctly"));
            }
        }
        return false;
    }
}
