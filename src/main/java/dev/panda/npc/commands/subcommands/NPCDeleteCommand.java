package dev.panda.npc.commands.subcommands;

import dev.panda.npc.PandaNPC;
import dev.panda.npc.npc.NPC;
import dev.panda.npc.npc.NPCManager;
import dev.panda.npc.utilities.ChatUtil;
import dev.panda.npc.utilities.command.BaseCommand;
import dev.panda.npc.utilities.command.Command;
import dev.panda.npc.utilities.command.CommandArgs;
import org.bukkit.command.CommandSender;

public class NPCDeleteCommand extends BaseCommand {

    private final NPCManager npcManager;

    public NPCDeleteCommand(PandaNPC plugin) {
        this.npcManager = plugin.getNpcManager();
    }

    @Command(name = "npc.delete", permission = "pandanpc.npc.delete", aliases = {"pandanpc.delete", "pnpc.delete"})
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        String label = command.getReplaceLabel("delete");

        if (args.length < 1) {
            sender.sendMessage(ChatUtil.translate("&cUsage: /" + label + " delete <npc>"));
            return;
        }

        String npcName = args[0];
        NPC npc = NPC.getByName(npcName);

        if (npc == null) {
            sender.sendMessage(ChatUtil.translate("&cNPC '&7" + npcName + "&c' already deleted."));
            return;
        }

        npcManager.delete(npc);
        sender.sendMessage(ChatUtil.translate("&bNPC '&7" + npcName + "&b' has been delete."));
    }
}
