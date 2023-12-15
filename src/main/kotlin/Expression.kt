import scanner.Token

sealed class Expr {

    abstract fun <T> accept(visitor: ExprVisitor<T>): T

    data class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr() {
        override fun <T> accept(visitor: ExprVisitor<T>): T = visitor.visit(this)
    }

    data class Grouping(val expression: Expr) : Expr() {
        override fun <T> accept(visitor: ExprVisitor<T>): T = visitor.visit(this)
    }

    data class Unary(val operator: Token, val right: Expr) : Expr() {
        override fun <T> accept(visitor: ExprVisitor<T>): T = visitor.visit(this)
    }

    data class Literal(val value: Any?) : Expr() {
        override fun <T> accept(visitor: ExprVisitor<T>): T = visitor.visit(this)
    }
}

interface ExprVisitor<T> {
    fun visit(binary: Expr.Binary): T
    fun visit(grouping: Expr.Grouping): T
    fun visit(unary: Expr.Unary): T
    fun visit(literal: Expr.Literal): T
}