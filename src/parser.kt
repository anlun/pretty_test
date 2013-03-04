package parser

import java.util.Vector
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.charset.StandardCharsets;

fun main(filename: String): Main.Operation {
    val linesList = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8)
    val lines     = Vector<String>(linesList)

    return parseOp(lines, 0).first
}

fun parseEx(lines: Vector<String>, startEl: Int = 0): Pair<Main.Expression, Int> {
    if (lines.size() == startEl) { throw IllegalArgumentException("Strange expression to parse!") }

    if (lines[startEl] == "!") { return Pair(Main.Number  (lines[startEl + 1].toInt()), startEl + 2) }
    if (lines[startEl] == "x") { return Pair(Main.Variable(lines[startEl + 1])        , startEl + 2) }

    if (lines[startEl] == "@") {
        val operation = lines[startEl + 1]
        val firstExpParse  = parseEx(lines, startEl + 2)
        val secondExpParse = parseEx(lines, firstExpParse.second)

        return Pair(
                Main.BinaryOperation(operation, firstExpParse.first, secondExpParse.first)
                , secondExpParse.second
        )
    }

    throw IllegalArgumentException("Strange expression to parse!")
}

fun parseOp(lines: Vector<String>, startEl: Int = 0): Pair<Main.Operation, Int> {
    if (lines.size() == startEl) { throw IllegalArgumentException("Strange operator to parse!") }

    //parse Skip
    if (lines[startEl] == "s")   { return Pair(Main.SkipOp(), startEl + 1) }

    //parse Assign
    if (lines[startEl] == "=") {
        val variable = Main.Variable(lines[startEl + 1])
        val expressionParse = parseEx(lines, startEl + 2)
        return Pair(Main.AssignOp(variable, expressionParse.first), expressionParse.second)
    }

    //parse Read
    if (lines[startEl] == "r") {
        return Pair(Main.ReadOp(Main.Variable(lines[startEl + 1])), startEl + 2)
    }

    //parse Write
    if (lines[startEl] == "w") {
        val expressionParse = parseEx(lines, startEl + 1)
        return Pair(Main.WriteOp(expressionParse.first), expressionParse.second)
    }

    //parse Sequence
    if (lines[startEl] == ";") {
        val firstOpParse  = parseOp(lines, startEl + 1)
        val secondOpParse = parseOp(lines, firstOpParse.second)
        return Pair(Main.Sequence(firstOpParse.first, secondOpParse.first), secondOpParse.second)
    }

    //parse If
    if (lines[startEl] == "i") {
        val expressionParse = parseEx(lines, startEl + 1)
        val trueBranchParse = parseOp(lines, expressionParse.second)
        val falseBranchParse = parseOp(lines, trueBranchParse.second)
        return Pair(
                Main.IfOp(expressionParse.first, trueBranchParse.first, falseBranchParse.first)
                , falseBranchParse.second
        )
    }

    //parse While
    if (lines[startEl] == "l") {
        val expressionParse = parseEx(lines, startEl + 1)
        val bodyParse       = parseOp(lines, expressionParse.second)
        return Pair(
                Main.WhileOp(expressionParse.first, bodyParse.first)
                , bodyParse.second)
    }

    //return Pair(Main.SkipOp(), startEl)
    throw IllegalArgumentException("Strange operator to parse!")
}