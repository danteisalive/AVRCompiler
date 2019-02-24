/*
 * ExprEvalDriver.java
 *
 * usage: 
 *   java ExprEval [--one-pass-eval | --two-pass-eval | --two-pass-mips ] infile
 *
 * This driver calls one of three possible expression evaluation parsers.
 * The default parser is the one-pass-eval.
 *
 */

package ast_visitors;

import ast.node.IntegerExp;
import ast.node.MinusExp;
import ast.node.MulExp;
import ast.node.Node;
import ast.node.PlusExp;
import ast.visitor.DepthFirstVisitor;
import java.util.Stack;

public class TestVisitor extends DepthFirstVisitor {
	private Node root;
	public String PostFix;
	public String PreFix;

	public TestVisitor(Node r){
		this.root=r;
		this.PostFix = new String();
		this.PreFix = new String();
	}

	public void PrintOut(){
		System.out.println("prefix:  " + this.PreFix);
        System.out.println("postfix: " + this.PostFix);
	}
	/**
	 * Override the default action taken at each visited node -- print out dot
	 * node with parse tree node's text as a label. Also print out edge from
	 * parent to this node.
	 */
	public void defaultCase(Node node) {
		
	}
	public void defaultIn(Node node) {
		//System.out.println("Get in a node.");

	}

	public void defaultOut(Node node) {
		//System.out.println("Get out a node.");
	}
	
	public void inIntegerExp(IntegerExp node){
		PreFix += node.toString();
		defaultIn(node);
	}

	public void outIntegerExp(IntegerExp node){
		PostFix += node.toString();
		defaultOut(node);
	}

	public void outPlusExp(PlusExp node){
		PostFix += "+ ";
		defaultOut(node);
	}
	
	public void outMinusExp(MinusExp node){
		PostFix += "- ";
		defaultOut(node);
	}
	
	public void outMulExp(MulExp node){
		PostFix += "* ";
		defaultOut(node);
	}
	public void inPlusExp(PlusExp node){
		PreFix += "+ ";
		defaultIn(node);
	}
	
	public void inMinusExp(MinusExp node){
		PreFix += "- ";
		defaultIn(node);
	}
	
	public void inMulExp(MulExp node){
		PreFix += "* ";
		defaultIn(node);
	}
}
