package me.jezzadabomb.es2.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.client.sound.Sounds;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockInventoryScanner extends BlockES {

	public BlockInventoryScanner(int par1, Material par2Material, String name) {
		super(par1, par2Material, name);
		setHardness(2.5F);
		setBlockBounds(0F, 0F, 0F, 1F, 0.1F, 1F);
	}

	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		if (world.blockHasTileEntity(x, y, z) && world.getBlockTileEntity(x, y, z) instanceof TileInventoryScanner && world.isRemote && !UtilMethods.isWearingItem(ModItems.glasses)) {
			ClientProxy.hudRenderer.addToRemoveList(x, y - 1, z);
		}
		return super.removeBlockByPlayer(world, player, x, y, z);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase livingBase, ItemStack itemStack) {
		Sounds.SCANNING_WAVE.playAtPlayer(livingBase);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		y -= 1;
		return world.blockHasTileEntity(x, y, z) ? (world.getBlockTileEntity(x, y, z) instanceof IInventory) : false;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return null;
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileInventoryScanner();
	}

	@Override
	public boolean renderWithModel(){
		return true;
	}
}