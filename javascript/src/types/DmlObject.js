import DmlKey from './DmlKey.js'
import DmlSerializer from '../DmlSerializer.js'
import DmlCommentableValue from './DmlCommentableValue.js'

class DmlObject extends DmlCommentableValue {
  #map

  constructor(map) {
    super()
    this.#map = map
  }

  set(key, value) {
    if (!(key instanceof DmlKey)) {
      key = new DmlKey(key)
    }
    this.#map.set(key.value(), [key, value])
  }

  get(key) {
    if (key instanceof DmlKey) {
      key = key.value()
    }
    return this.#map.get(key)[1]
  }

  serialize(indent) {
    let dense = [...this.#map.keys()]
      .map(v => this.#map.get(v))
      .map(v => v[0].serialize(2) + ': ' + v[1].serialize(0)).join(', ')
    return DmlSerializer.serializeComment(this.comment(), indent)
      + (dense.indexOf('\n') === -1 && dense.length <= 80)
      ? (this.#map.size === 0 ? '{}' : '{' + dense + '}')
      : DmlSerializer.indent(
        '{\n'
        + [...this.#map.keys()].map(v => this.#map.get(v))
          .map(v => v[0].serialize(2) + ': ' + v[1].serialize(0)).join('\n')
        + '\n}',
        indent
      )
  }

  serializeDocument() {
    return DmlSerializer.serializeComment(this.comment(), 0)
      + DmlSerializer.indent(
        [...this.#map.keys()]
          .map(v => this.#map.get(v))
          .map(v => v[0].serialize(0) + ': ' + v[1].serialize(0)).join(',\n'),
        0
      )
  }

  get [Symbol.toStringTag]() {
    return `DmlObject{${[...this.#map.values()].map(v => v[0] + ': ' + v[1]).join(', ')}}`
  }
}

export default DmlObject
