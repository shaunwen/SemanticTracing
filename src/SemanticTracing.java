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

public class SemanticTracing {

	private LinkedList<Trace> traces = null;

	/**
	 * The constructor of SemanticTracing
	 * 
	 * @author Xiong Wen (xw926@uowmail.edu.au)
	 * @param pathOfModel
	 */
	public SemanticTracing(String pathOfModel) {

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

	/**
	 * Given an effect scenario(es), do the tracing according to the given es
	 * 
	 * @author Xiong Wen (xw926@uowmail.edu.au)
	 * @param kb
	 *            knowledge base
	 * @param givenEff
	 *            the given effect scenario
	 */
	public void track(String kb, String givenEff) {

		// Graph class
		Graph_ST<Vertex_ST, Edge_ST> gST = new Graph_ST<Vertex_ST, Edge_ST>();

		// perform accumulation
		Accumulate_4ST acc = new Accumulate_4ST();

		// Iterate through each trace.
		if (traces != null) {
			for (Trace t : traces) {
				// test for printing out the nodes
				System.out.println("===========");
				System.out.println("New Tracing");

				gST = acc.trace_acc(t, kb, gST); // accumulation

				// given effect scenario,
				// we can track back to the previous effect scenario which
				// resulted to it
				backTrack(gST, givenEff);
				System.out.println("===========");
				System.out.println("\n");

			}
		}

	}

	public static void main(String[] args) {

		String pathOfModel = "models/example.bpmn";
		String kb = "(((a & b) -> ~c) & ((a&c) -> ~d))"; // knowledge base of
															// the Business
															// Model

		String givenEff1 = "(c) & (d)";
		String givenEff2 = "(a) & (d)";
		String givenEff3 = "((b) & (c)) & (d)";

		// start tracing
		SemanticTracing st = new SemanticTracing(pathOfModel);
		st.track(kb, givenEff1);
		st.track(kb, givenEff2);
		st.track(kb, givenEff3);

	}
}
