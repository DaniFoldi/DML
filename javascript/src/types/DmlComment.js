import DmlSerializer from '../DmlSerializer.js'

class DmlComment {
  #commentValue

  constructor(commentValue) {
    this.#commentValue = commentValue
  }

  value(commentValue) {
    if (typeof commentValue === 'undefined') {
      return this.#commentValue
    }

    this.#commentValue = commentValue
  }

  serialize(indent) {
    let spaceCount = this.#commentValue.split('\n').some(l => l.trimLeft().startsWith('* ')) ? 1 : 2
    return DmlSerializer.indent(this.#commentValue.indexOf('\n') === -1
      ? '//' + (this.#commentValue.startsWith(' ') ? '' : ' ') + this.#commentValue
      : '/*' + DmlSerializer.indent(this.#commentValue, spaceCount) + '*/', indent)
  }

  get [Symbol.toStringTag]() {
    return `DmlComment{${this.#commentValue}}`
  }
}

export default DmlComment
