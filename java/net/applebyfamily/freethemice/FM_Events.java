package net.applebyfamily.freethemice;

import java.util.List;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.Icon;
import javax.vecmath.Point3d;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;


import org.lwjgl.util.vector.Vector3f;
















import sun.awt.windows.ThemeReader;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import cpw.mods.fml.common.gameevent.TickEvent;



public class FM_Events {

	public ArrayList<Point3d> DrawersList, tmpDrawersList, Waypoints;
	public boolean calculating;
	public int range = 10;
	private boolean _UpdateList;
	public FM_Events()
	{
		DrawersList = new ArrayList<Point3d>();
		tmpDrawersList = new ArrayList<Point3d>();
		Waypoints = new ArrayList<Point3d>();
		_UpdateList = false;
	}
	
    public void gameTick()
    {

    	
    	if (calculating == true || _UpdateList == true)
    	{
    		return;
    	}
    	

    	Point3d tmpPlayer = new Point3d((int)FinderMod.instance.thePlayer.posX , (int)FinderMod.instance.thePlayer.posY, (int)FinderMod.instance.thePlayer.posZ );    	
    	 	
    	calculating = true;

    	
    	Point3d tmp2 = new Point3d();
    

    	
		World theWorld = FinderMod.instance.theWorld;
		
		int Find = FinderMod.instance.myGuiHandeler._MyGui.searchNumber;
		tmpDrawersList.clear();
		if (Find == 0)
		{		
			if (FinderMod.instance.myGuiHandeler._MyGui.searchName != null)
			{
				if (FinderMod.instance.myGuiHandeler._MyGui.searchName.equals("") == false)
				{
					if (FinderMod.instance.myGuiHandeler._MyGui.searchName.equalsIgnoreCase("waypoints") == true || FinderMod.instance.myGuiHandeler._MyGui.searchName.equalsIgnoreCase("waypoint") == true)
					{
						for( int i = 0; i < Waypoints.size();i++)
						{
							tmpDrawersList.add((Point3d) Waypoints.get(i).clone());
						}
						closeShortRange();
						_UpdateList = true;
						calculating = false;
						return;						
					}
						List tmpTEL = FinderMod.MC.theWorld.loadedEntityList;
						for(int x = 0; x< tmpTEL.size();x++)
						{
						
							if (tmpTEL.get(x).toString().toLowerCase().contains(FinderMod.instance.myGuiHandeler._MyGui.searchName.toLowerCase()) == true)					
							{
								Point3d tmp = new Point3d(((Entity)tmpTEL.get(x)).posX, ((Entity)tmpTEL.get(x)).posY, ((Entity)tmpTEL.get(x)).posZ);
								tmpDrawersList.add(tmp);
							}
							
						}
						tmpTEL = FinderMod.MC.theWorld.loadedTileEntityList;
						for(int x = 0; x< tmpTEL.size();x++)
						{	
							
							if (tmpTEL.get(x).toString().toLowerCase().contains(FinderMod.instance.myGuiHandeler._MyGui.searchName.toLowerCase()) == true)					
							{
								Point3d tmp = new Point3d(((TileEntity)tmpTEL.get(x)).xCoord, ((TileEntity)tmpTEL.get(x)).yCoord, ((TileEntity)tmpTEL.get(x)).zCoord);
								tmpDrawersList.add(tmp);
							}
								
							
							
						}		
						if (tmpDrawersList.size() == 0)
						{
							for(int x = -range; x< range + 1;x++)
							{			
					    		for(int y = 0; y< 256;y++)
					    		{
					        		for(int z = -range; z< range + 1;z++)
					        		{
										if (theWorld.getBlock((int)FinderMod.instance.thePlayer.posX + x, y, (int)FinderMod.instance.thePlayer.posZ + z).getUnlocalizedName().toString().toLowerCase().contains(FinderMod.instance.myGuiHandeler._MyGui.searchName.toLowerCase()) == true)					
										{        				
					        				Point3d tmp = new Point3d((int)FinderMod.instance.thePlayer.posX + x, y, (int)FinderMod.instance.thePlayer.posZ + z);
					        				if (tmp != null)
					        				{
					        					tmpDrawersList.add(tmp);
					        				}
					        				
					        			}
					        			
					        		}
					    		}
							}
							checkRang();
						}
						closeShortRange();
				}				
			}
			_UpdateList = true;
			calculating = false;
			return;
		}
			
		
		if (Find > 0)
		{
			for(int x = -range; x< range + 1;x++)
			{			
	    		for(int y = 0; y< 256;y++)
	    		{
	        		for(int z = -range; z< range + 1;z++)
	        		{
	        			
	        			if (Block.getIdFromBlock(theWorld.getBlock((int)FinderMod.instance.thePlayer.posX + x, y, (int)FinderMod.instance.thePlayer.posZ + z)) == Find)            				
	        			{	        				
	        				Point3d tmp = new Point3d((int)FinderMod.instance.thePlayer.posX + x, y, (int)FinderMod.instance.thePlayer.posZ + z);
	        				if (tmp != null)
	        				{
	        					tmpDrawersList.add(tmp);
	        				}
	        				
	        			}
	        			
	        		}
	    		}
			}
	
		}		
		checkRang();
		closeShortRange();

		_UpdateList = true;
		calculating = false;
    }
    public String getSearchingFor()
    {
		int Find = FinderMod.instance.myGuiHandeler._MyGui.searchNumber;
		tmpDrawersList.clear();
		if (Find == 0)
		{		
			if (FinderMod.instance.myGuiHandeler._MyGui.searchName != null)
			{
				if (FinderMod.instance.myGuiHandeler._MyGui.searchName.equals("") == false)
				{
					return FinderMod.instance.myGuiHandeler._MyGui.searchName;
				}
			}
			return "Nothing";
		}
		return Find + "";
    }
	private void closeShortRange() {

		
		
				
		try {
			if (tmpDrawersList.size() > 1)
			{
				Collections.sort(tmpDrawersList, new Point3dCompare());
			}
		} catch (Exception e) {
			return;
		}
		
		
		if (tmpDrawersList.size() > 100)
		{
			tmpDrawersList.subList(100, tmpDrawersList.size()).clear();	
		}
		
	}
	public int getMaxRange()
	{
		int by = 100;
		
		try {
			by = Integer.parseInt(FinderMod.instance.myGuiHandeler._MyGui.rangefield.getText());
		} catch (NumberFormatException e) {
		}
		if (by < 30)
		{
			by = 30;
		}
			
		return by;
	}
	private void checkRang() {
		if (tmpDrawersList.size() < 100)
		{
			range = range + 10;
			//System.out.println(range);
			int by = getMaxRange();
			
			if (range >  by)
			{
				range = 10;
				
			}
		}
	}
    public void playerEnterWorld(){
    	NBTTagCompound var4 = readNBTSettings();
    	Waypoints.clear();    	
    	int maxCount = var4.getInteger("max");
    	for(int i = 0; i<maxCount;i++)
    	{
    		double pointX = var4.getDouble(i + "_point_X");
    		double pointY = var4.getDouble(i + "_point_Y");
    		double pointZ = var4.getDouble(i + "_point_Z");
    		Point3d tmpPoint = new Point3d(pointX, pointY, pointZ);
    		Waypoints.add((Point3d) tmpPoint.clone());    		
    	}
    	
    }
	private String checkPath(String Path)
	{
		String FileSeparator = FinderMod.instance.MC.mcDataDir.separator;
        String ASP = Path;
		if (ASP.endsWith(".") == true)
		{
			ASP = ASP.substring(0, ASP.length() - 1);
		}
		if (ASP.endsWith(FileSeparator) == false)
		{
			ASP = ASP + FileSeparator;			
		}
		return ASP;
	}
	private NBTTagCompound readNBTSettings()
	{ 
		String MainLocation = getMainLocation();
		
		NBTTagCompound par1NBTTagCompoundSettings;
		String NameP =getFileName();
        File var1 = new File(MainLocation,  NameP);       
        if (var1.exists())
        {
            try
            {       		
        		
            	par1NBTTagCompoundSettings = CompressedStreamTools.readCompressed(new FileInputStream(var1));
            	
            	return par1NBTTagCompoundSettings.getCompoundTag("Waypoints");
            }
            catch (Exception var5)
            {
                var5.printStackTrace();
            }
        }
        else
        {        	
            NBTTagCompound var4 = new NBTTagCompound();
            saveNBTSettings(var4);                      
            return var4;
        }
        return null;	
	}
	public String getFileName()
	{
		String NameP = "Multiplayer";
		try {
			NameP = MinecraftServer.getServer().worldServers[0].getWorldInfo().getSeed() + "";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
		}
		return "Seed_" + NameP + ".dat";
	}
	public void saveNBTSettings(NBTTagCompound var4)
	{
		String MainLocation = getMainLocation();
		
		NBTTagCompound par1NBTTagCompoundSettings = new NBTTagCompound();
        try
        {
        	String NameP =getFileName();
        	par1NBTTagCompoundSettings.setTag("Waypoints", var4);
            File var3 = new File(MainLocation,  NameP);            
            CompressedStreamTools.writeCompressed(par1NBTTagCompoundSettings, new FileOutputStream(var3));
            
        }
        catch (Exception var5)
        {
            
        }
	}	
	
	private String getMainLocation() {
		String FileSeparator = FinderMod.instance.MC.mcDataDir.separator;
		String MainLocation = checkPath(Minecraft.getMinecraft().mcDataDir.getAbsolutePath());
        File versionsDir = new File(MainLocation, "FinderMod");
        if (versionsDir.exists() == false)
        {
        	versionsDir.mkdir();
        }
		MainLocation = checkPath(versionsDir.getAbsolutePath());
		return MainLocation;
	}	
    
    @SubscribeEvent
	public void onWorldRender( final RenderWorldLastEvent event )
    {
   
    	if (FinderMod.instance.loaded == true)
    	{

	        	/*Collections.sort(DrawersList, new Point3dCompare());
    			int maxCount = DrawersList.size();
    			if (maxCount > 100)
    			{
    				maxCount = 100;
    			}*/
    			for(int i = DrawersList.size() - 1; i > -1; i--)
    			{
    				Point3d tmp = DrawersList.get(i);
    				if (tmp != null)
    				{
	    				if (i < 1)
	    				{
	    					drawAroundBlock((int)tmp.x, (int)tmp.y, (int)tmp.z, 0.0, 0.9, 0.0);
	    				}
	    				else
	    				{
	    					drawAroundBlock((int)tmp.x, (int)tmp.y, (int)tmp.z, 0.9, 0.0, 0.0);
	    				}
    				}
    			}
    			
    			if (_UpdateList == true)
    			{
    				DrawersList = (ArrayList<Point3d>) tmpDrawersList.clone();
    				_UpdateList = false;
    				//System.out.println("Drawer Updated: " + DrawersList.size());
    			}
    	}
    }

	private void drawAroundBlock(int x, int y, int z, double r, double g, double b ) {
		final Block blockb = FinderMod.instance.theWorld.getBlock(x,y, z );
		//System.out.println(Block.getIdFromBlock( blockb ));
		if( Block.getIdFromBlock( blockb ) != 0 || FinderMod.instance.myGuiHandeler._MyGui.searchNumber < 1) {
			
			drawESP( AxisAlignedBB.getBoundingBox( x, y,z,
					x + 1, y + 1,z + 1 ), r, g, b );
		}
	}

    public void drawESP( final AxisAlignedBB bb, final double r, final double g, final double b )  {
		Minecraft.getMinecraft( ).entityRenderer.disableLightmap( 0 );
		GL11.glPushMatrix( );
		GL11.glEnable( 3042 );
		GL11.glBlendFunc( 770, 771 );
		GL11.glLineWidth( 1.5F );
		GL11.glDisable( GL11.GL_LIGHTING );
		GL11.glDisable( GL11.GL_TEXTURE_2D );
		GL11.glEnable( GL11.GL_LINE_SMOOTH );
		GL11.glDisable( 2929 );
		GL11.glDepthMask( false );
		GL11.glColor4d( r, g, b, 0.1825F );
		drawBoundingBox( bb );
		GL11.glColor4d( r, g, b, 1.0F );
		drawOutlinedBoundingBox( bb );
		GL11.glLineWidth( 2.0F );
		GL11.glDisable( GL11.GL_LINE_SMOOTH );
		GL11.glEnable( GL11.GL_TEXTURE_2D );
		GL11.glEnable( GL11.GL_LIGHTING );
		GL11.glEnable( 2929 );
		GL11.glDepthMask( true );
		GL11.glDisable( 3042 );
		GL11.glPopMatrix( );
		Minecraft.getMinecraft( ).entityRenderer.enableLightmap( 0 );
	}
    public static void drawBoundingBox( final AxisAlignedBB axisalignedbb ) {
		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads( ); // starts x
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.draw( );
		tessellator.startDrawingQuads( );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.draw( ); // ends x
		tessellator.startDrawingQuads( ); // starts y
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.draw( );
		tessellator.startDrawingQuads( );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.draw( ); // ends y
		tessellator.startDrawingQuads( ); // starts z
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.draw( );
		tessellator.startDrawingQuads( );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		tessellator.draw( ); // ends z
	}

	/**
	 * Draws lines for the edges of the bounding box.
	 */
	public static void drawOutlinedBoundingBox( final AxisAlignedBB axisalignedbb ) {
		final Tessellator var2 = Tessellator.instance;
		var2.startDrawing( 3 );
		var2.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		var2.draw( );
		var2.startDrawing( 3 );
		var2.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		var2.draw( );
		var2.startDrawing( 1 );
		var2.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.minZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.maxX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.minY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		var2.addVertex( axisalignedbb.minX - RenderManager.renderPosX, axisalignedbb.maxY
				- RenderManager.renderPosY, axisalignedbb.maxZ - RenderManager.renderPosZ );
		var2.draw( );
	}
    @SubscribeEvent
    public void SomethingPickedup(ItemPickupEvent event)
    {
    	//System.out.println("Test");
    }
    
}
class Point3dCompare implements Comparator<Point3d>{
	@Override
	public int compare(Point3d arg0, Point3d arg1) {
		
		
		Point3d tmpPlayer = new Point3d(FinderMod.MC.thePlayer.posX , FinderMod.MC.thePlayer.posY, FinderMod.MC.thePlayer.posZ );
		
		
        if(arg0.distance(tmpPlayer) > arg1.distance(tmpPlayer)){
            return 1;
        } else {
            return -1;
        }
	}
}

