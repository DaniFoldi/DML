class DmlParser {
    #dmlString
    #line
    #column
    #pointer
    constructor(dmlString) {
        this.dmlString = dmlString
        this.line = 1
        this.column = 1
        this.pointer = 0
    }

    #step(count = 1) {
        for (let i = 0; i < count; i++) {
            if (this.currentChar() == '\n') {
                this.column = 0
                this.line++
            }

            this.column++
            this.pointer++
        }
    }

    #stepNextNonWhitespace() {
        while (this.pointer < this.dmlString.length && isWhitespace(currentChar())) {
            this.step()
        }
    }

    #nextNonWhitespace(pointer) {
        while (pointer < this.dmlString.length && this.isWhitespace(this.charAt(pointer))) {
            pointer++
        }

        return pointer
    }

    #charAt(pointer) {
        return this.dmlString.charAt(pointer)
    }

    #currentChar() {
        return this.dmlString.charAt(this.pointer)
    }

    #nextChar() {
        return this.dmlString.charAt(pointer + 1)
    }

    #isCommentStart(c, n) {
        return c === '/' && (n === '/' || n === '*')
    }

    #isMultilineCommentEnd(c, n) {
        return c === '*' && n === '/'
    }

    #isWhitespace(c) {
        return c.test(/\s/)
    }

    #isDigit(c) {
        return c.test(/[0-9]/)
    }

    #isNewline(c) {
        return c === '\n'
    }

    #isKey(c) {
        return c.test(/[a-zA-Z0-9-_+]/)
    }

    #parseComment() {
        let comment = ""
        this.stepNextNonWhitespace()

        while (this.isCommentStart(this.currentChar(), this.nextChar())) {
            switch (this.nextChar()) {
                case '/':
                    this.step(2)
                    let start = this.pointer
                    while (!this.isNewline(this.currentChar())) {
                        this.step()
                    }
                    comment += this.dmlString.substring(start, this.pointer)
                    break
                case '*':
                    this.step(2)
                    let start = this.pointer
                    while (!this.isMultilineCommentEnd(this.currentChar(), this.nextChar())) {
                        this.step()
                    }
                    comment += this.dmlString.substring(start, this.pointer)
                    this.step(2)
            }
        }

        return new DmlComment(comment)
    }

    #parseBoolean(comment) {
        if (this.dmlString.substring(this.pointer, this.pointer + 4).toLowerCase() === 'true') {
            this.step(4)
            return new DmlBoolean(true).withComment(comment)
        }
        if (this.dmlString.substring(this.pointer, this.pointer + 5).toLowerCase() === 'false') {
            this.step(5)
            return new DmlBoolean(false).withComment(comment)
        }
        throw new DmlParseException(this.line, this.column, "true or false", this.dmlString.substring(this.pointer, this.pointer + 4))
    }

    #parseNumber(comment) {
        this.#stepNextNonWhitespace()
        let n = this.#pointer
        let negative = this.#currentChar == '-'
        if (negative) {
            this.#step()
        }
        let whole = ""
        let fraction = "0"
        let exponent = "0"

        let start = this.pointer
        while (this.#isDigit(this.#currentChar())) {
            this.#step()
        }
        whole = pointer > start ? this.dmlString.substring(start, this.pointer) : "0"
        if (this.#currentChar() == '.') {
            this.#step()
            start = this.pointer
            while (this.#isDigit(this.#currentChar())) {
                this.#step()
            }
            fraction = this.pointer > start ? this.dmlString.substring(start, this.pointer) : "0"
        }
        if (this.#currentChar() === 'e') {
            this.#step()
            start = this.pointer
            while (this.#isDigit(this.#currentChar())) {
                this.#step()
            }
            let nexp = this.#currentChar() === '-'
            if (nexp) {
                this.#step()
            }
            exponent = (nexp ? "-" : "") + this.pointer > start ? this.dmlString.substring(start, this.pointer) : "0"
        }
        
    }
}