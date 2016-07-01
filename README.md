## Drcom账户管理Server端
> 本文针对Drcom查询账户URL的解析和抓取数据进行分析和讲解。
Drcom是大学生宿舍上网普遍使用的联网客户端，然而对于自己账号的信息查询和修改密码等都没有提供一个合理的方式去解决，因此小弟尝试去编写实现移动端的 app以方便大学生针对自己drcom账户的管理。


#### 架构由来和设计

* 刚开始我是想直接基于某公司开发的b/s架构去实现客户端，然而当我去了解该B/S架构的时候才发现去实现C和B/S的架构不合理。为什么不合理呢？因为该B/S架构中当然使用到session机制和图片验证机制等等的，要在移动端进行编写实在是工作量和逻辑有些复杂，并且编写出来的客户端势必会有比较大的累赘。因此我放下当前的工作，仔细思索一个合理的解决这种累赘的方法。 最后我想到了自己去实现server，让这个server端进行业务逻辑的处理。
* 当然我实现server第一想到是通过servlet去实现（因为小弟目前就只会使用jsp/servlet去实现server端）。这种架构的设计，个人取了一个名字，不知道是不是已经有了这个架构设计--------------css架构。 一个客户端，两个server端。这里的两个server端，相信不用说大家也知道是什么了。没错，就是我的server和某公司的server。（ps:所有的所有，还是因为小弟不才，搞不到那个数据库，只能用这种“卑鄙”的手段去解决问题）

#### 核心逻辑
  
##### 1、对密码进行加密的方式。
* 首先大家要了解fiddler这个抓包工具。在这个项目开始之前，我先通过fiddler抓取并大概了解到该B/S架构的业务逻辑。
* 通过fiddler进行抓包，发现登录的http请求中会对密码进行加密，然后每次加密后的密码都不一样而且是32位的一个字符串。当时我的第一反应就是该加密方式可能是通过MD5进行加密。然而令我困惑的是该browser端是如何对密码进行加密的，而且是如何做到每次加密后的密码不一样。之后我去看该html的源码，发现了一个重要的信息。就是这个html中嵌入了javascript，而这个javascript标签却不是固定的，是动态的。
	
		<script>
		function IsDigit(cCheck)
		{
		return (('0'<=cCheck) && (cCheck<='9'));
		}

		function IsAlpha(cCheck)
		{
         return ((('a'<=cCheck) && (cCheck<='z')) ||(cCheck!='_') || (('A'<=cCheck) && (cCheck<='Z')))
		}

		function IsaNull(cCheck)
		{
		return(cCheck != " ")
		}

		function checkform()
		{
        var cCheck;
        var nIndex;
		strUserID = document.LOGIN.name.value;
		if (strUserID == "")
		{
		alert("请输入用户帐号");
		document.LOGIN.name.focus();
		return false;
		}

		for (nIndex=0; nIndex<strUserID.length; nIndex++)
		{
                cCheck = strUserID.charAt(nIndex);
                if (!(IsDigit(cCheck) || IsAlpha(cCheck) || cCheck=='-' || cCheck=='_' || cCheck=='.'))
			{
			alert("帐号名只能使用字母、数字以及-、_和.，并且不能使用中文");
                        document.LOGIN.providername.focus();
			return false;
			};
        }
		strUserID = document.LOGIN.serial.value;
    	queryNum ="null";

		if (strUserID == "")
		{
		alert("请输入4位随机码");
		document.LOGIN.serial.focus();
		return false;
		}

    	Sessionid ="84b3613e39876c78a177bf0555a4da93";
    	LOGIN.password.value = LOGIN.password.value + Sessionid;
    	LOGIN.password.value = calcMD5(LOGIN.password.value);
    	LOGIN.submit();
		}
		</script>

加密方式就是上面显示的。
>
之后我首先抓包获取一个登录请求的参数中加密后的密码，然后获取该sessionid，最后通过自己的md5加密算法对正确的密码进行加密获取一个32位加密后的字符串。通过匹配，我发现该密码和http请求中的密码参数完全符合。之后我就确定了我的加密方式和该browser端的加密方式完全一样。
每次请求主页的时候，对该html页面进行解析，获取sessionid然后登陆的时候通过md5进行加密。这样就解决了对密码进行加密的工作。  

#####  2、该逻辑中使用到了session。
session这个技术也是我这次刚接触到的。

###### session:
* 在编程里是会话的意思
* Session 对象存储特定用户会话所需的信息。这样，当用户在应用程序的 Web 页之间跳转时，存储在 Session 对象中的变量将不会丢失，而是在整个用户会话中一直存在下去。
* 当用户请求来自应用程序的 Web 页时，如果该用户还没有会话，则 Web 服务器将自动创建一个 Session 对象。当会话过期或被放弃后，服务器将终止该会话。
* Session 对象最常见的一个用法就是存储用户的首选项。例如，如果用户指明不喜欢查看图形，就可以将该信息存储在 Session 对象中。有关使用 Session 对象的详细信息，请参阅“ASP 应用程序”部分的“管理会话”。
* 注意 会话状态仅在支持 cookie 的浏览器中保留。

>
针对该browser端，其中使用到了session和图片验证。之后我查询资料，并自行去实现图片验证登录，或者说是session登录验证。实现原理: 第一次请求服务端会产生一个session放在<set-cookies>中response给浏览器，获取图片的时候必须将该session作为cookies发送给服务端，这个时候服务端做的工作就是首先随机产生一个四位数然后将该数字作为值设置给session，也就是将session和随机码进行绑定，这样做的目的是在登录的时候服务端可以判断该四位随机数是否正确。

##### 3、重定向。
>
重定向状态码也就是3xx.  针对这个架构，进行http请求的时候如果session和图片验证都正确的话，状态码会是302，这个问题，当时也纠结了我好长时间。  因为在代码中如果不判断状态码的话，请求一直都是一个页面。 当时我一直以为是session的原因。 302真是个神奇的状态码，哈哈。回到正文，如果账号 密码 session 和图片验证都正确的话，会进行重定向，包头会有一个location，这个location就是要跳转的页面

![302](http://img.blog.csdn.net/20140618002200156?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbTY5NDQ0OTIxMg==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center).
    
 针对该架构要注意的一点就是每一个http请求都必须在请求头增加cookie键，值就是第一次产生的session。
 
##### 4、附加:
客户端的编写逻辑
     主界面首先对获取图片的api进行请求，该接口会返回session，然后保存在后面的请求中都加上session这个参数。

 
[客户端源码](https://github.com/xiyouMc/DrcomClient)

[服务端源码](https://github.com/xiyouMc/DrcomServer)

Thanks for your reading,by Mc. 

希望大家可以follow我的Github.以后相互交流技术。感谢你们的star。