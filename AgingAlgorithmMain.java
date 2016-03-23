package PageReplacement;


import java.io.*;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class AgingAlgorithmMain  {

	     int page = 0;
	     int memReferences =0;
		 int frames[] ;//= new int[1000];
		 int counter[];// = new int[1000];
		 int pageFaults;
		 //int pf = 0;
		boolean found = false;
		boolean emptyFrame = false;
		final XYSeries pageFaultGraph = new XYSeries("PageFaults");
		 final XYSeriesCollection dataset = new XYSeriesCollection();
		 
		private void initializeFrames(){
		for(int i=0;i<frames.length;i++){
			 frames[i] = -1;
		     counter[i] = 0;
		 }
		}
		
		private void resetCounter(){
			 for(int k=0;k<counter.length;k++){
	    		 counter[k] = counter[k]/2;
	    		 
			 }
		}
 
		
		public void runAlgo() throws IOException{
			for(int l=100;l<1100;l+=100){
				frames = new int[l];
				counter = new int[l];
				pageFaults=0;
			 initializeFrames();
			
		 FileReader fr = new FileReader ("M:\\file.txt"); //Reads the input file       
	       BufferedReader br = new BufferedReader (fr);
	       
	       String line = br.readLine();
	       
	       while(line!=null){
               memReferences++;
	    	   resetCounter();
			   page = Integer.parseInt(line);
			 for(int j=0;j<frames.length;j++){
				 if(frames[j]==page){ 
						 counter[j] +=  128;
						 found = true;
						 break;	 
				 } 
			 }
			      if(!found){
			    	  pageFaults++;
					 for(int k=0;k<frames.length;k++){
						 if(frames[k]== -1){
							
					         frames[k] = page;
					         counter[k] += 128;
					         emptyFrame = true;
					         break;
					        }
					 }
					     if(!emptyFrame){
					    	 
					    	  int temp = 257;
					    	  int index = 0;
					    	 for(int k=0;k<counter.length;k++){
					    		
					    		 if(counter[k]<temp){
					    			 temp = counter[k];
					    			 index= k;
					    		 }
					    	 }
					    	 frames[index] = page;
					    	 counter[index] = 128;	  
					 }
			       }
			        
			        found =false;
			        emptyFrame = false;
		            line = br.readLine();  
	}
	        int pf = pageFaults/1000;
	        pageFaultGraph.add(l,pf);
	        br.close();
		}
			dataset.addSeries(pageFaultGraph);
			GraphPlotting chart = new GraphPlotting("Aging Algorithm", "Frames = "+frames.length,dataset);
		       chart.pack();
	 	        chart.setVisible(true);
		     //System.out.println("Page faults : "+ pageFaults);
		     //System.out.println("Memory References : "+ pf);
		}

		public class GraphPlotting extends JFrame{
			
			 private static final long serialVersionUID = 1L;
			 
			 public GraphPlotting(String applicationTitle,String chartTitle, XYDataset dataset){
				 
				 super(applicationTitle);
				 JFreeChart pageFaultChart = ChartFactory.createXYLineChart(chartTitle, "Page Frames","Page Faults/1000memory references", 
						 dataset,PlotOrientation.VERTICAL,true,true,false);
				 XYPlot axis = pageFaultChart.getXYPlot();
				 NumberAxis domain = (NumberAxis) axis.getDomainAxis();
			        domain.setRange(0, 1000);
			        domain.setTickUnit(new NumberTickUnit(100));
			        domain.setVerticalTickLabels(true);
			        NumberAxis range = (NumberAxis) axis.getRangeAxis();
			        range.setRange(0, 20);
			        range.setTickUnit(new NumberTickUnit(2));
				 ChartPanel chartPanel = new ChartPanel(pageFaultChart);
				 chartPanel.setPreferredSize(new java.awt.Dimension(1000, 500));
				 setContentPane(chartPanel);
			 }
		}
		
	       public static void main(String[] args) throws IOException {
	    	   new AgingAlgorithmMain().runAlgo();
	       }
}


