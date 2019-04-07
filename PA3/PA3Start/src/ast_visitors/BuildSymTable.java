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
   private String errors;

   public BuildSymTable(PrintWriter out, SymTable st) {
     if(st==null) {
          throw new InternalException("unexpected null argument");
      }

      this.STout = out;
      this.SymbolTable = st;
      errors  = null;
   }

   //========================= Overriding the visitor interface

    public void inTopClassDecl(TopClassDecl node){
 		   // look up class name
   		 //System.out.println("\nin BuildSymTable.inTopClassDecl(" + node.getName() + ") ... ");
   		 if(SymbolTable.lookupInnermost(node.getName()) != null){
           errors += "[" + node.getLine() + "," + node.getPos() + "] Redefined Class " + node.getName() + "\n";
   			   //throw new SemanticException("Class " + node.getName() + " already defined!");
 		   }

   		Scope classScope = new Scope("Class",SymbolTable.peekScopeStack());
   		ClassSTE classSte = new ClassSTE(node.getName(), false, null, classScope);

  		// insert into current scope stack
  		SymbolTable.insert(classSte);
      // push it to the top on the scope stack
  		SymbolTable.pushScope(node.getName());
  		// memorize current class ste
  		currClass = classSte;

      LinkedList<VarDecl> vars_list = node.getVarDecls();
      Iterator vars_itr = vars_list.iterator();
      while(vars_itr.hasNext())
      {
        VarDecl var_node = (VarDecl)vars_itr.next();
        STE ste = SymbolTable.lookupInnermost(var_node.getName());
        if(ste != null)
        {
            errors += "[" + var_node.getLine() + "," + var_node.getPos() + "] Redefined Class Variable " + var_node.getName() + "\n";
        }
        else
        {
            SymbolTable.insert(new VarSTE(var_node.getName(), convertType(var_node.getType()), "Z", 0));
        }
      }

    }

   public void outTopClassDecl(TopClassDecl node){
      SymbolTable.popScope();

   }


   	public void inMethodDecl(MethodDecl node){
   		// Look up method name in current symbol table to see if there are any duplicates.
   		// only look into the innermost scope


   		if(SymbolTable.lookupInnermost(node.getName()) != null){
        errors += "[" + node.getLine() + "," + node.getPos() + "] Redefined Method " + node.getName() + "\n";
   			//throw new SemanticException("Method " + node.getName() + " already defined!");
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
   		//System.out.println("inserted " + methodSTE.toString());

   		// push it to the top on the scope stack
   		SymbolTable.pushScope(node.getName());
   		//System.out.println("after push, scope is like this: " + SymbolTable.getStackScope());

      offset = 1;

   		VarSTE varSte = new VarSTE("THIS", new Type(currClass.getSTEName()), "Y", 0);
   		SymbolTable.insert(varSte);
      //offset += varSte.getSTEType().getAVRTypeSize();
   	}

   	public void outMethodDecl(MethodDecl node){

      LinkedList<VarDecl> vars_list = node.getVarDecls();
      Iterator vars_itr = vars_list.iterator();
      while(vars_itr.hasNext())
      {
        VarDecl var_node = (VarDecl)vars_itr.next();
        STE ste = SymbolTable.lookupInnermost(var_node.getName());
        if(ste != null)
        {
            errors += "[" + var_node.getLine() + "," + var_node.getPos() + "] Redefined Method Variable " + var_node.getName() + "\n";
        }
        else
        {
            SymbolTable.insert(new VarSTE(var_node.getName(), convertType(var_node.getType()), "Y", 0));
        }
      }

   		SymbolTable.popScope();

      offset = 0;

   	}


    public void outFormal(Formal node){
  		// check if var name has already been inserted in SymTable using st.lookup(name).  Error if 		   there is a duplicate.
  		STE ste = SymbolTable.lookupInnermost(node.getName());
  		if(ste != null){
        errors += "[" + node.getLine() + "," + node.getPos() + "] Redefined Formal " + node.getName() + "\n";
  			//throw new SemanticException("Redefined Formal",node.getLine(), node.getPos());
  		} else {

  			VarSTE varSte = new VarSTE(node.getName(), convertType(node.getType()), "Y" , 0);
        //offset += varSte.getSTEType().getAVRTypeSize();
        SymbolTable.insert(varSte);
        //System.out.println("Added VarSTE: " + varSte.toString());
  		}
  	}

    public Type convertType(IType iType){
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
        if (iType instanceof IntArrayType){
          return Type.INTARRAY;
        }
        if (iType instanceof ColorArrayType){
          return Type.COLORARRAY;
        }
        if (iType instanceof ClassType) {
              //System.out.println("ClassType: " + (String)((ClassType)iType).getName());
              return new Type((String)((ClassType)iType).getName());
        }
        else {
          errors += "type " + " is not defined\n";
          return null;
        }
  }



  public void inMainClass(MainClass node){
     // check to see the name of file is the same as main class
  }

  public void outProgram(Program node){
      Scope globalScope = SymbolTable.getGlobalScope();
      SymbolTable.printSymTable(this.STout, globalScope);
      STout.flush();

      if (errors != null){
        System.out.println(errors);
        throw new SemanticException("Errors found while building symbol table");
      }
  }

}
