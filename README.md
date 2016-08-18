# fatjar
#### 1. 简介 

开发过程中经常需要将多个jar包整合成一个jar包对外输出。在eclipse上开发时有一个eclipse fatjar plugin可以帮助我们完成这项工作，但开发工具换成Android studio之后我没有找到类似功能的插件，所以就自己写了一个整合jar的小工具，然后在build.gradle中添加一个task来执行它，也实现了类似的功能。

#### 2. 使用方法 

* 将准备合并的jar的路径配置到fatjar_congig.xml中；
* 将 fatjar_congig.xml 和 fatjar_0818.jar 放在同一个目录下，双击fatjar_0818.jar即可。
* 添加gradle的task: 

    task fatjar (type: JavaExec) {
        javaexec {
            main="-jar";
            args = ["C:\\fatjar\\fatjar_0818.jar"]
        }
    }
