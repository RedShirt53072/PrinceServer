# DimManager
DimManagerはディメンション間の移動、ゲームモード変更、ホーム機能などを提供するプラグインです。

# GUI
## ワールド選択GUI
コンパスのクリックか/worldでアクセスできます。  
他のディメンションへの移動とホームの設定、ホームへのテレポートが可能です。

# コマンド
## /home
* /home sethere  
ホームを今の座標に設定する
* /home teleport  
ホームに移動する

## /world
* /world  
ワールド選択GUIを開く
* /world \<ディメンション名\>  
ディメンションを移動する

# 管理コマンド
## /manage dim
* /manage dim reload  
ディメンションの設定configを再読み込みします。
* /manage dim login \<ディメンション名\>  
ログイン時に飛ばされるディメンション(ロビー)を設定します。
* /manage dim delete \<ディメンション名\>  
ディメンションの登録を削除します。
* /manage dim addhomedim \<ディメンション名\>  
そのディメンションでホーム機能を使用できるようにします。
* /manage dim delhomedim \<ディメンション名\>  
そのディメンションでホーム機能を使用できないようにします。
* /manage dim delhomedim \<ディメンション名\>  
そのディメンションでホーム機能を使用できないようにします。
### 以下3つはプレイヤーからのみ使用可能です。
* /manage dim register \<ディメンション名\> \<隠すかどうか\> \<ゲームモード\> \<x\> \<y\> \<z\> \<yaw\> \<pitch\>  
今いるディメンションを登録します。
* /manage dim dimall \<ディメンション名\>  
普段隠されているディメンションも含めて、ワールドを移動します。
* /manage dim teleport \<プレイヤー\> \<ディメンション名\>  
他のプレイヤーのホームの座標や通常ディメンションでの座標にテレポートします。



