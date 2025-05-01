# FE

## browser engine

### example

* webkit - it is an **open-source web rendering engine** originally developed by **Apple**
* blink
  * it is still **open-source web rendering engine** developed by Google.
  * it is a fork of webkit from 2013

### why blink fork

1.  Google wanted more control to make optimizations specific to Chrome.
2.  Google removed components (like WebKit2’s multi-process architecture) that were unnecessary for Chrome.
3.  The fork allowed Google to innovate without being constrained by WebKit’s development direction.

### how it works

* input
  * html code, css code, javascript code
* process
  1. parses html into a DOM
  2. parsed css to stylesheets
  3. combines DOM and stylesheets into a Render Tree
  4. perform layout to determine the exact size and position of every element on the page
  5. executes javascript code which can modify DOM and CSS
* output
  * determines what needst to be drawn and where. It generates a series of drawing commands based on the layout and styling
  * it may then call Skia in android webview to really draw the content.

## chromium

### what?

* it is a **open-source browser project** started by Google.

### component

1. Blink
2. V8
3. Cronet
4. Multi-Process Architecture & Sandbox
5. Shell
6. UI, and tab management.
7. ⋯⋯

### Chrome

1. Google’s **proprietary web browser** based on the Chromium project.
2. it is **not open source**
3. It adds proprietary components and features to Chromium, including: Google Sync, Google Brand, Usage Tracking.

## v8

### what?

* V8 is an open-source JavaScript and WebAssembly engine, developed by Google primarily for the Chrome browser. It's written in C++.

### feature

1. it doesn't just interpret the javascript code, it compiles it directly into native machine code using techniques like JIT compilation.
2. Optimized for fast execution.
3. Embeddable: Designed as a library that can be embedded into *any* C++ application. This is the crucial point for Node.js.

### don't have

1. The Document Object Model (DOM) or other web browser APIs (like window, document, fetch *in the browser context*).
2. File system access.
3. Networking capabilities (opening sockets, listening for connections).
4. Operating system interactions.

## node.js

### what?

1. it is an open-source, cross-platform **runtime environment** that allows developers to execute JavaScript code *outside* of a web browser.

### components

1. V8
2. libuv -  a C library providing asynchronous, event-driven I/O capabilities.
3. Node.js Bindings & APIs - A layer of C++ code that "binds" the high-level JavaScript functions developers use in Node.js (like fs.readFile, http.createServer) to the lower-level functionalities provided by libuv and other system libraries. It exposes these capabilities to the JavaScript code running in V8.
4. Node.js Standard Library: a collection of built-in JavaScript modules (http, https, fs, path, os, events, etc.) that provide the core functionality for server-side development and scripting.

### relationship

* v8 is the engine and node.js is the car.