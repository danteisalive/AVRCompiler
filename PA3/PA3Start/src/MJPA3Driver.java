/*
 * MJDriver.java for just the scanner
 *
 */
import java.io.FileReader;
import java.io.PrintWriter;
// needed because we DON'T parse (see below)
import java_cup.runtime.*;

import mjparser.*;
import ast_visitors.*;
import ast.node.*;
import ast_visitors.DotVisitor;
import ast_visitors.CheckTypes;

import symtable.SymTable;

public class MJPA3Driver {

      private static void usage() {
          System.err.println(
            "MJ: Specify input file in program arguments");
      }

      public static void main(String args[])
      {

        if(args.length < 1)
        {
            usage();
            System.exit(1);
        }

        // filename should be the last command line option
        String filename = args[args.length-1];
        symtable.SymTable SymbolTable = new SymTable();
        try {
          // construct the lexer and feed it to the parser
          // the lexer will be the same for all of the parsers
          /* open input files, etc. here */
          mj mjparser = new mj (new Yylex(new FileReader(filename)));
          mjparser.programName = filename;
          java.io.PrintStream astout =
            new java.io.PrintStream(
                new java.io.FileOutputStream(filename + ".ast.dot.temp"));

          java.io.PrintStream STout =
            new java.io.PrintStream(
                new java.io.FileOutputStream(filename + ".ST.dot.temp"));

          Program ast_root = (Program)mjparser.parse().value;
          ast_root.accept(new DotVisitor(new PrintWriter(astout)));
          ast_root.accept(new BuildSymTable(new PrintWriter(STout), SymbolTable));
          ast_root.accept(new CheckTypes(SymbolTable));


        }catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        finally {
        	/* do close out here */
        }

      }

}
