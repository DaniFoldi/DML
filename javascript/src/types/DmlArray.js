import DmlSerializer from '../DmlSerializer.js'
import DmlCommentableValue from './DmlCommentableValue.js'

class DmlArray extends DmlCommentableValue {
  #value

  constructor(value) {
    super()
    this.#value = value
  }

  value(value) {
    if (typeof value === 'undefined') {
      return this.#value
    }

    this.#value = value
  }

  add(value) {
    this.#value.push(value)
  }

  serialize(indent) {
    let dense = this.#value.map(v => v.serialize(0)).join(', ')
    return DmlSerializer.serializeComment(this.comment(), indent)
      + (dense.indexOf('\n') === -1 && dense.length <= 80)
      ? (this.#value.length === 0 ? '[]' : '[' + dense + ']')
      : DmlSerializer.indent('[\n' + Object.keys(this.#value).map(v => v.serialize(2)).join('\n') + '\n]', indent)
  }

  serializeDocument() {
    return this.serialize(0)
  }

  get [Symbol.toStringTag]() {
    return `DmlArray{${this.#value}}`
  }
}

export default DmlArray
