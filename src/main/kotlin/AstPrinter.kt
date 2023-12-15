class AstPrinter : ExprVisitor<String> {
    fun print(expr: Expr): String = expr.accept(this)
    override fun visit(binary: Expr.Binary): String = parenthesize(binary.operator.lexeme, binary.left, binary.right)
    override fun visit(grouping: Expr.Grouping): String = parenthesize("group", grouping.expression)
    override fun visit(unary: Expr.Unary): String = parenthesize(unary.operator.lexeme, unary.right)
    override fun visit(literal: Expr.Literal): String = literal.value?.toString() ?: "nil"
    private fun parenthesize(name: String, vararg expressions: Expr): String {
        val parenthesized = expressions.joinToString(" ") { it.accept(this) }
        return "$name($parenthesized)"
    }
}