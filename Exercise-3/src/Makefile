#########################################################################
# Makefile for a simple PA0 print(expr)* language
#   Wim Bohm, based on Michelle Strouts Circle example

all: parser.java Yylex.java parser

#### lexer Java
Yylex.java: PA0.lex
	java -jar JLex.jar PA0.lex
	mv PA0.lex.java Yylex.java

#### parser Java
parser.java: PA0.cup
	java -jar java-cup-11a.jar  PA0.cup


#### parser
parser: Yylex.java parser.java
	javac -classpath .:java-cup-11a-runtime.jar -d . parser.java sym.java Yylex.java

clean:
	rm *.class
