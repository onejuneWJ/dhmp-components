# DHMP-FILE
文件客户端，方便文件保存和读取

# 如何使用

在`FileClient`提供了一些基础操作方法

springboot环境直接引入。非springboot，需要将DefaultFileClient注册为bean。然后直接注入到组件中使用。


### 类 `FileInfo` :

`uid`: 文件保存后生成的唯一ID

`bucket`：桶名，文件存放的根目录下的第一个文件夹名称

`objectName`：文件对象名称，文件名，可以包含文件夹，如: text.txt、path1/text.txt

`fileName`: 文件名，text.txt

 - 文件最终保存路径将会是`{root}/bucket/objcetName`. 


    