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

public class CheckTypes extends DepthFirstVisitor
{

   private SymTable mCurrentST;

   public CheckTypes(SymTable st) {
     if(st==null) {
          throw new InternalException("unexpected null argument");
      }
      mCurrentST = st;
   }

   //========================= Overriding the visitor interface

   public void defaultOut(Node node) {
       System.err.println("Node not implemented in CheckTypes, " + node.getClass());
   }
   // program type check.
   public void outProgram(Program node){}

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

    public void outMeggySetPixel(MeggySetPixel node){

      Type xexpType = this.mCurrentST.getExpType(node.getXExp());
      Type yexpType = this.mCurrentST.getExpType(node.getYExp());
      Type colorExpType = this.mCurrentST.getExpType(node.getColor());

      if(xexpType != Type.BYTE){
        throw new SemanticException("Invalid argument type for method MeggySetPixel",
                                    node.getXExp().getLine(),
                                    node.getXExp().getPos()
                                    );
      }
      if(yexpType != Type.BYTE){
        throw new SemanticException("Invalid argument type for method MeggySetPixel",
                                    node.getXExp().getLine(),
                                    node.getXExp().getPos()
                                    );
      }
      if(colorExpType != Type.COLOR){
        throw new SemanticException("Invalid argument type for method MeggySetPixel",
                                    node.getXExp().getLine(),
                                    node.getXExp().getPos()
                                    );
      }
    }

   public void outByteCast(ByteCast node){
      Type  expType = this.mCurrentST.getExpType(node.getExp());
      if (expType == Type.INT  || expType == Type.BYTE){
          this.mCurrentST.setExpType(node, Type.BYTE);
      }
      else{
         throw new SemanticException("Byte Cast is only possible on INT or BYTE operands!",
                                      node.getExp().getLine(),
                                      node.getExp().getPos()
                                      );
      }
   }

   public void outMeggyCheckButton(MeggyCheckButton node){
     Type expType = this.mCurrentST.getExpType(node.getExp());
     if (expType == Type.BUTTON){
        this.mCurrentST.setExpType(node, Type.BOOL);
     }
     else{
        throw new SemanticException("Meggy Check Button expects Button Type argument!",
                                    node.getExp().getLine(),
                                    node.getExp().getPos()
                                    );
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
           throw new SemanticException("Y cordination should be Int or Byte!",
                                       node.getYExp().getLine(),
                                       node.getYExp().getPos()
                                       );
        }
      }
      else{
        throw new SemanticException("X cordination should be Int or Byte!",
                                    node.getXExp().getLine(),
                                    node.getYExp().getPos()
                                    );
      }
   }

   public void outNegExp(NegExp node){
       Type expType = this.mCurrentST.getExpType(node.getExp());
       if ((expType==Type.INT  || expType==Type.BYTE)){
           this.mCurrentST.setExpType(node, Type.INT);
       } else {
           throw new SemanticException(
            "Operands to - operator must be INT or BYTE",
            node.getExp().getLine(),
            node.getExp().getPos());
       }
   }

   public void outAndExp(AndExp node)
   {
     if(this.mCurrentST.getExpType(node.getLExp()) != Type.BOOL) {
       throw new SemanticException(
         "Invalid left operand type for operator &&",
         node.getLExp().getLine(), node.getLExp().getPos());
     }

     if(this.mCurrentST.getExpType(node.getRExp()) != Type.BOOL) {
       throw new SemanticException(
         "Invalid right operand type for operator &&",
         node.getRExp().getLine(), node.getRExp().getPos());
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
       throw new SemanticException("Operands to == operator must be INT or BYTE or COLOR or BOOL or TONE",
                                    node.getLExp().getLine(),
                                    node.getLExp().getPos());
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
       throw new SemanticException("Operands to < operator must be INT or BYTE!",
                                    node.getLExp().getLine(),
                                    node.getLExp().getPos());
     }
   }

   public void outNotExp(NotExp node)
   {
     Type expType = this.mCurrentST.getExpType(node.getExp());

     if(expType != Type.BOOL) {
       throw new SemanticException(
         " Operand type for operator ! should be BOOL type!",
         node.getExp().getLine(),
         node.getExp().getPos());
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
           throw new SemanticException(
                   "Operands to + operator must be INT or BYTE",
                   node.getLExp().getLine(),
                   node.getLExp().getPos());
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
           throw new SemanticException(
                   "Operands to - operator must be INT or BYTE",
                   node.getLExp().getLine(),
                   node.getLExp().getPos());
       }

   }

   public void outMulExp(MulExp node)
   {
       Type lexpType = this.mCurrentST.getExpType(node.getLExp());
       Type rexpType = this.mCurrentST.getExpType(node.getRExp());
       if ((lexpType==Type.INT  || lexpType==Type.BYTE) &&
           (rexpType==Type.INT  || rexpType==Type.BYTE)
          ){
           this.mCurrentST.setExpType(node, Type.INT);
       } else {
           throw new SemanticException(
                   "Operands to * operator must be INT or BYTE",
                   node.getLExp().getLine(),
                   node.getLExp().getPos());
       }

   }
}
