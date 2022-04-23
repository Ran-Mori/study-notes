# JavaScript



## Introduction

* What is JavaScript
  * JavaScript is a cross-platform, object-oriented scripting language used to make webpages interactive
  * *Client-side JavaScript* extends the core language by supplying objects to control a browser and its *Document Object Model* (DOM).
  * *Server-side JavaScript* extends the core language by supplying objects relevant to running JavaScript on a server.
  * node.js is a more advanced server side versions of JavaScript.
* ECMA script
  * The ECMAScript specification is a set of requirements for implementing ECMAScript.
  * ECMAScript is a Standard for scripting languages such as JavaScript. JavaScript is a language based on ECMAScript.
  * JavaScript supports all functionality outlined in the ECMAScript specification.





## Gramer and types

* basics

  * case-sensitive
  * use Unicode character set
  * simicolon after each statement

* declarations

  * `var` - local or grobal variable
  * `let` - block-scoped variable
  * `const` - block-scoped read-only constant

* evaluate variables

  * no assigned value variable has the type `undefined`
  * `undefined` converts to `false`、`NAN`
  * `null` converts to `0`

* variable scope

  * `var` variable is either global or local in a funtion 
  * `let` and `const ` support block-scoped

* variable hoisting

  * variables in JavaScript are hoisted to the top of the function or statement.
  * `var`  are hoisted return a value of `undefined`. 
  * `let` and `const` are hoisted but not initialized.

* function hoisting 

  * hoisted if defined using function declarations, but not hoisted if defined using function expressions

    ```javascript
    //function declarations
    function calcRectArea(width, height) {}
    ```

    ```javascript
    //function expressions
    const getRectArea = function(width, height) {}
    ```

* constant

  * vairable is constant is not equal to the member or value of this variable is constant.





## Data structures and types

* eight types

  * seven promitives
    * Boolean
    * null 
    * undefined
    * Number - An integer or floating point number.
    * Bigint - An integer with arbitrary precision.
    * String 
    * Symbol
  * one other
    * object - named containers for values

* data type conversion

  * numeric values conver to strings when `+`, numeric values not conver to strings when `other operators`

  * convert string to number

    ```javascript
    parseInt()
    ```

    ```javascript
    parseFloat()
    ```





## literal

* array literal

  * ```javascript
    let array = ["1","2", 1232]
    ```

  * Array literals are also `Array` objects. 

* boolean literal
  * `true`、`false`
  * Boolean object is a wrapper around the primitive Boolean data type.

* numberic literal
  * integer literals in different bases and floating-point literals in base-10.
  * `123.4` - numeric literals should be unsigned. `-123.4` is a plus of `-` and `123.4`

* integer literal
  * Integer and BigInt literals can be written in decimal, hexadecimal, octal and binary.
  * `123456n` - A trailing `n` suffix on an integer literal indicates a BigIntliteral. 

* float point literal
  * parts
    * An unsigned decimal integer
    * A decimal point ("`.`")
    * A fraction (another decimal number)
    * An exponent
  * `3.1415926`、`3.1E+12`

* object literals

  * An object literal is a list of zero or more pairs of property names and associated values of an object, enclosed in curly braces (`{}`).

  * ```javascript
    var car = { myCar: 'Saturn', getCar: carTypes('Honda'), special: sales };
    ```

  * In ES2015, object literals are extended, and it's just like more classed base object.

* string literals

  * both `'` and `"` is ok

  * javaScript automatically converts the string literal to a temporary String object.

  * `template` 

    * enclosed by the back-tick (**`**)

    * ```javascript
      `Hello ${name}, how are you ${time}?`
      ```

