package dev.panda.npc.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ChatUtil {

    public String MENU_BAR = ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH.toString() + "------------------------";

    public String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public List<String> translate(List<String> list){
        return list.stream().map(ChatUtil::translate).collect(Collectors.toList());
    }
}