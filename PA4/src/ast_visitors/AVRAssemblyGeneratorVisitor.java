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

import symtable.VarSTE;

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

     public void outToneExp(ToneLiteral node){
   		out.println("# Push " + node.getLexeme() + " onto the stack.");
   		out.println("ldi    r25, hi8(" + node.getIntValue() + ")");
   		out.println("ldi    r24, lo8(" + node.getIntValue() + ")");
   		out.println("# push two byte expression onto stack");
   		out.println("push   r25");
   		out.println("push   r24");
   		out.println("");

   	}

     public void outButtonExp(ButtonLiteral node){
     out.println("# Button expression "+node.getLexeme());
     out.println("ldi    r22" + node.getIntValue() + "");
     out.println("# push onto stack as single byte");
     out.println("push   r22");
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
               String b1 = new Label().toString();
               String b2 = new Label().toString();
               String b3 = new Label().toString();
               String b4 = new Label().toString();
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
             String b1 = new Label().toString();
             String b2 = new Label().toString();
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
             String b1 = new Label().toString();
             String b2 = new Label().toString();
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


     public void outIdLiteral(IdLiteral node){
     		System.out.println("\nin AVRGenVisitor.outIdLiteral(" + node.toString() + ") ...");
     		out.println("# IdExp");

     		out.println("# load value for variable" + node.toString()); //
     		out.println("# variable is a local or param variable");

     		VarSTE varSte = (VarSTE)this.mCurrentST.lookup(node.toString());

     		if(varSte.getSTEType().getAVRTypeSize() == 2){
     			out.println("ldd	r24, " + varSte.getSTEBase() + "+" + (varSte.getSTEOffset()+1));
     			out.println("push	r24\n");
     		}
     		out.println("ldd	r24, " + varSte.getSTEBase() + "+" + varSte.getSTEOffset());
     		out.println("push	r24\n");

   	}


     public void outMinusExp(MinusExp node){
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
               String b1 = new Label().toString();
               String b2 = new Label().toString();
               String b3 = new Label().toString();
               String b4 = new Label().toString();
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
               String b1 = new Label().toString();
               String b2 = new Label().toString();
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
               String b1 = new Label().toString();
               String b2 = new Label().toString();
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
    		out.println("    # clear r0 and r1, thanks Brendan!");
    		out.println("    eor    r0,r0");
    		out.println("    eor    r1,r1");
    		out.println("");

    }

    public void outMeggyCheckButton(MeggyCheckButton node)
    {

  		String[] buttonSplited = ((ButtonLiteral)node.getExp()).getLexeme().split("\\.");

  		out.println("    ### MeggyCheckButton");
  		out.println("    call    _Z16CheckButtonsDownv");
  		out.println("    lds    r24, " + buttonSplited[1] + "_" + buttonSplited[2]);
  		out.println("    # push one byte expression onto stack");
  		out.println("    push   r24");
  		out.println("");

  	}

    public void outLtExp(LtExp node){
  		Type lexpType = this.mCurrentST.getExpType(node.getLExp());
  		Type rexpType = this.mCurrentST.getExpType(node.getRExp());
  		// int int
  		if(lexpType == Type.INT && rexpType == Type.INT){
  			String true_branch = new Label().toString();
  			String false_branch = new Label().toString();
  			String result_branch = new Label().toString();

  			out.println("# less than expression");
  			out.println("# load a two byte expression off stack");
  			out.println("pop    r18");
  			out.println("pop    r19");
  			out.println("# load a two byte expression off stack");
  			out.println("pop    r24");
  			out.println("pop    r25");
  			out.println("cp    r24, r18");
  			out.println("cpc   r25, r19");
  			out.println("brlt " + true_branch + "\n"); // true_branch
  			out.println("# load false");
  			out.println(false_branch + ":"); // false_branch
  			out.println("ldi     r24, 0");
  			out.println("jmp      " + result_branch + "\n"); // result_branch
  			out.println("# load true");
  			out.println(true_branch + ":"); // true_branch
  			out.println("ldi    r24, 1\n");
  			out.println("# push result of less than");
  			out.println(result_branch + ":"); // result_branch
  			out.println("# push one byte expression onto stack");
  			out.println("push   r24");
  			out.println("");
  		}

  		// int byte
  		if(lexpType == Type.INT && rexpType == Type.BYTE){
  			String MJ_L3 = new Label().toString();
  			String MJ_L4 = new Label().toString();
  			String MJ_L5 = new Label().toString();
  			String MJ_L6 = new Label().toString();
  			String MJ_L7 = new Label().toString();
  			out.println("# less than expression");
  			out.println("# load a one byte expression off stack");
  			out.println("pop    r18");
  			out.println("# load a two byte expression off stack");
  			out.println("pop    r24");
  			out.println("pop    r25");
  			out.println("# promoting a byte to an int");
  			out.println("tst     r18");
  			out.println("brlt     " + MJ_L6); // MJ_L6
  			out.println("ldi    r19, 0");
  			out.println("jmp    " + MJ_L7); // MJ_L7
  			out.println(MJ_L6 + ":"); // MJ_L6
  			out.println("ldi    r19, hi8(-1)");
  			out.println(MJ_L7 + ":"); // MJ_L7
  			out.println("cp    r24, r18");
  			out.println("cpc   r25, r19");
  			out.println("brlt " + MJ_L4 + "\n"); // MJ_L4
  			out.println("# load false");
  			out.println(MJ_L3 + ":"); // MJ_L3
  			out.println("ldi     r24, 0");
  			out.println("jmp      " + MJ_L5 + "\n"); // MJ_L5
  			out.println("# load true");
  			out.println(MJ_L4 + ":"); // MJ_L4
  			out.println("ldi    r24, 1\n");
  			out.println("# push result of less than");
  			out.println(MJ_L5 + ":"); // MJ_L5
  			out.println("# push one byte expression onto stack");
  			out.println(" push   r24");
  			out.println("");
  		}

  		// byte int
  		if(lexpType == Type.BYTE && rexpType == Type.INT){
  			String MJ_L3 = new Label().toString();
  			String MJ_L4 = new Label().toString();
  			String MJ_L5 = new Label().toString();
  			String MJ_L6 = new Label().toString();
  			String MJ_L7 = new Label().toString();
  			out.println("# less than expression");
  			out.println("# load a two byte expression off stack");
  			out.println("pop    r18");
  			out.println("pop    r19");
  			out.println("# load a one byte expression off stack");
  			out.println("pop    r24");
  			out.println("# promoting a byte to an int");
  			out.println("tst     r24");
  			out.println("brlt     " + MJ_L6);
  			out.println("ldi    r25, 0");
  			out.println("jmp    " + MJ_L7);
  			out.println(MJ_L6 + ":");
  			out.println("ldi    r25, hi8(-1)");
  			out.println(MJ_L7 + ":");
  			out.println("cp    r24, r18");
  			out.println("cpc   r25, r19");
  			out.println("brlt " + MJ_L4);
  			out.println("# load false");
  			out.println(MJ_L3 + ":");
  			out.println("ldi     r24, 0");
  			out.println("jmp      " + MJ_L5);
  			out.println("# load true");
  			out.println(MJ_L4 + ":");
  			out.println("ldi    r24, 1");
  			out.println("# push result of less than");
  			out.println(MJ_L5 + ":");
  			out.println("# push one byte expression onto stack");
  			out.println(" push   r24");
  			out.println("");

  		}
  		// byte byte
  		if(lexpType == Type.BYTE && rexpType == Type.BYTE){
  			String MJ_L3 = new Label().toString();
  			String MJ_L4 = new Label().toString();
  			String MJ_L5 = new Label().toString();
  			out.println("# less than expression");
  			out.println("# load a one byte expression off stack");
  			out.println("pop    r18");
  			out.println("# load a one byte expression off stack");
  			out.println("pop    r24");
  			out.println("cp    r24, r18");
  			out.println("brlt " + MJ_L4 +"\n"); // MJ_L4
  			out.println("# load false");
  			out.println(MJ_L3 + ":"); // MJ_L3
  			out.println("ldi     r24, 0");
  			out.println("jmp      " + MJ_L5 + "\n"); // MJ_L5
  			out.println("# load true");
  			out.println(MJ_L4 + ":"); // MJ_L4
  			out.println("ldi    r24, 1\n");
  			out.println("# push result of less than");
  			out.println(MJ_L5 + ":"); // MJ_L5
  			out.println("# push one byte expression onto stack");
  			out.println("push   r24");
  			out.println("");
  		}


  	}

    public void outTrueExp(TrueLiteral node){
  		out.println("# True/1 expression");
  		out.println("ldi    r22, 1");
  		out.println("# push one byte expression onto stack");
  		out.println("push   r22");
  		out.println("");
  	}

  	public void outFalseExp(FalseLiteral node){
  		out.println("# False/0 expression");
  		out.println("ldi    r22,0");
  		out.println("# push one byte expression onto stack");
  		out.println("push   r22");
  		out.println("");
  	}

    public void inAndExp(AndExp node){
  		out.println("#### short-circuited && operation");
  		out.println("# &&: left operand");
  		out.println("");
  	}


  	public void visitAndExp(AndExp node){
  		inAndExp(node);
  		String right_branch = new Label().toString();
  		String done_branch = new Label().toString();
          if(node.getLExp() != null)
          {
              node.getLExp().accept(this);
          }
          out.println("# &&: if left operand is false do not eval right");
          out.println("# load a one byte expression off stack");
          out.println("pop    r24");
          out.println("# push one byte expression onto stack");
          out.println("push   r24");
          out.println("# compare left exp with zero");
          out.println("ldi r25, 0");
          out.println("cp    r24, r25");
          out.println("# Want this, breq done_branch");
          out.println("brne  " + right_branch); // right_branch
          out.println("jmp   " + done_branch); // done_branch
          out.println("");
          if(node.getRExp() != null)
          {
              node.getRExp().accept(this);
          }
          out.println(right_branch + ":"); // right_branch
          out.println("# right operand");
          out.println("# load a one byte expression off stack");
          out.println("pop    r24");
          out.println("# True/1 expression");
          out.println("ldi    r22, 1");
          out.println("# push one byte expression onto stack");
          out.println("push   r22");
          out.println("# load a one byte expression off stack");
          out.println("pop    r24");
          out.println("# push one byte expression onto stack");
          out.println("push   r24");
          out.println(done_branch + ":"); //done_branch
          out.println("");

          outAndExp(node);
  	}

    public void inIfStatement(IfStatement node){
  	}

    public void visitIfStatement(IfStatement node){
        out.println("    #### if statement");
        out.println("");
      //  inIfStatement(node);
        String branch_else = new Label().toString();
        String branch_then = new Label().toString();
        String branch_done = new Label().toString();
        if(node.getExp() != null)
        {
            node.getExp().accept(this);
        }
        out.println("    # load condition and branch if false");
        out.println("    # load a one byte expression off stack");
        out.println("    pop    r24");
        out.println("    #load zero into reg");
        out.println("    ldi    r25, 0\n");
        out.println("    #use cp to set SREG");
        out.println("    cp     r24, r25");
        out.println("    #WANT breq " + branch_else);
        out.println("    brne   " + branch_then);
        out.println("    jmp    " + branch_else + "\n");
        out.println("    # then label for if");
        out.println(branch_then + ":");
        out.println("");

        if(node.getThenStatement() != null)
        {
            node.getThenStatement().accept(this);
        }
        out.println("    jmp    " + branch_done);
        out.println("\n    # else label for if");
        out.println(branch_else + ":");
        out.println("");

        if(node.getElseStatement() != null)
        {
            node.getElseStatement().accept(this);
        }
        out.println("    # done label for if");
        out.println(branch_done + ":");
        out.println("");

        //outIfStatement(node);
    }
      public void outMeggyDelay(MeggyDelay node){
        out.println("    ### Meggy.delay() call");
        out.println("    # load delay parameter");
        out.println("    # load a two byte expression off stack");
        out.println("    pop    r24");
        out.println("    pop    r25");
        out.println("    call   _Z8delay_msj");
        out.println("");
      }

      public void outMeggyGetPixel(MeggyGetPixel node){
    		out.println("    ### Meggy.getPixel(x,y) call");
    		out.println("    # load a one byte expression off stack");
    		out.println("    pop    r22");
    		out.println("    # load a one byte expression off stack");
    		out.println("    pop    r24");
    		out.println("    call   _Z6ReadPxhh");
    		out.println("    # push one byte expression onto stack");
    		out.println("    push   r24");
    		out.println("");
    	}

      public void inWhileStatement(WhileStatement node){
            out.println("#### while statement\n");
      }
      public void visitWhileStatement(WhileStatement node){

        String start = new Label().toString();
        String body = new Label().toString();
        String end = new Label().toString();

        inWhileStatement(node);
        out.println(start + ":");
            if(node.getExp() != null)
            {
                node.getExp().accept(this);
            }
            // loop body
            out.println("# if not(condition)");
            out.println("# load a one byte expression off stack");
            out.println("pop    r24");
            out.println("ldi    r25,0");
            out.println("cp     r24, r25");
            out.println("# WANT breq " + end);
            out.println("brne   " + body);
            out.println("jmp    " + end);
            out.println("# while loop body");
            out.println(body + ":");
            out.println("");
            if(node.getStatement() != null)
            {
                node.getStatement().accept(this);
            }
            out.println("# jump to while test");
            out.println("jmp    " + start);
            out.println("# end of while");
            out.println(end + ":");
            out.println("");

            outWhileStatement(node);
      }

      public void outMeggyToneStart(MeggyToneStart node){
          out.println("### Meggy.toneStart(tone, time_ms) call");
          out.println("# load a two byte expression off stack");
          out.println("pop	r22");
          out.println("pop	r23");
          out.println("# load a two byte expression off stack");
          out.println("pop	r24");
          out.println("pop	r25");
          out.println("call	_Z10Tone_Startjj");
          out.println("");
  	}

    public void outNotExp(NotExp node){
  		out.println("# not operation");
  		out.println("# load a one byte expression off stack");
  		out.println("pop    r24");
  		out.println("ldi     r22, 1");
  		out.println("eor     r24,r22");
  		out.println("# push one byte expression onto stack");
  		out.println("push   r24");
  		out.println("");
  	}

  	public void outEqualExp(EqualExp node){
  		// both int
  		Type lexpType = this.mCurrentST.getExpType(node.getLExp());
  		Type rexpType = this.mCurrentST.getExpType(node.getRExp());

  		if(lexpType == Type.INT && rexpType == Type.INT){
        String false_branch = new Label().toString();
  			String true_branch = new Label().toString();
  			String result_branch = new Label().toString();
  			out.println("  	 # equality check expression");
  			out.println("    # load a two byte expression off stack");
  			out.println("    pop    r18");
  			out.println("    pop    r19");
  			out.println("    # load a two byte expression off stack");
  			out.println("    pop    r24");
  			out.println("    pop    r25");
  			out.println("    cp    r24, r18");
  			out.println("    cpc   r25, r19");
  			out.println("    breq      " + true_branch); // true
  			out.println("    # result is false");
  			out.println(false_branch + ":"); // false
  			out.println("    ldi     r24, 0");
  			out.println("    jmp      " + result_branch); // result
  			out.println("    # result is true");
  			out.println(true_branch + ":"); // true
  			out.println("    ldi     r24, 1");
  			out.println("    # store result of equal expression");
  			out.println(result_branch + ":"); // result
  			out.println("    # push one byte expression onto stack");
  			out.println("    push   r24");
  			out.println("");
  		}

  		// first byte second int.
  		if(lexpType == Type.BYTE && rexpType == Type.INT){
        String branch_3 = new Label().toString();
        String branch_4 = new Label().toString();
        String branch_5 = new Label().toString();
  			String branch_6 = new Label().toString();
  			String branch_7 = new Label().toString();

  			out.println("    # equality check expression");
  			out.println("    # load a two byte expression off stack");
  			out.println("    pop    r18");
  			out.println("    pop    r19");
  			out.println("    # load a one byte expression off stack");
  			out.println("    pop    r24");
  			out.println("    # promoting a byte to an int");
  			out.println("    tst     r24");
  			out.println("    brlt     " + branch_6);
  			out.println("    ldi    r25, 0");
  			out.println("    jmp    " + branch_7);
  			out.println(branch_6 + ":");
  			out.println("    ldi    r25, hi8(-1)");
  			out.println(branch_7 + ":");
  			out.println("    cp    r24, r18");
  			out.println("    cpc   r25, r19");
  			out.println("    breq " + branch_4);
  			out.println("    # result is false");
  			out.println(branch_3 + ":");
  			out.println("    ldi	   r24, 0");
  			out.println("    jmp      " + branch_5);
  			out.println("    # result is true");
  			out.println(branch_4 + ":");
  			out.println("    ldi     r24, 1");
  			out.println("    # store result of equal expression");
  			out.println(branch_5 + ":");
  			out.println("    # push one byte expression onto stack");
  			out.println("    push   r24");
  			out.println("");
  		}
  		// both byte
  		if(lexpType == Type.BYTE && rexpType == Type.BYTE){
        String false_branch = new Label().toString();
  			String true_branch = new Label().toString();
  			String result_branch = new Label().toString();
  			out.println("    # equality check expression");
  			out.println("    # load a one byte expression off stack");
  			out.println("    pop    r18");
  			out.println("    # load a one byte expression off stack");
  			out.println("    pop    r24");
  			out.println("    cp    r24, r18");
  			out.println("    breq      " + true_branch); // true
  			out.println("    # result is false");
  			out.println(false_branch + ":"); // false
  			out.println("    ldi     r24, 0");
  			out.println("    jmp      " + result_branch); // result
  			out.println("    # result is true");
  			out.println(true_branch + ":"); // true
  			out.println("    ldi     r24, 1");
  			out.println("    # store result of equal expression");
  			out.println(result_branch + ":"); // result
  			out.println("    # push one byte expression onto stack");
  			out.println("    push   r24");
  			out.println("");
  		}
  		// first int second byte
  		if(lexpType == Type.INT && rexpType == Type.BYTE){

  			String branch_1 = new Label().toString();
        String branch_2 = new Label().toString();
        String branch_3 = new Label().toString();
        String branch_4 = new Label().toString();
        String branch_5 = new Label().toString();

  			out.println("    # equality check expression");
  			out.println("    # load a one byte expression off stack");
  			out.println("    pop    r18");
  			out.println("    # load a two byte expression off stack");
  			out.println("    pop    r24");
  			out.println("    pop    r25");
  			out.println("    # promoting a byte to an int");
  			out.println("    tst     r18");
  			out.println("    brlt     " + branch_4);
  			out.println("    ldi    r19, 0");
  			out.println("    jmp    " + branch_5);
  			out.println(branch_4 + ":");
  			out.println("    ldi    r19, hi8(-1)");
  			out.println(branch_5 + ":");
  			out.println("    cp    r24, r18");
  			out.println("    cpc   r25, r19");
  			out.println("    breq " + branch_2);
  			out.println("    # result is false");
        out.println(branch_1 + ":");
  			out.println("    ldi     r24, 0");
  			out.println("    jmp      " + branch_3);
  			out.println("    # result is true");
  			out.println(branch_2 + ":");
  			out.println("    ldi     r24, 1");
  			out.println("    # store result of equal expression");
  			out.println(branch_3 + ":");
  			out.println("    # push one byte expression onto stack");
  			out.println("    push   r24");
  			out.println("");
  		}
  	}

}
