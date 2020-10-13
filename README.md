# cordova-urovo-pda-plugin
 🎉🎉🎉 基于cordova开发的PDA扫描插件 

## 前言
   PDA扫码相对于调用摄像头扫码有诸多优点: 1. 支持多样化的二维码以及条形码设置 2.扫码响应速度快 3.扫码稳定等等。本插件是基于cordova项目构建的,因此你可以通过 ``` cordova add plugin XXX ```导入到你的插件列表中。


### 如何安装
 -  ``` git clone https://github.com/sixiaodong123/cordova-urovo-pda-plugin ```   
 -  ``` cordova plugin add XXX(插件的路径,例如: /Users/mac/Desktop/cordova-urovo-pda-plugin/scanPda)  ```
  
  
    假如你安装好了，你可以通过```cordova plugin list```查看到此插件。


###  如何使用

 ```js
  window.cordova.define.moduleMap['scanPda.scanPda'].exports.coolMethod('start', function(params) {
      alert(params);
  })
 ```

 ⚡⚡⚡ 注意: 可能不同的项目``` define.moduleMap['scanPda.scanPda'] ```这里会不一样,我们可以调试的时候在浏览器输出cordova, 然后一层一层点进去即可。


 ### FAQ
  ##### 这里有几个点需要注意点一下 
  - lib/andorid目录下的jar包很关键, 下面两个类就是这个jar包获取的(andoroid 3以后似乎默认不支持android.device.)
      ```java
      import android.device.ScanManager;
      import android.device.scanner.configuration.PropertyID;
      ```
  -  scnaPda.java中的``` SCAN_ACTION ```是我们在PDA中自定义的动作,使用的时候最好校对一下值是否匹配。
