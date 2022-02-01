import DmlArray from '../types/DmlArray.js'
import DmlNumber from '../types/DmlNumber.js'
import DmlBoolean from '../types/DmlBoolean.js'
import DmlObject from '../types/DmlObject.js'
import DmlString from '../types/DmlString.js'

function toJson(dmlValue) {
  if (dmlValue instanceof DmlArray) {
    return dmlValue.value().map(store)
  } else if (dmlValue instanceof DmlObject) {
    let o = {}
    dmlValue.keys().forEach(key => o[key] = store(dmlValue.get(key)))
    return o
  } else if (dmlValue instanceof DmlNumber) {
    return dmlValue.value().toNumber()
  } else if (dmlValue instanceof DmlBoolean) {
    return dmlValue.value()
  } else if (dmlValue instanceof DmlString) {
    return dmlValue.value()
  }

  return dmlValue.toString()
}

function fromJson(json) {
  if (Array.isArray(json)) {
    return new DmlArray(json.map(fromJson))
  } else if (typeof json === 'object') {
    let o = new DmlObject(new Map())
    Object.keys(json).forEach(key => o.set(key, fromJson(json[key])))
    return o
  } else if (typeof json === 'number') {
    return new DmlNumber(json)
  } else if (typeof json === 'boolean') {
    return new DmlBoolean(json)
  } else if (typeof json === 'string') {
    return new DmlString(json)
  }

  return json
}

export {toJson, fromJson}