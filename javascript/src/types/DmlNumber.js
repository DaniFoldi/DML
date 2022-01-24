import DmlCommentableValue from './DmlCommentableValue.js'
import DmlSerializer from '../DmlSerializer.js'

class DmlNumber extends DmlCommentableValue {
  #value
  #cachedString

  constructor(value, cachedString) {
    super()
    this.#value = value
    this.#cachedString = cachedString
  }

  value(value) {
    if (typeof value === 'undefined') {
      return this.#value
    }

    this.#value = value
    this.#cachedString = ''
  }

  serialize(indent) {
    return DmlSerializer.serializeComment(this.comment(), indent)
      + DmlSerializer.indent(
        typeof this.#cachedString !== 'undefined' && this.#cachedString !== ''
          ? this.#cachedString
          : this.#value.toString(),
        indent
      )
  }

  get [Symbol.toStringTag]() {
    return `DmlNumber{${this.#cachedString ? this.#cachedString : this.#value}}`
  }
}

export default DmlNumber
