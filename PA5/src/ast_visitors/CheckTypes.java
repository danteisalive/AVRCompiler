package ast_visitors;

/**
 * CheckTypes
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

import symtable.SymTable;
import symtable.Type;
import exceptions.InternalException;
import exceptions.SemanticException;
import symtable.ClassSTE;
import symtable.MethodSTE;
import symtable.VarSTE;
import symtable.STE;

public class CheckTypes extends DepthFirstVisitor
{

   private SymTable mCurrentST;
   private String errors;

   public CheckTypes(SymTable st) {
     if(st==null) {
          throw new InternalException("unexpected null argument");
      }
      mCurrentST = st;
      errors = "";
   }

   //========================= Overriding the visitor interface

     public void defaultOut(Node node) {
         //System.err.println("Node not implemented in CheckTypes, " + node.getClass());
     }

     // main class type check.
     public void outMainClass(MainClass node){}

     // blocked statement.
     public void outBlockStatement(BlockStatement node){}


     public void outIntegerExp(IntLiteral node){
        this.mCurrentST.setExpType(node, Type.INT);
   	 }

   	 public void outColorExp(ColorLiteral node){
        this.mCurrentST.setExpType(node, Type.COLOR);
   	 }

   	 public void outButtonExp(ButtonLiteral node){
        this.mCurrentST.setExpType(node, Type.BUTTON);
   	 }

     public void outToneExp(ToneLiteral node){
        this.mCurrentST.setExpType(node, Type.TONE);
   	 }

   	 public void outTrueExp(TrueLiteral node){
        this.mCurrentST.setExpType(node, Type.BOOL);
   	 }

   	 public void outFalseExp(FalseLiteral node){
        this.mCurrentST.setExpType(node, Type.BOOL);
   	 }


     public void outMeggyDelay(MeggyDelay node){
         Type expType = this.mCurrentST.getExpType(node.getExp());
         if(expType != Type.INT){
             errors +=  "[" + node.getLine() + "," + node.getPos() + "]" +
                      " Invalid param type for Meggy Delay\n";
         }
     }

     public void outIfStatement(IfStatement node){
         Type expType = this.mCurrentST.getExpType(node.getExp());
         if (expType != Type.BOOL){
             errors +=  "[" + node.getLine() + "," + node.getPos() + "]" +
                        " Invalid param type for if statement\n";
         }
     }

     public void outWhileStatement(WhileStatement node){
         Type expType = this.mCurrentST.getExpType(node.getExp());
         if(expType !=Type.BOOL){
             errors +=  "[" + node.getLine() + "," + node.getPos() + "]" +
                      " Invalid param type for while statement\n";
         }
     }


    public void outMeggySetPixel(MeggySetPixel node){

      Type xexpType = this.mCurrentST.getExpType(node.getXExp());
      Type yexpType = this.mCurrentST.getExpType(node.getYExp());
      Type colorExpType = this.mCurrentST.getExpType(node.getColor());

      if(xexpType != Type.BYTE){
        errors +=  "[" + node.getXExp().getLine() + "," + node.getXExp().getPos() + "]" +
                " Invalid argument type for method MeggySetPixel\n";
      }
      if(yexpType != Type.BYTE){
        errors +=  "[" + node.getXExp().getLine() + "," + node.getXExp().getPos() + "]" +
                  " Invalid argument type for method MeggySetPixel\n";
      }
      if(colorExpType != Type.COLOR){
        errors += "[" + node.getXExp().getLine() + "," + node.getXExp().getPos() + "]" +
                  " Invalid argument type for method MeggySetPixel\n";
      }
    }

   public void outByteCast(ByteCast node){
      Type  expType = this.mCurrentST.getExpType(node.getExp());
      if (expType == Type.INT  || expType == Type.BYTE){
          this.mCurrentST.setExpType(node, Type.BYTE);
      }
      else{
          errors += "[" + node.getLine() + "," + node.getPos() + "]" +
                    " Byte Cast is only possible on INT or BYTE operands!\n";
      }
   }

   public void outMeggyCheckButton(MeggyCheckButton node){
     Type expType = this.mCurrentST.getExpType(node.getExp());
     if (expType == Type.BUTTON){
        this.mCurrentST.setExpType(node, Type.BOOL);
     }
     else{
       errors += "[" + node.getLine() + "," + node.getPos() + "]" +
                 " Meggy Check Button expects Button Type argument!\n";
     }


   }

   public void outMeggyGetPixel(MeggyGetPixel node){
      Type xexpType = this.mCurrentST.getExpType(node.getXExp());
      Type yexpType = this.mCurrentST.getExpType(node.getYExp());

      if (xexpType == Type.INT || xexpType == Type.BYTE){
        if (yexpType == Type.INT || yexpType == Type.BYTE){
           this.mCurrentST.setExpType(node, Type.COLOR);
        }
        else{
          errors += "[" + node.getLine() + "," + node.getPos() + "]" +
                    " Y cordination should be Int or Byte!\n";
        }
      }
      else{
        errors += "[" + node.getLine() + "," + node.getPos() + "]" +
                  " X cordination should be Int or Byte!\n";
      }
   }

   public void outNegExp(NegExp node){
       Type expType = this.mCurrentST.getExpType(node.getExp());
       if ((expType==Type.INT  || expType==Type.BYTE)){
           this.mCurrentST.setExpType(node, Type.INT);
       } else {
         errors += "[" + node.getLine() + "," + node.getPos() + "]" +
                   " Operands to - operator must be INT or BYTE\n";
       }
   }

   public void outAndExp(AndExp node)
   {
     if(this.mCurrentST.getExpType(node.getLExp()) != Type.BOOL) {
       errors += "[" + node.getLine() + "," + node.getPos() + "]" +
                 " Invalid left operand type for operator &&\n";
     }

     if(this.mCurrentST.getExpType(node.getRExp()) != Type.BOOL) {
       errors += "[" + node.getLine() + "," + node.getPos() + "]" +
                 " Invalid right operand type for operator &&\n";
     }

     this.mCurrentST.setExpType(node, Type.BOOL);
   }

   public void outEqualExp(EqualExp node)
   {
     Type lexpType = this.mCurrentST.getExpType(node.getLExp());
     Type rexpType = this.mCurrentST.getExpType(node.getRExp());

     if((lexpType == Type.INT || lexpType == Type.BYTE) &&
        (rexpType == Type.INT || rexpType == Type.BYTE))
     {
        this.mCurrentST.setExpType(node, Type.BOOL);
     }
     else if (lexpType == Type.BOOL && rexpType == Type.BOOL)
     {
        this.mCurrentST.setExpType(node, Type.BOOL);
     }
     else if (lexpType == Type.COLOR && rexpType == Type.COLOR)
     {
        this.mCurrentST.setExpType(node, Type.BOOL);
     }
     else if (lexpType == Type.TONE && rexpType == Type.TONE)
     {
        this.mCurrentST.setExpType(node, Type.BOOL);
     }
     else
     {
       errors += "[" + node.getLine() + "," + node.getPos() + "]" +
                 " Operands to == operator must be INT or BYTE or COLOR or BOOL or TONE\n";
     }
   }

   public void outLtExp(LtExp node)
   {
     Type lexpType = this.mCurrentST.getExpType(node.getLExp());
     Type rexpType = this.mCurrentST.getExpType(node.getRExp());

     if((lexpType == Type.INT || lexpType == Type.BYTE) &&
        (rexpType == Type.INT || rexpType == Type.BYTE))
     {
        this.mCurrentST.setExpType(node, Type.BOOL);
     }
     else
     {
       errors += "[" + node.getLine() + "," + node.getPos() + "]" +
                 " Operands to < operator must be INT or BYTE!\n";
     }
   }

   public void outNotExp(NotExp node)
   {
     Type expType = this.mCurrentST.getExpType(node.getExp());

     if(expType != Type.BOOL) {
       errors += "[" + node.getLine() + "," + node.getPos() + "]" +
                 " Operand type for operator ! should be BOOL type!\n";
     }

     this.mCurrentST.setExpType(node, Type.BOOL);
   }

   public void outPlusExp(PlusExp node)
   {
       Type lexpType = this.mCurrentST.getExpType(node.getLExp());
       Type rexpType = this.mCurrentST.getExpType(node.getRExp());
       if ((lexpType==Type.INT  || lexpType==Type.BYTE) &&
           (rexpType==Type.INT  || rexpType==Type.BYTE)
          ){
           this.mCurrentST.setExpType(node, Type.INT);
       } else {
         errors += "[" + node.getLine() + "," + node.getPos() + "]" +
                   " Operands to + operator must be INT or BYTE\n";
       }
   }

   public void outMinusExp(MinusExp node)
   {
       Type lexpType = this.mCurrentST.getExpType(node.getLExp());
       Type rexpType = this.mCurrentST.getExpType(node.getRExp());
       if ((lexpType==Type.INT  || lexpType==Type.BYTE) &&
           (rexpType==Type.INT  || rexpType==Type.BYTE)
          ){
           this.mCurrentST.setExpType(node, Type.INT);
       } else {
         errors += "[" + node.getLine() + "," + node.getPos() + "]" +
                   " Operands to - operator must be INT or BYTE\n";
       }

   }

   public void outMulExp(MulExp node)
   {
       Type lexpType = this.mCurrentST.getExpType(node.getLExp());
       Type rexpType = this.mCurrentST.getExpType(node.getRExp());
       if ((/*lexpType==Type.INT  ||*/ lexpType==Type.BYTE) &&
           (/*rexpType==Type.INT  ||*/ rexpType==Type.BYTE)
          ){
           this.mCurrentST.setExpType(node, Type.INT);
       } else {
          errors += "[" + node.getLine() + "," + node.getPos() + "]" +
                   " Operands to * operator must be BYTE\n";
       }

   }

   public void outNewArrayExp(NewArrayExp node){

   }


   public void outProgram(Program node){
       if (!errors.equals("")){
         System.out.print(errors);
       }
   }

   public void outCallExp(CallExp node){

   }

   public void outCallStatement(CallStatement node){
     // first lets see which class this function belongs to
     String id = node.getId();
     ClassSTE classSte = this.mCurrentST.lookupClass(((NewExp)node.getExp()).getId());
     // now serach in class scope to see whether there is a function named as nodet.getId() or not
     STE methodSte = classSte.getScope().lookupInnermost(node.getId());
     if (methodSte != null){
       if (methodSte instanceof MethodSTE){
         // now we have the method STE, therefore we can find the return value and set this call statement return type
         this.mCurrentST.setExpType(node, ((MethodSTE)methodSte).getSignature().getReturnType());
       }
       else {
         errors += "[" + node.getLine() + "," + node.getPos() + "]" +
                  " STE is not a method name!\n";
       }
     }
     else{
       errors += "[" + node.getLine() + "," + node.getPos() + "]" +
                " Method is not found in class scope!\n";
     }
   }
}
