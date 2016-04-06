package net.applebyfamily.freethemice;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

class FM_Events {

    private List<BlockPos> blockList;
    private List<Entity> entityList;
    private boolean isRendering, isUpdating;

    FM_Events() {
        this.blockList = Lists.newArrayList();
        this.entityList = Lists.newArrayList();
        isRendering = false;
        isUpdating = false;
    }

    void update(List<BlockPos> blockList, List<Entity> entities) {
        if (isRendering) {
            return;
        }
        isUpdating = true;
        this.blockList = blockList;
        this.entityList = entities;
        isUpdating = false;
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onOverlayRender(final RenderGameOverlayEvent.Text event) {
        if (Minecraft.getMinecraft().currentScreen == null) {
            if (!FinderMod.instance.searching.isEmpty() && !FinderMod.instance.searching.equalsIgnoreCase("nothing")) {
                ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
                FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
                int width = scaledresolution.getScaledWidth();
                int height = scaledresolution.getScaledHeight();
                fontrenderer.drawStringWithShadow(Integer.toString(FinderMod.instance.found), width / 2, height / 8, 0xffffffff);
            }
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onWorldRender(final RenderWorldLastEvent event) {
        if (isUpdating) {
            return;
        }
        isRendering = true;
        int i = 0;
        for (BlockPos pos : this.blockList) {
            switch (i) {
                case 0:
                    RenderUtils.blockESPBox(pos, new Color(0, 0, 0.9F));
                    break;
                case 1:
                    RenderUtils.blockESPBox(pos, new Color(0, 0.9F, 0));
                    break;
                case 2:
                    RenderUtils.blockESPBox(pos, new Color(0.9F, 0.9F, 0.2F));
                    break;
                case 3:
                    RenderUtils.blockESPBox(pos, new Color(0.9F, 0.4F, 0));
                    break;
                default:
                    RenderUtils.blockESPBox(pos, new Color(0.9F, 0, 0));
            }
            i++;
            if (i > 100) {
                break;
            }
        }

        i = 0;
        for (Entity e : this.entityList) {
            switch (i) {
                case 0:
                    RenderUtils.entityESPBox(e, new Color(0, 0, 0.9F));
                    break;
                case 1:
                    RenderUtils.entityESPBox(e, new Color(0, 0.9F, 0));
                    break;
                case 2:
                    RenderUtils.entityESPBox(e, new Color(0.9F, 0.9F, 0.2F));
                    break;
                case 3:
                    RenderUtils.entityESPBox(e, new Color(0.9F, 0.4F, 0));
                    break;
                default:
                    RenderUtils.entityESPBox(e, new Color(0.9F, 0, 0));
            }
            i++;
            if (i > 100) {
                break;
            }
        }
        isRendering = false;
    }

}

