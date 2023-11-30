package scanner

import scanner.Token.Type.*

val Keywords = hashMapOf(
    "and" to AND,
    "class" to CLASS,
    "else" to ELSE,
    "false" to FALSE,
    "for" to FOR,
    "fun" to FUN,
    "if" to IF,
    "nil" to NIL,
    "or" to OR,
    "print" to PRINT,
    "return" to RETURN,
    "super" to SUPER,
    "this" to THIS,
    "true" to TRUE,
    "var" to VAR,
    "while" to WHILE,
)