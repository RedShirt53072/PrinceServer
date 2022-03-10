package com.github.redshirt53072.api.gui;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.redshirt53072.api.message.LogManager;
import com.github.redshirt53072.api.server.GrowthPlugin;

public abstract class ChildGui extends Gui{
	protected Gui parent;
	
	public ChildGui(GrowthPlugin plugin) {
	    super(plugin);
	}
	
	public void open(Gui parent) {
		if(this.parent != null) {
			LogManager.logError("[Gui]既に親GUIが登録された子GUIに親GUIを登録しようとしています。", plugin,new Throwable(), Level.WARNING);
			return;
		}
		if(parent == null) {
			LogManager.logError("[Gui]存在しない親GUIに子GUIを登録しようとしています。", plugin,new Throwable(), Level.WARNING);
			return;
		}
		this.parent = parent;
		player = parent.getPlayer();
		parent.registerChild(this);
		GuiManager.addNotClose(player);
		player.closeInventory();
		onRegister();
	}
	/**
	 * @Deprecated 子GUIはプレイヤーではなく、親GUIから登録してください。
	 */
	@Deprecated
	@Override
	public void open(Player player) {
		LogManager.logError("[Gui]子GUIを登録しようとしています。", plugin,new Throwable(), Level.WARNING);
		return;
	}
	@Override
	public void close() {
		GuiManager.addNotClose(player);
		player.closeInventory();
		parent.onReturn();
	}
	
	protected void asyncReturn() {
		Bukkit.getScheduler().runTask(plugin, new Runnable() {
    		@Override
    		public void run() {
    			parent.onReturn();
    		}
    	});
	}
	
	@Override
	public void onEmergency() {
		onClose();
		close();
		parent.onEmergency();
	}
}