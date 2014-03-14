package me.jezzadabomb.es2.common.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.tickers.QuantumBombTicker;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;

public class TileQuantumStateDisruptor extends TileES {

    private boolean registered, placed;

    public TileQuantumStateDisruptor() {
        registered = placed = false;
    }

    @Override
    public void updateEntity() {
        invalidate();
        if (worldObj != null && !worldObj.isRemote && !registered) {
            registered = true;
            QuantumBombTicker.addToWatchList(this);
        }

        boolean hasAirBelow = worldObj.isAirBlock(xCoord, yCoord - 1, zCoord);

        if (hasAirBelow) {
            placed = true;
            worldObj.setBlock(xCoord, yCoord - 1, zCoord, Blocks.bedrock);
        }
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getAABBPool().getAABB(xCoord - 1, yCoord, zCoord - 1, xCoord + 2, yCoord, zCoord + 2);
    }

    public void removeSelf() {
        // Yoda conditions! No null check for me. :P
        if (ModBlocks.quantumStateDisrupter.equals(worldObj.getBlock(xCoord, yCoord, zCoord))) {
            worldObj.removeTileEntity(xCoord, yCoord, zCoord);
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        }
        if (placed && Blocks.bedrock.equals(worldObj.getBlock(xCoord, yCoord - 1, zCoord)))
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        invalidate();
        worldObj.createExplosion(null, xCoord, yCoord, zCoord, 2.5F, true);
    }

    @Override
    public Object getGui(int id, Side side, EntityPlayer player) {
        return null;
    }
}
