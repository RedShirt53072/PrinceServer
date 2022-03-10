package com.github.redshirt53072.api.player;

import org.bukkit.entity.Player;

/**
 * プレイヤーのログアウト時に実行されるonLogOutを提供する
 * @see PlayerManager#registerLogOut(LogoutListener) ここに登録する必要がある
 * @author redshirt
 * 
 */
public interface LogoutListener {
	/**
	 * プレイヤーのログイン時に実行される 
	 */
	public void onLogout(Player player);
}
