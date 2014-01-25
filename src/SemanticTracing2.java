
import java.util.LinkedList;

import be.fnord.util.logic.Accumulate_4ST;
import be.fnord.util.processModel.Edge;
import be.fnord.util.processModel.Graph;
import be.fnord.util.processModel.Trace;
import be.fnord.util.processModel.Vertex;
import be.fnord.util.processModel.Graph_ST;
import be.fnord.util.processModel.Vertex_ST;
import be.fnord.util.processModel.Edge_ST;
import be.fnord.util.processModel.util.GraphChecker;
import be.fnord.util.processModel.util.GraphLoader;
import be.fnord.util.processModel.util.GraphTransformer;


/**
 * This class is for tracing the semantic route in a BPMN semantic accumulation
 * 
 * @author Xiong Wen (xw926@uowmail.edu.au)
 */
public class SemanticTracing2 {
	
	private LinkedList<Trace> traces = null;
	
	public SemanticTracing2(String pathOfModel) {

		// Setup the environment
				new a.e();
				Vertex.TO_STRING_WITH_WFFS = true;
				// 1. load the BPMN model in the form of texture
				// Store our traces in a list
				this.traces = loadModel(pathOfModel); // Loading a model and convert
														// into a set of traces
	}
	
	/**
	 * Load the process model from xml file, get the traces
	 * 
	 * @return traces
	 * @author Evan
	 */
	public static LinkedList<Trace> loadModel(String pathOfModel) {

		LinkedList<Trace> traces = new LinkedList<Trace>();
		// For details of below refer to Decision free graph conversion and
		// model loading
		Graph<Vertex, Edge> g1 =
			GraphLoader.loadModel(
				pathOfModel, a.e.DONT_SAVE_MESSAGES_AND_PARTICIPANTS);
		LinkedList<Graph<Vertex, Edge>> _decisionless =
			GraphTransformer.makeDecisionFree(g1);
		LinkedList<Graph<Vertex, Edge>> decisionless =
			GraphTransformer.removeDupesFromDecisionFreeGraphs(_decisionless);
		for (Graph<Vertex, Edge> g : decisionless) {
			GraphChecker gcc = new GraphChecker();
			boolean isgood = gcc.CheckGraph(g);
			if (isgood) {
				LinkedList<Trace> _traces = GraphTransformer.createTrace(g);
				traces.addAll(_traces);
			}

		}

		return traces;
	}

	/**
	 * Given an effect scenario, it can find out from which previous effect
	 * scenario it is resulted
	 * 
	 * @param gST
	 *            the graph object which contains effect scenarios(vertices) and
	 *            the links(edges) between them
	 * @param esWFF
	 *            the effect scenario which needs to be tracked
	 */
	public static void backTrack(Graph_ST<Vertex_ST, Edge_ST> gST, String esWFF) {

		if (!gST.edgeSet().isEmpty()) {
			for (Edge_ST e : gST.edgeSet()) {
				// build up links between effect scenarios
				if (gST.getEdgeTarget(e).esWFF.equals(esWFF)) {
					System.out.println("\nGiven effect scenario: " + esWFF);
					System.out.println("Found accumulation:  " +
						gST.getEdgeSource(e).esWFF + " --> " +
						gST.getEdgeTarget(e).esWFF);
				}
			}
		}

	}
	
	public void track(String kb, String givenEff){
		// Graph class
				Graph_ST<Vertex_ST, Edge_ST> gST = new Graph_ST<Vertex_ST, Edge_ST>();
				
				//perform accumulation
				 Accumulate_4ST acc = new Accumulate_4ST();
//				Accumulate acc = new Accumulate();

				// Iterate through each trace.
				if (traces != null) {
					for (Trace t : traces) {
						//test for printing out the nodes
						System.out.println("New Tracing");
//						for (Vertex v : t.getNodes()) {
//							a.e.println("Vertex task: " + v.name);
////							System.out.println("vertex id: " + v.id);
//							a.e.println("Vertex effect: " + v.getWFF());
//							// question: how to filter out the subsets(effect scenario)?
//						}
						
//						System.out.println("\n\n");
						gST = acc.trace_acc(t, kb, gST); //accumulation
						
						//given effect scenario: (c) & (d) or (a) & (d) or ((b) & (c)) & (d)  
						//we can track back to the previous effect scenario which resulted to it
						backTrack(gST, givenEff);
						System.out.println("\n");
						
						//test for edges in the new built graph
//						System.out.println("edges count " + gST.edgeSet().size());
//						for(Edge_ST e: gST.edgeSet()){
//							System.out.println(gST.getEdgeSource(e).esWFF + " to " + gST.getEdgeTarget(e).esWFF);
////							System.out.println(gST.getEdgeSource(e).esWFF);
//						}
						
						//test for display of the vertices in the new Graph_ST
//						System.out.println("\nThe number of nodes in Graph_ST: " + gST.vertexSet().size());
//						for(Vertex_ST vst: gST.vertexSet()){
//							
//							System.out.println("\n---Begin a node in the graph---");
//							System.out.println("taskName: " + vst.taskName);
//							System.out.println("immediate effect: " + vst.immWFF);
//							System.out.println("cummulative effect: " + vst.esWFF);
//							System.out.println("---End a node in the graph----\n");
//							
//						}
						 
						 
//						
//						System.out.println("\n\n");
//						for (WFF e : _ee) {
//							if (e.isEmpty())
//								continue;
//							System.out.println("Effect scenario resulting from acc: " +
//								e);
//							System.out.println("Checking if " + e +
//								" is consistent : " + e.issat());
		//
//						}
					}
				}
		
	}

	public static void main(String[] args) {

		String pathOfModel = "models/example.bpmn";
		String kb = "(((a & b) -> ~c) & ((a&c) -> ~d))"; //knowledge base of the Business Model
		
		String givenEff1 = "(c) & (d)";
		String givenEff2 = "(a) & (d)";
		String givenEff3 = "((b) & (c)) & (d)";	
		
		//start tracing
		SemanticTracing2 st = new SemanticTracing2(pathOfModel);
		st.track(kb, givenEff1);
		st.track(kb, givenEff2);
		st.track(kb, givenEff3);
		
		

	}
}
