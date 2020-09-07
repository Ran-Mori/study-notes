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

```javascript
JSON.parse();
JSON.stringfy(); 
```



## 6 面向对象编程

* 类

  ```javascript
  class Student{
      sayHello(){
          console.log("Hello");
      }
      // 和java的构造函数不同
      constructor(name){
          this.name = name;
      }  
  }
  ```

* 使用对象

  ```javascript
  let xiaoMing=new Student("小明");
  ```

* 继承

  ```javascript
  class Rectangle extends Shape{
      constructor(type,name){
          super(type);
          this.name=name;
      }
      ...
  }
  ```



## 7 操作BOM对象

### 7.1获取当前浏览器范围高度宽度

> window

```javascript
window.innerHeight;
window.innerWith;
```

### 7.2 获取用户的浏览器信息

> navigator

* 比如用户是用的手机访问还是Windows访问，用户使用的是什么浏览器
* 大多数时候不会使用navigator对象，因为它能够被人为修改

```javascript
navigator.userAgent;
```

### 7.3 获取屏幕的宽度和高度

> screen

* 当电脑缩放为150%时，分辨率变成了1280

```javascript
screen.height;
screen.width;
```

### 7.4 导航

> location 代表URL信息
* host - 主机； href - URL； reload() - 重加载方法； assign() - 跳转方法
```javascript
console.log(location);
location.reload();
location.assign("https://www.bilibili.com")
```

### 7.5 文档树

> document 

* 可以对浏览器节点进行新增和删除，实现对界面的修改和动态变化
* document可以获得cookie信息，可能会被恶意劫持暴露用户信息。

### 7.6 历史记录

> history  代表浏览器的历史记录

```javascript
history.back();
history.forward();
```

### 7.7 删除节点

> document    通过父节点来进行删除
* 删除是一个动态的过程

```javascript
let father=son.parentElement;
father.removeChild(son);
father.children[i]; //删除了第一个过后，后面的索引依次减一
```

### 7.8 插入节点

```javascript
father.appendChild(son);
```

