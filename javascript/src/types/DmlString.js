import DmlCommentableValue from './DmlCommentableValue.js'
import DmlSerializer from '../DmlSerializer.js'

class DmlString extends DmlCommentableValue {
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

  serialize(indent) {
    DmlSerializer.serializeComment(this.comment(), indent)
      + DmlSerializer.indent('\'' + this.#value.replace('\'', '\\\'') + '\'', indent)
  }
}

export default DmlString
