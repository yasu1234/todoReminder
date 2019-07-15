## タスクTODOスケジューラー
___

アプリのダウンロードは[こちら](https://play.google.com/store/apps/details?id=com.kumaydevelop.todoreminder)から

指定した時間に通知がくるTodoリストのアプリです。  

#### 使用環境
Kotlin 1.2.30  
Android Studio 3.1  
Realm 2.1.1  

#### 使用技術
*データの登録・更新・削除*  
*AlarmManagerを使ったアラーム機能(再起動やアプリ更新も考慮)*  
*NotificationManagerを使った通知機能*  
*listViewとDataBindingの組み合わせ*  
*CircleCIとDeployGateを使った自動化*  
*Junit4とEspresso3.0.2(UIテスト)を使った単体テスト*  

#### 画面イメージ

①タスク名・詳細・期限・通知時間を登録できる。  
  一覧のセルを押すと詳細を表示し、更新・削除ができる。  

![todo1](https://user-images.githubusercontent.com/20049397/61213995-a4bbe180-a741-11e9-9ffc-588079a9f5c5.gif)

②通知を押すと、アプリが起動し、一覧が表示される。  
  
![todo2](https://user-images.githubusercontent.com/20049397/61214124-03815b00-a742-11e9-9c89-118064cb42fd.gif)
