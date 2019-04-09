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
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import symtable.SymTable;
import symtable.Type;
import exceptions.InternalException;
import exceptions.SemanticException;
import label.Label;

public class AVRAssemblyGeneratorVisitor extends DepthFirstVisitor
{

   private SymTable mCurrentST;
   private String errors;
   private PrintWriter out;

   public AVRAssemblyGeneratorVisitor(PrintWriter out, SymTable st) {
     if(st==null) {
          throw new InternalException("unexpected null argument");
      }
      this.out = out;
      this.mCurrentST = st;
      this.errors = "";
   }

   //========================= Overriding the visitor interface


     public void defaultOut(Node node) {
         //System.err.println("Node not implemented in CheckTypes, " + node.getClass());
     }

     public void inProgram(Program node){
         BufferedReader br = null;
         try
         {
            br = new BufferedReader(new FileReader("./src/avrH.rtl.s"));
            String line = br.readLine();
            while (line != null) {
                out.println(line);
                line = br.readLine();
            }
            out.println("");
         }
         catch (Exception e1){
            e1.printStackTrace();
         }
         finally {
           try{ if (br != null) br.close();}
           catch(Exception e2) { e2.printStackTrace(); }
         }

     }

     public void outProgram(Program node){
       out.flush();
     }

     public void outMainClass(MainClass node){
       BufferedReader br = null;
       try
       {
          br = new BufferedReader(new FileReader("./src/avrF.rtl.s"));
          String line = br.readLine();
          while (line != null) {
              out.println(line);
              line = br.readLine();
          }
          out.println("");
       }
       catch (Exception e1){
          e1.printStackTrace();
       }
       finally {
         try{ if (br != null) br.close();}
         catch(Exception e2) { e2.printStackTrace(); }
       }
     }


     public void outIntegerExp(IntLiteral node){
       Integer val = node.getIntValue();
       out.println("    # Load constant int " + val);
       out.println("    ldi    r24,lo8(" + val + ")");
       out.println("    ldi    r25,hi8(" + val + ")");
       out.println("    # push two byte expression onto stack");
       out.println("    push   r25");
       out.println("    push   r24");
       out.println("");
     }

     public void outByteCast(ByteCast node){
        if (this.mCurrentST.getExpType(node.getExp()) == Type.BYTE){
          return;
        }
        out.println("    # Casting int to byte by popping");
        out.println("    # 2 bytes off stack and only pushing low order bits");
        out.println("    # back on.  Low order bits are on top of stack.");
        out.println("    pop    r24");
        out.println("    pop    r25");
        out.println("    push   r24");
        out.println("");

     }

     public void outColorExp(ColorLiteral node){
       out.println("    # Color expression " + node.toString());
       out.println("    ldi    r22," + node.getIntValue());
       out.println("    # push one byte expression onto stack");
       out.println("    push   r22");
       out.println("");
     }

     public void outMeggySetPixel(MeggySetPixel node){
       out.println("    ### Meggy.setPixel(x,y,color) call");
       out.println("    # load a one byte expression off stack");
       out.println("    pop    r20");
       out.println("    # load a one byte expression off stack");
       out.println("    pop    r22");
       out.println("    # load a one byte expression off stack");
       out.println("    pop    r24");
       out.println("    call   _Z6DrawPxhhh");
       out.println("    call   _Z12DisplaySlatev");
       out.println("");
     }

     public void outPlusExp(PlusExp node){
         String b1 = new Label().toString();
         String b2 = new Label().toString();
         String b3 = new Label().toString();
         String b4 = new Label().toString();

         if(this.mCurrentST.getExpType(node.getLExp()) == Type.INT &&
           this.mCurrentST.getExpType(node.getRExp()) == Type.INT){
           out.println("    # load a two byte expression off stack");
           out.println("    pop    r18");
           out.println("    pop    r19");
           out.println("    # load a two byte expression off stack");
           out.println("    pop    r24");
           out.println("    pop    r25");
           out.println("    # Do add operation");
           out.println("    add    r24, r18");
           out.println("    adc    r25, r19");
           out.println("    # push two byte expression onto stack");
           out.println("    push   r25");
           out.println("    push   r24");
           out.println("");
         }

         else if(this.mCurrentST.getExpType(node.getLExp()) == Type.BYTE &&
             this.mCurrentST.getExpType(node.getRExp()) == Type.BYTE){
           out.println("    # load a one byte expression off stack");
           out.println("    pop    r18");
           out.println("    # load a one byte expression off stack");
           out.println("    pop    r24");
           out.println("    # promoting a byte to an int");
           out.println("    tst     r24");
           out.println("    brlt     " + b1);
           out.println("    ldi    r25, 0");
           out.println("    jmp    " + b2);
           out.println(b1 + ":");
           out.println("    ldi    r25, hi8(-1)");
           out.println(b2 + ":");
           out.println("    # promoting a byte to an int");
           out.println("    tst     r18");
           out.println("    brlt     " + b3);
           out.println("    ldi    r19, 0");
           out.println("    jmp    " + b4);
           out.println(b3 + ":");
           out.println("    ldi    r19, hi8(-1)");
           out.println(b4 + ":");
           out.println("    # Do add operation");
           out.println("    add    r24, r18");
           out.println("    adc    r25, r19");
           out.println("    # push two byte expression onto stack");
           out.println("    push   r25");
           out.println("    push   r24");
           out.println("");

         }

       else if(this.mCurrentST.getExpType(node.getLExp()) == Type.BYTE &&
           this.mCurrentST.getExpType(node.getRExp()) == Type.INT){
           out.println("    # load a two byte expression off stack");
           out.println("    pop    r18");
           out.println("    pop    r19");
           out.println("    # load a one byte expression off stack");
           out.println("    pop    r24");
           out.println("    # promoting a byte to an int");
           out.println("    tst     r24");
           out.println("    brlt     " + b1);
           out.println("    ldi    r25, 0");
           out.println("    jmp    " + b2);
           out.println(b1 + ":");
           out.println("    ldi    r25, hi8(-1)");
           out.println(b2 + ":");
           out.println("    # Do add operation");
           out.println("    add    r24, r18");
           out.println("    adc    r25, r19");
           out.println("    # push two byte expression onto stack");
           out.println("    push   r25");
           out.println("    push   r24");
           out.println("");
       }
       else if(this.mCurrentST.getExpType(node.getLExp()) == Type.INT &&
           this.mCurrentST.getExpType(node.getRExp()) == Type.BYTE){
           out.println("    # load a one byte expression off stack");
           out.println("    pop    r18");
           out.println("    # load a two byte expression off stack");
           out.println("    pop    r24");
           out.println("    pop    r25");
           out.println("    # promoting a byte to an int");
           out.println("    tst     r18");
           out.println("    brlt     " + b1);
           out.println("    ldi    r19, 0");
           out.println("    jmp    " + b2);
           out.println(b1 + ":");
           out.println("    ldi    r19, hi8(-1)");
           out.println(b2 + ":");
           out.println("    # Do add operation");
           out.println("    add    r24, r18");
           out.println("    adc    r25, r19");
           out.println("    # push two byte expression onto stack");
           out.println("    push   r25");
           out.println("    push   r24");
           out.println("");
       }
     }

     public void outMinusExp(MinusExp node){
         String b1 = new Label().toString();
         String b2 = new Label().toString();
         String b3 = new Label().toString();
         String b4 = new Label().toString();

         if(this.mCurrentST.getExpType(node.getLExp()) == Type.INT &&
             this.mCurrentST.getExpType(node.getRExp()) == Type.INT){
             out.println("    # load a two byte expression off stack");
             out.println("    pop    r18");
             out.println("    pop    r19");
             out.println("    # load a two byte expression off stack");
             out.println("    pop    r24");
             out.println("    pop    r25");
             out.println("");
             out.println("    # Do INT sub operation");
             out.println("    sub    r24, r18");
             out.println("    sbc    r25, r19");
             out.println("    # push hi order byte first");
             out.println("    # push two byte expression onto stack");
             out.println("    push   r25");
             out.println("    push   r24");
             out.println("");
          }

           else if(this.mCurrentST.getExpType(node.getLExp()) == Type.BYTE &&
             this.mCurrentST.getExpType(node.getRExp()) == Type.BYTE){
             out.println("    # load a one byte expression off stack");
             out.println("    pop    r18");
             out.println("    # load a one byte expression off stack");
             out.println("    pop    r24");
             out.println("    # promoting a byte to an int");
             out.println("    tst     r24");
             out.println("    brlt     " + b1);
             out.println("    ldi    r25, 0");
             out.println("    jmp    " + b2);
             out.println(b1 + ":");
             out.println("    ldi    r25, hi8(-1)");
             out.println(b2 + ":");
             out.println("    # promoting a byte to an int");
             out.println("    tst     r18");
             out.println("    brlt     " + b3);
             out.println("    ldi    r19, 0");
             out.println("    jmp    " + b4);
             out.println(b3 + ":");
             out.println("    ldi    r19, hi8(-1)");
             out.println(b4 + ":");
             out.println("");
             out.println("    # Do INT sub operation");
             out.println("    sub    r24, r18");
             out.println("    sbc    r25, r19");
             out.println("    # push hi order byte first");
             out.println("    # push two byte expression onto stack");
             out.println("    push   r25");
             out.println("    push   r24");
             out.println("");
           }

           else if(this.mCurrentST.getExpType(node.getLExp()) == Type.BYTE &&
             this.mCurrentST.getExpType(node.getRExp()) == Type.INT){
             out.println("    # load a two byte expression off stack");
             out.println("    pop    r18");
             out.println("    pop    r19");
             out.println("    # load a one byte expression off stack");
             out.println("    pop    r24");
             out.println("    # promoting a byte to an int");
             out.println("    tst     r24");
             out.println("    brlt     " + b1);
             out.println("    ldi    r25, 0");
             out.println("    jmp    " + b2);
             out.println(b1 + ":");
             out.println("    ldi    r25, hi8(-1)");
             out.println(b2 + ":");
             out.println("    # Do INT sub operation");
             out.println("    sub    r24, r18");
             out.println("    sbc    r25, r19");
             out.println("    # push hi order byte first");
             out.println("    # push two byte expression onto stack");
             out.println("    push   r25");
             out.println("    push   r24");
             out.println("");
           }

           else if(this.mCurrentST.getExpType(node.getLExp()) == Type.INT &&
             this.mCurrentST.getExpType(node.getRExp()) == Type.BYTE){
             out.println("    # load a one byte expression off stack");
             out.println("    pop    r18");
             out.println("    # load a two byte expression off stack");
             out.println("    pop    r24");
             out.println("    pop    r25");
             out.println("    # promoting a byte to an int");
             out.println("    tst     r18");
             out.println("    brlt     " + b1);
             out.println("    ldi    r19, 0");
             out.println("    jmp    " + b2);
             out.println(b1 + ":");
             out.println("    ldi    r19, hi8(-1)");
             out.println(b2 + ":");
             out.println("");
             out.println("    # Do INT sub operation");
             out.println("    sub    r24, r18");
             out.println("    sbc    r25, r19");
             out.println("    # push hi order byte first");
             out.println("    # push two byte expression onto stack");
             out.println("    push   r25");
             out.println("    push   r24");
             out.println("");
           }
     }

     public void outNegExp(NegExp node){
         if(this.mCurrentST.getExpType(node.getExp()) == Type.INT){
           out.println("    # neg int");
           out.println("    # load a two byte expression off stack");
           out.println("    pop    r24");
           out.println("    pop    r25");
           out.println("    ldi     r22, 0");
           out.println("    ldi     r23, 0");
           out.println("    sub     r22, r24");
           out.println("    sbc     r23, r25");
           out.println("    # push two byte expression onto stack");
           out.println("    push   r23");
           out.println("    push   r22");
           out.println("");
         }
         else if (this.mCurrentST.getExpType(node.getExp()) == Type.BYTE){
           String b1 = new Label().toString();
           String b2 = new Label().toString();
           out.println("    # neg byte");
           out.println("    # load a one byte expression off stack");
           out.println("    pop    r24");
           out.println("    # promoting a byte to an int");
           out.println("    tst     r24");
           out.println("    brlt     " + b1);
           out.println("    ldi    r25, 0");
           out.println("    jmp    " + b2);
           out.println(b1 + ":");
           out.println("    ldi    r25, hi8(-1)");
           out.println(b2 + ":");
           out.println("    ldi    r22, 0");
           out.println("    ldi    r23, 0");
           out.println("    sub     r22, r24");
           out.println("    sbc     r23, r25");
           out.println("    # push two byte expression onto stack");
           out.println("    push   r23");
           out.println("    push   r22");
           out.println("");
        }
      }


    public void outMulExp(MulExp node){

    		out.println("    # MulExp");
    		out.println("    # load a one byte expression off stack");
    		out.println("    pop    r18");
    		out.println("    # load a one byte expression off stack");
    		out.println("    pop    r22");
    		out.println("    # move low byte src into dest reg");
    		out.println("    mov    r24, r18");
    		out.println("    # move low byte src into dest reg");
    		out.println("    mov    r26, r22");
    		out.println("    # Do mul operation of two input bytes");
    		out.println("    muls   r24, r26");
    		out.println("    # push two byte expression onto stack");
    		out.println("    push   r1");
    		out.println("    push   r0");
    		out.println("    # clear r0 and r1");
    		out.println("    eor    r0,r0");
    		out.println("    eor    r1,r1");
    		out.println("");

    }


    public void outMeggyCheckButton(MeggyCheckButton node){

  		String[] buttonSplited = ((ButtonLiteral)node.getExp()).getLexeme().split("\\.");

  		out.println("    ### MeggyCheckButton");
  		out.println("    call    _Z16CheckButtonsDownv");
  		out.println("    lds    r24, " + buttonSplited[1] + "_" + buttonSplited[2]);
  		out.println("    # push one byte expression onto stack");
  		out.println("    push   r24");
  		out.println("");

  	}

    public void inIfStatement(IfStatement node){
          out.println("    #### if statement");
          out.println("");
  	}

    public void visitIfStatement(IfStatement node){
        inIfStatement(node);
        String branch_then = new Label().toString();
        String branch_else = new Label().toString();
        String branch_done = new Label().toString();
        if(node.getExp() != null)
        {
            node.getExp().accept(this);
        }
        out.println("# load condition and branch if false");
        out.println("# load a one byte expression off stack");
        out.println("pop    r24");
        out.println("#load zero into reg");
        out.println("ldi    r25, 0\n");
        out.println("#use cp to set SREG");
        out.println("cp     r24, r25");
        out.println("#WANT breq " + branch_else); // branch_else
        out.println("brne   " + branch_then); // branch_then
        out.println("jmp    " + branch_else + "\n"); // branch_else
        out.println("# then label for if");
        out.println(branch_then + ":"); // branch_then
        out.println("");

        if(node.getThenStatement() != null)
        {
            node.getThenStatement().accept(this);
        }
        out.println("jmp    " + branch_done); // branch_done
        out.println("\n# else label for if");
        out.println(branch_else + ":"); // branch_else
        out.println("");

        if(node.getElseStatement() != null)
        {
            node.getElseStatement().accept(this);
        }
        out.println("# done label for if");
        out.println(branch_done + ":"); // branch_done
        out.println("");

        outIfStatement(node);
}

}
