package com.github.redshirt53072.growthapi.server;
/**
 * サーバーストップなどの緊急時に実行されるonEmergencyを提供する
 * @see GrowthPluginManager#registerEmergency(EmergencyListener) ここに登録する必要がある
 * @author redshirt
 * 
 */
public interface EmergencyListener {
	/**
	 * サーバーストップなどの際に実行される 
	 */
	public void onEmergency();
}
