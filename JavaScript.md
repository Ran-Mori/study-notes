# JavaScript



## 1 什么是JavaScript

### 	1.1  概述

* JavaScript是世界上最流行的脚本语言

### 1.2  历史

* 最新版本的JavaScript已经是ES6了，但很多浏览器只支持 **ES5**



## 2 快速入门

### 2.1 Hello World

* 间接方式

  ```html
  <head>
      <meta charset="UTF-8">
      <title>index</title>
      <script src="alert.js"></script>
  </head>
  ```

* 直接方式

  ```HTML
  <head>
      <meta charset="UTF-8">
      <title>index</title>
      <script>
      	alert("Hello World");
      </script>
  </head>
  ```

* 注意： 间接引入不要把标签整成斜线闭合，不然容易报错。 

### 2.2基础

```Html
<script>
	// 变量定义
    var i=1;
    
    // if判断
    if(i>0){
        
    }else if(){
             
    }else{
        
    }
    
    // for循环
    for(i=0;i<10;i++){
        
    }
    
    // "==" 只要求值一样，不要求类型一样
    // "===" 要求值和类型都一样
    
    // 对象
    var person={
        name: "Izumi Sakai",
        age: 40
    }
</script>
```



### 2.3控制台使用

* 打印命令 - **console.log(score)**

### 2.4浮点数精度损失

* ```java
  Math.abs(1/3-(1-2/3))<0.0000000001
  ```

