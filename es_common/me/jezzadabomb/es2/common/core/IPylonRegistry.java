package me.jezzadabomb.es2.common.core;

import java.util.HashMap;
import java.util.HashSet;

import me.jezzadabomb.es2.common.core.interfaces.IPylon;
import me.jezzadabomb.es2.common.core.interfaces.IPylonReceiver;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import net.minecraft.world.World;

public class IPylonRegistry {

    private static HashMap<Integer, HashSet<CoordSet>> pylonMap = new HashMap<Integer, HashSet<CoordSet>>();
    private static HashMap<Integer, HashSet<CoordSet>> userMap = new HashMap<Integer, HashSet<CoordSet>>();

    public static void debug(int dim) {
        HashSet<CoordSet> pylonSet = pylonMap.get(Integer.valueOf(dim));
        // if (pylonSet != null)
        // pylonSet.clear();
        ESLogger.info(pylonSet);

        HashSet<CoordSet> userSet = userMap.get(Integer.valueOf(dim));
        // if (userSet != null)
        // userSet.clear();
        ESLogger.info(userSet);
    }

    public static boolean registerPylon(World world, CoordSet pylon) {
        if (world == null)
            return false;

        int dimID = world.provider.dimensionId;
        confirmPylon(dimID);

        pylonMap.get(Integer.valueOf(dimID)).add(pylon);
        broadcastPylonUpdate(world);
        return pylonMap.get(Integer.valueOf(dimID)).contains(pylon);
    }

    public static boolean removePylon(World world, CoordSet pylon) {
        if (world == null)
            return false;

        int dimID = world.provider.dimensionId;
        confirmPylon(dimID);

        pylonMap.get(Integer.valueOf(dimID)).remove(pylon);
        broadcastPylonUpdate(world);
        return !pylonMap.get(Integer.valueOf(dimID)).contains(pylon);
    }

    public static boolean registerUser(World world, CoordSet pylonReceiver) {
        if (world == null)
            return false;

        int dimID = world.provider.dimensionId;
        confirmUser(dimID);

        userMap.get(Integer.valueOf(dimID)).add(pylonReceiver);
        return userMap.get(Integer.valueOf(dimID)).contains(pylonReceiver);
    }

    public static boolean removeUser(World world, CoordSet pylonReceiver) {
        if (world == null)
            return false;

        int dimID = world.provider.dimensionId;
        confirmUser(dimID);

        userMap.get(Integer.valueOf(dimID)).remove(pylonReceiver);
        return !userMap.get(Integer.valueOf(dimID)).contains(pylonReceiver);
    }

    private static void broadcastPylonUpdate(World world) {
        if (world == null)
            return;

        int dimID = world.provider.dimensionId;
        confirmUser(dimID);
        for (CoordSet pylonReceiver : userMap.get(Integer.valueOf(dimID)))
            if (pylonReceiver.isPylonReciever(world))
                ((IPylonReceiver) pylonReceiver.getTileEntity(world)).notifyPylonUpdate();
    }

    /**
     * Note for self: Keep an eye on this.
     */
    public static IPylon isPowered(World world, CoordSet coordSet) {
        if (world == null || world.isRemote)
            return null;

        int dimID = world.provider.dimensionId;
        confirmPylon(dimID);

        if (pylonMap.get(Integer.valueOf(dimID)).isEmpty())
            return null;

        IPylon highest = null;

        for (CoordSet pylonSet : pylonMap.get(Integer.valueOf(dimID)))
            if (pylonSet.isPylon(world)) {
                IPylon pylon = (IPylon) pylonSet.getTileEntity(world);
                if (pylon.isPowering(coordSet))
                    if (highest == null || pylon.getPowerLevel() > highest.getPowerLevel())
                        highest = pylon;
            }

        return highest;
    }

    private static void confirmPylon(int dimID) {
        if (!pylonMap.containsKey(Integer.valueOf(dimID)))
            pylonMap.put(Integer.valueOf(dimID), new HashSet<CoordSet>());
    }

    private static void confirmUser(int dimID) {
        if (!userMap.containsKey(Integer.valueOf(dimID)))
            userMap.put(Integer.valueOf(dimID), new HashSet<CoordSet>());
    }
}
