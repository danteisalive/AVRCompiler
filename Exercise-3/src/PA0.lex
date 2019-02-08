/* PA0.lex */

/* Build instructions (as in Makefile)
   be in this (src) directory

    java -jar JLex.jar PA0.lex
    mv PA0.lex.java Yylex.java
    
*/
/* complete this ... */


import java_cup.runtime.Symbol;
%%
%cup

%eofval{
  return new Symbol(sym.EOF, null);
%eofval}

%%
"+" { return new Symbol(sym.PLUS, null);}
"-" { return new Symbol (sym.MINUS, null);}
";" { return new Symbol(sym.SEMI, null); }
"*" { return new Symbol(sym.TIMES, null); }
"print" { return new Symbol(sym.PRINT, null); }
[ \t\r\n\f] { /* ignore white space. */ }
[0-9]+ {return new Symbol(sym.NUMBER, new Integer(yytext())); }
. { System.err.println("Illegal character: "+yytext()); }
