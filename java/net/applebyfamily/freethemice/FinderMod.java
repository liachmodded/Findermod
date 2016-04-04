package net.applebyfamily.freethemice;


import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.Timer;

@Mod(modid = FinderMod.MODID, version = FinderMod.VERSION)
public class FinderMod {

    public static final String MODID = "Finder Mod";
    public static final String VERSION = "1.0.1710";
    @Instance(value = MODID)
    public static FinderMod instance;
    public static Minecraft MC;
    public static FM_Keyboard myKeyboard;
    @SidedProxy(clientSide = "net.applebyfamily.freethemice.ClientProxy", serverSide = "net.applebyfamily.freethemice.CommonProxy")
    public static ClientProxy proxy;
    public EntityClientPlayerMP thePlayer;
    public World theWorld;
    public Timer GameTick;
    public boolean loaded, runOnEnter;
    public int testCount = 0;
    public Tessellator mainDraw;
    public FM_GuiHandler myGuiHandeler;
    public FM_Events eventManager;
    public int[] NumberforB = new int[2];
    public int[] NumberforN = new int[2];
    private FM_NBTTags MyNBTSetting;
    private int ticker = 0;

    public void sendChatMessage(String textout) {

        IChatComponent GoinOut = IChatComponent.Serializer.func_150699_a("FinderMod");
        GoinOut.appendText(": " + textout);

        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(GoinOut);

    }

    private void setupKeyBinding(String Discription, int KeyCode) {

        KeyBinding[] tmp111 = new KeyBinding[Minecraft.getMinecraft().gameSettings.keyBindings.length + 1];
        for (int i = 0; i < Minecraft.getMinecraft().gameSettings.keyBindings.length; i++) {
            tmp111[i] = Minecraft.getMinecraft().gameSettings.keyBindings[i];
        }
        tmp111[Minecraft.getMinecraft().gameSettings.keyBindings.length] = new KeyBinding(Discription, KeyCode, "key.categories.misc");

        Minecraft.getMinecraft().gameSettings.keyBindings = tmp111.clone();
    }

    @EventHandler
    public void init(FMLLoadCompleteEvent event) {

        instance = this;
        MyNBTSetting = new FM_NBTTags("settings");
        MyNBTSetting.FileName = "SettingsAreHere.dat";
        FMLCommonHandler.instance().bus().register(this);

        MC = FMLClientHandler.instance().getClient();
        myKeyboard = new FM_Keyboard();

        myGuiHandeler = new FM_GuiHandler();
        MinecraftForge.EVENT_BUS.register(myGuiHandeler);


        eventManager = new FM_Events();
        FMLCommonHandler.instance().bus().register(eventManager);
        MinecraftForge.EVENT_BUS.register(eventManager);


        NBTTagCompound tmpNBTTag = MyNBTSetting.readNBTSettings();

        NumberforB[0] = tmpNBTTag.getInteger("Finder: Menu");
        if (NumberforB[0] == 0) {
            NumberforB[0] = 48;
        }
        NumberforB[1] = Minecraft.getMinecraft().gameSettings.keyBindings.length;
        setupKeyBinding("Finder: Menu", NumberforB[0]);

        NumberforN[0] = tmpNBTTag.getInteger("Finder: Add/Delete Waypoint");
        if (NumberforN[0] == 0) {
            NumberforN[0] = 49;
        }
        NumberforN[1] = Minecraft.getMinecraft().gameSettings.keyBindings.length;
        setupKeyBinding("Finder: Add/Delete Waypoint", NumberforN[0]);

    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        ticker++;
        if (ticker < 20) {
            return;
        }
        Minecraft.getMinecraft().func_152344_a(new Runnable() {

            @Override
            public void run() {
                if (NumberforB[0] != Minecraft.getMinecraft().gameSettings.keyBindings[NumberforB[1]].getKeyCode()) {
                    NumberforB[0] = Minecraft.getMinecraft().gameSettings.keyBindings[NumberforB[1]].getKeyCode();
                    NBTTagCompound tmpNBTTag = MyNBTSetting.readNBTSettings();
                    tmpNBTTag.setInteger("Finder: Menu", Minecraft.getMinecraft().gameSettings.keyBindings[NumberforB[1]].getKeyCode());
                    MyNBTSetting.saveNBTSettings(tmpNBTTag);
                    System.out.println("Saved B");
                }
                if (NumberforN[0] != Minecraft.getMinecraft().gameSettings.keyBindings[NumberforN[1]].getKeyCode()) {
                    NumberforN[0] = Minecraft.getMinecraft().gameSettings.keyBindings[NumberforN[1]].getKeyCode();
                    NBTTagCompound tmpNBTTag = MyNBTSetting.readNBTSettings();
                    tmpNBTTag
                            .setInteger("Finder: Add/Delete Waypoint", Minecraft.getMinecraft().gameSettings.keyBindings[NumberforN[1]].getKeyCode());
                    MyNBTSetting.saveNBTSettings(tmpNBTTag);
                    System.out.println("Saved N");
                }
                // TODO Auto-generated method stub
                if (thePlayer == null) {
                    if (MC.thePlayer != null) {
                        thePlayer = MC.thePlayer;
                    }
                }
                if (theWorld == null) {
                    if (MC.theWorld != null) {
                        theWorld = MC.theWorld;
                        mainDraw = Tessellator.instance;
                    }
                }

                if (thePlayer != null && theWorld != null) {
                    loaded = true;
                    if (thePlayer != MC.thePlayer) {
                        thePlayer = null;
                        loaded = false;
                        runOnEnter = false;
                    }
                    if (theWorld != MC.theWorld) {
                        theWorld = null;
                        loaded = false;
                        runOnEnter = false;
                    }


                }
                if (loaded) {
                    if (!runOnEnter) {
                        runOnEnter = true;
                        eventManager.playerEnterWorld();
                    }
                    eventManager.gameTick();
                }
            }
        });
        ticker = 0;
    }
}
