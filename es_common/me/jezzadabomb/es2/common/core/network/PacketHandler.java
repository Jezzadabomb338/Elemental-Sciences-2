package me.jezzadabomb.es2.common.core.network;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.core.network.packet.IPacket;
import me.jezzadabomb.es2.common.core.network.packet.client.ConsoleInfoPacket;
import me.jezzadabomb.es2.common.core.network.packet.client.GuiConsolePacket;
import me.jezzadabomb.es2.common.core.network.packet.client.InventoryRequestPacket;
import me.jezzadabomb.es2.common.core.network.packet.client.SetBlockChunkPacket;
import me.jezzadabomb.es2.common.core.network.packet.server.DroneBayDoorPacket;
import me.jezzadabomb.es2.common.core.network.packet.server.HoverHandlerPacket;
import me.jezzadabomb.es2.common.core.network.packet.server.InventoryPacket;
import me.jezzadabomb.es2.common.core.network.packet.server.NeighbourChangedPacket;

public class PacketHandler {

    public static void init() {
        registerPacket(InventoryRequestPacket.class);
        registerPacket(SetBlockChunkPacket.class);
        registerPacket(HoverHandlerPacket.class);
        registerPacket(InventoryPacket.class);
        registerPacket(NeighbourChangedPacket.class);
        registerPacket(ConsoleInfoPacket.class);
        registerPacket(DroneBayDoorPacket.class);
        registerPacket(GuiConsolePacket.class);
    }

    private static void registerPacket(Class<? extends IPacket> clazz) {
        ElementalSciences2.packetPipeline.registerPacket(clazz);
    }

}
