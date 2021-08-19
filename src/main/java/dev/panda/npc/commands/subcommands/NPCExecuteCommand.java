package dev.panda.npc.commands.subcommands;

import dev.panda.npc.npc.NPC;
import dev.panda.npc.utilities.ChatUtil;
import dev.panda.npc.utilities.command.BaseCommand;
import dev.panda.npc.utilities.command.Command;
import dev.panda.npc.utilities.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public class NPCExecuteCommand extends BaseCommand {

    @Command(name = "npc.execute", permission = "pandanpc.npc.execute", aliases = {"pandanpc.execute", "pnpc.execute"})
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        String label = command.getReplaceLabel("execute");

        if (args.length < 2) {
            sender.sendMessage(ChatUtil.translate("&cUsage: /" + label + " execute <npc> (add|remove|list)"));
            return;
        }

        String npcName = args[0];
        NPC npc = NPC.getByName(npcName);

        if (npc == null) {
            sender.sendMessage(ChatUtil.translate("&cNPC '&7" + npcName + "&c' not found."));
            return;
        }

        switch (args[1].toLowerCase()) {
            case "add": {
                if (args.length < 4) {
                    sender.sendMessage(ChatUtil.translate("&cUsage: /" + label + " execute <npc> (add|remove|list) (console) (command)"));
                    return;
                }

                boolean console = Boolean.parseBoolean(args[2]);
                String commandNPC = StringUtils.join(args, ' ', 3, args.length);

                npc.getCommands().put(commandNPC, console);
                npc.save();

                sender.sendMessage(ChatUtil.translate("&bNPC command has been added &7(&r" + commandNPC + "&7)"));
                break;
            }
            case "remove": {
                if (args.length < 3) {
                    sender.sendMessage(ChatUtil.translate("&cUsage: /" + label + " execute <npc> (add|remove|list) (command|all)"));
                    return;
                }

                String commandNPC = StringUtils.join(args, ' ', 2, args.length);

                if (commandNPC.equalsIgnoreCase("all")) {
                    npc.getCommands().clear();
                    npc.save();
                    sender.sendMessage(ChatUtil.translate("&bAll NPC commands has been removed."));
                    return;
                }

                npc.getCommands().remove(commandNPC);
                npc.save();

                sender.sendMessage(ChatUtil.translate("&bNPC command has been removed &7(&f" + commandNPC + "&7)"));
                break;
            }
            case "list": {
                sender.sendMessage(ChatUtil.MENU_BAR);
                sender.sendMessage(ChatUtil.translate("&3&l" + npc.getName() + " NPC Command List"));

                if (npc.getCommands().isEmpty()) {
                    sender.sendMessage(ChatUtil.translate("&cNo commands found."));
                }
                else {
                    for (String key : npc.getCommands().keySet()) {
                        boolean console = npc.getCommands().get(key);

                        sender.sendMessage(ChatUtil.translate(" &7\u25B6 &b" + key + " &7| " + (console ? "&a" : "&c") + console));
                    }
                }

                sender.sendMessage(ChatUtil.MENU_BAR);
                break;
            }
            default:
                sender.sendMessage(ChatUtil.translate("&cArgument not found. (add|remove|list)"));
                break;
        }
    }
}
