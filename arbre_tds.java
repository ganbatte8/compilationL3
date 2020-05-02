public class SymbolTableRow{
	String name;
	String type;
	String cat;
	int value;
	int nbparam;
	int rank;
	String scope;
	int nblock;	// not sure what this is for

	public SymbolTableRow(String name, String type, String cat){
		this.name = name;
		this.type = type;
		this.cat = cat;
	}
	public SymbolTableRow(String name, String type, String cat, int value){
		this.value = value;
		this(name,type,cat);
	}
	public SymbolTableRow(String name, String type, String cat, int value, int nbparam, int rank, String scope, int nblock){
		this(name,type,cat);
		this.value = value;
		this.nbparam = nbparam;
		this.rank = rank;
		this.scope = scope;
		this.nblock = nblock; // not sure what this means. Seems to be used for functions
	}
}

public class RunTests{
	public static void main(String[] args){
		example1();
	}

	public static void example1(){
		// building the symbol table :
		SymbolTableRow[] st = new SymbolTableRow[1];
		st[0] = new SymbolTableRow("main", "void", "fonction");
		
		// building the abstract tree :
		Program program = new Program();
		program.instructions[0] = new Function("main");

		// maybe call something like a print method from the "program" instance to print some assembly code.
		program.print(st);
		// for this example we should expect an output like this :
		/*
			.include beta.uasm
			. = 0
			CMOVE(stack,SP)	| Makes the stack pointer (SP) point to the beginning of the "stack" label at the end of the file.
			CALL(function_main)	| BR(function_main, LP). This saves PC into LP before branching to the function.
			HALT()

			function_main:
			PUSH(LP)
			PUSH(BP)
			MOVE(SP,BP)
			MOVE(BP,SP)
			POP(BP)
			POP(LP)
			RTN()		| JMP(LP)

			stack:

		*/
	}
	public static void example2(){ 
		// building the symbol table :
		SymbolTableRow[] st = new SymbolTableRow[5];
		st[0] = new SymbolTableRow("main", "void", "fonction");
		st[1] = new SymbolTableRow("i", "int", "global", 10); // 4th param is value
		st[2] = new SymbolTableRow("j", "int", "global", 20);
		st[3] = new SymbolTableRow("k", "int", "global");
		st[4] = new SymbolTableRow("l", "int", "global");

		// building the abstract tree :
		Program program = new Program();
		program.instructions[0] = new Function("main");

		// printing assembly code :
		program.print(st);
		/* expected result :
			.include beta.uasm
			. = 0
			CMOVE(stack,SP)	| Makes the stack pointer (SP) point to the beginning of the "stack" label at the end of the file.
			CALL(function_main)	| BR(function_main, LP). This saves PC into LP before branching to the function.
			HALT()

			global_i: LONG(10)
			global_j: LONG(20)
			global_k: LONG(0)
			global_l: LONG(0)

			function_main:
			PUSH(LP)
			PUSH(BP)
			MOVE(SP,BP)
			MOVE(BP,SP)
			POP(BP)
			POP(LP)
			RTN()		| JMP(LP)

			stack:
		*/
	}
	public static void example3(){
		// building the symbol table :
		SymbolTableRow[] st = new SymbolTableRow[5];
		st[0] = new SymbolTableRow("main", "void", "fonction");
		st[1] = new SymbolTableRow("i", "int", "global", 10); // 4th param is value
		st[2] = new SymbolTableRow("j", "int", "global", 20);
		st[3] = new SymbolTableRow("k", "int", "global");
		st[4] = new SymbolTableRow("l", "int", "global");

		// building the abstract tree :
		Program program = new Program();
		program.instructions[0] = new Function("main");
		program.instructions[0].instructions[0] = new Assignment(); // k = 2;
		program.instructions[0].instructions[0].left = new Variable("k");
		program.instructions[0].instructions[0].right = new Constant(2);
		program.instructions[0].instructions[1] = new Assignment(); // l = i + 3*j;
		program.instructions[0].instructions[1].left = new Variable("l");
		program.instructions[0].instructions[1].right = new Binary_Expression(); // i + 3*j;
		program.instructions[0].instructions[1].right.operand = '+';
		program.instructions[0].instructions[1].right.left = new Variable("i");
		program.instructions[0].instructions[1].right.right = new BinaryExpression(); // 3*j
		program.instructions[0].instructions[1].right.right.operand = '*';
		program.instructions[0].instructions[1].right.right.left = new Constant(3);
		program.instructions[0].instructions[1].right.right.right = new Variable("j");

		program.print(st);

		/* expected result :		
			.include beta.uasm
			. = 0
			CMOVE(stack,SP)	| Makes the stack pointer (SP) point to the beginning of the "stack" label at the end of the file.
			CALL(function_main)	| BR(function_main, LP). This saves PC into LP before branching to the function.
			HALT()

			global_i: LONG(10)
			global_j: LONG(20)
			global_k: LONG(0)
			global_l: LONG(0)
			somelabel: LONG(1)

			function_main:
			PUSH(LP)
			PUSH(BP)
			MOVE(SP,BP)

			| {some relatively complicated stuff here}
			
			MOVE(BP,SP)
			POP(BP)
			POP(LP)
			RTN()		| JMP(LP)

			stack:
		*/
	}
	public static void example4(){
		// building the symbol table :
		SymbolTableRow[] st = new SymbolTableRow[3];
		st[0] = new SymbolTableRow("main", "void", "fonction");
		st[1] = new SymbolTableRow("i", "int", "global");
		st[2] = new SymbolTableRow("j", "int", "global", 20);

		// building the abstract tree :
	}
	public static void example5(){
		// building the symbol table :
		SymbolTableRow[] st = new SymbolTableRow[2];
		st[0] = new SymbolTableRow("main", "void", "fonction");
		st[1] = new SymbolTableRow("i", "int", "global");

		// building the abstract tree :
	}
	public static void example6(){
		// building the symbol table :
		SymbolTableRow[] st = new SymbolTableRow[3];
		st[0] = new SymbolTableRow("main", "void", "fonction");
		st[1] = new SymbolTableRow("i", "int", "global");
		st[2] = new SymbolTableRow("n", "int", "global", 5);

		// building the abstract tree :
	}
	public static void example7(){
		// building the symbol table :
		SymbolTableRow[] st = new SymbolTableRow[6];
		//String name, String type, String cat, int value, int nbparam, int rank, String scope, int nblock
		st[0] = new SymbolTableRow("main", "void", "fonction");
		st[1] = new SymbolTableRow("a", "int", "global", 10);
		st[2] = new SymbolTableRow("f", "void", "fonction", 0, 1, 0, null, 2); // nbparam = 1, nblock = 2 (?)
		st[3] = new SymbolTableRow("i", "int", "param", 0, 0, 0, "f", 0); // rank = 0, scope = f
		st[4] = new SymbolTableRow("x", "int", "local"); // rank = 0, scope = f
		st[5] = new SymbolTableRow("y", "int", "local"); // rank = 0, scope = f

		// building the abstract tree :
	}
	public static void example8(){
		// building the symbol table :
		SymbolTableRow[] st = new SymbolTableRow[6];
		st[0] = new SymbolTableRow("main", "void", "fonction");
		st[1] = new SymbolTableRow("a", "int", "global");
		st[2] = new SymbolTableRow("f", "int", "fonction", 0, 2, 0, null, 1); //nbparam=2, nblock=1 (?)
		st[3] = new SymbolTableRow("x", "int", "local", 0, 0, 0, "f", 0); //rank=0, scope=f
		st[4] = new SymbolTableRow("i", "int", "param", 0, 0, 0, "f", 0); //rank=0, scope=f
		st[5] = new SymbolTableRow("j", "int", "param", 0, 0, 1, "f", 0); //rank=1, scope=f

		// building the abstract tree :
	}

}

class Program{
	// children : function nodes. Expecting the last one to be the main / entry point.
	Function[] functions = new Function[10];

	public void print(SymbolTableRow[] st){
		// code for the beginning of the program :
		System.out.print(".include beta.uasm\n"
						+". = 0\n"
						+"CMOVE(stack,SP)	| Makes the stack pointer (SP) point to the beginning of the 'stack' label at the end of the file.\n"
						+"CALL(function_main)	| BR(function_main, LP). This saves PC into LP before branching to the function.\n"
						+"HALT()\n\n\n");
		
		// declaring the global integer variables we can find in the symbol table :
		// st fields reminder : String name, String type, String cat, int value, int nbparam, int rank, String scope, int nblock
		for (int i = 0; i < st.length; i++)
			if (st[i].cat == "global")
				System.out.print("global_"+st[i].name+": LONG("+st[i].value+")\n");
		System.out.print("\n\n");

		// writing the functions :
		for (int i = 0 ; i < 10 && this.functions[i] != null; i++)
			this.functions[i].print(st);
		
		// writing the stack label at the end of the file :
		System.out.print("stack:\n");
	}
}

class Function extends Instruction{
	String name;
	// children : any number of instruction nodes. There could be no instruction at all.
	Instruction[] instructions = new Instruction[10];

	public Function(String name){this.name = name;}

	public void print(SymbolTableRow st){
		System.out.print("function_"+this.name+":\n"
						+"PUSH(LP)\n"
						+"PUSH(BP)\n"
						+"MOVE(SP,BP)\n");
		for (int i = 0; i < 10 && this.instructions[i] != null; i++)
			this.instructions[i].print();
		System.out.print("MOVE(BP,SP)\n"
						+"POP(BP)\n"
						+"POP(LP)\n"
						+"RTN()		| JMP(LP)\n\n\n");
	}
}

abstract class Instruction {}

class Assignment extends Instruction{
	// children : a left member and a right member. 
	// Left member should be a variable with an identifier
	// Right member should be something that returns a value.
	// (ReadNode, some arithmetic expression, some constant, some variable, function call...)
	Variable left;
	Expression right;

	public void print(SymbolTableRow[] st){
		// first evaluate the expression on the right, push the result into the stack
		right.print(st); // assume result is pushed into the stack
		System.out.print("POP(r0)\n"
						+"ST(r0, global_"+left.name+")"); // probably store into the global integer for now... Might be more intricate later on if there is ambiguity
	}
}

class Write extends Instruction{
	// not sure what that would do exactly (write into a file ?)
}

class Read extends Instruction{
	// looks like this is for reading input from the user.
}

class If extends Instruction{
	Expression condition;
	Block if;
	Block else;
}
class While extends Instruction{
	Expression condition;
	Block block;
}

class FunctionCall extends Instruction{
	String name;
}

class Block{
	Instruction[] instructions = new Instruction[10];
}

abstract class Expression{}

class Binary_Expression extends Expression{
	char operator; // < > + - * / 
	Expression left;
	Expression right;

	/* how do you generate the code for a binary expression ?
		evaluate the expression on the left, get the result into r0, store it in the stack...
		evaluate the expression on the right, get the result into r0, store it in the stack...
		pop these two stack values, compute the result into r0, store it into the stack... is that it ?
	*/
	public void print(SymbolTableRow[] st){
		this.left.print(st); // assume the result is pushed into the stack
		this.right.print(st); // same
		System.out.print("POP(r0)\n"
						+"POP(r1)\n"); // this.left : r1; this.right : r0
		if (operator == '+')
			System.out.print("ADD(r0,r1,r0)\n");
		else if (operator == '-')
			System.out.print("SUB(r1,r0,r0)\n"); // order matters ! the intended calculation is r1-r0, i.e. left-right
		else if (operator == '*')
			System.out.print("MUL(r0,r1,r0)\n");
		else if (operator == '/')
			System.out.print("DIV(r1,r0,r0)\n"); // order matters
		else if (operator == '<')
			System.out.print("CMPLT(r1,r0,r0)\n"); // 1 if r1 < r0, 0 otherwise 
		else if (operator == '>')
			System.out.print("CMPLT(r0,r1,r0)\n"); // 1 if r1 > r0, 0 otherwise 
		System.out.print("PUSH(r0)\n");

	}
}

class Constant extends Expression{
	int value;
	public Constant(int value){this.value = value;}
	public void print(SymbolTableRow[] st){
		System.out.print("ADDC(r31,"+value+",r0)\n"
						+"PUSH(r0)\n");
	}

}

class Variable extends Expression{
	String name;
	public Variable(String name){this.name = name;}
	public void print(SymbolTableRow[] st){
		// which label ? just try to match the name for now, and make sure the type is "int".
		// Maybe later on we'll have to do additional checks because maybe a name without a scope is ambiguous for example.
		String label;
		for (int i = 0; i < st.length; i++){
			if (st[i].type == "int" && st[i].name.equals(this.name)){
				label = st[i].cat + "_" + st[i].name;
				break;
			}
		}
		System.out.print("LD("+label+",r0)\n"
						+"PUSH(r0)\n");
	}
}