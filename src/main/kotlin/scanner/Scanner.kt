package scanner

import Lox
import scanner.Token.Type.*
import java.lang.Character.isDigit

class Scanner(private val source: String) {
    private val tokens = mutableListOf<Token>()

    private var start = 0
    private var current = 0
    private var line = 1

    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            start = current
            scanToken()
        }

        tokens.add(Token(EOF, "", null, line))
        return tokens
    }

    private fun scanToken() {
        when (val c = advance()) {
            '(' -> addToken(LEFT_PAREN)
            ')' -> addToken(RIGHT_PAREN)
            '{' -> addToken(LEFT_BRACE)
            '}' -> addToken(RIGHT_BRACE)
            ',' -> addToken(COMMA)
            '.' -> addToken(DOT)
            '-' -> addToken(MINUS)
            '+' -> addToken(PLUS)
            ';' -> addToken(SEMICOLON)
            '*' -> addToken(STAR)
            '!' -> addToken(if (match('=')) BANG_EQUAL else BANG)
            '=' -> addToken(if (match('=')) EQUAL_EQUAL else EQUAL)
            '>' -> addToken(if (match('=')) GREATER_EQUAL else GREATER)
            '<' -> addToken(if (match('=')) LESS_EQUAL else LESS)
            '/' -> {
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) advance()
                } else {
                    addToken(SLASH)
                }
            }

            '\n' -> line++
            '\t',
            '\r',
            ' ' -> Unit

            '"' -> string()

            else -> when {
                isDigit(c) -> number()
                isAlpha(c) -> identifier()
                else -> Lox.error(line, "Unexpected character")
            }
        }
    }

    private fun isAlpha(c: Char): Boolean = c.isLetter() || c == '_'

    private fun isAlphaNumeric(c: Char): Boolean = isAlpha(c) || isDigit(c)
    private fun identifier() {
        while (isAlphaNumeric(peek())) advance()

        val value = source.subSequence(start, current)
        val tokenType = Keywords[value] ?: IDENTIFIER
        addToken(tokenType)
    }

    private fun number() {
        while (isDigit(peek())) advance()

        if (peek() == '.' && isDigit(peekNext())) {
            advance()
            while (isDigit(peek())) advance()
        }

        val value = source.subSequence(start, current).toString().toDouble()
        addToken(NUMBER, value)
    }

    private fun string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++
            advance()
        }

        if (isAtEnd()) {
            Lox.error(line, "Unterminated string")
            return
        }

        advance()

        val value = source.subSequence(start + 1, current - 1) // +1 -1 for trim surrounding quotes
        addToken(STRING, value)
    }

    private fun peekNext(): Char =
        if (current + 1 > source.length) '\u0000'
        else source[current + 1]

    private fun peek(): Char =
        if (isAtEnd()) '\u0000'
        else source[current]

    private fun match(expected: Char): Boolean = when {
        isAtEnd() -> false
        source[current] != expected -> false
        else -> {
            current++
            true
        }
    }

    private fun addToken(type: Token.Type, literal: Any? = null) {
        val lexeme = source.substring(start, current)
        tokens.add(Token(type, lexeme, literal, line))
    }

    private fun advance(): Char = source[current++]
    private fun isAtEnd(): Boolean = current >= source.length
}