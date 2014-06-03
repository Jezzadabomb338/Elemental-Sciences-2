package me.jezzadabomb.es2.client.renderers.tile;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslated;
import me.jezzadabomb.es2.client.models.ModelCrystalObelisk;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.tileentity.TileObelisk;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TileCrystalObeliskRenderer extends TileEntitySpecialRenderer {

    ModelCrystalObelisk modelCrystalObelisk;

    public TileCrystalObeliskRenderer() {
        modelCrystalObelisk = new ModelCrystalObelisk();
    }

    public void renderTileObelisk(TileObelisk obelisk, double x, double y, double z, float tick) {
        glPushMatrix();

        glTranslated(x + 0.5F, y + 0.5F, z + 0.5F);
        glScalef(0.5F, 0.5F, 0.5F);

        switch (obelisk.getRenderType()) {
            case 2:
                // RenderUtils.bindTexture(TextureMaps.CRYSTAL_TOP_OBELISK);
                // modelCrystalObelisk.renderUpper();
                float tempScale = 0.90F;
                glScalef(tempScale, 1.0F, tempScale);
                // break;
            case 1:
                tempScale = 0.90F;
                glScalef(tempScale, 1.0F, tempScale);
            case 0:
            default:
                RenderUtils.bindTexture(TextureMaps.CRYSTAL_OBELISK);
                modelCrystalObelisk.renderLower();
        }

        glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
        if (tileEntity instanceof TileObelisk)
            renderTileObelisk((TileObelisk) tileEntity, x, y, z, tick);
    }

}
