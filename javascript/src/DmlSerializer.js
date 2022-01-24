class DmlSerializer {
  static indent(text, spaces) {
    text.split('\n').map(l => l.padStart(l.length + spaces)).join('\n')
  }

  static serializeComment(comment, indent) {
    if (typeof comment === 'undefined' || comment.value() === '') {
      return ''
    }

    return comment.serialize(indent) + '\n'
  }

  static serialize(document) {
    return document.serializeDocument()
  }
}

export default DmlSerializer
