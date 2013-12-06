#!/bin/bash

# bash script for compiling the modules


# check if input is present
if [ -z "$1" ] 
then
    echo "Enter the type of Module KNN | RNN | KNNRTree | RNNRTree "
    exit
fi

echo "Initiated Compilation" 

# Compile KNNRTree with libraries in jars folder with KNNRTree.java
# Create the jar file in the respective folder

if [ "$1" = "KNNRTree" ]
then
    cd RTree
    rm KNNRTree.jar
    rm *.class
    javac -classpath ../jars/hadoop-core-1.2.1.jar:../jars/jsi-1.0.0.jar:../jars/trove4j-2.0.2.jar KNNRTree.java
    jar cf KNNRTree.jar *.class
    rm *.class
    cd ..
    echo "jar created"

# Compile RNNRTree with libraries in jars folder with RNNRTree.java
# Create the jar file in the respective folder

elif [ "$1" = "RNNRTree" ]
then
    cd RTree
    rm RNNRTree.jar
    rm *.class
    javac -classpath ../jars/hadoop-core-1.2.1.jar:../jars/jsi-1.0.0.jar:../jars/trove4j-2.0.2.jar RNNRTree.java
    jar cf RNNRTree.jar *.class
    rm *.class
    cd ..
    echo "jar created"

# Compile RNNRTree with libraries in jars folder with KNN.java
# Create the jar file in the respective folder

elif [ "$1" = "KNN" ]
then
cd DistVector
    rm KNN.jar
    rm *.class
    javac -classpath ../jars/hadoop-core-1.2.1.jar Point.java Distance.java KNN.java
    jar cf KNN.jar *.class
    rm *.class
    cd ..
    echo "jar created"


# Compile RNNRTree with libraries in jars folder with RNN.java
# Create the jar file in the respective folder

elif [ "$1" = "RNN" ]
then
cd DistVector
    rm RNN.jar
    rm *.class
    javac -classpath ../jars/hadoop-core-1.2.1.jar Point.java Distance.java RNN.java
    jar cf RNN.jar *.class
    rm *.class
    cd ..
    echo "jar created"


else
    echo "invalid option"
fi


