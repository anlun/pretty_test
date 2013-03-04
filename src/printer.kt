package printer

import pretty.*

fun printEx(expression: Main.Expression): PrimeDoc =
    when (expression) {
        is Main.Number   -> text(expression.value.toString())
        is Main.Variable -> text(expression.name)
        is Main.BinaryOperation ->
            group(printEx(expression.leftEx) / text(expression.operation) / printEx(expression.rightEx))

        else -> throw IllegalArgumentException("Unknown Expression implementer!")
    }

fun printOp(operation: Main.Operation, nestSize: Int = 2): PrimeDoc =
    when (operation) {
        is Main.SkipOp   -> text("skip")
        is Main.ReadOp   -> text("read(")  + printEx(operation.variable) + text(")")
        is Main.WriteOp  -> text("write(") + printEx(operation.ex)       + text(")")
        is Main.AssignOp ->
            group(
                    printEx(operation.variable) + text(" :=") + nest(nestSize, line() + printEx(operation.ex))
            )
        is Main.Sequence -> (printOp(operation.leftOp) + text(";")) / printOp(operation.rightOp)
        is Main.WhileOp  ->
            group(
                group(text("while (") + nest("while (".length, printEx(operation.ex)) + text(") {"))
                + nest(nestSize, line() + printOp(operation.body))
                / text("}")
            )
        is Main.IfOp -> (
                group(text("if (") + nest("if (".length, printEx(operation.ex)) + text(") {"))
                + nest(nestSize, line() + printOp(operation.trueOp))
                / text("} else {")
                + nest(nestSize, line() + printOp(operation.falseOp))
                / text("}")
        )

        else -> throw IllegalArgumentException("Unknown Operation implementer!")
    }
