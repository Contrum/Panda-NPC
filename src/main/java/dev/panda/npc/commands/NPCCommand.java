package dev.panda.npc.commands;

import dev.panda.npc.utilities.ChatUtil;
import dev.panda.npc.utilities.command.BaseCommand;
import dev.panda.npc.utilities.command.Command;
import dev.panda.npc.utilities.command.CommandArgs;
import org.bukkit.command.CommandSender;

public class NPCCommand extends BaseCommand {

    @Command(name = "npc", permission = "pandanpc.npc", aliases = {"pandanpc", "pnpc"})
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String label = command.getLabel();

        sender.sendMessage(ChatUtil.MENU_BAR);
        sender.sendMessage(ChatUtil.translate("&3&lPandaNPC Command"));
        sender.sendMessage(ChatUtil.translate(""));
        sender.sendMessage(ChatUtil.translate(" &7\u25B6 &b/" + label + " create <npc>"));
        sender.sendMessage(ChatUtil.translate(" &7\u25B6 &b/" + label + " delete <npc>"));
        sender.sendMessage(ChatUtil.translate(" &7\u25B6 &b/" + label + " list"));
        sender.sendMessage(ChatUtil.translate(" &7\u25B6 &b/" + label + " setdisplayname <npc> (displayname)"));
        sender.sendMessage(ChatUtil.translate(" &7\u25B6 &b/" + label + " setskin <npc> (skin|player)"));
        sender.sendMessage(ChatUtil.translate(" &7\u25B6 &b/" + label + " setarmor <npc>"));
        sender.sendMessage(ChatUtil.translate(" &7\u25B6 &b/" + label + " teleport <npc>"));
        sender.sendMessage(ChatUtil.translate(" &7\u25B6 &b/" + label + " tphere <npc>"));
        sender.sendMessage(ChatUtil.translate(" &7\u25B6 &b/" + label + " execute <npc> (add|remove|list)"));
        sender.sendMessage(ChatUtil.MENU_BAR);
    }
}
