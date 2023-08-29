# Http

## URL与资源

### URL

* 通用格式

  ```
  <scheme>://<user>:<password>@<host>:<port>/<path>;<params>?<query>#<frag>
  ```

* params

  ```
  http://www.joes-hardware.com/hammers;sale=false/index.html;graphics=true
  ```

  * `sale=false`是参数
  * `graphics=true`是参数

* frag

  ```
  http://www.joes-hardware.com/tools.html#drills
  ```

  * server don't use the frag value, but browser uses it to scroll to the specific position

### 字符

* url字符集 - `ASCII`

* 转义

  ```
  https://www.google.com?surl=https%3A%2F%2Fcn.bing.com
  ```

  * [HTML URL Encoding Reference](https://www.w3schools.com/tags/ref_urlencode.ASP)
    * space -> `%20`
    * & -> `%26`

* 字符限制

  * 保留字符在用于保留用途之外时，要在URL中对其进行编码
  * `%, /, ., ? ……`


***

## 报文

### 组成

1. start line
2. headers
3. blank line `(ASCII-13 + ASCII-10)`
4. body

### 方法

* 安全方法 - 不会对server产生影响的方法，如`get`，`post`就不是安全方法
* head方法
  * 与get一样，只不过只返回header不返回body
  * 允许client在未获取实际资源的情况下，对资源首部进行检查
* trace方法
  * 允许client在最终请求发送给服务器时，看看此时的请求报文和发出的请求报文的差别
* option方法
  * 允许client请求服务端告知其支持的各种功能
* 拓展方法
  * 没在http/1.1规范中定义的方法

### 状态码

* 100 - 199

  * 101 - Switching Protocols

    ```http
    GET wss://tch781577.tch.poe.com/up/chan53-8888/ HTTP/1.1
    Connection: Upgrade
    Cache-Control: no-cache
    Upgrade: websocket
    Sec-WebSocket-Version: 13
    Sec-WebSocket-Key: bl28k7TBI4f3+1436GawSw==
    ```

    ```http
    HTTP/1.1 101 Switching Protocols
    Upgrade: websocket
    Connection: Upgrade
    Sec-WebSocket-Accept: mrRV46bnpzdAFQWy7ZtoC+lLa2I=

* 200 - 299成功

  * 202 Accepted - 请求已被接受，但服务器还未对其执行任何动作。

  * 204 No Content - 只有first line和header，无body。常见于post请求

  * 206 Partial Content - 成功执行一个部分或Range请求。必须包含Content-Range, Date, ETag, Content-Location等header

    ``` http
    HTTP/1.1 206 Partial Content
    ETag: "a1778cfae31830a659a49e1e433ae1f7"
    Content-Range: bytes 0-2538306/2538307
    ETag: "a1778cfae31830a659a49e1e433ae1f7"
    ```

* 300 -399重定向

  * 301 - Move Permanently

  * 302- Moved Temporarily

    ```http
    HTTP/1.1 302 Moved Temporarily
    Location: https://50d17fc3613a967045b95ade6aa1c2cf.free.com
    ```

  * 303 - See Other. 可以用于负载均衡

  * 304 - Not Modified. 客户端的缓存依旧有效，可以继续用

  * 305 - Use Proxy. 用来说明必须通过一个代理来访问资源;代理的位置由 Location 首部给出。

* 400 - 499客户端错误

  * 400 - Bad Request. 服务端告知客户端它发送了一个错误的请求
  * 401 - Unauthorized. 
  * 403 - Forbidden. 请求被服务端拒绝了
  * 404 - Not Found. 服务器无法找到所请求的Url.
  * 405 - Method Not Allowed
  * 406 - Not Acceptable. 服务器没有与客户端可接受的URL相匹配的资源

* 500 - 599 服务端错误

  * 500 - Internal Server Error. 服务器遇到一个妨碍它为请求提供服务的错误
  * 502 - Bad Getway. 作为代理或网关使用的服务器从请求响应链的下一条链路上收到了 一条伪响应(比如，它无法连接到其父网关)


### 首部

* 分类
  1. 通用首部
  2. 请求首部
  3. 响应首部
  4. 实体首部 - 用于说明body部分的header
  5. 拓展首部

***

## 连接管理

### 性能聚焦

* tcp连接建立握手
  * http程序员看不到tcp连接的过程，对其只暴露创建tcp连接的时延
  * 小的http事务，很可能会花超过50%的时间来建立tcp连接，真正传输数据反而用不到什么时间
* 用于捎带确认的tcp延迟确认算法
* tcp慢启动拥塞控制
* 数据聚集的Nagle算法
* TIME_WAIT时延和端口耗尽

### 优化

* 并行连接

  * 一个客户端并行建立多条tcp连接

* 持久连接

  * 在http事务结束之后，保持tcp连接为打开状态，以便未来重用。

  * 持久连接还能避免慢启动和拥塞适应阶段

  * http1.0 Keep-Alive

    ```http
    Connection: Keep-Alive
    Keep-Alive: max=5, timeout=120
    ```

    * 如果客户端没有发送`Connetion: Keep-Alive`，服务端就会在事务结束后自动关闭连接
    * 代理和逐跳首部 - 代理不能向下一级转发`Connection`首部，不然会有哑代理问题

  * http1.1

    * 1.1中所有连接默认是持有连接，除非带上`Connection: close`，否则连接不会关闭

* 管道化连接

  * 不必等待上一条事务返回，就发送下一条事务
  * 只有持久连接，才能用管道
  * 不应以管道化连接的方式发送非幂等请求

* 复用的连接

***

## 代理

### 代理与网关

* 代理连接的是两个或多个使用`相同`协议的端点
* 网关连接的是两个或多个使用`不同`协议的端点

### 为什么使用代理

1. 儿童过滤器
2. 访问权限控制
3. 防火墙
4. web缓存
5. 反向代理
   * 正向代理 - 代理代理了客户端，如翻墙软件
   * 反向代理 - 代理代理了服务端，如负载均衡
6. 内容路由器
7. 转码器
   * 压缩图片，压缩文本，更改语言
8. 匿名访问
   * 代理删除客户端IP，From，Referer，Cookie



