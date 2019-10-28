import java.awt.BorderLayout;

import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.gui.graphvisualizer.BIFFormatException;
import weka.gui.graphvisualizer.GraphVisualizer;


public class main {

	public static void main(String[] args) throws Exception {
		
		// Read in file and create train dataset
	    DataSource source = new DataSource("res/data_gr_5000.csv");
	    Instances data = source.getDataSet();
	    data.setClassIndex(data.numAttributes() - 1);

	    int trainSetSize = Math.round((data.numInstances() * 66)/100);
		int testSetSize = data.numInstances() - trainSetSize;

		// data = randomizeSet(data);
		Instances train_data = new Instances(data, 0, trainSetSize);
		Instances test_data = new Instances(data, trainSetSize, testSetSize);

	    // Create new instance of scheme
	    weka.classifiers.bayes.BayesNet scheme = new weka.classifiers.bayes.BayesNet();
	    scheme.setOptions(weka.core.Utils.splitOptions("-D"));
	    
	    weka.classifiers.bayes.net.search.local.K2 algorithm = new weka.classifiers.bayes.net.search.local.K2();
	    algorithm.setOptions(weka.core.Utils.splitOptions("-P 2 -S BAYES"));
	    
	    weka.classifiers.bayes.net.estimate.SimpleEstimator estimator = new weka.classifiers.bayes.net.estimate.SimpleEstimator();
	    estimator.setOptions(weka.core.Utils.splitOptions("-A 0.5"));
	    
	    scheme.setSearchAlgorithm(algorithm);
	    scheme.setEstimator(estimator);
	    
	    scheme.buildClassifier(train_data);
	    
	    // Eval it with test data based on the test data
	    Evaluation eval = new Evaluation(train_data);
	    eval.evaluateModel(scheme, test_data);
	    
	    System.out.println(eval.toSummaryString("=== Summary ===\n", false));
	    System.out.println(eval.toClassDetailsString());
	    System.out.println(eval.toMatrixString("=== Confusion Matrix ===\n"));
		
	    visualizeBayesNet(scheme.graph(), "K2");
	}
	
	/**
	 * Pops up a GraphVisualizer for the BayesNet classifier from the currently
	 * selected item in the results list.
	 * 
	 * @param XMLBIF the description of the graph in XMLBIF ver. 0.3
	 * @param graphName the name of the graph
	 */
	private static void visualizeBayesNet(String XMLBIF, String graphName) {
		
		final javax.swing.JFrame jf = new javax.swing.JFrame("Weka Classifier Graph Visualizer: " + graphName);
		jf.setSize(500, 400);
		jf.getContentPane().setLayout(new BorderLayout());
		
		GraphVisualizer gv = new GraphVisualizer();
		try {
			gv.readBIF(XMLBIF);
		} catch (BIFFormatException be) {
			System.err.println("unable to visualize BayesNet");
			be.printStackTrace();
		}
		gv.layoutGraph();

		jf.getContentPane().add(gv, BorderLayout.CENTER);
		jf.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				jf.dispose();
			}
		});
		jf.setVisible(true);
	}

}