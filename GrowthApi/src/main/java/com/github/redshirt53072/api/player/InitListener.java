package com.github.redshirt53072.api.player;

import org.bukkit.entity.Player;

/**
 * プレイヤーのログインなどの初期化時に実行されるonInitを提供する
 * @see PlayerManager#registerInit(InitListener) ここに登録する必要がある
 * @author redshirt
 * 
 */
public interface InitListener {
	/**
	 * プレイヤーのログイン時に実行される 
	 */
	public void onInit(Player player);
}
