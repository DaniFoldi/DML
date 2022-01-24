import DmlCommentableValue from './DmlCommentableValue.js'
import DmlSerializer from '../DmlSerializer.js'

class DmlBoolean extends DmlCommentableValue {
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
    return DmlSerializer.serializeComment(this.comment(), indent)
      + DmlSerializer.indent(this.#value ? 'true' : 'false', indent)
  }
}

export default DmlBoolean
