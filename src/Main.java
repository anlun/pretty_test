import pretty.PrettyPackage;
import printer.PrinterPackage;
import parser.ParserPackage;

public class Main {
	private static int pageWidth = 30;
	private static int nestSize  = 2;

	public static abstract class Expression {
		@Override
		public String toString() {
			return PrettyPackage.pretty(pageWidth, PrinterPackage.printEx(this));
		}
	}

	public static class Number extends Expression {
		public Number(int val) {
			this.value = val;
		}
		public int value;
	}

	public static class Variable extends Expression {
		public Variable(String name) {
			this.name = name;
		}
		public String name;
	}

	public static class BinaryOperation extends Expression {
		public BinaryOperation(String operation, Expression leftEx, Expression rightEx) {
			this.operation = operation;
			this.leftEx    = leftEx;
			this.rightEx   = rightEx;
		}
		public String     operation;
		public Expression leftEx;
		public Expression rightEx;
	}

	public static abstract class Operation {
		@Override
		public String toString() {
			return PrettyPackage.pretty(pageWidth, PrinterPackage.printOp(this, nestSize));
		}
	}

	public static class SkipOp extends Operation {
	}

	public static class ReadOp extends Operation {
		public ReadOp(Variable var) {
			this.variable = var;
		}
		public Variable variable;
	}

	public static class WriteOp extends Operation {
		public WriteOp(Expression ex) {
			this.ex = ex;
		}
		public Expression ex;
	}

	public static class AssignOp extends Operation {
		public AssignOp(Variable var, Expression ex) {
			this.variable = var;
			this.ex  = ex;
		}
		public Variable variable;
		public Expression ex;
	}

	public static class Sequence extends Operation {
		public Sequence(Operation leftOp, Operation rightOp) {
			this.leftOp  = leftOp;
			this.rightOp = rightOp;
		}
		public Operation leftOp;
		public Operation rightOp;
	}

	public static class IfOp extends Operation {
		public IfOp(Expression ex, Operation trueOp, Operation falseOp) {
			this.ex      = ex;
			this.trueOp  = trueOp;
			this.falseOp = falseOp;
		}
		public Expression ex;
		public Operation  trueOp;
		public Operation  falseOp;
	}

	public static class WhileOp extends Operation {
		public WhileOp(Expression ex, Operation body) {
			this.ex   = ex;
			this.body = body;
		}
		public Expression ex;
		public Operation  body;
	}

    public static void main(String[] args) {
		String filename = "n.x";
		Operation op = ParserPackage.main(filename);
		System.out.println(op);
    }
}
