import Big from 'big.js'

import DmlParseError from './error/DmlParseError.js'
import DmlArray from './types/DmlArray.js'
import DmlBoolean from './types/DmlBoolean.js'
import DmlComment from './types/DmlComment.js'
import DmlKey from './types/DmlKey.js'
import DmlNumber from './types/DmlNumber.js'
import DmlObject from './types/DmlObject.js'
import DmlString from './types/DmlString.js'

class DmlParser {
  #dmlString
  #line
  #column
  #pointer
  constructor(dmlString) {
    this.#dmlString = dmlString
    this.#line = 1
    this.#column = 1
    this.#pointer = 0
  }

  #step(count = 1) {
    for (let i = 0; i < count; i++) {
      if (this.#currentChar() == '\n') {
        this.#column = 0
        this.#line++
      }
      this.#column++
      this.#pointer++
    }
  }

  #stepNextNonWhitespace() {
    while (this.#pointer < this.#dmlString.length && this.#isWhitespace(this.#currentChar())) {
      this.#step()
    }
  }

  #nextNonWhitespace(pointer) {
    while (pointer < this.#dmlString.length && this.#isWhitespace(this.#charAt(pointer))) {
      pointer++
    }

    return pointer
  }

  #charAt(pointer) {
    return this.#dmlString.charAt(pointer)
  }

  #currentChar() {
    return this.#dmlString.charAt(this.#pointer)
  }

  #nextChar() {
    return this.#dmlString.charAt(this.#pointer + 1)
  }

  #isCommentStart(c, n) {
    return c === '/' && (n === '/' || n === '*')
  }

  #isMultilineCommentEnd(c, n) {
    return c === '*' && n === '/'
  }

  #isWhitespace(c) {
    return /\s/.test(c)
  }

  #isDigit(c) {
    return /[0-9]/.test(c)
  }

  #isNewline(c) {
    return c === '\n'
  }

  #isKey(c) {
    return /[a-zA-Z0-9-_+]/.test(c)
  }

  #parseComment() {
    let comment = ''
    this.#stepNextNonWhitespace()

    while (this.#isCommentStart(this.#currentChar(), this.#nextChar())) {
      switch (this.#nextChar()) {
        case '/': {
          this.#step(2)
          let start = this.#pointer
          while (!this.#isNewline(this.#currentChar())) {
            this.#step()
          }
          comment += this.#dmlString.substring(start, this.#pointer)
          break
        }
        case '*': {
          this.#step(2)
          let start = this.#pointer
          while (!this.#isMultilineCommentEnd(this.#currentChar(), this.#nextChar())) {
            this.#step()
          }
          comment += this.#dmlString.substring(start, this.#pointer)
          this.#step(2)
          break
        }
      }
    }

    return new DmlComment(comment)
  }

  #parseBoolean(comment) {
    if (this.#dmlString.substring(this.#pointer, this.#pointer + 4).toLowerCase() === 'true') {
      this.#step(4)
      return new DmlBoolean(true).withComment(comment)
    }
    if (this.#dmlString.substring(this.#pointer, this.#pointer + 5).toLowerCase() === 'false') {
      this.#step(5)
      return new DmlBoolean(false).withComment(comment)
    }
    throw new DmlParseError(
      this.#line,
      this.#column,
      'true or false',
      this.#dmlString.substring(this.#pointer, this.#pointer + 4)
    )
  }

  #parseNumber(comment) {
    this.#stepNextNonWhitespace()
    let n = this.#pointer
    let negative = this.#currentChar() === '-'
    if (negative) {
      this.#step()
    }
    let whole = ''
    let fraction = '0'
    let exponent = '0'

    let start = this.#pointer
    while (this.#isDigit(this.#currentChar())) {
      this.#step()
    }
    whole = this.#pointer > start ? this.#dmlString.substring(start, this.#pointer) : '0'
    if (this.#currentChar() == '.') {
      this.#step()
      start = this.#pointer
      while (this.#isDigit(this.#currentChar())) {
        this.#step()
      }
      fraction = this.#pointer > start ? this.#dmlString.substring(start, this.#pointer) : '0'
    }
    if (this.#currentChar() === 'e') {
      this.#step()
      start = this.#pointer
      while (this.#isDigit(this.#currentChar())) {
        this.#step()
      }
      let nexp = this.#currentChar() === '-'
      if (nexp) {
        this.#step()
      }
      exponent = (nexp ? '-' : '') + this.#pointer > start ? this.#dmlString.substring(start, this.#pointer) : '0'
    }
    let b = new Big(whole + '.' + fraction)
    if (negative) {
      b = b.times(-1)
    }
    b = b.pow(parseInt(exponent))
    return new DmlNumber(b, this.#dmlString.substring(n, this.#pointer)).withComment(comment)
  }

  #parseString(comment) {
    this.#stepNextNonWhitespace()
    switch (this.#currentChar()) {
      case '\'': {
        let start = this.#pointer
        let didEscape = false
        do {
          didEscape = this.#currentChar() === '\\'
          this.#step()
        } while (this.#currentChar() !== '\'' || didEscape)
        this.#step()
        return new DmlString(this.#dmlString.substring(start + 1, this.#pointer - 1).replace('\\\'', '\''))
          .withComment(comment)
      }
      case '"': {
        let start = this.#pointer
        let didEscape = false
        do {
          didEscape = this.#currentChar() === '\\'
          this.#step()
        } while (this.#currentChar() !== '"' || didEscape)
        this.#step()
        return new DmlString(this.#dmlString.substring(start + 1, this.#pointer - 1).replace('\\"', '"'))
          .withComment(comment)
      }
      default:
        throw new DmlParseError(this.#line, this.#column, '\' or "', this.#currentChar())
    }
  }

  #parseKey(comment) {
    this.#stepNextNonWhitespace()
    let start = this.#pointer
    while (this.#isKey(this.#currentChar())) {
      this.#step()
    }
    return new DmlKey(this.#dmlString.substring(start, this.#pointer)).withComment(comment)
  }

  #parseValue(comment) {
    this.#stepNextNonWhitespace()
    switch (this.#currentChar()) {
      case '{':
        return this.#parseObject(comment)
      case '[':
        return this.#parseArray(comment)
      case '\'':
      case '"':
        return this.#parseString(comment)
      case 't':
      case 'f':
        return this.#parseBoolean(comment)
      default:
        return this.#parseNumber(comment)
    }
  }

  #parseObjectValue(topLevel) {
    let object = new DmlObject(new Map())
    while (this.#charAt(this.#nextNonWhitespace(this.#pointer)) !== '}' && this.#pointer < this.#dmlString.length) {
      this.#stepNextNonWhitespace()
      let comment = this.#parseComment()
      this.#stepNextNonWhitespace()
      let key = this.#parseKey(comment)
      this.#stepNextNonWhitespace()
      if (this.#currentChar() !== ':') {
        throw new DmlParseError(this.#line, this.#column, ':', this.#currentChar())
      }
      this.#step()
      this.#stepNextNonWhitespace()
      let comment1 = this.#parseComment()
      this.#stepNextNonWhitespace()
      let value = this.#parseValue(comment1)
      object.set(key, value)
      let sline = this.#line
      this.#stepNextNonWhitespace()
      if ((this.#pointer >= this.#dmlString.length && topLevel) || this.#currentChar() === '}') {
        break
      }
      if (this.#currentChar() !== ',' && sline == this.#line) {
        throw new DmlParseError(this.#line, this.#column, ',', this.#currentChar())
      }
      if (this.#currentChar() === ',') {
        this.#step()
      }
    }
    return object
  }

  #parseObject(comment) {
    this.#step()
    if (comment == null) {
      comment = this.#parseComment()
    }
    let object = this.#parseObjectValue(false).withComment(comment)

    if (this.#charAt(this.#nextNonWhitespace(this.#pointer)) !== '}') {
      throw new DmlParseError(this.#line, this.#column, '}', this.#charAt(this.#nextNonWhitespace(this.#pointer)))
    }
    this.#stepNextNonWhitespace()
    this.#step()
    return object
  }

  #parseArray(comment) {
    this.#step()
    if (comment == null) {
      comment = this.#parseComment()
    }
    let array = new DmlArray([]).withComment(comment)

    while (this.#charAt(this.#nextNonWhitespace(this.#pointer)) !== ']') {
      this.#stepNextNonWhitespace()
      let comment1 = this.#parseComment()
      this.#stepNextNonWhitespace()
      let value = this.#parseValue(comment1)
      array.add(value)
      let sline = this.#line
      this.#stepNextNonWhitespace()
      if (this.#pointer >= this.#dmlString.length || this.#currentChar() === ']') {
        break
      }
      if (this.#currentChar() !== ',' && sline == this.#line) {
        throw new DmlParseError(this.#line, this.#column, ',', this.#currentChar())
      }
      if (this.#currentChar() === ',') {
        this.#step()
      }
    }

    if (this.#charAt(this.#nextNonWhitespace(this.#pointer)) !== ']') {
      throw new DmlParseError(this.#line, this.#column, ']', this.#charAt(this.#nextNonWhitespace(this.#pointer)))
    }
    this.#stepNextNonWhitespace()
    this.#step()
    return array
  }

  #parseDocument() {
    this.#stepNextNonWhitespace()
    let comment = this.#parseComment()
    this.#stepNextNonWhitespace()
    let result
    switch (this.#charAt(this.#stepNextNonWhitespace(this.#pointer))) {
      case '{':
        result = this.#parseObject(comment)
        break
      case '[':
        result = this.#parseArray(comment)
        break
      default:
        if (this.#isKey(this.#currentChar())) {
          result = this.#parseObjectValue(true).withComment(comment)
        } else {
          throw new DmlParseError(this.#line, this.#column, '{, [ or key', this.#currentChar())
        }
    }
    if (this.#nextNonWhitespace(this.#pointer) < this.#dmlString.length) {
      throw new DmlParseError(
        this.#line,
        this.#column,
        'document end',
        this.#charAt(this.#nextNonWhitespace(this.#pointer))
      )
    }

    return result
  }

  static parse(dmlString) {
    return new DmlParser(dmlString).#parseDocument()
  }
}

export default DmlParser
