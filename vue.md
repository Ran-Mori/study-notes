# VueLeanring

**************

学习Vue

## vue基础



### 前端四要素

* 逻辑
  * 循环、判断
* 事件
  * 浏览器事件——window、document
  * DOM事件——增、删、修改节点内容、遍历查询节点
  * 事件框架——jQuery
* 视图
  * html, css
* 通信
  * 通信框架——xhr, ajax

******************



### Vue概述

* 构建用户界面的渐进式框架，被设计为自顶向上的逐层应用

*****************

### HelloWorld

* 代码

  * `src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"`是引入vue的资源，就用这个地址，其他容易挂
    * 而且不能用 "/>"简写方式，这种方式也要挂
  * 定义一个Vue对象var app = new Vue
    * el是元素的意思，根据id选择器选择元素
    * data是数据，用键值对存储
  * {{}}是引用js里的数据

  ```HTML
  <!DOCTYPE html>
  <html lang="en">
  <head>
      <meta charset="UTF-8">
      <title>HelloWorld</title>
      <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
      <script>
          window.onload=function(){
              var app = new Vue({
                  el: '#test',
                  data: {
                      message: 'Hello Vue!'
                  }
              })
          };
      </script>
  </head>
  <body>
      <div id="test">
          {{ message }}
      </div>
  </body>
  </html>
  ```

* 三层关系

  * view——就是带{{}}的部分
  * model——就是Vue对象部分
  * view-model——按F12输入代码双向绑定，双向变化

**************



### 语法

* if判断

  * `v-if="ok"`，`v-else`，`v-else-if`表示判断，其中OK是Vue对象中的一个布尔属性值

  ```HTML
  <div id="app-4" v-if="ok">
      {{message}}
  </div>
  ```

  ```javascript
  data: {
      ok:true,
      message: "ok为true才显示"
  }
  ```

* for判断

  * `v-for="todo in todos"`

  ```HTML
  <div id="app-4">
      <ol>
          <li v-for="todo in todos">
              {{ todo.text }}
          </li>
      </ol>
  </div>
  ```

  ```javascript
  data: {
      todos: [
          { text: '学习 JavaScript' },
          { text: '学习 Vue' },
          { text: '整个牛项目' }
      ]
  }
  ```

* 事件

  * "v-on:"可以替换成"@"；"v-on:keyup.enter"

  ```HTML
  <button v-on:click="showAlert()">heoo</button>
  ```

  ```javascript
  methods: {
    showAlert: function () {
          alert(this.message);
    }
  }
  ```

* 双向绑定

  * v-model会忽略所有的value，checked，selected属性而是只将vue作为数据源

  ```HTML
  <body>
      <div id="test">
          <input type="text" v-model="message">{{message}}
      </div>
  </body>
  <script>
      window.onload=function () {
          var vm=new Vue({
              el: "#test",
              data: {
                  message: ""
              }
          })
      }
  </script>
  ```

* v-text指令，和th:text指令是一样的

  ```javascript
  v-text="message+'其他单引号补充内容'"
  ```

* v-html指令，和th:html是一样的

* v-show指令，传一个布尔值，true就显示

* v-blnd指令，设置属性。且v-bind可以省略

  ```javascript
  v-bind:src="isChange?'./imag/test.png':''"
  ```

******************************



## axios



### HelloWorld

* 导入依赖`<script src="https://unpkg.com/axios/dist/axios.min.js"></script>`

* sript代码

  ```JavaScript
  <script>
      var operation1=new Vue({
          el:"#test",
          data:{
              content:""
          },
          methods: {
              buttonClick: function () {
                  // https://autumnfish.cn/api/joke
                  // http://localhost:10001//algorithm/3
                  axios.get('https://autumnfish.cn/api/joke')
                      .then(response=>{console.log(response);this.content=response.data})
                      .catch(error=>{console.log(error)});
              }
          }
      })
  </script>
  ```

* 必须要用{{content}}而不能用v-text

***************

## vue-cli



### 基础简介

* vue-cli : 相当于maven，就是一个工具
* node.js:  javascript的运行环境

***************



### 下载node.js

* 直接下载
* 下载后改变默认module目录。不要放在C盘
* 使用淘宝镜像
* 修改环境变量，移动目录，使cnpm全局可用



### 创建项目

* 使用模板骨架创建项目 **vue  init  webpack-simpe  helloworld-vue**
* 进入工程目录下载依赖 **cnpm  install** 。安装完成后会生成一个module文件夹，用于存放下载的依赖
* 开发模式运行项目  **cnpm  run  dev**  。node.js会提供一个后端运行环境打开8080端口来跑项目

**************



### 文件结构

* **module**   下载的依赖
* **src**  写代码的地方
* **packa.json**  项目的各种配置信息
* **webpack.config.js**   真正的方配置信息
* **index.html**  无论多复杂也只有11行源代码
* **main.js**   是这个项目的入口
* **App.vue**  实际上就是一个vue对象，也被成为vue组件

> 特点：无论页面多复杂始终查看源代码只有10行，真正的代码都在src下

********************



### App.vue介绍

* **<template>**  下面必须有一个根节点标签 ，写HTML
* **script**   写JavaScript
* **style**  写css

***********



### vue组件套vue组件

* 在src/components下创建一个 **header.vue**的组件

  > **name** 属性必须对应class, **el**  才对应ID

  ```HTML
  <template>
      <div class="header">
        <h1>{{message}}</h1>
      </div>
  </template>
  
  <script>
      export default {
        name: "header",
        data() {
          return {
            message: "头部信息"
          }
        },
        methods: {
  
        }
      }
  </script>
  ```

* 在main.js中全局注册组件，拿来当标签一样使用

  ```javascript
  import Header from './components/header'
  
  //全局注册当标签用
  Vue.component('MyHeader',Header)
  ```
  
* 在App.vue中当标签使用

  ```HTML
  <template>
    <div id="app">
      <MyHeader></MyHeader>
      fajgkljsl
    </div>
  </template>
  
  <script>
  export default {
    name: 'app',
    data () {
      return {
        msg: 'Welcome to Your Vue.js App'
      }
    }
  }
  </script>
  ```
  
* 局部注册

  ```HTML
  <script>
    <!--导入-->
    import Footer from "./components/footer"
  
  export default {
    name: 'app',
    data () {
      return {
        msg: 'Welcome to Your Vue.js App'
      }
    },
     <!--引入components-->
    components: {
      "MyFooter":Footer
    }
  }
  </script>
  ```

*******************



### 组件参数传递

* 父组件向子组件传递参数

  * 子组件代码如下

    * 在 **pros** 里面定义了一个参数 parameter，并且在HTML中显示这个参数值

    ```HTML
    <template>
        <div class="pros">
          {{parameter}}
        </div>
    </template>
    
    <script>
        export default {
          name: "pros",
          props: ["parameter"]
        }
    </script>
    ```

  * 父组件代码如下

    * 引入组件 **import Pros from './components/pros'**
    * 把组件本地注入到这个容器组件   **components**
    * 定义想要传递的参数数据   **parameterValue: "父亲的参数值"**
    * 进行双向绑定 **:parameter="parameterValue"**传值

    ```HTML
    <template>
      <div id="app">
        <Pros :parameter="parameterValue"></Pros>
      </div>
    </template>
    
    <script>
      import Pros from './components/pros'
    
    export default {
      name: 'app',
      data () {
        return {
          parameterValue: "父亲的参数值"
        }
      },
      components: {
        "Pros": Pros
      }
    }
    </script>
    ```

  > 子组件在pros里面定义了可以接受的参数，父组件通过在标签中写明参数的键值来传递参数 

* 子组件向父组件传递参数

  * 把上面的传递的参数变成一个传递一个方法。

  * 子组件接住了父组件的方法，然后子组件调用这个方法相当于父组件调用这个方法

  * 可以把子组件理解成接口

    ```json
    props: {
      'btn': {
        type: Function,
        required: true,
        default: function () {}
      }
    }
    ```

* 参数传递简单写法

  * 子组件

    * 子组件对外发射一个信息

    ```HTML
    <template>
        <div class="pros">
          <button @click="doclick">点我</button>
        </div>
    </template>
    
    <script>
        export default {
          name: "pros",
          methods: {
            doclick(){
              this.$emit("key","value")
            }
          }
        }
    </script>
    ```

  * 父组件

    * 子组件的标签中使用@符号接收子组件发射的信息
    * 使用$event的方式获取发射值，并且把值赋给父组件的msg

    ```HTML
    <template>
      <div id="app">
        <Pros @key="msg=$event"></Pros>
        {{msg}}
      </div>
    </template>
    ```

*****************



### 使用axios

* 安装 **cnpm install --save axios vue-axios**

* 在main.js中引入

  ```JavaScript
  import axios from 'axios'
  import VueAxios from 'vue-axios'
  
  Vue.use(VueAxios,axios)
  ```

* App.vue中使用

  ```HTML
  <template>
    <div id="app">
      <form>
        用户名:<input type="text" name="username" v-model="username">
        密码: <input type="password" name="password" v-model="password">
        <input type="submit" value="提交">
      </form>
      <br>
      <button @click="changejoke">切换笑话</button> {{joketest}}
    </div>
  </template>
  
  <script>
  export default {
    name: 'app',
    data () {
      return {
        msg: 'Welcome to Your Vue.js App',
        username: 'IzumiSaki',
        password: '',
        joketest: ''
      }
    },
    methods: {
      changejoke: function () {
          this.axios({
          	method: 'get',
          	url: 'https://autumnfish.cn/api/joke'
              data: {}
        	}).then(respongse=>{this.joketest=respongse.data})
      }
    }
  }
  </script>
  ```

* 页面加载前执行请求

  ```javascript
  create(){ }
  ```

*******************************



### 跨域问题

* 问题简介： 只要端口不同就发生跨域问题

*******************



### 路由

* 作用：可以实现在一个组件中实现不同组件的相互切换

* 安装路由模块 **cnpm  install  vue-router  -g**

* 在src下新建 **router.js** 文件

  ```JavaScript
  //引入vue
  import Vue from 'vue';
  //引入vue-router
  import VueRouter from 'vue-router';
  //第三方库需要use一下才能用
  Vue.use(VueRouter)
  //引用页面
  import RouterPage from "./components/RouterPage";
  
  //定义routes路由的集合，数组类型
  const routes=[
      //单个路由均为对象类型，path代表的是路径，component代表组件
      {path:'/router',component:RouterPage}
  ]
  
  //实例化VueRouter并将routes添加进去
  const router=new VueRouter({
  //ES6简写，等于routes：routes
      routes: routes
  });
  
  //抛出这个这个实例对象方便外部读取以及访问
  export default router
  ```

* 在main.js中导入使用路由模块并注册路由表

  ```JavaScript
  import Vue from 'vue'
  import App from './App.vue'
  
  import router from "./router";
  Vue.config.productionTip = false
  
  new Vue({
    router,
    render: h => h(App),
  }).$mount('#app')
  ```

* App.vue使用

  ```HTML
  <router-link to="/router">router</router-link>
  <router-view></router-view>
  ```

***************



### 路由传递参数

* 通过路由表设置参数。**/:id**代表参数

  ```javascript
  const routes={
      {path:'/router/:id',component:RouterPage}
  }
  ```

* App.vue传递参数

  ```HTML
  <router-link to="/router/3">router</router-link>
  ```

* router.vue界面接收参数

  ```javascript
  data(){
      return {
          parameter: $route.params.id
      }
  }
  ```

********************



### 通过js实现路由跳转

```javascript
btn: function(){
    this.$router.push("/home/2");
}
```

*********************



### style的scope属性

* 如果不加scope属性则默认作用于全局，因此最好加上scope属性

**************



### 静态资源打包问题

* 最后发布的打包版本全部打包成一个 **dist.js** 文件