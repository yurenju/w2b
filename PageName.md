# Introduction #

不介意我用中文吧。這是個小小的程式，可以用來從 Wordpress 的備份檔轉移文章到 Blogger 上面。

# Usage #

首先在您的 Wordpress 系統管理介面的 Manage → Export 點選 Download Export File。

至 w2b 專案首頁 下載，解壓縮後，直接滑鼠雙擊 w2b.jar，或者是使用命令提示字元在解壓縮的目錄中執行 java -jar w2b.jar 。

![http://farm1.static.flickr.com/199/441659204_434a5e606d_o.png](http://farm1.static.flickr.com/199/441659204_434a5e606d_o.png)

第一欄選擇剛剛匯出的 Wordpress 備份檔，第二欄填寫你 Blogger 的 blog 網址，記得最後面要有斜線 /，後面兩欄填寫 Blogger 的帳號密碼，別忘了 帳號是 xxx@gmail.com 而非 xxx。接著按下 Import 就會開始匯入了。下面那個 Progress bar 跟進度有點詭異，雖然匯入成功但是下面的狀態常常會漏掉很多筆。還有 Delete All 按鈕會『儘可能』的幫你刪除 Blogger 上的文章，但是也不會全刪。還有一件非常重要的事情，不要常常轉了又刪，不然後台系統會沒辦法用，大概是把我識別成機器人吧？ XD

# 問題 #
**目前路徑不能有中文** 如果 XML 文件不能通過驗證，通常也沒辦法轉換，遇到這個情形你使用 Issue 回報，並且附上您的 XML 檔案。