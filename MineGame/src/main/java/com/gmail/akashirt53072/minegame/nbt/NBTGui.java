package com.gmail.akashirt53072.minegame.nbt;

import org.bukkit.entity.Player;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.gui.GuiID;

public class NBTGui extends NBTLoader{

    public NBTGui(Main plugin,Player player) {
    	super(plugin,player);
    }
    public void init() {
    	super.writeString("guiID", "NONE");
    	
    }
    public void setID(GuiID id) {
    	super.writeString("guiID", id.toString());
    }
    
    public GuiID getID() {
    	String result = super.readString("guiID");
    	if(result == null) {
    		return GuiID.NONE;
    	}
    	return GuiID.valueOf(result);
    }
}
