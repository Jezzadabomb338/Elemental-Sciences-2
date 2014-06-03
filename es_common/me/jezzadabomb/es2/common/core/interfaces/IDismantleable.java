package me.jezzadabomb.es2.common.core.interfaces;

import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IDismantleable {

    /**
     * Dismantles the block. If returnBlock is true, the drop(s) should be placed into the player's inventory.
     */
    public ItemStack dismantleBlock(EntityPlayer player, World world, CoordSet coordSet, boolean returnBlock);

    /**
     * Return true if the block can be dismantled. The criteria for this is entirely up to the block.
     */
    public boolean canDismantle(EntityPlayer player, World world, CoordSet coordSet);

}
