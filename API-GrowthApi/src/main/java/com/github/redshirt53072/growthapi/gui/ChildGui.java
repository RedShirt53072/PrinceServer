package com.github.redshirt53072.growthapi.gui;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.server.GrowthPlugin;

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
		onRegister();
	}
	/**
	 * @Deprecated 子GUIはプレイヤーではなく、親GUIから登録してください。
	 */
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
	
	@Override
	public void onEmergency() {
		onClose();
		close();
		parent.onEmergency();
	}
}