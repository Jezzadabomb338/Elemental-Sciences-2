package me.jezzadabomb.es2.common.items;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemArmourES extends ItemArmor {

    protected ArrayList<String> infoList = new ArrayList<String>();
    protected ArrayList<String> shiftList = new ArrayList<String>();
    
	int slot;
	String textureLocation;

	public ItemArmourES(int id, EnumArmorMaterial armorMaterial, int renderIndex, ArmourSlotIndex armourIndex, String name, String textureLocation) {
		super(id, armorMaterial, renderIndex, armourIndex.slot);
		slot = armourIndex.slot;
		this.textureLocation = textureLocation;
		setMaxDamage(0);
		setUnlocalizedName(name);
		setCreativeTab(ElementalSciences2.creativeTab);
		register(name);
	}

	public void register(String name) {
		GameRegistry.registerItem(this, name);
	}

	@Override
	public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
		return armorType == slot;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return false;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer) {
		return textureLocation;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		itemIcon = register.registerIcon(Reference.MOD_ID + ":" + this.getUnlocalizedName().replace("item.", ""));
	}
	
	@Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        shiftList.clear();
        infoList.clear();
        addInformation();
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            list.addAll(shiftList);
        } else {
            list.addAll(infoList);
        }
    }

    protected void defaultInfoList(){
        infoList.add("Press shift for more info.");
    }
    
    protected void addInformation() {}
}