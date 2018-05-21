<ul>
 <li>
  <table>
   <tr>
    <td><img src="/image/1E7DB2.png"></td>
    <td><img src="/image/2897D3.png"></td>
   </tr>
   <tr>
    <td>rgb(30,125,178)</td>
    <td>rgb(40,151,211)</td>
   </tr>
  </table>
 </li>
</ul>


***

Wedサイト上の色を調べられます。<br>
https://chrome.google.com/webstore/detail/colorpick-eyedropper/ohcpnigalekghcmgcdcenkpelffpdolg
<br>
カッコイイWedデザインがみれるサイト<br>
https://www.behance.net/gallery/
<br>
画像編集サイトWedアプリ<br>
https://pixlr.com/
<br>
アイコン画像のフリー素材サイト<br>
http://icooon-mono.com/
<br>

***

コレクションをArrayListオブジェクトにできます。<br>
コンストラクタ:ArrayList(Collection c)
例:ArrayList arrList = new ArrayList(Map.values());
<br><br>
ArrayListを配列にできます。<br>
Integer numbers = arrList.toArray(new Integer[0]);<br>
***
## 対局終了後の処理（クライアント側の処理が多くなるようにしました | 太字がServer側）※これよりいい方法があったらどんどん変更してください。
- Othello.isGameover() == trueが２回続く(相手と自分の打つ手がない。１回目のときに通信上でフラグを送って２回目に検知できるといいかもしれません。)
- Othello.checkWinner()で自分の勝敗を調べる。
- Clientで対局後の自分自身のRatingの計算をする。
- Clientで勝敗数、とRatingのなどのPlayerクラスのプロパティを更新する。(Player.setWin(),Player.setRate()など)
- Clientで勝敗結果とRatingを描画する。
- Serverに自分自身の勝敗結果とRatingを送信する。(Othelloインスタンスは対局中のClientがそれぞれ１つずつ持つので、自分だけの勝敗をServerに送信すればいよい。)
- **ServerでClientの勝敗結果とRatingを受け取る。**
- **Serverが持っているPlayerオブジェクトに受け取った勝敗結果とRatingを反映させる。**<br>
(参考までにですが、Listを使うなら、List.get(int index)で取り出しList.set(int index, Player)で更新<br>
Mapを使うなら、Map.get(key="アカウント名など")で取り出しMap.put(key,Player)で更新)<br>
List:https://www.sejuku.net/blog/20140<br>
Map:https://www.sejuku.net/blog/16067<br>
- ClientでstartMatch()で作った出力ストリームをcloseする。
- ClientでmatchReceive()で作った入力ストリームをcloseする。
- この後はユーザーホームに戻るなどで繰り返し・・・
***

***
## 内部クラスとメソッド（クライアントプログラム/コントローラー側）
<table>
 <tr>
  <th>内部クラス</th>
 </tr>
 <tr>
  <th>ReceiveInvite</th><td>対局の申し込み（サーバーからの文字列）を受け取るスレッド<br>
  
  **マッチング画面で呼び出す**
  
  </td>
 </tr>
 <tr>
  <th>MatchReceive</th><td>対局中の操作（サーバーからの文字列）を受信するスレッド<br>
 
 **対局画面で呼び出す**
 
 </td>
 </tr>
 <tr>
  <th>メソッド<th>
 </tr>
　<br>
 <tr>
  <th>myPlayerRequest()</th><td>サーバー上の自分のPlayerオブジェクトをサーバーから受信する<br>
 
 **成績参照などで呼び出す**
 
 </td>
 </tr>
 <tr>
  <th>otherPlayerRequest()</th><td>サーバー上の他のPlayerオブジェクトのコレクションを持つListオブジェクトを受信する<br>
 
 **マッチング画面&対局待ちプレイヤーを更新するときに呼び出す**
 
 </td>
 </tr>
 <tr>
  <th>startMatch()</th><td>対局中、自分の操作をサーバーに送るためのストリームをつくる<br>
 
 **対局画面で呼び出す**
 
 </td>
 </tr>
 <tr>
  <th>acceptOpponentOperation()</th><td>サーバーから送られてきた相手の操作（文字列）をOthelloオブジェクトに反映させる<br>
 
 **対局中、サーバーから相手の操作を受信したとき呼び出す**
 
 </td>
 </tr>
 <tr>
  <th>acceptMyOperation()</th><td>自分の操作をOhtelloオブジェクトに反映させる<br>
 
 **マスをクリックしたとき呼び出す（もしかしたら
  、いらないかも）**</td>
 </tr>
</table>

***
## つくったクラスはパッケージothelloに入れてください<br>
***
## フォルダー"*Source*"作りました。<br>
つくったソースはフォルダー"*Source*"直下に。<br>
途中のうまく動く段階で、『 othello_v1.java 』,『 othello_v2.java 』・・・ みたいに保存。<br>
動作テストとかのファイルがあれば、フォルダー"*Test*"に保存。
***
<img src="./image/grids.png"><br>
  <table>
    <tr>
      <th>議事録</th>
      <td>Hearing</td>
    </tr>
    <tr>
      <th>要求分析</th>
      <td>RequirementAnalysis</td>
    </tr>
    <tr>
      <th>設計</th>
      <td>Design</td>
    </tr>
    <tr>
     <th>ソースコード</th>
     <td>Source</td>
    </tr>
    <tr>
     <th>確認プログラム(Source直下)</th>
     <td>Test</td>
  </tr>
  </table>

<ul>
  <li>
    <table>
      <tr>
        <td><img src="./image/71A600.png"></td>
        <td><img src="./image/3771BA.png"></td>
        <td><img src="./image/04316C.png"></td>
        <td><img src="./image/E65D00.png"></td>
        <td><img src="./image/DBDBDB.png"></td>
        <td><img src="./image/F8F8F8.png"></td>
      </tr>
      <tr>
        <td>rgb(113,166,0)</td>
        <td>rgb(55,113,184)</td>
        <td>rgb(4,49,105)</td>
        <td>rgb(230,92,0)</td>
        <td>rgb(217,217,217)</td>
        <td>rgb(247,247,247)</td>
      </tr>
    </table><br>
    <img src="./image/image_01.png">
  </li>
</ul>

