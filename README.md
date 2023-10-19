# Maastricht University Project - 1st year | 1st Semester

## Group 22  
This repository contains the code for the third phase of project 1 for which an algorithm to most accurately find the chromatic number for different graphs had to be produced.

### Compiling 
- `cd src`
- `javac Main.java`
- `jar -cvmf Group22.mf Group22.jar *.class`

### Running 
`java -jar Group22.jar <Path to Graph file>`

### Structure 
- Source : `/src`
- example Graphs : `/graphs`

### Code 
- `Phase3\src\Main.java`  contains the configuration of calls to the different Algorithms as well as the code keeping track of the bounds and results
- `Phase3\src\Graph.java` contains the definition of the graph as well as most coloring algorithms 
- `Phase3\src\GraphDecomposer.java` decomposes the graph into its subgraphs 
- `Phase3\src\RingStructure.java` finds a ring structure in the graph 
- `Phase3\src\Evolution.java` and `src\Individual.java` contain code relating to a Genetic Algorithm
- `Phase3\src\Tester.java` contains some scripts to test different parts of this code 
