import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Scanner
import kotlin.system.exitProcess

object Lox {

    private var hadError = false

    @JvmStatic
    fun main(args: Array<String>) {
        when {
            args.size > 1 -> {
                println("Usage: klox [script]")
                exitProcess(64)
            }

            args.size == 1 -> runFile(args.first())
            else -> runRepl()
        }
    }

    private fun runRepl() {
        val input = InputStreamReader(System.`in`)
        val reader = BufferedReader(input)

        while (true) {
            println("> ")
            val line = reader.readLine() ?: break
            run(line)
            hadError = false
        }
    }

    private fun runFile(path: String) {
        val bytes = Files.readAllBytes(Paths.get(path))
        run(String(bytes))
        if (hadError) exitProcess(65)
    }

    private fun run(source: String) {
        val scanner = Scanner(source)
        val tokens = scanner.tokens()

        tokens.forEach { println(it) }
    }

    private fun error(line: Int, message: String) {
        System.err.println("[line $line] Error: $message")
        hadError = true
    }
}