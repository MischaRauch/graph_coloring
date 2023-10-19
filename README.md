# Maastricht University Project - 1st year | 1st Semester

## Group 22  
This repository contains the code for the third phase of project 1 for which an algorithm to most accurately find the chromatic number for different graphs had to be produced.

## Essay:
For a detailed understanding of the research and methodologies employed in this project, you can refer to the [essay](https://drive.google.com/file/d/1Bbm0HR7J3Kz3iQrtSeOSRZtxdNnQWNiQ/view?usp=share_link).

### Project Highlights
Decomposition: Our approach involves breaking down complex graphs into more manageable subgraphs. This process often reveals special structures, such as bipartite or complete graphs, simplifying the computation of the Chromatic Number.

Bounds Calculation: In cases where no special structures are detected, we calculate upper and lower bounds. When these bounds align, they yield the exact Chromatic Number. When they diverge, they provide constraints for our backtracking algorithm.

Algorithms: Our upper bound algorithm relies on a greedy approach to estimate the Chromatic Number, while the lower bound algorithm identifies maximum cliques using the Bron-Kerbosch algorithm.

### Experimental Insights
Our experiments have demonstrated that our approach significantly reduces computation time, particularly when dealing with graphs containing special structures. The upper and lower bound algorithms consistently provide valuable insights, and the overall approach successfully determines 50% of Chromatic Numbers in our dataset.

Join us on this journey as we explore graph colouring and seek to optimize and adapt our techniques for even better performance.

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
