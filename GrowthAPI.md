# GrowthAPI
様々な内部的な機能を提供します。また、メンテナンスなどのサーバー管理機能があります。  
なお、基本的にプレイヤーから見えるような機能の実装はありません。

# Config
## core/maintenance.yml
* onMain: 0  
メンテナンス中かどうかです。基本的に手動で設定する必要はありません。
* maxplayer: 50  
通常のプレイヤーの最大ログイン数です。権限が登録されているプレイヤーはこれを超えてログインできます。  
設定の変更は手動で行ってください。
* op1: 
*   name: redshirt72
*   uuid: ffff-ffffff-f-f-f-f-f-f-f-f
* op2: 
*   name: nog_prince
*   uuid: eeee-eeeeee-e-e-e-e-e-e-e-e
growth.opの権限を付与可能なプレイヤーの登録です。ユーザー名とUUIDが必要です。  
複数人登録する場合は、"op\<数字\>:"というように登録を増やしてください。  
設定の変更は手動で行ってください。
* join1: 
*   name: redshirt72
*   uuid: ffff-ffffff-f-f-f-f-f-f-f-f
* join2: 
*   name: nog_prince
*   uuid: eeee-eeeeee-e-e-e-e-e-e-e-e
メンテナンス時ログイン権限を付与可能なプレイヤーの登録です。ユーザー名とUUIDが必要です。  
複数人登録する場合は、"join\<数字\>:"というように登録を増やしてください。  
growth.opと重複して記述しても問題はないです。  
設定の変更は手動で行ってください。
