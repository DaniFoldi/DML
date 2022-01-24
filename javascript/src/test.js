import DmlParser from './DmlParser.js'

const s
= `
hello: 1
world: 2
x+y-z: "s"
arr: []
obj: {
  a: 1
  b: 2
}
eo: {}, x: 2.5
v: [true, false]
`

console.log(DmlParser.parse(s))
