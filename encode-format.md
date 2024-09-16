# encode-format

## unicode

### Characters and Glyphs

* character: it is an abstract representation of the smallest components of written language that have

  semantic value.

* glyph: it represents the shapes that characters can have when they are rendered or displayed.

* unicode defines how characters are interpreted, not how glyphs are rendered.

### Text Element

* code point:  it is an encoded character represented by a number from 0 to 0x10FFFF.
* text element: it is represented by a sequence of one or more encoded code points.

### allocation

#### plane0(BMP)

* range: 0 - 0xFFFF(65535)

* examples of code points

  | position | number | name                               | appearance |
  | -------- | ------ | ---------------------------------- | ---------- |
  | 0x0076   | 118    | LATIN SMALL LETTER V               | v          |
  | 0x04A1   | 1185   | CYRILLIC SMALL LETTER BASHKIR KA   | ҡ          |
  | 0x0F29   | 3881   | TIBETAN DIGIT NINE                 | ༩          |
  | 0x261D   | 9757   | WHITE UP POINTING INDEX            | ☝          |
  | 0x270C   | 9996   | VICTORY HAND                       | ✌          |
  | 0x30BC   | 12476  | KATAKANA LETTER ZE                 | ゼ         |
  | 0x3197   | 12695  | IDEOGRAPHIC ANNOTATION MIDDLE MARK | ㆗         |
  | 0xF929   | 63785  | CJK COMPATIBILITY IDEOGRAPH-F929   | 朗         |
  | 0xFE6B   | 65131  | SMALL COMMERCIAL AT                | ﹫         |

#### plane1(SMP)

* range: 0x10000 - 0x1FFFF(131071)

* examples of code points

  | position | number | name                               | appearance |
  | -------- | ------ | ---------------------------------- | ---------- |
  | 0x10049  | 65609  | LINEAR B SYLLABLE B033 RA3         | 𐁉          |
  | 0x118B6  | 71862  | WARANG CITI CAPITAL LETTER YU      | 𑢶          |
  | 0x1F232  | 127538 | SQUARED CJK UNIFIED IDEOGRAPH-7981 | 🈲          |

#### other

* range: 0x20000 - 0x10FFFF (1114111)
* max binary - `00010000 11111111 11111111`

## utf-32

* feature

  1. Each Unicode code point is represented directly by a single 32-bit code unit. 

  2. it is a fixed-width character encoding form.

  3. it may be a preferred encoding form where memory or disk storage space for characters is not a particular concern.

* Bit Distribution

  | Scalar Value                        | One part                            |
  | ----------------------------------- | ----------------------------------- |
  | 00000000 000uuuuu xxxxxxxx xxxxxxxx | 00000000 000uuuuu xxxxxxxx xxxxxxxx |

* decode

  ```kotlin
  /**
   * src: 输入待解码的字节数组
   * index: 输入的一个随机索引
   * @return unicode code point
   */
  fun decodeUtf32(src: ByteArray, index: Int): Long {
      val indexForDecode = index and 0xFFFC // utf-32解码一定要从4的整数开始解
  
      val firstByteValue = 0L // 首字节全是0根本没用
      val secondByteValue = src[indexForDecode + 1].toLong().shl(16) // 第二字节，移2byte(16)位
      val thirdByteValue = src[indexForDecode + 2].toLong().shl(8) // 第三字节，移byte(8)位
      val fourthByteValue = src[indexForDecode + 3].toLong() // 第四字节，直接取值
  
      return firstByteValue + secondByteValue + thirdByteValue + fourthByteValue
  }
  ```

## utf-16

* Bit Distribution

  > 1. wwww = uuuuu - 1
  > 2. 11011000 00000000 = 0xD800 = 55296
  > 3. 11011100 00000000 = 0xDC00 = 56320
  > 4. surrogate code point: 0xD800 - 0xDFFF
  > 5. 0xD800 <= 110110ww wwxxxxxx < 0xDC00 <= 110111xx xxxxxxxx < 0xDFFF

  | Scalar Value               | First part        | Second part       |
  | -------------------------- | ----------------- | ----------------- |
  | 00000000 xxxxxxxx xxxxxxxx | xxxxxxxx xxxxxxxx |                   |
  | 000uuuuu xxxxxxxx xxxxxxxx | 110110ww wwxxxxxx | 110111xx xxxxxxxx |

* decode

  ```kotlin
  /**
   * src: 输入待解码的字节数组
   * index: 输入的一个随机索引
   * @return unicode code point
   */
  fun decodeUtf16(src: ByteArray, index: Int): Long {
      val index2Align = index and 0xFFFE // 因为2字节是一个整体，首先保证index只能是0, 2, 4, 6, ...
      val codeUnitValue = src.get(index2Align).toLong().shl(8) + src.get(index2Align + 1) // 高8位要进行shl(8)
      return if (codeUnitValue < 0xD800 || codeUnitValue > 0xDFFF) {
          // 使用one code unit编码
          decodeTwoByteUtf16(src.get(index2Align), src.get(index2Align + 1))
      } else if (0xD800 <= codeUnitValue && codeUnitValue < 0xDC00) {
          // 使用2个code unit，当前code unit是高16位
          decodeFourByteUtf16(src.get(index2Align), src.get(index2Align + 1), src.get(index2Align + 2), src.get(index2Align + 3))
      } else {
          // 使用2个code unit，当前code unit是低16位
          decodeFourByteUtf16(src.get(index2Align - 2), src.get(index2Align - 1), src.get(index2Align), src.get(index2Align + 1))
      }
  }
  
  
  private fun decodeTwoByteUtf16(first: Byte, second: Byte): Long {
      val firstValue = first.toLong().shl(8) // 直接移8位
      val secondValue = second.toLong() // 什么都不用操作
      return firstValue + secondValue
  }
  
  private fun decodeFourByteUtf16(first: Byte, second: Byte, third: Byte, fourth: Byte): Long {
      val frontW = first.and(0x03).toLong().shl(2) //只有低2位有效，左移2位
      val endW = second.toLong().shr(6) // 只要高2位
      val allW = frontW + endW
      val allUValue =  (allW + 1).shl(16) // 需要 + 1， 左移16位
  
      val secondValue = second.toLong().and(0x3F).shl(10) // 只有低6位有用，左移10位
      val thirdValue = third.toLong().and(0x03).shl(6) // 只有低2位有用，左移8位
      val fourthValue = fourth.toLong() // 直接用
      return allUValue + secondValue + thirdValue + fourthValue
  }
  ```

## utf-8

* Bit Distribution

  | Scalar Value               | First Byte | Second Byte | Third Byte | Fourth Byte |
  | -------------------------- | ---------- | ----------- | ---------- | ----------- |
  | 00000000 00000000 0xxxxxxx | 0xxxxxxx   |             |            |             |
  | 00000000 00000yyy yyxxxxxx | 110yyyyy   | 10xxxxxx    |            |             |
  | 00000000 zzzzyyyy yyxxxxxx | 1110zzzz   | 10yyyyyy    | 10xxxxxx   |             |
  | 000uuuuu zzzzyyyy yyxxxxxx | 11110uuu   | 10uuzzzz    | 10yyyyyy   | 10xxxxxx    |

* decode

  ```kotlin
  /**
   * src: 输入待解码的字节数组
   * index: 输入的一个随机索引
   * @return unicode code point
   */
  fun decodeUtf8(src: ByteArray, index: Int): Long {
      val indexedByte: Byte = src.get(index)
      val firstByteValue = indexedByte.toInt().shr(7) // 第一个bit位
      if (firstByteValue == 0) {
          // 如果第一个bit位为0，则肯定是只使用one byte的ASCII编码
          return decodeOneByteUtf8(indexedByte)
      } else {
          // 向前最多找3个，则一定能找到解码起点
          var indexOfDecode = 0
          for (i in 0 until 4) {
              if (src.get(index - i).and(0x40) != 0.toByte()) {
                  indexOfDecode = index - i
                  break
              }
          }
          val firstByte = src.get(indexOfDecode)
          if (firstByte >= 0xF0) {
              // 高4位全是1，用4字节编码
              return decodeFourByteUtf8(src.get(indexOfDecode), src.get(indexOfDecode + 1), src.get(indexOfDecode + 2), src.get(indexOfDecode + 3))
          } else if (firstByte >= 0xE0) {
              // 高3位全是1，用3字节编码
              return decodeThreeByteUtf8(src.get(indexOfDecode), src.get(indexOfDecode + 1), src.get(indexOfDecode + 2))
          } else  {
              // 高2位全是1， 用2字节编码
              return decodeTwoByteUtf8(src.get(indexOfDecode), src.get(indexOfDecode + 1))
          }
      }
  }
  
  
  private fun decodeOneByteUtf8(byte: Byte): Long {
      return byte.toLong()
  }
  
  private fun decodeTwoByteUtf8(first: Byte, second: Byte): Long {
      val firstValue = first.toLong().and(0x1F).shl(6) // 只有低5位有用，左移6位
      val secondValue = second.toLong().and(0x3F) // 只有低6位有用，不用移位
      return firstValue + secondValue
  }
  
  private fun decodeThreeByteUtf8(first: Byte, second: Byte, third: Byte): Long {
      val firstValue = first.toLong().and(0xF).shl(12) // 只有低4位有用，左移12位
      val secondValue = second.toLong().and(0x3F).shl(6) // 只有低6位有用，左移6位
      val thirdValue = third.toLong().and(0x3F) // 只有低6位有用，不用移位
      return firstValue + secondValue + thirdValue
  }
  
  private fun decodeFourByteUtf8(first: Byte, second: Byte, third: Byte, fourth: Byte): Long {
      val firstValue = first.toLong().and(0x7).shl(18) // 只有低3位有用，左移18位
      val secondValue = second.toLong().and(0x3F).shl(12) // 只有低6位有用，左移12位
      val thirdValue = third.toLong().and(0x3F).shl(6) // 只有低6位有用，左移6位
      val fourthValue = third.toLong().and(0x3F) // 只有低6位有用，不用移位
      return firstValue + secondValue + thirdValue + fourthValue
  }
  ```

  







