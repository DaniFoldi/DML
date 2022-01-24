class DmlParseError extends Error {
  constructor(line, column, expect, found) {
    super(`DML parsing failed at line ${line}:${column} (Expected ${expect}, found ${found})`)
  }
}

export default DmlParseError
