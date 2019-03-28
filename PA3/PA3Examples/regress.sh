#!/bin/sh

# usage:
#   ./regress.sh


files='PA4bluedot.java PA4MazeSolver.java PA4raindrop.java  PA5movedot.java
      PA5RunningExample.java PA5obj.java'

#for filename in `ls *.java`
for filename in $files;
do
    echo "Regression testing MJPA3.jar $filename"

    # run the input file with our MJ Parser
    java -jar ../PA3Start/MJPA3.jar $filename
    #mv   $filename.ST.dot myOutput.java.ast.dot
    # run the input file with reference MJ Parser
    #java -jar ../../ReferenceCompiler/MJ.jar $filename
    #mv   $filename.ast.dot expectedOutput.java.ast.dot

    result=$(diff $filename.java.ST.dot.temp $filename.java.ST.dot)

    if [ "$result" = "" ]
    then
        echo "SUCCESS!"
    else
        echo "ERROR!"
    fi

    # rm *.java.s  > /dev/null 2>&1
    # rm *.java.ST.dot > /dev/null 2>&1
    # rm *.java.ast.dot > /dev/null 2>&1

    echo "DONE with MJPA3.jar $filename"
    echo "============================="

done
