package dev.panda.npc.commands.subcommands;

import dev.panda.npc.npc.NPC;
import dev.panda.npc.utilities.ChatUtil;
import dev.panda.npc.utilities.command.BaseCommand;
import dev.panda.npc.utilities.command.Command;
import dev.panda.npc.utilities.command.CommandArgs;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NPCTeleportHereCommand extends BaseCommand {

    @Command(name = "npc.tphere", permission = "pandanpc.npc.tphere", aliases = {"pandanpc.tphere", "pnpc.tphere"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();
        String label = command.getReplaceLabel("tphere");

        if (args.length < 1) {
            player.sendMessage(ChatUtil.translate("&cUsage: /" + label + " tphere <npc>"));
            return;
        }

        String npcName = args[0];
        NPC npc = NPC.getByName(npcName);

        if (npc == null) {
            player.sendMessage(ChatUtil.translate("&cNPC '&7" + npcName + "&c' not found."));
            return;
        }

        Location location = player.getLocation();
        npc.setLocation(location);
        npc.setYaw(location.getYaw());
        npc.setPitch(location.getPitch());
        npc.setHeadYaw(location.getYaw());
        npc.reset();

        player.sendMessage(ChatUtil.translate("&bNPC location has been set."));
    }
}
