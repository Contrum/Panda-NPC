package dev.panda.npc.commands.subcommands;

import dev.panda.npc.npc.NPC;
import dev.panda.npc.utilities.ChatUtil;
import dev.panda.npc.utilities.command.BaseCommand;
import dev.panda.npc.utilities.command.Command;
import dev.panda.npc.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class NPCTeleportCommand extends BaseCommand {

    @Command(name = "npc.teleport", permission = "pandanpc.npc.teleport", aliases = {"pandanpc.teleport", "pnpc.teleport"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();
        String label = command.getReplaceLabel("teleport");

        if (args.length < 1) {
            player.sendMessage(ChatUtil.translate("&cUsage: /" + label + " teleport <npc>"));
            return;
        }

        String npcName = args[0];
        NPC npc = NPC.getByName(npcName);

        if (npc == null) {
            player.sendMessage(ChatUtil.translate("&cNPC '&7" + npcName + "&c' not found."));
            return;
        }

        player.teleport(npc.getLocation());
        player.sendMessage(ChatUtil.translate("&bYou've teleported to &f" + npc.getName() + " NPC&b."));
    }
}
