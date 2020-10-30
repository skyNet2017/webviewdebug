# webview logger
[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-webviewdebug-green.svg?style=flat )]( https://android-arsenal.com/details/3/6436 )

[![](https://jitpack.io/v/skyNet2017/webviewdebug.svg)](https://jitpack.io/#skyNet2017/webviewdebug)

# Android上最详尽的webview和原生日志

包括

* webviewclient和webviewchromeclient的所有回调打印到logcat, 美化显示.   过滤tag: debugw   实现方式: 静态代理,每个回调均个性化定制
* 所有@javaInterface交互调用均打印到logcat和js的console        aop实现,无侵入.开关配置在gradle脚本中,release可关
* 所有webview原生调用js均打印到logcat和js的console                aop实现,无侵入.开关配置在gradle脚本中,release可关
* 所有页面自动注入eruda.(移动端web开发最常用的调试面板之一)
* 页首显示url,加载时长,点击可快速显示cookie





![image-20201029194236926](https://gitee.com/hss012489/picbed/raw/master/picgo/1603971756972-image-20201029194236926.jpg)

![image-20201029194125715](https://gitee.com/hss012489/picbed/raw/master/picgo/1603971685767-image-20201029194125715.jpg)



![image-20201029194422756](https://gitee.com/hss012489/picbed/raw/master/picgo/1603971862804-image-20201029194422756.jpg)

![image-20201029194607064](https://gitee.com/hss012489/picbed/raw/master/picgo/1603971967114-image-20201029194607064.jpg)

![image-20201029194537414](https://gitee.com/hss012489/picbed/raw/master/picgo/1603971937462-image-20201029194537414.jpg)

![image-20201029194515119](https://gitee.com/hss012489/picbed/raw/master/picgo/1603971915166-image-20201029194515119.jpg)















## Getting the Library

<b>gradle</b>
```groovy
	repositories {
		maven {
			maven { url "https://jitpack.io" }
		}
	}

	dependencies {
		compile 'com.github.skyNet2017:webviewdebug:1.1.7'
	}
```

## Usage

Output in logcat uses this tag: `DebugWVClient,DebugWVClient-chrome`.

### 1.Debugging a WebViewClient,WebChromeClient
###### 1. Fast way if you already have a WebViewClient
If you already have a `WebViewClient` implementation, wrap it with `DebugWebViewClient` before assigning it to the WebView.

```java

final DebugWebViewClient debugWebViewClient = new DebugWebViewClient(new MyCustomWebViewClient());
debugWebViewClient.setLoggingEnabled(true);
webView.setWebViewClient(debugWebViewClient);

DebugWebChromeClient chromeClient = new DebugWebChromeClient(new MyCustomWebChromeClient());
chromeClient.setLoggingEnabled(true);
webView.setWebChromeClient(chromeClient);
```

###### 2. You have a custom WebViewClient but want more control
You can use `DebugWebViewClientLogger` to log things as needed in your own `WebViewClient`.

Make sure you pass the parameters and any return values of your own `WebViewClient` to the equivalent methods of the `DebugWebViewClientLogger`.

###### 3. You don't have a WebViewClient but you want to know what is going on
Just instantiate and assign a `DebugWebViewClient`to the WebView.

```java
final DebugWebViewClient debugWebViewClient = new DebugWebViewClient();
debugWebViewClient.setLoggingEnabled(true);
webView.setWebViewClient(debugWebViewClient);
```

### Controlling output
Both `DebugWebViewClient`  and `DebugWebViewClientLogger` implemetn `LogControl` which contains the following signatures:

* `isLoggingEnabled()`: Check if logging is globally enabled
* `setLoggingEnabled(boolean)`: Enable or disable logging
* `isLogKeyEventsEnabled()`: Check if logging of `KeyEvent` related methods is enabled
* `setLogKeyEventsEnabled(boolean)`: Enable or disable logging of `KeyEvent` related methods is enabled

`KeyEvent` related methods have more granularity due to privacy concerns, as all keystrokes will be logged.

`setLoggingEnabled(boolean)` is a global switch which overrides `setLogKeyEventsEnabled(boolean)`

## 2.inject edura.js to have a js debug pannel on any url

<https://github.com/liriliri/eruda>

```
debugWebViewClient.setJsDebugPannelEnable(true);
```



![image-20201029194422756](https://gitee.com/hss012489/picbed/raw/master/picgo/1603971862804-image-20201029194422756.jpg)

![image-20201029194607064](https://gitee.com/hss012489/picbed/raw/master/picgo/1603971967114-image-20201029194607064.jpg)







## License

    Copyright (C) 2017 Alexandros Schillings
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
