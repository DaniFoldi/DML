import DmlKey from './DmlKey.js'
import DmlSerializer from '../DmlSerializer.js'

class DmlObject {
  #map

  constructor(map) {
    this.#map = map
  }

  set(key, value) {
    if (!(key instanceof DmlKey)) {
      key = new DmlKey(key)
    }
    this.#map.set(key, value)
  }

  get(key) {
    if (!(key instanceof DmlKey)) {
      key = new DmlKey(key)
    }
    return this.#map.get(key)
  }

  serialize(indent) {
    let dense = [...this.#map.keys()].map(v => v.serialize(2) + ': ' + this.#map.get(v).serialize(0)).join(', ')
    return DmlSerializer.serializeComment(this.comment(), indent)
      + (dense.indexOf('\n') === -1 && dense.length <= 80)
      ? (this.#map.size === 0 ? '{}' : '{' + dense + '}')
      : DmlSerializer.indent(
        '{\n'
        + [...this.#map.keys()].map(v => v.serialize(2) + ': ' + this.#map.get(v).serialize(2)).join('\n')
        + '\n}',
        indent
      )
  }

  serializeDocument() {
    return DmlSerializer.serializeComment(this.comment(), 0)
      + DmlSerializer.indent(
        [...this.#map.keys()].map(v => v.serialize(0)
        + ': '
        + this.#map.get(v).serialize(0)).join(',\n'),
        0
      )
  }
}

export default DmlObject
