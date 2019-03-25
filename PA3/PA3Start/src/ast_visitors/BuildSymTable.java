package ast_visitors;

/**
 * BuildSymTable
 *
 * This AST visitor traverses a MiniJava Abstract Syntax Tree and checks
 * for a number of type errors.  If a type error is found a SymanticException
 * is thrown
 *
 * CHANGES to make next year (2012)
 *  - make the error messages between *, +, and - consistent <= ??
 *
 * Bring down the symtab code so that it only does get and set Type
 *  for expressions
 */

import ast.node.*;
import ast.visitor.DepthFirstVisitor;
import java.util.*;
import java.io.PrintWriter;

import symtable.SymTable;
import symtable.ClassSTE;
import symtable.MethodSTE;
import symtable.VarSTE;
import symtable.Type;
import symtable.STE;
import symtable.Scope;
import symtable.Signature;
import exceptions.InternalException;
import exceptions.SemanticException;

public class BuildSymTable extends DepthFirstVisitor
{
   private PrintWriter STout;
   private SymTable SymbolTable;
   private ClassSTE currClass;
   private int  offset;

   public BuildSymTable(PrintWriter out, SymTable st) {
     if(st==null) {
          throw new InternalException("unexpected null argument");
      }

      this.STout = out;
      this.SymbolTable = st;
   }

   //========================= Overriding the visitor interface

    public void inTopClassDecl(TopClassDecl node){
 		   // look up class name
   		 System.out.println("\nin BuildSymTable.inTopClassDecl(" + node.getName() + ") ... ");
   		 if(SymbolTable.lookupInnermost(node.getName()) != null){
   			   throw new SemanticException("Class " + node.getName() + " already defined!");
 		   }

   		Scope classScope = new Scope("Class",SymbolTable.peekScopeStack());
   		ClassSTE classSte = new ClassSTE(node.getName(), false, null, classScope);

  		// insert into current scope stack
  		SymbolTable.insert(classSte);
  		//System.out.println("inserted " + classSte.toString());
      // push it to the top on the scope stack
  		SymbolTable.pushScope(node.getName());
  		System.out.println("pushed " + classSte.toString());
  		System.out.println("after push, scope is like this: " + SymbolTable.getStackScope());
  		// memorize current class ste
  		currClass = classSte;
    }

   public void outTopClassDecl(TopClassDecl node){
      System.out.println("\nin BuildSymTable.outTopClassDecl(" + node.getName() + ") ... ");
      System.out.println("pop top of the scope stack");
      SymbolTable.popScope();
      System.out.println("after pop, scope is like this: "+ SymbolTable.getStackScope() +"\n");
   }


   	public void inMethodDecl(MethodDecl node){
   		// Look up method name in current symbol table to see if there are any duplicates.
   		// only look into the innermost scope
   		System.out.println("\nin BuildSymTable.inMethodDecl(" + node.getName() + ") ... ");

   		if(SymbolTable.lookupInnermost(node.getName()) != null){
   			throw new SemanticException("Method " + node.getName() + " already defined!");
   		}
   		// create a function signature
   		LinkedList<Formal> formal_list = node.getFormals();
   		LinkedList<Type> formalType_list = new LinkedList<Type>();
   		Iterator itr = formal_list.iterator();
   		while(itr.hasNext()){
   			formalType_list.add(convertType(((Formal)itr.next()).getType()));
   		}


   		Signature signature = new Signature(convertType(node.getType()), formalType_list);


   		Scope methodScope = new Scope("Method",SymbolTable.peekScopeStack());
   		MethodSTE methodSTE = new MethodSTE(node.getName(), signature, methodScope);

   		// insert the MethodSTE into the symbol table with SymbolTable.insert
   		SymbolTable.insert(methodSTE);
   		System.out.println("inserted " + methodSTE.toString());

   		// push it to the top on the scope stack
   		SymbolTable.pushScope(node.getName());
   		System.out.println("after push, scope is like this: " + SymbolTable.getStackScope());

      offset = 1;

   		VarSTE varSte = new VarSTE("this", new Type(currClass.getSTEName()), "Y", offset);
   		SymbolTable.insert(varSte);
      offset += varSte.getSTEType().getAVRTypeSize();

      System.out.println("Added VarSTE: " + varSte.toString());
   		// an easy way to associate function to it's ste.
   		// to deal with same method name in different class.
   		// String func_name = currClass.getName() + "_" + node.getName();
   		// SymbolTable.methodList.put(func_name, methodSTE);
   		// System.out.println("added func_name: " + func_name + " into methodList\n");
   	}

   	public void outMethodDecl(MethodDecl node){

   		System.out.println("\nin BuildSymTable.outMethodDecl(" + node.getName() + ") ... ");
   		System.out.println("pop top of the scope stack");
   		SymbolTable.popScope();
   		System.out.println("after pop, scope is like this: "+ SymbolTable.getStackScope() +"\n");
      offset = 0;

   	}


    public void outFormal(Formal node){
  		// check if var name has already been inserted in SymTable using st.lookup(name).  Error if 		   there is a duplicate.
  		System.out.println("\nin BuildSymTable.outFormal(" + node.getName() + ") ... ");
  		STE ste = SymbolTable.lookupInnermost(node.getName());
  		if(ste != null){
  			throw new SemanticException("Redefined Formal",node.getLine(), node.getPos());
  		} else {

  			VarSTE varSte = new VarSTE(node.getName(), convertType(node.getType()), "Y" , offset);
        offset += varSte.getSTEType().getAVRTypeSize();
        SymbolTable.insert(varSte);
        System.out.println("Added VarSTE: " + varSte.toString());
  		}
  	}

    public static Type convertType(IType iType){
        if(iType instanceof BoolType){
          return Type.BOOL;
        }
        if(iType instanceof IntType){
          return Type.INT;
        }
        if(iType instanceof ByteType){
          return Type.BYTE;
        }
        if(iType instanceof ColorType){
          return Type.COLOR;
        }
        if(iType instanceof ButtonType){
          return Type.BUTTON;
        }
        if(iType instanceof VoidType){
          return Type.VOID;
        }
        if(iType instanceof ToneType){
          return Type.TONE;
        }
        if (iType instanceof ClassType) {
              System.out.println("FUCK: " + (String)((ClassType)iType).getName());
              return new Type((String)((ClassType)iType).getName());
        }
        else {
          return null;
        }
  }



  public void inMainClass(MainClass node){
     // check to see the name of file is the same as main class
  }

  public void outProgram(Program node){
      STout.println("digraph SymTable {");
      STout.println("  graph [rankdir=\"LR\"];");
      STout.println("  node [shape=record];");
      STout.flush();

      Scope globalScope = SymbolTable.getGlobalScope();
      globalScope.printSTEs(STout, 0);
      STout.println("}");
      STout.flush();
  }

}
