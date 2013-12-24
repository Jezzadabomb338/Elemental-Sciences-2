package me.jezzadabomb.es2.client.renderers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

import me.jezzadabomb.es2.client.tickers.PlayerTicker;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilHelpers;
import me.jezzadabomb.es2.common.hud.StoredQueues;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.packets.InventoryPacket;
import me.jezzadabomb.es2.common.packets.InventoryTerminatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HUDRenderer {
	private ArrayList<InventoryPacket> packetList = new ArrayList<InventoryPacket>();
	private ArrayList<InventoryPacket> removeList = new ArrayList<InventoryPacket>();

	private final RenderItem customItemRenderer;

	private boolean underBlock = false;
	private int tickTiming = 0;

	public HUDRenderer() {
		customItemRenderer = new RenderItem() {
			@Override
			public boolean shouldBob() {
				return false;
			}

			@Override
			public boolean shouldSpreadItems() {
				return false;
			}
		};
		customItemRenderer.setRenderManager(RenderManager.instance);
	}

	public InventoryPacket getPacket(int x, int y, int z) {
		for (InventoryPacket packet : packetList) {
			if (packet.x == x && packet.y == y && packet.z == z) {
				return packet;
			}
		}
		return null;
	}

	public void printPacketList() {
		for (InventoryPacket packet : packetList) {
			System.out.println(packet);
		}
	}

	public void addPacketToList(InventoryPacket p) {
		if (doesPacketAlreadyExistAtXYZ(p)) {
			packetList.set(getPosInList(p), p);
		} else {
			packetList.add(p);
		}
	}

	private boolean doesPacketAlreadyExistAtXYZ(InventoryPacket p) {
		for (InventoryPacket packet : packetList) {
			if (p.equals(packet)) {
				return true;
			}
		}
		return false;
	}

	public int getPosInList(InventoryPacket p) {
		for (InventoryPacket packet : packetList) {
			if (p.equals(packet)) {
				return packetList.indexOf(packet);
			}
		}
		return -1;
	}

	public InventoryPacket getPacketAtXYZ(String loc) {
		int[] coord = UtilHelpers.getArrayFromString(loc);
		for (InventoryPacket p : packetList) {
			if (p.x == coord[0] && p.y == coord[1] && p.z == coord[2]) {
				return p;
			}
		}
		return null;
	}

	@ForgeSubscribe
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		if (packetList.isEmpty())
			return;
		tickTiming++;
		for (InventoryPacket packet : packetList) {
			if (UtilHelpers.isWearingItem(ModItems.glasses)) {
				if (!StoredQueues.instance().getStrXYZ(packet.inventoryTitle, packet.x, packet.y, packet.z)) {
					removeList.add(packet);
				}
			} else {
				if (packet.tickTiming > 120) {
					removeList.add(packet);
				}
			}
		}

		packetList.removeAll(removeList);
		removeList.clear();

		// ArrayList<InventoryPacket> disList = new ArrayList<InventoryPacket>();
		// ArrayList<InventoryPacket> addList = new ArrayList<InventoryPacket>();
		//
		// boolean added = false;
		// EntityPlayer player = null;
		// if ((Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer)) {
		// player = (EntityPlayer) Minecraft.getMinecraft().renderViewEntity;
		// }
		// if(player == null){
		// return;
		// }
		// for (InventoryPacket p : packetList) {
		// for(InventoryPacket tempP : disList){
		// if(p.isCloserThan(tempP, player)){
		// addList.add(disList.indexOf(tempP), p);
		// added = true;
		// break;
		// }
		// }
		// if(!added)
		// addList.add(p);
		// disList.clear();
		// disList.addAll(addList);
		// added = false;
		// }

		for (InventoryPacket p : packetList) {
			renderInfoScreen(p.x, p.y, p.z, event.partialTicks, p);
			if (UtilHelpers.canShowDebugHUD()) {
				RenderUtils.renderColouredBox(event, p, underBlock);
				if (!underBlock)
					RenderUtils.drawTextInAir(p.x, p.y + 0.63F, p.z, event.partialTicks, p.inventoryTitle);
				underBlock = false;
			}
			p.tickTiming++;
		}
	}

	private void renderInfoScreen(double x, double y, double z, double partialTicks, InventoryPacket p) {
		if ((Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer)) {
			EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().renderViewEntity;

			glPushMatrix();
			glDisable(GL_LIGHTING);
			glDisable(GL_CULL_FACE);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			RenderUtils.resetHUDColour();

			float[] num = RenderUtils.translateToWorldCoordsShifted(player, partialTicks, x, y, z);
			if (num == null) {
				return;
			}

			float xd = num[0];
			float zd = num[1];
			float yd = num[2];

			int xTextureOffset = 11;
			int yTextureOffset = 18;
			int xInventoryPos = -87;
			int yInventoryPos = -130;
			if (player.worldObj.blockExists(p.x, p.y + 1, p.z) && (player.worldObj.getBlockId(p.x, p.y + 1, p.z) != ModBlocks.inventoryScanner.blockID)) {
				yInventoryPos = 190;
				yd += 1.0F;
				// TODO Add support for blocks on top of inventory.
				glEnable(GL_CULL_FACE);
				glDisable(GL_BLEND);
				glPopMatrix();
				underBlock = true;
				return;
			}
//			if(){player.worldObj
//				
//			}

			float rotYaw = (float) (Math.atan2(xd, zd) * 180.0D / 3.141592653589793D);
			float rotPitch = (float) (Math.atan2(yd, Math.sqrt(xd * xd + zd * zd)) * 180.0D / 3.141592653589793D);

			glRotatef(rotYaw + 180.0F, 0.0F, 1.0F, 0.0F);

			if (Reference.HUD_VERTICAL_ROTATION) {
				glRotatef(rotPitch + 0.0F, 1.0F, 0.0F, 0.0F);
			}

			glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
			glScalef(0.01F, 0.006F, 0.01F);
			glScalef(0.6F, 0.6F, 0.6F);

			// Inventory background
			RenderUtils.bindTexture(TextureMaps.HUD_INVENTORY);
			RenderUtils.drawTexturedQuad(xInventoryPos, yInventoryPos, 0, 0, 174, 250, 0);
			glTranslated(xInventoryPos + xTextureOffset, yInventoryPos + yTextureOffset, 0.0D);
			int xOffset = 52;
			int yOffset = 74;

			int indexNum = -1;
			int rowNum = 0;
			int totalSlots = 0;
			ArrayList<ItemStack> sortedList = new ArrayList<ItemStack>();
			ArrayList<ItemStack> addList = new ArrayList<ItemStack>();

			boolean added = false;
			for (ItemStack itemStack : p.getItemStacks()) {
				for (ItemStack tempStack : sortedList) {
					if (itemStack.stackSize > tempStack.stackSize) {
						addList.add(sortedList.indexOf(tempStack), itemStack);
						added = true;
						break;
					}
				}
				if (!added)
					addList.add(itemStack);

				sortedList.clear();
				sortedList.addAll(addList);
				added = false;
			}

			for (ItemStack itemStack : sortedList) {
				if (totalSlots > 8) {
					break;
				}
				if (indexNum < 2) {
					indexNum++;
				} else {
					indexNum = 0;
					rowNum++;
				}
				RenderUtils.drawItemAndSlot(indexNum * xOffset, rowNum * yOffset, itemStack, customItemRenderer, -2, indexNum, rowNum);
				totalSlots++;
			}
			glEnable(GL_CULL_FACE);
			glDisable(GL_BLEND);
			glPopMatrix();
		}
	}

	public void addToRemoveList(InventoryTerminatePacket inventoryTerminatePacket) {
		packetList.clear();
		removeList.add(getPacketAtXYZ(inventoryTerminatePacket.loc));
	}
}
