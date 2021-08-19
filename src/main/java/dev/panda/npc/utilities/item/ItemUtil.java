package dev.panda.npc.utilities.item;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

@UtilityClass
public class ItemUtil {

    public String serialize(ItemStack itemStack){
        StringBuilder serialized = new StringBuilder();

        serialized.append("material:").append(itemStack.getType()).append(";")
            .append("durability:").append(itemStack.getDurability()).append(";")
            .append("amount:").append(itemStack.getAmount()).append(";");

        if (itemStack.hasItemMeta()) {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta.hasDisplayName()) {
                serialized.append("name:").append(meta.getDisplayName()).append(";");
            }
            if (meta.hasLore()){
                serialized.append("lore:").append(String.join(",", meta.getLore())).append(";");
            }
        }

        return serialized.toString();
    }

    public ItemStack deserialize(String string){
        ItemBuilder itemBuilder = null;

        String[] serializedSplit = string.split(";");

        for (String str : serializedSplit) {
            if (str.contains("material")) {
                String material = str.replace("material:", "");
                itemBuilder = new ItemBuilder(Material.valueOf(material));
            }
            else if (str.contains("durability")) {
                String durability = str.replace("durability:", "");
                itemBuilder.durability(Short.parseShort(durability));
            }
            else if (str.contains("name")) {
                String name = str.replace("name:", "");
                itemBuilder.name(name);
            }
            else if (str.contains("lore")) {
                String lore = str.replace("lore:", "");
                itemBuilder.lore(Arrays.asList(lore.split(",")));
            }
            else if (str.contains("amount")) {
                itemBuilder.amount(Integer.parseInt(str.replace("amount:", "")));
            }
        }
        return itemBuilder.build();
    }
}