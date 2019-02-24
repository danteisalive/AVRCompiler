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
//import ast_visitors.DotVisitor;
public class MJDriver {

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

        try {
          // construct the lexer and feed it to the parser
          // the lexer will be the same for all of the parsers
          /* open input files, etc. here */
          Symbol parse_tree = null;
          mj mjparser = new mj (new Yylex(new FileReader(filename)));
          mjparser.programName = filename;
          //System.out.println(filename);
          parse_tree = mjparser.parse();
          //parse_tree = mjparser.debug_parse();


        }catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        finally {
        	/* do close out here */
        }

      }

}
