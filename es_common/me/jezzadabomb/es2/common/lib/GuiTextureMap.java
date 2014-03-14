package me.jezzadabomb.es2.common.lib;

import net.minecraft.util.ResourceLocation;

public class GuiTextureMap {
    
    public static final String GUI_SHEET_LOCATION = "textures/gui/";

    public static final ResourceLocation GUI_CONSOLE_TEXTURE = getResource("console");
    public static final ResourceLocation GUI_PLAYER_INVENTORY = getResource("playerInventory");
    
    
    private static ResourceLocation getResource(String loc) {
        return new ResourceLocation(Reference.MOD_ID.toLowerCase(), GUI_SHEET_LOCATION + loc + ".png");
    }
}
