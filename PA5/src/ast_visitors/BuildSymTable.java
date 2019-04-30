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
   private int  formalOffset;
   private String errors;

   public BuildSymTable(PrintWriter out, SymTable st) {
     if(st==null) {
          throw new InternalException("unexpected null argument");
      }

      this.STout = out;
      this.SymbolTable = st;
      errors  = "";
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
      Integer _mSize = 0;
      Integer offset = 0 ;
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
            SymbolTable.insert(new VarSTE(var_node.getName(), convertType(var_node.getType()), "Z", offset));
            _mSize += convertType(var_node.getType()).getAVRTypeSize();
            offset += convertType(var_node.getType()).getAVRTypeSize();
        }
      }

      classSte.setClassSize(_mSize);

    }

   public void outTopClassDecl(TopClassDecl node){
      SymbolTable.popScope();

   }


   	public void inMethodDecl(MethodDecl node){
   		// Look up method name in current symbol table to see if there are any duplicates.
   		// only look into the innermost scope
      formalOffset = 1;
      Integer formal_offset = 0;
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

      itr = formalType_list.iterator();
      while(itr.hasNext()){
        formal_offset += ((Type)itr.next()).getAVRTypeSize();
      }


   		Signature signature = new Signature(convertType(node.getType()), formalType_list);


   		Scope methodScope = new Scope("Method",SymbolTable.peekScopeStack());
   		MethodSTE methodSTE = new MethodSTE(node.getName(), signature, methodScope);

   		// insert the MethodSTE into the symbol table with SymbolTable.insert
   		SymbolTable.insert(methodSTE);

   		// push it to the top on the scope stack
   		SymbolTable.pushScope(node.getName());

      Integer offset = 1;

   		VarSTE varSte = new VarSTE("THIS", new Type(currClass.getSTEName()), "Y", offset);
   		SymbolTable.insert(varSte);
      //System.out.println("FORMAL_OFFSET: " + formal_offset);
      offset += formal_offset;
      offset += varSte.getSTEType().getAVRTypeSize(); //2
      formalOffset += varSte.getSTEType().getAVRTypeSize(); //2
      //System.out.println("VAR OFFSET: " + offset);
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
            VarSTE _varSte = new VarSTE(var_node.getName(), convertType(var_node.getType()), "Y", offset);
            SymbolTable.insert(_varSte);
            //System.out.println("VAR: " + var_node.getName() + " Y + "+ offset);
            offset += _varSte.getSTEType().getAVRTypeSize();


        }
      }

   	}

   	public void outMethodDecl(MethodDecl node){

   		SymbolTable.popScope();

   	}


    public void outFormal(Formal node){
  		// check if var name has already been inserted in SymTable using st.lookup(name).  Error if 		   there is a duplicate.

  		STE ste = SymbolTable.lookupInnermost(node.getName());
  		if(ste != null){
        errors += "[" + node.getLine() + "," + node.getPos() + "] Redefined Formal " + node.getName() + "\n";
  			//throw new SemanticException("Redefined Formal",node.getLine(), node.getPos());
  		} else {

  			VarSTE varSte = new VarSTE(node.getName(), convertType(node.getType()), "Y" , formalOffset);
        //System.out.println("VAR FORMAL: " + node.getName() + " Y + "+ formalOffset);
        formalOffset += varSte.getSTEType().getAVRTypeSize();
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

  public void outPlusExp(PlusExp node)
  {
      String lExpName; String rExpName;

      if (node.getLExp() instanceof IdLiteral){
        lExpName = ((IdLiteral)node.getLExp()).getLexeme();
        STE ste = SymbolTable.lookup(lExpName);
        if (ste != null){
          if (ste instanceof VarSTE){
            this.SymbolTable.setExpType(node.getLExp(), ((VarSTE)ste).getSTEType());
          }
          else{
            errors += "[" + node.getLExp().getLine() + "," + node.getLExp().getPos() + "]" +
            " Unrecongnized variable type\n";
          }
        }
        else {
          errors += "[" + node.getLExp().getLine() + "," + node.getLExp().getPos() + "]" +
                    " Left Variable not defined\n";
        }
      }

      if (node.getRExp() instanceof IdLiteral){
        rExpName = ((IdLiteral)node.getRExp()).getLexeme();
        STE ste = SymbolTable.lookup(rExpName);
        if (ste != null){
          if (ste instanceof VarSTE){
            this.SymbolTable.setExpType(node.getRExp(), ((VarSTE)ste).getSTEType());
          }
          else{
            errors += "[" + node.getRExp().getLine() + "," + node.getRExp().getPos() + "]" +
                      " Unrecongnized variable type\n";
          }
        }
        else {
          errors += "[" + node.getRExp().getLine() + "," + node.getRExp().getPos() + "]" +
                  " Right Variable not defined\n";
        }
      }

  }


  public void outMinusExp(MinusExp node)
  {

    String lExpName; String rExpName;

    if (node.getLExp() instanceof IdLiteral){
      lExpName = ((IdLiteral)node.getLExp()).getLexeme();
      STE ste = SymbolTable.lookup(lExpName);
      if (ste != null){
        if (ste instanceof VarSTE){
          this.SymbolTable.setExpType(node.getLExp(), ((VarSTE)ste).getSTEType());
        }
        else{
          errors += "[" + node.getLExp().getLine() + "," + node.getLExp().getPos() + "]" +
          " Unrecongnized variable type\n";
        }
      }
      else {
        errors += "[" + node.getLExp().getLine() + "," + node.getLExp().getPos() + "]" +
                  " Left Variable not defined\n";
      }
    }

    if (node.getRExp() instanceof IdLiteral){
      rExpName = ((IdLiteral)node.getRExp()).getLexeme();
      STE ste = SymbolTable.lookup(rExpName);
      if (ste != null){
        if (ste instanceof VarSTE){
          this.SymbolTable.setExpType(node.getRExp(), ((VarSTE)ste).getSTEType());
        }
        else{
          errors += "[" + node.getRExp().getLine() + "," + node.getRExp().getPos() + "]" +
                    " Unrecongnized variable type\n";
        }
      }
      else {
        errors += "[" + node.getRExp().getLine() + "," + node.getRExp().getPos() + "]" +
                " Right Variable not defined\n";
      }
    }

  }

  public void outMulExp(MulExp node)
  {

        String lExpName; String rExpName;

        if (node.getLExp() instanceof IdLiteral){
          lExpName = ((IdLiteral)node.getLExp()).getLexeme();
          STE ste = SymbolTable.lookup(lExpName);
          if (ste != null){
            if (ste instanceof VarSTE){
              this.SymbolTable.setExpType(node.getLExp(), ((VarSTE)ste).getSTEType());
            }
            else{
              errors += "[" + node.getLExp().getLine() + "," + node.getLExp().getPos() + "]" +
              " Unrecongnized variable type\n";
            }
          }
          else {
            errors += "[" + node.getLExp().getLine() + "," + node.getLExp().getPos() + "]" +
                      " Left Variable not defined\n";
          }
        }

        if (node.getRExp() instanceof IdLiteral){
          rExpName = ((IdLiteral)node.getRExp()).getLexeme();
          STE ste = SymbolTable.lookup(rExpName);
          if (ste != null){
            if (ste instanceof VarSTE){
              this.SymbolTable.setExpType(node.getRExp(), ((VarSTE)ste).getSTEType());
            }
            else{
              errors += "[" + node.getRExp().getLine() + "," + node.getRExp().getPos() + "]" +
                        " Unrecongnized variable type\n";
            }
          }
          else {
            errors += "[" + node.getRExp().getLine() + "," + node.getRExp().getPos() + "]" +
                    " Right Variable not defined\n";
          }
        }
  }

  public void outAndExp(AndExp node){
    String lExpName; String rExpName;

    if (node.getLExp() instanceof IdLiteral){
      lExpName = ((IdLiteral)node.getLExp()).getLexeme();
      STE ste = SymbolTable.lookup(lExpName);
      if (ste != null){
        if (ste instanceof VarSTE){
          this.SymbolTable.setExpType(node.getLExp(), ((VarSTE)ste).getSTEType());
        }
        else{
          errors += "[" + node.getLExp().getLine() + "," + node.getLExp().getPos() + "]" +
          " Unrecongnized variable type\n";
        }
      }
      else {
        errors += "[" + node.getLExp().getLine() + "," + node.getLExp().getPos() + "]" +
                  " Left Variable not defined\n";
      }
    }

    if (node.getRExp() instanceof IdLiteral){
      rExpName = ((IdLiteral)node.getRExp()).getLexeme();
      STE ste = SymbolTable.lookup(rExpName);
      if (ste != null){
        if (ste instanceof VarSTE){
          this.SymbolTable.setExpType(node.getRExp(), ((VarSTE)ste).getSTEType());
        }
        else{
          errors += "[" + node.getRExp().getLine() + "," + node.getRExp().getPos() + "]" +
                    " Unrecongnized variable type\n";
        }
      }
      else {
        errors += "[" + node.getRExp().getLine() + "," + node.getRExp().getPos() + "]" +
                " Right Variable not defined\n";
      }
    }
  }

  public void outLtExp(LtExp node){
    String lExpName; String rExpName;

    if (node.getLExp() instanceof IdLiteral){
      lExpName = ((IdLiteral)node.getLExp()).getLexeme();
      STE ste = SymbolTable.lookup(lExpName);
      if (ste != null){
        if (ste instanceof VarSTE){
          this.SymbolTable.setExpType(node.getLExp(), ((VarSTE)ste).getSTEType());
        }
        else{
          errors += "[" + node.getLExp().getLine() + "," + node.getLExp().getPos() + "]" +
          " Unrecongnized variable type\n";
        }
      }
      else {
        errors += "[" + node.getLExp().getLine() + "," + node.getLExp().getPos() + "]" +
                  " Left Variable not defined\n";
      }
    }

    if (node.getRExp() instanceof IdLiteral){
      rExpName = ((IdLiteral)node.getRExp()).getLexeme();
      STE ste = SymbolTable.lookup(rExpName);
      if (ste != null){
        if (ste instanceof VarSTE){
          this.SymbolTable.setExpType(node.getRExp(), ((VarSTE)ste).getSTEType());
        }
        else{
          errors += "[" + node.getRExp().getLine() + "," + node.getRExp().getPos() + "]" +
                    " Unrecongnized variable type\n";
        }
      }
      else {
        errors += "[" + node.getRExp().getLine() + "," + node.getRExp().getPos() + "]" +
                " Right Variable not defined\n";
      }
    }
  }

  public void outEqualExp(EqualExp node)
  {
    String lExpName; String rExpName;

    if (node.getLExp() instanceof IdLiteral){
      lExpName = ((IdLiteral)node.getLExp()).getLexeme();
      STE ste = SymbolTable.lookup(lExpName);
      if (ste != null){
        if (ste instanceof VarSTE){
          this.SymbolTable.setExpType(node.getLExp(), ((VarSTE)ste).getSTEType());
        }
        else{
          errors += "[" + node.getLExp().getLine() + "," + node.getLExp().getPos() + "]" +
          " Unrecongnized variable type\n";
        }
      }
      else {
        errors += "[" + node.getLExp().getLine() + "," + node.getLExp().getPos() + "]" +
                  " Left Variable not defined\n";
      }
    }

    if (node.getRExp() instanceof IdLiteral){
      rExpName = ((IdLiteral)node.getRExp()).getLexeme();
      STE ste = SymbolTable.lookup(rExpName);
      if (ste != null){
        if (ste instanceof VarSTE){
          this.SymbolTable.setExpType(node.getRExp(), ((VarSTE)ste).getSTEType());
        }
        else{
          errors += "[" + node.getRExp().getLine() + "," + node.getRExp().getPos() + "]" +
                    " Unrecongnized variable type\n";
        }
      }
      else {
        errors += "[" + node.getRExp().getLine() + "," + node.getRExp().getPos() + "]" +
                " Right Variable not defined\n";
      }
    }
  }

  public void outNegExp(NegExp node){

    if (node.getExp() instanceof IdLiteral){
      String rExpName = ((IdLiteral)node.getExp()).getLexeme();
      STE ste = SymbolTable.lookup(rExpName);
      if (ste != null){
        if (ste instanceof VarSTE){
          this.SymbolTable.setExpType(node.getExp(), ((VarSTE)ste).getSTEType());
        }
        else{
          errors += "[" + node.getExp().getLine() + "," + node.getExp().getPos() + "]" +
          " Unrecongnized variable type\n";
        }
      }
      else {
        errors += "[" + node.getExp().getLine() + "," + node.getExp().getPos() + "]" +
                  " Neg exp varaible is not defined\n";
      }
    }

  }

  public void outMeggyGetPixel(MeggyGetPixel node){

    if (node.getXExp() instanceof IdLiteral){
      String expName = ((IdLiteral)node.getXExp()).getLexeme();
      STE ste = SymbolTable.lookup(expName);
      if (ste != null){
        if (ste instanceof VarSTE){
          this.SymbolTable.setExpType(node.getXExp(), ((VarSTE)ste).getSTEType());
        }
        else{
          errors += "[" + node.getXExp().getLine() + "," + node.getXExp().getPos() + "]" +
          " Unrecongnized arg type\n";
        }
      }
      else {
        errors += "[" + node.getXExp().getLine() + "," + node.getXExp().getPos() + "]" +
                  " MeggyGetPixel X arg is not defined\n";
      }
    }

    if (node.getYExp() instanceof IdLiteral){
      String expName = ((IdLiteral)node.getYExp()).getLexeme();
      STE ste = SymbolTable.lookup(expName);
      if (ste != null){
        if (ste instanceof VarSTE){
          this.SymbolTable.setExpType(node.getYExp(), ((VarSTE)ste).getSTEType());
        }
        else{
          errors += "[" + node.getYExp().getLine() + "," + node.getYExp().getPos() + "]" +
          " Unrecongnized arg type\n";
        }
      }
      else {
        errors += "[" + node.getYExp().getLine() + "," + node.getYExp().getPos() + "]" +
                  " MeggyGetPixel Y arg is not defined\n";
      }
    }

    this.SymbolTable.setExpType(node, Type.BYTE);

  }

  public void outMeggyCheckButton(MeggyCheckButton node){
    if (node.getExp() instanceof IdLiteral){
      String expName = ((IdLiteral)node.getExp()).getLexeme();
      STE ste = SymbolTable.lookup(expName);
      if (ste != null){
        if (ste instanceof VarSTE){
          this.SymbolTable.setExpType(node.getExp(), ((VarSTE)ste).getSTEType());
        }
        else{
          errors += "[" + node.getExp().getLine() + "," + node.getExp().getPos() + "]" +
          " Unrecongnized arg type\n";
        }
      }
      else {
        errors += "[" + node.getExp().getLine() + "," + node.getExp().getPos() + "]" +
                  " MeggyCheckButton arg is not defined\n";
      }
    }
  }

  public void outMeggySetPixel(MeggySetPixel node){
    if (node.getXExp() instanceof IdLiteral){
      String expName = ((IdLiteral)node.getXExp()).getLexeme();
      STE ste = SymbolTable.lookup(expName);
      if (ste != null){
        if (ste instanceof VarSTE){
          this.SymbolTable.setExpType(node.getXExp(), ((VarSTE)ste).getSTEType());
        }
        else{
          errors += "[" + node.getXExp().getLine() + "," + node.getXExp().getPos() + "]" +
          " Unrecongnized arg type\n";
        }
      }
      else {
        errors += "[" + node.getXExp().getLine() + "," + node.getXExp().getPos() + "]" +
                  " MeggySetPixel X arg is not defined\n";
      }
    }

    if (node.getYExp() instanceof IdLiteral){
      String expName = ((IdLiteral)node.getYExp()).getLexeme();
      STE ste = SymbolTable.lookup(expName);
      if (ste != null){
        if (ste instanceof VarSTE){
          this.SymbolTable.setExpType(node.getYExp(), ((VarSTE)ste).getSTEType());
        }
        else{
          errors += "[" + node.getYExp().getLine() + "," + node.getYExp().getPos() + "]" +
          " Unrecongnized arg type\n";
        }
      }
      else {
        errors += "[" + node.getYExp().getLine() + "," + node.getYExp().getPos() + "]" +
                  " MeggySetPixel Y arg is not defined\n";
      }
    }

    if (node.getColor() instanceof IdLiteral){
          String expName = ((IdLiteral)node.getColor()).getLexeme();
          STE ste = SymbolTable.lookup(expName);
          if (ste != null){
            if (ste instanceof VarSTE){
              this.SymbolTable.setExpType(node.getColor(), ((VarSTE)ste).getSTEType());
            }
            else{
              errors += "[" + node.getColor().getLine() + "," + node.getColor().getPos() + "]" +
              " Unrecongnized arg type\n";
            }
          }
          else {
            errors += "[" + node.getColor().getLine() + "," + node.getColor().getPos() + "]" +
                      " MeggySetPixel Color arg is not defined\n";
          }
    }
  }

  public void outNewArrayExp(NewArrayExp node){
      if (convertType(node.getType()) == Type.INT ||
          convertType(node.getType()) == Type.COLOR)
      {
        if (node.getExp() instanceof IdLiteral){
          String expName = ((IdLiteral)node.getExp()).getLexeme();
          STE ste = SymbolTable.lookup(expName);
          if (ste != null){
            if (ste instanceof VarSTE){
              if (convertType(node.getType()) == Type.INT)
                this.SymbolTable.setExpType(node, Type.INTARRAY);
              else if (convertType(node.getType()) == Type.COLOR)
                this.SymbolTable.setExpType(node, Type.COLORARRAY);
            }
            else{
              errors += "[" + node.getExp().getLine() + "," + node.getExp().getPos() + "]" +
                        " Unrecongnized variable type\n";
            }
          }
          else {
            errors += "[" + node.getExp().getLine() + "," + node.getExp().getPos() + "]" +
                    " index variable is not defined\n";
          }
        }
        else{
          if (convertType(node.getType()) == Type.INT)
            this.SymbolTable.setExpType(node, Type.INTARRAY);
          else if (convertType(node.getType()) == Type.COLOR)
            this.SymbolTable.setExpType(node, Type.COLORARRAY);
        }


      }
      else{
        errors += "[" + node.getLine() + "," + node.getPos() + "]" +
                  " Invalid type for new array expression\n";
      }
  }

  public void outAssignStatement(AssignStatement node){

        STE ste = SymbolTable.lookup(node.getId());
        if (ste != null){
          if (ste instanceof VarSTE){
            this.SymbolTable.setExpType(node, ((VarSTE)ste).getSTEType());
          }
          else{
            errors += "[" + node.getLine() + "," + node.getPos() + "]" +
            " Unrecongnized variable type\n";
          }
        }
        else {
          errors += "[" + node.getLine() + "," + node.getPos() + "]" +
                    " Left side of assign statement is not defined\n";
        }


  }


  public void outCallStatement(CallStatement node){
      if (node.getExp() instanceof IdLiteral){
          String expName = ((IdLiteral)node.getExp()).getLexeme();
          STE ste = SymbolTable.lookup(expName);
          if (ste != null){
            if (ste instanceof ClassSTE){
              this.SymbolTable.setExpType(node.getExp(), new Type(((ClassSTE)ste).getSTEName()));
            }
            else{
              errors += "[" + node.getExp().getLine() + "," + node.getExp().getPos() + "]" +
              " Unrecongnized class\n";
            }
          }
          else {
            errors += "[" + node.getExp().getLine() + "," + node.getExp().getPos() + "]" +
                      " Class is not declared\n";
          }
      }

      if (node.getExp() instanceof ThisLiteral){
          this.SymbolTable.setExpType(node.getExp(), new Type(currClass.getSTEName()));
      }

      // set the return type


  }

  public void outNewExp(NewExp node){
      this.SymbolTable.setExpType(node, new Type(node.getId()));
  }


  public void inMainClass(MainClass node){
     // check to see the name of file is the same as main class
  }

  public void outProgram(Program node){
      Scope globalScope = SymbolTable.getGlobalScope();
      SymbolTable.printSymTable(this.STout, globalScope);
      STout.flush();

      if (!errors.equals("")){
        System.out.print(errors);
      }
  }

  public void inCallExp(CallExp node){
  }

}
