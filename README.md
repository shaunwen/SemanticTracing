SemanticTracing for TextSeer

This project aims at realising the semantic tracing of BPMN (Business Process Model and Notation) model. Specifically, given an
effect scenario( a subset of a cummulative effect), the program is able to find out from which previous effect scenario it is
resulted during the effect accumulation. A sample output of the program is as following:

##Begin of sample output

Given effect scenario: (c) & (d) Found accumulation: (a) & (c) --> (c) & (d)

Given effect scenario: (a) & (d) Found accumulation: (a) & (c) --> (a) & (d)

Given effect scenario: ((b) & (c)) & (d) Found accumulation: (b) & (c) --> ((b) & (c)) & (d)

###End of sample output


SemanticTracing is developed as an extension of the TextSeer(https://github.com/edm92/TextSeer) which is developed by Evan Morrison
in University of Wollongong.

The SemanticTracing module consists of 4 programs, namely src/SemanticTracing.java, src/be/fnord/util/processModel/Graph_ST.java,
src/be/fnord/util/processModel/Vertex_ST.java and src/be/fnord/util/processMel/Edge_ST.java. The JGraphT library is used in this
project for dealing with graph data structure.

Updates

2014-1-15 -- Finished SemanticTracing module. 
2014-1-9 -- Added src/SemanticTracing.java; added src/be/fnord/util/processModel/Graph_ST.java; added src/be/fnord/util/processModel/Vertex_ST.java; added src/be/fnord/util/processMel/Edge_ST.java. 

Apache License, Version 2.0, Apache License Version 2.0, January 2004 http://www.apache.org/licenses/
