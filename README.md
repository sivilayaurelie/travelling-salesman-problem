# TRAVELLING SALESMAN PROBLEM

This project is a framework for the computation of TSP instances' solutions.

# PROJECT

## Instances

Only instances from **TSPLIB** are supported for now. They can be found [here](http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95/tsp/).

To support other formats, it is possible to create a new object which implements `tsp.util.InstanceParser`.

## Algorithms

Implemented are:
- **Nearest Neighbour Algorithm**;
- **Ant Colony Optimisation**;
- **Pairwise Optimisation** (also known as 2-opt Optimization).

To support other algorithms, it is possible to create a new class which, depending of the need, implements either `algorithm.ConstructiveAlgorithm` or `algorithm.OptimizationAlgorithm`.

## TSPSolver

It is possible to override the `application.conf` properties:
- *algorithm.initialvertexindex*, index of the vertex chosen to be the starting point of a constructive heuristic if necessary (0 by default);
- *runtime.filedirectory*, directory of the file describing the instance (the project directory by default);
- *runtime.timelimit*, hard limit on time to find a solution (60000 milliseconds by default).

### To run the application:

#### Using an IDE:

1. Import project using the `build.sbt` file.
2. Run the main class `tsp.TSPSolver` with program argument `<instanceName>` and VM options (optional) to override the `application.conf` properties.

#### Using command lines:

1. Assemble the project:
```
sbt assembly
```
2. Run the project:
```
java \
-cp ./target/scala-2.11/travelling-salesman-problem-assembly-0.1.0-SNAPSHOT.jar \
tsp.TSPSolver <instanceName>
```
With VM options:
```
java \
-cp ./target/scala-2.11/travelling-salesman-problem-assembly-0.1.0-SNAPSHOT.jar \
-Druntime.filedirectory=<instanceDirectory> \
tsp.TSPSolver <instanceName>
```
