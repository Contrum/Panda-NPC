package dev.panda.npc.commands.subcommands;

import dev.panda.npc.npc.NPC;
import dev.panda.npc.utilities.ChatUtil;
import dev.panda.npc.utilities.command.BaseCommand;
import dev.panda.npc.utilities.command.Command;
import dev.panda.npc.utilities.command.CommandArgs;
import org.bukkit.command.CommandSender;

public class NPCListCommand extends BaseCommand {

    @Command(name = "npc.list", permission = "pandanpc.npc.list", aliases = {"pandanpc.list", "pnpc.list"})
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage(ChatUtil.MENU_BAR);
        sender.sendMessage(ChatUtil.translate("&3&lNPC List"));

        NPC.getNpcs().values()
                .forEach(npc -> sender.sendMessage(ChatUtil.translate(" &7\u25B6 &b" + npc.getName() + " NPC")));

        sender.sendMessage(ChatUtil.MENU_BAR);
    }
}
