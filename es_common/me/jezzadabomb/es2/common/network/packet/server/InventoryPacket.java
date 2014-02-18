package me.jezzadabomb.es2.common.network.packet.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.ArrayList;

import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.PacketUtils;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.network.packet.IPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.relauncher.Side;

public class InventoryPacket implements IPacket {

    public ArrayList<ItemStack> itemStacks;
    public String loc = null;
    public int tickTiming;
    public CoordSet coordSet;

    public InventoryPacket(TileEntity tileEntity, String loc) {
        if (tileEntity == null || !(tileEntity instanceof IInventory || tileEntity instanceof ISidedInventory))
            return;

        itemStacks = new ArrayList<ItemStack>();
        this.loc = loc;
        IInventory inventory = ((IInventory) tileEntity);
        for (int i = 0; i < inventory.getSizeInventory(); i++)
            if (inventory.getStackInSlot(i) != null)
                itemStacks.add(inventory.getStackInSlot(i));
    }

    public InventoryPacket() {
    }

    @Override
    public void writeBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        PacketUtils.writeStringByteBuffer(buffer, loc);
        buffer.writeShort(itemStacks != null ? (itemStacks.isEmpty() ? (short) 0 : (short) itemStacks.size()) : (short) 0);
        if (itemStacks != null)
            for (ItemStack i : itemStacks)
                PacketUtils.writeItemStack(buffer, i);
    }

    @Override
    public void readBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        loc = PacketUtils.readStringByteBuffer(buffer);
        short length = buffer.readShort();
        itemStacks = new ArrayList<ItemStack>(length);
        for (int i = 0; i < length; i++)
            itemStacks.add(PacketUtils.readItemStack(buffer));
    }

    @Override
    public void executeClientSide(EntityPlayer player) {
        World world = player.worldObj;
        int[] coord = UtilMethods.getArrayFromString(loc);
        if (coord == null)
            return;
        coordSet = new CoordSet(coord[0], coord[1], coord[2]);
        ClientProxy.getHUDRenderer().addPacketToList(this);
        tickTiming = 0;
    }

    @Override
    public void executeServerSide(EntityPlayer player) {
        ESLogger.severe("Tried to send a packet to the wrong side!");
    }
    
    public String getItemStacksInfo() {
        StringBuilder temp = new StringBuilder();
        for (ItemStack tempStack : getItemStacks()) {
            temp.append(tempStack.getUnlocalizedName() + ":" + tempStack.stackSize + ",");
        }
        return temp.toString();
    }

    public ArrayList<ItemStack> getItemStacks() {
        ArrayList<ItemStack> tempStacks = new ArrayList<ItemStack>();
        boolean added = false;
        for (ItemStack itemStack : itemStacks) {
            for (ItemStack tempStack : tempStacks) {
                if (UtilMethods.areItemStacksEqual(itemStack, tempStack)) {
                    UtilMethods.mergeItemStacks(tempStacks.get(tempStacks.indexOf(tempStack)), itemStack, true);
                    added = true;
                }
            }
            if (!added) {
                tempStacks.add(itemStack);
            }
            added = false;
        }
        return tempStacks;
    }

    @Override
    public String toString() {
        return loc + " " + getItemStacksInfo();
    }

    @Override
    public boolean equals(Object other) {
        return equals(other, false);
    }

    public boolean equals(Object other, boolean includeItemStacks) {
        if (other == null || !(other instanceof InventoryPacket))
            return false;

        InventoryPacket tempPacket = (InventoryPacket) other;
        if (!includeItemStacks)
            return coordSet.equals(tempPacket.coordSet);
        return coordSet.equals(tempPacket.coordSet) && this.itemStacks.equals(tempPacket.itemStacks);
    }

    public boolean isCloserThan(InventoryPacket tempP, EntityPlayer player) {
        CoordSet externalSet = tempP.coordSet;
        return player.getDistance(coordSet.getX(), coordSet.getY(), coordSet.getZ()) < player.getDistance(externalSet.getX(), externalSet.getY(), externalSet.getZ());
    }
    
}
