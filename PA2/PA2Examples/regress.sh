#!/bin/sh

# usage:
#   ./regress.sh


#files='Byte.java PA2bluedot.java CondExpr.java If.java IfStmt.java PA2FlowerSimple.java PA3buttondot.java PA3Cylon.java PA3Expressions.java PA3Test1.java PA3Test2.java PA3Test3.java'
files='PA4bluedot.java PA4raindrop.java PA5Cylon.java PA5movedot.java'
#files='PA4bluedot.java '
#for filename in `ls *.java`
for filename in $files;
do
    echo "Regression testing MJ.jar $filename"

    # run the input file with our MJ Parser
    java -jar ../PA2Start/MJ.jar $filename

    # run the input file with reference MJ Parser
    java -jar ../../ReferenceCompiler/MJ.jar $filename

    diff $filename.ast.dot.final $filename.ast.dot
    echo "DONE with MJ.jar $filename"
    echo "============================="

done
