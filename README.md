# KeyboardMonitor
Probably the best keyboard monitor on the github.

大概是全网最好的键盘监听了.

The android keyboard listener listens for keyboard height to show or hide.

安卓 键盘监听器 监听键盘高度跟显示or隐藏.

It can be registered in dialog and popupWindow. It can accurately recognize the status of the pop-up keyboard and accurately obtain the height of the current keyboard. It is compatible with full screen status, hiding or enabling virtual navigation keys, dynamically changing dp and other emergencies.

可以在dialog跟popupWindow中注册使用。能够准确的识别弹出键盘状态以及准确获取当前键盘的高度,兼容全屏状态、隐藏或启用虚拟导航键、动态改变dp等突发状况。

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.fengxiaocan:KeyboardMonitor:v1.0'
	}
  
  eg:
  
  	KeyboardHelper.registerKeyboardListener(getWindow(),new OnKeyboardListener(){
            @Override
            public void onKeyBoardEvent(boolean isShow,int height){
                
            }
        });
  
![avatar](/doc/image1.png)
![avatar](/doc/image2.png)
