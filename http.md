# Http

## URL与资源

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

  * 407 - Proxy Authorization Required. 访问此代理需要证书

    ```http
    HTTP/1.0 407 Proxy Authorization Required
    Proxy-Authenticate: Basic realm="Secure Stuff"

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

### 代理部署

1. 部署在本地网络的出口，控制和过滤所有出口流量
2. 部署在ISP访问入口点上，用来当缓存
3. 反向代理部署在网络边缘，假装当服务器
4. 网络交换代理，部署在因特网交换点上，当路由器的缓存

### 代理获取流量

* 修改客户端 - 浏览器客户端预留了设置，用户可以手动设置代理，设置后流量就不会自动直接流向目的服务器，而是流向代理
* 修改网络 - 在客户端毫不知情的情况下，监视流量交换及路由的物理设备，进行拦截，这种代理被称为拦截代理
* 修改DNS命名空间 - 直接改DNS查询的返回
* 修改web服务器 - 返回305重定向强制让客户端访问代理服务器

### URI解析

* 没有代理时，请求的URL是域名，会通过DNS进行查找，如果输入`google`来查询DNS返回错误，浏览器一般会自动拓展，再一次输入`www.google.com`进行DNS查询返回正确结果
* 但设置了显示代理时，请求的URL显示设置了IP地址如`http://127.0.0.1:7080`，这个时候就不会通过DNS查询，也就没有了浏览器自动拼接的这个feature

### Via header

* 行为：报文每经过一个节点，都必须将这个节点条件到`Via`的尾部

* 示例：

  ```http
  Via: 1.1 proxy-62.irenes-isp.net, 1.0 cache.joes-hardware.com
  ```

* Via会记录网关的协议转换

  ```http
  Via: FTP/1.0 proxy.irenes-isp.net (Traffic-Server/5.0.1-17882 [cMs f ])
  ```

* server应该添加自己到Server首部，不能修改Via首部

  ```http
  Server: Apache/1.3.14 (Unix) PHP/4.0.4

* Via也需要注意隐私，比如防火墙后面的网络结构有代理，但防火墙后的网络结构应该对客户端透明

### other

* `Max-Forwards` header 限制了最大的代理转发次数

***

## 缓存

### 解决的问题

1. 冗余的数据传输 - 同一个内容传很多遍
2. 带宽瓶颈
3. 瞬间拥塞 - 大量访问同一个资源
4. 距离延迟 - 近的地方访问快，远的地方访问慢

### 方式

1. 缓存命中 - 客户端 -> 读缓存 -> 客户端
2. 缓存不命中 - 客户端 -> 读缓存 -> 服务端 -> 写缓存 -> 客户端
3. 缓存再验证命中(304 status_code) - 客户端 -> 服务器 -> 读缓存 -> 客户端

### 命中率

1. 缓存命中率 - 以文档来做统计，容易受文档大小而影响准确性
2. 字节命中率 - 用流量来做统计，更精确

### 类型

1. 私有缓存 - 仅供单个用户使用，比如浏览器内的缓存
2. 公有缓存 - 一般是代理缓存服务器
3. 网状缓存 - 动态决策，更加复杂

### 处理步骤

1. 接收 - 从网络中读取抵达的请求报文
2. 解析 - 对请求报文进行解析，提取URL和各种首部
3. 查询 - 查询是否有缓存，如果没有，就从服务端请求，然后存入缓存
4. 新鲜度检测 - 查看缓存是否足够新，如果不够新，就询问服务器是否有更新
5. 创建响应 - 304响应或者200响应

### 文档新鲜

* Expires和Cache-Control

  * Expires是http1.0的，Cache-Control: max-age是http1.1的

  * 它们两个本质上做的事是一模一样的

  * Cache-Control使用的是相对时间，而不是计算机本地的绝对时间，更准确，因此倾向于使用它

  * Cache-Control

    * max-age 值定义了文档的最大使用期——从第一次生成文档到文档不再新 鲜、无法使用为止，最大的合法生存时间(以秒为单位)

    ```http
    HTTP/1.0 200 OK
    Cache-Control: max-age = 484200
    ```

  * Expires

    * 指定一个绝对的过期时间，如果过期时间已经过了，就说明文档不再新鲜了

      ```http
      HTTP/1.1 200 OK
      Expires: Fri, 05 Jul 2002, 05:00:00 GMT
      ```

* 再验证

  * 只有不再新鲜了才会再验证，如果依旧新鲜，则不会请求服务端再验证

* If-Modified-Since

  * send a request

    ```http
    Get http://www.cn.bing.com HTTP/1.0
    If-Modified-Since: Sat, 29 Jun 2002, 14:30:00 GMT
    ```

  * get 304 response

    ```http
    HTTP/1.0 304 Not-Modified
    Date: Wed, 03 Jul 2002, 19:18:23 GMT
    Expires: Fri, 05 Jul 2002, 14:30:00 GMT // return Expires

  * get 200 response

    ```http
    HTTP/1.0 200 OK
    Date: Fri, 05 Jul 2002, 17:54:40 GMT
    Content-type: text/plain
    Content-length: 124
    Expires: Mon, 09 Sep 2002, 05:00:00 GMT // return Expires
    
    All exterior house paint on sale through
    Labor Day. Just another reason for you
    to shop this summer at Joe's Hardware!
    ```

* If-None-Match

  * why need this instead of If-Modified-Since

    1. 周期性写入内容，但最初和最终是一样的
    2. 文档被修改了，但修改内容无关紧要
    3. 有些服务器无法准确判断文档最后的修改时间
    4. 以秒为单位不能应付亚秒的情况，即1s内文件内容发生变更

  * ETag - 可能包含文档序列号或版本号，或者文档内容的校验或者其他信息

  * send a request

    ```http
    GET /announce.html HTTP/1.0
    If-None-Match: "v2.6"
    ```

  * get 304 response

    ```http
    HTTP/1.0 304 Not Modified
    Date: Wed, 03 Jul 2002, 19:18:23 GMT
    ETag: "v2.6" // return etag
    Expires: Fri, 05 Jul 2002, 05:00:00 GMT
    ```

* 与原则

  * 当客户端请求同时带上`If-Modified-Since`和`ETag`标签时
  * 当且仅当两个条件都满足时，server才能返回304 response


### 控制缓存

* 响应Cache-Control: no - store
  * 禁止缓存
* 响应Cache-Control: no - cache
  * 实际是可以把缓存存储在本地的，只是在与原始服 务器进行新鲜度再验证之前，缓存不能将其提供给客户端使用。
  * 这个首部使用 do-not-serve-from-cache-without-revalidation 这个名字会更恰当一些
* 响应Cache-Control: max-age = 3600
  * 从服务器将文档传来之时起，可以认为此文档处于新鲜的秒数
* 响应Expires
  * 不推荐使用，不同的设备时钟可能设置不同，更推荐使用过期秒数而不是绝对时刻
* 响应Cache-Control: must-revalidate
  * 即使文档现在处于新鲜状态，但依旧要进行再验证
* 请求Cache-Control:max-age = 3600
  * 缓存无法返回缓存时间长于 3600 秒的文档。
* 请求Cache-Control: no-cache
  * 除非文档进行了再验证，否则客户端不会接受缓存
* 请求Cache-Control: only-if-cached
  * 只有当缓存中有副本存在时，客户端才会获取一份副本

### 缓存与广告

* 现状：如果广告是按照请求服务器次数计费的话，设计得足够好的缓存会让服务器根本收不到请求
* 解决方式
  1. 响应上加上no-cache，强制要求再验证
  2. 重写广告的url
  3. 使用缓存，但缓存服务器要告诉广告方有多少命中

***

## 集成点

### 网关

* 描述： `<客户端协议>/<服务端协议>`，如`HTTP/NNTP`
* 两侧
  1. 服务端网关: 通过HTTP与客户端通信，通过其他协议与服务端通信
  2. 客户端网关: 通过其他协议与客户端通信，通过HTTP协议与服务端通信

### 隧道

* 作用: 通过http发送非http流量。这样就能绕过只允许web流量通过的防火墙

* The CONNECT method requests that the recipient establish a tunnel to the destination origin server identified by the request-target and, if successful, thereafter restrict its behavior to blind forwarding of packets, in both directions, until the tunnel is closed.

* [http_tunnel wike](https://en.wikipedia.org/wiki/HTTP_tunnel); [rfc7231](https://datatracker.ietf.org/doc/html/rfc7231#section-4.3.6)

* Connect请求

  ```http
  Connect ome.netscape.com:443 HTTP/1.0
  ```

  ```http
  HTTP/1.0 200 Connection Established
  ```

* 为什么存在？ - 如果只有客户端和服务端两个通信端点，则完全可以不需要建立隧道。因为客户端和服务端都实现了`http, tls`协议，它们已经完全可以做到网络栈信息自上而写，自下而上传输。但网络中通常包含很多的代理和中间节点，代理节点不可能解密tls，不然数据就不安全了，因此代理唯一能够做的事情就是在更底层的协议中将`tcp`流量直接转发。相当于代理不需要管上层的http和tls

### https

* TLS (Transport Layer Security) operates between the TCP (Transmission Control Protocol) layer and the HTTP (Hypertext Transfer Protocol) layer in the network stack.
* On the client side, the data is passed from the HTTP layer to the TLS layer, while on the server side, the data is passed from the TLS layer to the HTTP layer.
* TLS is the successor and evolution of SSL. TLS was developed as a replacement for SSL to address security vulnerabilities and improve upon its functionality.

### 中继

* 中继(relay)是没有完全遵循http规范的简单http代理。
* Http很复杂，所以实现基本的代理功能对流量进行盲转发，而且不执行任何首部和方法逻辑，有时是很有用的。盲中继很容易实现，所以有时会提供简单的过滤、诊断或内容转换功能。
* 中继由于对`Connection: keep-alive`进行盲转发可能会导致很多问题。

***

## Cookie

### 个性化接触

* 目的
  1. 个性化的问候
  2. 有的放矢的推荐
  3. 管理信息的存档
  4. 记录会话
* 方式
  1. HTTP首部
  2. IP地址追踪
  3. 用户登录，用认证来识别用户
  4. 在URL中嵌入识别技术

### HTTP首部

* From -> 用户的邮件地址
* Reffer -> 用户从那个页面跳转过来的
* Authorization -> 用户名和密码
* Cookie -> Cookie
* X-Forwarded-For -> 客户端的ip地址，因为有代理，因此这个代表源客户端的ip地址

### fat url

* 动态url，在url中包含用户的特定信息
* 缺点
  1. url丑陋
  2. 无法共享url，因为包含个人信息
  3. 破坏缓存
  4. 服务器负担加重
  5. 非持久

### Cookie

* 是识别用户，实现持久会话的最好方式

* 分类

  * 会话cookie - 存在内存中，离开浏览器就会删除。设置了Discard，或者没有设置Expires或Max-Age就是会话cookie
  * 持久cookie - 存在磁盘中，离开浏览器也不会删除

* 示例

  ```http
  HTTP/1.0 200 OK
  Set-Cookie: csrf_session_id=563b2d84281ea5cd70c901502e9348e8;gateway_sid=5f1bab6ac77d8a39b265c8602d4ea930;tech=tech-user
  ```

  ```http
  GET https://www.cn.bing.com HTTP/1.0
  Cookie: csrf_session_id=563b2d84281ea5cd70c901502e9348e8; gateway_sid=5f1bab6ac77d8a39b265c8602d4ea930;tech=tech-user
  ```

* 规范

  * Set-Cookie: name=value [; expires=date] [; path=path] [; domain=domain] [; secure]
  * Cookie: name1=value1 [; name2=value2] ...

* 缓存

  * `Cache-Control:no-cache="Set-Cookie"`，除了Set-Cookie外的首部是可以缓存的
  * 服务端收到带有cookie的请求时，这次的相应就是私有的，一般情况下不建议进行缓存了

***

## 基本认证机制

### 过程示意

```http
GET /family/jeff.jpg HTTP/1.0
```

```http
HTTP/1.0 401 Authorization Required 
WWW-Authenticate: Basic realm="Family"
```

```http
GET /family/jeff.jpg HTTP/1.0 
Authorization: Basic YnJpYW4tdG90dHk6T3ch
```

```http
HTTP/1.0 200 OK 
Content-type: image/jpeg ...<image data included>
```

### 缺陷

1. 通过网络发送用户名和密码，且编解码方式简单
2. 及时破解很难，也能拦截获取，以后每次重复发送
3. 假冒服务器非常容易

***

## 安全的Http

### 基本要求

1. 服务器认证。客户端知道它在与真正的服务器进行通话
2. 客户端认证。服务器知道它在与真正的客户端进行通过
3. 完整性。数据不会被修改
4. 加密。不能明文传输
5. 效率。算法运行要足够快
6. 普适性。基本上所有的客户端和服务端都要支持

### 对称加密

* 加密密钥和解密密钥是同一个
* 服务器端会维持N个解密密钥，较为复杂
* 常见算法：DES、Triple-DES、RC2 和 RC4

### 非对称密钥

* 加密密钥(公钥)有很多个，但解密密钥(私钥)只有一个
* 服务端只用维持一个解密密钥即可
* 公开密钥非对称加密要满足知道下面信息后，依旧无法破解
  1. 公钥
  2. 一段密文
  3. 一段明文，和这段明文对应的密文

### https

* TLS自身不会去检验服务器证书，证书的检验是浏览器做的
* TLS只是一个协议，OpenSSL是它的一个开源实现

***

## 实体和编码

### 报文和实体

* 描述报文的首部
  * Content-Location -> 一个备用位置，请求时可通过它获得对象
  * Content-Range -> 如果这是部分实体，这个首部说明它是整体的哪个部分
  * Content-MD5 -> 实体主体内容的校验和
  * Allow -> 该资源所允许的各种请求方法，例如，GET 和 HEAD

* 多部分表格提交

  ```http
  POST /upload HTTP/1.1
  Host: example.com
  Content-Type: multipart/form-data; boundary=---------------------------1234567890
  
  -----------------------------1234567890
  Content-Disposition: form-data; name="name"
  
  John Doe
  -----------------------------1234567890
  Content-Disposition: form-data; name="file"; filename="example.jpg"
  Content-Type: image/jpeg
  
  (Binary file data here)
  -----------------------------1234567890--
  ```

### 范围请求

* 示例

  ```http
  GET /bigfile.html HTTP/1
  ```

  ```http
  HTTP/1.1 200 0k // 200 说明这时候还不是部分请求
  Content-type: text/html 
  Content-length: 65327
  Accept-ranges: bytes // 表示服务端支持range请求
  ```

  ```http
  GET /bigfile.html HTTP.1.1  
  Range: bytes=20224- // 客户端请求20224字节之后的内容
  ```

  ```http
  HTTP/1.1 206 partial // 206部分返回说明是部分请求
  Content-Range: bytes=20224- //返回的内容是20224之后
  Accept-ranges: bytes
  ```


***

## 国际化

### 字符集和编码

* Content-Type

  ```hit
  Content-Type: text/html; charset=utf-8
  ```

  * 客户端(浏览器)将使用`utf-8`编码格式，将http文本二进制内容转换成人能看懂的内容

### 字符编码方案

* 固定宽度 - 如ASCII、iso-8859家族
* 可变宽度(无模态) - 如utf-8
* 可变宽度(有模态) - 如iso-2000-jp，它有几个转义序列，可以切换不同的字符集

### language tag

* 标准语言
  * 英语 -> en
  * 汉语 -> zh
  * 韩语 -> ko
* 变种方言
  * 美式英语 -> en-US
  * 台湾汉语 -> zh-TW

### URI字符集合

* 限制

  * URI中允许且仅允许出现ASCII字符

* ASCII字符分类

  1. 未保留字符 - [A-Za-z0-9] | "-" | "_" | "." | "!" | "~" | "*" | " ' " | "(" | ")"
     * 随便用
  2. 保留字符 - ";" | "/" | "?" | ":" | "@" | "&" | "=" | "" | "$" | ","
     * 都有特殊含义，不能随便用。在用于保留用途之外时，要在URL中对其进行编码
  3. 转义字符 - "%" <HEX> <HEX>
     * 用于转义

* 转义

  ```
  https://www.google.com?surl=https%3A%2F%2Fcn.bing.com
  ```

  * [HTML URL Encoding Reference](https://www.w3schools.com/tags/ref_urlencode.ASP)
    * space -> `%20`
    * & -> `%26`

***

## 内容协商

### 协商方式

1. 客户端驱动协商
   * 需要两次请求才能获取最终的内容
   * 每一个choice的URL不同，可能为`/english`、`/chinese`
   * 两种方式。一个html文档内有多个不同版本的URL，一个300 Multiple Choices的响应用户自己选。但两种方式的体验都不好
2. 服务端驱动协商
   * 内容协商首部集 - Accept-XXX
   * 内容协商质量值 - `Accept-Language: en;q=0.5, fr;q=0.0, nl;q=1.0, tr;q=0.0`
3. 代理驱动协商
   * Vary首部。server返回带有Vary首部，代理缓存这个response，当客户端下次请求时，当且仅当Vary首部与请求首部匹配时才用缓存
   * `Vary: User-Agent, Cookie`

### 转码

