package me.jezzadabomb.es2.client.drone;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

import cpw.mods.fml.common.network.PacketDispatcher;

import me.jezzadabomb.es2.client.models.ModelConstructorDrone;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.MathHelper;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.core.utils.Vector3F;
import me.jezzadabomb.es2.common.core.utils.Vector3I;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import net.minecraft.network.NetServerHandler;
import net.minecraft.world.World;

public class DroneState {
    Vector3F coordSet, targetSet, workingSet;
    TileAtomicConstructor tAC;
    float motionX, motionY, motionZ;
    Vector3I mainCoordSet, targetTAC;
    float xMinMargin = 0.12F;
    float xMaxMargin = 0.89F;
    float yMinMargin = 0.12F;
    float yMaxMargin = 0.87F;
    float zMinMargin = 0.10F;
    float zMaxMargin = 0.89F;

    // 1 - Idle
    // 2 - Working
    int mode = 1;
    int tickTiming = 0;

    public DroneState() {
        coordSet = new Vector3F(MathHelper.clipFloat((float) new Random().nextFloat(), xMinMargin, xMaxMargin), MathHelper.clipFloat((float) new Random().nextFloat(), yMinMargin, yMaxMargin), MathHelper.clipFloat((float) new Random().nextFloat(), zMinMargin, zMaxMargin));
        targetSet = new Vector3F(MathHelper.clipFloat((float) new Random().nextFloat(), xMinMargin, xMaxMargin), MathHelper.clipFloat((float) new Random().nextFloat(), yMinMargin, yMaxMargin), MathHelper.clipFloat((float) new Random().nextFloat(), zMinMargin, zMaxMargin));
        setMotion(0.01F);
        mode = 1;
    }

    public void setMainCoordSet(Vector3I mainCoordSet) {
        this.mainCoordSet = mainCoordSet;
    }

    public void setMotion(float motion) {
        motionX = motionY = motionZ = motion;
    }

    public void setIdle() {
        mode = 1;
    }

    public void setWorking() {
        mode = 2;
    }

    public void setMovingWork() {
        mode = 3;
    }

    public void setWorking(Vector3F workingSet) {
        this.workingSet = workingSet;
        mode = 2;
    }

    public int getMode() {
        return mode;
    }

    public void moveDrone() {
        if (MathHelper.withinRange(coordSet.getX(), targetSet.getX(), motionX) && MathHelper.withinRange(coordSet.getY(), targetSet.getY(), motionY) && MathHelper.withinRange(coordSet.getZ(), targetSet.getZ(), motionZ)) {
            if (isIdle()) {
                setNewTargetCoordsForIdle();
            } else if (isWorking()) {
                setNewTargetCoordsForWork();
            }
        }

//        targetSet.setX(MathHelper.clipFloat(targetSet.getX(), xMinMargin, xMaxMargin));
//        targetSet.setY(MathHelper.clipFloat(targetSet.getY(), yMinMargin, yMaxMargin));
//        targetSet.setZ(MathHelper.clipFloat(targetSet.getZ(), zMinMargin, zMaxMargin));

        if (!MathHelper.withinRange(coordSet.getX(), targetSet.getX(), motionX)) {
            if (coordSet.getX() < targetSet.getX()) {
                coordSet.addX(motionX);
            } else {
                coordSet.substractX(motionX);
            }
        }
        if (!MathHelper.withinRange(coordSet.getY(), targetSet.getY(), motionY)) {
            if (coordSet.getY() < targetSet.getY()) {
                coordSet.addY(motionY);
            } else {
                coordSet.substractY(motionY);
            }
        }
        if (!MathHelper.withinRange(coordSet.getZ(), targetSet.getZ(), motionZ)) {
            if (coordSet.getZ() < targetSet.getZ()) {
                coordSet.addZ(motionZ);
            } else {
                coordSet.substractZ(motionZ);
            }
        }
    }

    public boolean isIdle() {
        return mode == 1;
    }

    public boolean isWorking() {
        return mode == 2;
    }

    public void setNewTargetCoords(Vector3F targetSet) {
        this.targetSet = targetSet;
    }

    public void setNewTargetCoordsForIdle() {
        if (MathHelper.withinRange(coordSet.getX(), targetSet.getX(), motionX))
            targetSet.setX(MathHelper.clipFloat(new Random().nextFloat(), xMinMargin, xMaxMargin));
        if (MathHelper.withinRange(coordSet.getY(), targetSet.getY(), motionY))
            targetSet.setY(MathHelper.clipFloat(new Random().nextFloat(), yMinMargin, yMaxMargin));
        if (MathHelper.withinRange(coordSet.getZ(), targetSet.getZ(), motionZ))
            targetSet.setZ(MathHelper.clipFloat(new Random().nextFloat(), zMinMargin, zMaxMargin));
    }

    private boolean isMatch(int x, int y, int z) {
        World world = tAC.worldObj;
        return world.blockHasTileEntity(x, y, z) && world.getBlockTileEntity(x, y, z) instanceof TileAtomicConstructor;
    }

    public void moveToNewMainCoordSystem(Vector3I tempSet) {
        if (canMoveIntoCoordSet(tempSet)) {

        }
    }

    public void setNewTargetCoordsForWork() {
        float tempX = targetSet.getX();
        float tempY = targetSet.getY();
        float tempZ = targetSet.getZ();

        int replacingNum = new Random().nextInt(3);
        switch (replacingNum) {
            case 0:
                tempX = new Random().nextBoolean() ? 0.1F : 0.9F;
                break;
            case 1:
                tempY = new Random().nextBoolean() ? 0.1F : 0.9F;
                break;
            case 2:
                tempZ = new Random().nextBoolean() ? 0.1F : 0.9F;
                break;
        }
        replacingNum = new Random().nextInt(3);
        switch (replacingNum) {
            case 0:
                tempX = new Random().nextBoolean() ? 0.1F : 0.9F;
                break;
            case 1:
                tempY = new Random().nextBoolean() ? 0.1F : 0.9F;
                break;
            case 2:
                tempZ = new Random().nextBoolean() ? 0.1F : 0.9F;
                break;
        }

        targetSet.setX(tempX);
        targetSet.setY(tempY);
        targetSet.setZ(tempZ);
    }

    public void render(ModelConstructorDrone modelConstructorDrone) {
        glPushMatrix();
        glTranslated(coordSet.getX(), coordSet.getY(), coordSet.getZ());
        glScaled(0.1D, 0.1D, 0.1D);
        RenderUtils.bindTexture(TextureMaps.CONSTRUCTOR_DRONE);
        modelConstructorDrone.render();
        glPopMatrix();
        if (isWorking()) {
            RenderUtils.drawLineFrom(coordSet.getX(), coordSet.getY(), coordSet.getZ(), workingSet.getX(), workingSet.getY(), workingSet.getZ());
            if (++tickTiming > UtilMethods.getTicksFromSeconds(20)) {
                setIdle();
                tickTiming = 0;
            }
        }
    }

    public boolean canMoveIntoCoordSet(Vector3I movingSet) {
        return mainCoordSet.isAdjacent(movingSet);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof DroneState))
            return false;
        DroneState other = (DroneState) obj;
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "Mode: " + mode + ", Coord Set: " + coordSet + ", TargetSet: " + targetSet;
    }
}
