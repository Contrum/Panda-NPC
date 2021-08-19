package dev.panda.npc.commands.subcommands;

import dev.panda.npc.npc.NPC;
import dev.panda.npc.utilities.ChatUtil;
import dev.panda.npc.utilities.command.BaseCommand;
import dev.panda.npc.utilities.command.Command;
import dev.panda.npc.utilities.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public class NPCSetDisplayNameCommand extends BaseCommand {

    @Command(name = "npc.setdisplayname", permission = "pandanpc.npc.setdisplayname", aliases = {"pandanpc.setdisplayname", "pnpc.setdisplayname"})
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        String label = command.getReplaceLabel("setdisplayname");

        if (args.length < 2) {
            sender.sendMessage(ChatUtil.translate("&cUsage: /" + label + " setdisplayname <npc> (displayname)"));
            return;
        }

        String npcName = args[0];
        NPC npc = NPC.getByName(npcName);

        if (npc == null) {
            sender.sendMessage(ChatUtil.translate("&cNPC '&7" + npcName + "&c' not found."));
            return;
        }

        String displayName = StringUtils.join(args, ' ', 1, args.length);

        npc.setDisplayName(ChatUtil.translate(displayName));
        npc.reset();

        sender.sendMessage(ChatUtil.translate("&bNPC displayname has been set &7(&r" + displayName + "&7)"));
    }
}
