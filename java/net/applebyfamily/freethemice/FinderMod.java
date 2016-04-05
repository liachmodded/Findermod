package net.applebyfamily.freethemice;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nullable;

@Mod(modid = FinderMod.MOD_ID, version = FinderMod.VERSION)
public class FinderMod {

    static final String MOD_ID = "Finder Mod";
    static final String VERSION = "1.0.19";
    private static final Minecraft MC = Minecraft.getMinecraft();
    public static FinderMod instance;
    int range = 30;
    String searching = "";
    int found = 0;
    private FM_Events eventManager;
    // new fields
    private KeyBinding menuKey;
    private FM_Gui gui;
    private boolean clientRunning = false;
    Timer timer;

    @EventHandler
    @SuppressWarnings("unused")
    public void init(FMLInitializationEvent event) {
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);

        eventManager = new FM_Events();
        MinecraftForge.EVENT_BUS.register(eventManager);

        menuKey = new KeyBinding("Finder: Menu", 48, "key.categories.misc");
        ClientRegistry.registerKeyBinding(menuKey);
        timer = new Timer("Finder");
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (gui != null && menuKey.isPressed()) {
            if (MC.currentScreen == gui) {
                MC.displayGuiScreen(null);
            } else {
                MC.displayGuiScreen(gui);
            }
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void playerEnter(FMLNetworkEvent.ClientConnectedToServerEvent e) {
        this.gui = new FM_Gui();
        this.clientRunning = true;
        timer.scheduleAtFixedRate(new Finder(), 0, 50);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void playerExit(FMLNetworkEvent.ClientDisconnectionFromServerEvent e) {
        this.gui = null;
        this.clientRunning = false;
        timer.cancel();
    }

    private boolean isSearching() {
        return !searching.isEmpty() && !searching.equalsIgnoreCase("nothing");
    }

    private static class Finder extends TimerTask {

        transient WorldClient world;
        transient EntityPlayerSP player;

        public void run() {
            if (MC.theWorld == null || MC.thePlayer == null) {
                return;
            }
            if (instance.clientRunning && instance.isSearching()) {
                world = MC.theWorld;
                player = MC.thePlayer;
                AxisAlignedBB aabb = player.getEntityBoundingBox().expandXyz(instance.range);
                List<Entity> list = world.getEntitiesWithinAABB(Entity.class, aabb, ContainsEntitySelector.INSTANCE);
                Collections.sort(list, new EntityDistanceComparator(player));
                List<TileEntity> tileCopy = Lists.newArrayList(world.loadedTileEntityList);
                List<BlockPos> poses = Lists.newArrayList();
                BlockPos playerPos = player.getPosition();
                double t = instance.range * instance.range;
                for (TileEntity te : tileCopy) {
                    BlockPos pos = te.getPos().toImmutable();
                    if (pos.distanceSq(playerPos) <= t) {
                        poses.add(pos);
                    }
                }
                int x = (int) (player.posX + 0.5);
                int y = (int) (player.posY);
                int z = (int) (player.posZ + 0.5);
                Iterable<BlockPos> iterable = BlockPos.getAllInBox(new BlockPos(x - instance.range, Math.max(0, y - instance.range), z - instance
                        .range), new BlockPos(x + instance.range, Math.min(255, y + instance.range), z + instance.range));
                for (BlockPos pos : iterable) {
                    if (world.getBlockState(pos).getBlock().getUnlocalizedName().toLowerCase().contains(instance.searching.toLowerCase())) {
                        poses.add(pos);
                    }
                }
                Collections.sort(poses, new TileDistanceComparator(player));
                instance.found = list.size() + poses.size();
                instance.eventManager.update(poses, list);
            }
        }
    }

    private static class ContainsEntitySelector implements Predicate<Entity> {

        private static final ContainsEntitySelector INSTANCE = new ContainsEntitySelector();

        private ContainsEntitySelector() {
        }

        @Override public boolean apply(@Nullable Entity input) {
            return input != null && input.getName().toLowerCase().contains(instance.searching.toLowerCase());
        }
    }

    private static class EntityDistanceComparator implements Comparator<Entity> {

        private final double x, y, z;

        private EntityDistanceComparator(EntityPlayerSP player) {
            this.x = player.posX;
            this.y = player.posY;
            this.z = player.posZ;
        }

        @Override public int compare(Entity o1, Entity o2) {
            double t = o1.getDistanceSq(x, y, z) - o2.getDistanceSq(x, y, z);
            return t < 0 ? -1 : 1;
        }
    }

    private static class TileDistanceComparator implements Comparator<BlockPos> {

        private final double x, y, z;

        private TileDistanceComparator(EntityPlayerSP player) {
            this.x = player.posX;
            this.y = player.posY;
            this.z = player.posZ;
        }

        @Override public int compare(BlockPos o1, BlockPos o2) {
            return (int) (o1.distanceSq(x, y, z) - o2.distanceSq(x, y, z));
        }
    }

}
