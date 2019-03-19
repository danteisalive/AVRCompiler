#!/bin/sh

# usage:
#   ./regress.sh


# files='Byte.java CondExpr.java If.java IfStmt.java PA2bluedot.java
#       PA2FlowerSimple.java PA3buttondot.java PA3Cylon.java
#       PA3Expressions.java PA3Test1.java PA3Test2.java
#       PA3Test3.java PA4bluedot.java PA4MazeSolver.java
#       PA4raindrop.java PA5Cylon.java PA5movedot.java
#       PA5obj.java PA5PaddleBall.java PA5RunningExample.java'

#files='PA7GameOfLife.java'

for filename in `ls *.java`
#for filename in $files;
do
    echo "Regression testing MJPA3.jar $filename"

    # run the input file with our MJ Parser
    java -jar ../PA3Start/MJPA3.jar $filename
    mv   $filename.ast.dot myOutput.java.ast.dot
    # run the input file with reference MJ Parser
    java -jar ../../ReferenceCompiler/MJ.jar $filename
    mv   $filename.ast.dot expectedOutput.java.ast.dot

    result=$(diff myOutput.java.ast.dot expectedOutput.java.ast.dot)

    if [ "$result" = "" ]
    then
        echo "SUCCESS!"
    else
        echo "ERROR!"
    fi

    # rm *.java.s  > /dev/null 2>&1
    # rm *.java.ST.dot > /dev/null 2>&1
    # rm *.java.ast.dot > /dev/null 2>&1

    echo "DONE with MJ.jar $filename"
    echo "============================="

done
