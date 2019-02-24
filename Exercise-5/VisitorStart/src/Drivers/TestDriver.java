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
            TestVisitor test_visitor = new TestVisitor(ast_root);
            ast_root.accept(test_visitor);
            test_visitor.PrintOut();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }  
    }
}