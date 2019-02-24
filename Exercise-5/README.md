Exercise 5: an example using visitors
=====================================

The goal of this example is to learn out to use the visitor design pattern to visit an expression tree. We will print out the prefix and the postfix of an infix expression to the screen. The infix expressions may contain constant integers, addition, subtraction,  multiplication and parentheses. For example, if the expression below is in a file:
	4 + 5 * (6 + 2) * 2
then you should output the prefix and postfix of expression after you visit it as shown below:

    prefix:     + 4 * * 5 + 6 2 2
    postfix:    4 5 6 2 + * 2 * +

------------------------------
1) You should have the following:

* VisitorStart/
  * src/
    * ast_visitors/
      * TestVisitor.java
    * ast/visitor/
      * ......
    * ast/node/
      * ......
    * Drivers/
      * TestDriver.java			
	
---------------------------------
2) Examine the TestDriver.java file in VisitorStart/src/Drivers/. Here is the source code for TestDriver.java:

```
package Drivers;

/*
 * TestDriver.java
 *
 * usage: 
 *   java TestDriver infile
 *
 * This driver uses TestVisitor to visit the expression tree
 * and print out equivalent prefix and postfix expressions.
 *
 */

import ast.node.*;

import ast_visitors.TestVisitor;

public class TestDriver {
	private static void usage() {
		System.err.println(
		"TestDriver: Specify input file in program arguments");
	}	 
	public static void main(String args[]) 
	{
		try {
			// construct the lexer, 
			// the lexer will be the same for all of the parsers
			// create the Expression: 4 + 5 * (6 + 2) * 2
			Node ast_root = new PlusExp(new IntegerExp(new Token("4",0,0)),
					new MulExp(new MulExp(new IntegerExp(new Token("5",0,0)),
					new PlusExp(new IntegerExp(new Token("6",0,0)), 
					new IntegerExp(new Token("2",0,0)))),
					new IntegerExp(new Token("2",0,0))));
			// use TestVisitor to visit the expression tree
			ast_root.accept(new TestVisitor(ast_root));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}  
	}
}
```

---------------------------------
3) Now you can start to add some code into TestVisitor.java to define the behavior of visitor. Make sure that you understand what the visitor should do while entering the node and leaving the node.
