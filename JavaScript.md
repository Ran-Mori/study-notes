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

### 2.5 JavaScript严格检查

* 局部变量建议都是用 **let** 来定义。

```html
<script>
	'user strict';
    i =1;//会报错
</script>
```

### 2.6 对象

```javascript
var person={
    name: "IzumiSakai",
    age :40,
    toString: function(){
        return "Hello,my name is ${this.name} and I'm ${age} years old";
    }
}
```

### 2.7 流程判断

```javascript
// if判断
if(i>0){

}
else if(){

}
else{

}

// for循环
for(let i=0;i<10;i++){

}
```



## 4 函数

### 4.1 定义函数

```javascript
function abs(x){
    if(x>=0)
        return x;
    else
        return -x;
}
```

### 4.2 arguments

* JavaScript 函数传入的参数统一封装成arguments数组

### 4.3 rest参数

* 用rest来接收其他的参数

```javascript
function restTest(x,y,z,...rest){
    ...
}
```

### 4.4 JavaScript里的所有函数其实是对象

```javascript
var method=function(){
    ...
}
```

### 4.5 全局变量规范 - 与框架有关

* 自己定义一个对象，所有的全局变量全部绑定到自定义的对象上。不能让它默认绑定在window上，不然乱套。

```javascript
var AllViriable={};
AllVirable.PI=3.1415926;
```

### 4.6 let关键字

* 现在JavaScript推荐只使用 **let**  和  **const**，不推荐用 **var** 了





## 5 JSON对象

### 5.1使用 JSON关键字进行对象与json格式的转换与操作。