package net.applebyfamily.freethemice;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.opengl.GL11;







import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.server.FMLServerHandler;

@Mod(modid = FinderMod.MODID, version = FinderMod.VERSION)
public class FinderMod {
	
    public static final String MODID = "Finder Mod";
    public static final String VERSION = "1.0.172";
    public EntityClientPlayerMP thePlayer;
    public World theWorld;
    
    @Instance(value = MODID)
    public static FinderMod instance;
    public static Minecraft MC;
    public static FM_Keyboard myKeyboard;
        

    
    
    public Timer GameTick;
    public boolean loaded, runOnEnter;
    public int testCount = 0;
    public Tessellator mainDraw ;
    public FM_GuiHandler myGuiHandeler;
    public FM_Events eventManager;
    
    @SidedProxy(clientSide = "net.applebyfamily.freethemice.ClientProxy", serverSide = "net.applebyfamily.freethemice.CommonProxy")
    public static ClientProxy proxy;
    

    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {    	

    	instance = this;
    	
    	MC = FMLClientHandler.instance().getClient();
    	myKeyboard = new FM_Keyboard();
    	
    	myGuiHandeler = new FM_GuiHandler();
    	MinecraftForge.EVENT_BUS.register(myGuiHandeler);
    	

    	eventManager = new FM_Events();
    	FMLCommonHandler.instance().bus().register(eventManager);
    	MinecraftForge.EVENT_BUS.register(eventManager);
    	
    	
    	
    	GameTick = new Timer();
    	GameTick.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {

				// TODO Auto-generated method stub
				if (thePlayer == null)
				{
					if (MC.thePlayer != null)
					{
						thePlayer = MC.thePlayer;
					}
				}
				if (theWorld == null)
				{
					if (MC.theWorld != null)
					{
						 theWorld = MC.theWorld;
						 mainDraw = Tessellator.instance;
					}
				}
				
				if (thePlayer != null && theWorld != null)
				{
					loaded = true;
					if (thePlayer != MC.thePlayer)
					{
						thePlayer = null;
						loaded = false;
						runOnEnter = false;
					}
					if (theWorld != MC.theWorld)
					{
						theWorld = null;
						loaded = false;
						runOnEnter = false;
					}
					
					
				}
				if (loaded == true)
				{					
					if (runOnEnter == false)
					{				    				    					    	
						runOnEnter = true;
						eventManager.playerEnterWorld();	
					}
					eventManager.gameTick();
				}
			}
		}, 10, 10);
        
    }
    

}
