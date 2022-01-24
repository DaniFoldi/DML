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
    let spaceCount = this.#commentValue.split('\n').anyMatch(l => l.startsWith('* ')) ? 1 : 2
    return DmlSerializer.indent(!this.#commentValue.contains('\n')
      ? '//' + (this.#commentValue.startsWith(' ') ? ' ' : '') + this.#commentValue
      : '/* ' + DmlSerializer.indent(this.#commentValue, spaceCount) + '\n */', indent)
  }
}

export default DmlComment
