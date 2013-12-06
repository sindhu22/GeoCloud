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
    rm RTree/KNNRTree.jar
    rm RTree/*.class
    javac -classpath jars/hadoop-core-0.20.2.jar:jars/jsi-1.0.0.jar:jars/trove4j-2.0.2.jar RTree/KNNRTree.java
    jar cf RTree/KNNRTree.jar RTree/*.class
    rm RTree/*.class
    echo "jar created"

# Compile RNNRTree with libraries in jars folder with RNNRTree.java
# Create the jar file in the respective folder

elif [ "$1" = "RNNRTree" ]
then
    rm RTree/RNNRTree.jar
    rm RTree/*.class
    javac -classpath jars/hadoop-core-0.20.2.jar:jars/jsi-1.0.0.jar:jars/trove4j-2.0.2.jar RTree/RNNRTree.java
    jar cf RTree/RNNRTree.jar RTree/*.class
    rm RTree/*.class
    echo "jar created"

# Compile RNNRTree with libraries in jars folder with KNN.java
# Create the jar file in the respective folder

elif [ "$1" = "KNN" ]
then
    rm DistVector/KNN.jar
    rm DistVector/*.class
    javac -classpath jars/hadoop-core-0.20.2.jar DistVector/Point.java DistVector/Distance.java DistVector/KNN.java
    jar cf DistVector/KNN.jar DistVector/*.class
    rm DistVector/*.class
    echo "jar created"


# Compile RNNRTree with libraries in jars folder with RNN.java
# Create the jar file in the respective folder

elif [ "$1" = "RNN" ]
then
    rm DistVector/RNN.jar
    rm DistVector/*.class
    javac -classpath jars/hadoop-core-0.20.2.jar DistVector/Point.java DistVector/Distance.java DistVector/RNN.java
    jar cf DistVector/RNN.jar DistVector/*.class
    rm DistVector/*.class
    echo "jar created"


else
    echo "invalid option"
fi


