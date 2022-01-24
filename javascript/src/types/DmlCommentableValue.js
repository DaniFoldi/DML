class DmlCommentableValue {
  #comment

  comment(comment) {
    if (comment === undefined) {
      return this.#comment
    }
    this.#comment = comment
  }

  withComment(comment) {
    this.#comment = comment
    return this
  }
}

export default DmlCommentableValue
