package dev.panda.npc.commands.subcommands;

import dev.panda.npc.PandaNPC;
import dev.panda.npc.npc.NPC;
import dev.panda.npc.npc.NPCManager;
import dev.panda.npc.utilities.ChatUtil;
import dev.panda.npc.utilities.command.BaseCommand;
import dev.panda.npc.utilities.command.Command;
import dev.panda.npc.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class NPCCreateCommand extends BaseCommand {

    private final NPCManager npcManager;

    public NPCCreateCommand(PandaNPC plugin) {
        this.npcManager = plugin.getNpcManager();
    }

    @Command(name = "npc.create", permission = "pandanpc.npc.create", aliases = {"pandanpc.create", "pnpc.create"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();
        String label = command.getReplaceLabel("create");

        if (args.length < 1) {
            player.sendMessage(ChatUtil.translate("&cUsage: /" + label + " create <npc>"));
            return;
        }

        String npcName = args[0];
        NPC npc = NPC.getByName(npcName);

        if (npc != null) {
            player.sendMessage(ChatUtil.translate("&cNPC '&7" + npcName + "&c' already created."));
            return;
        }

        npcManager.create(npcName, player.getLocation());
        player.sendMessage(ChatUtil.translate("&bNPC '&7" + npcName + "&b' has been create."));
    }
}
