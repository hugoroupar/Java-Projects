/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chart;

import java.awt.Color;
import static java.awt.Color.GREEN;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.Zoomable;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.Function2D;
import org.jfree.data.function.PolynomialFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author Hugo.Leite
 */
public class plotData  extends ApplicationFrame implements MouseWheelListener{

    ArrayList<Coordinates> xy;
    double zoomFactor;
         
      final XYDataset dataset;
      final JFreeChart chart;
      final ChartPanel chartPanel; 
          int flag = 0;
      
       double px = 0.0, py = 0.0, prx = 0.0, pry = 0.0, chartpx = 0.0, chartpy = 0.0, 
            chartX = 0.0, chartY = 0.0;
   
        
        
    public plotData(final String f, ArrayList<Coordinates> xy) {
        //public plotData(ArrayList<Coordinates> xy) {
        
        super(f);
        this.xy = xy;
        
        this.dataset = createDataset();
        this.chart = createChart(dataset);
        this.chartPanel = new ChartPanel(chart);
        
        
        ///////////////
        chartPanel.addChartMouseListener(new ChartMouseListener() {

            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {

            }

            @Override
            public void chartMouseMoved(ChartMouseEvent cme) {
                ChartEntity ce = cme.getEntity();
                if (ce instanceof XYItemEntity) {
                    XYItemEntity e = (XYItemEntity) ce;
                    XYDataset d = ((XYItemEntity) ce).getDataset();
                    int i = ((XYItemEntity) ce).getItem();
                    chartpx = d.getXValue(0, i);
                    chartpy = d.getYValue(0, i);
                    //System.out.println("X:" + chartpx + ", Y:" + chartpy);
                }
                Point2D po = chartPanel.translateScreenToJava2D(cme.getTrigger().getPoint());
                Rectangle2D plotArea = chartPanel.getScreenDataArea();
                XYPlot plot = (XYPlot) chart.getPlot(); // your plot
                chartX = plot.getDomainAxis().java2DToValue(po.getX(), plotArea, plot.getDomainAxisEdge());
                chartY = plot.getRangeAxis().java2DToValue(po.getY(), plotArea, plot.getRangeAxisEdge());
                System.out.println("X:" + chartX + ", Y:" + chartY);
            }
        });
        
        /////////////////
      
       
        this.zoomFactor = 0.10;
        this.chartPanel.addMouseWheelListener(this);
       
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }
    
     public double getZoomFactor() {
        return this.zoomFactor;
    }
     public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }
    public void mouseWheelMoved(MouseWheelEvent e) {
        JFreeChart chart = this.chartPanel.getChart();
        if (chart == null) {
            return;
        }
        Plot plot = chart.getPlot();
        if (plot instanceof Zoomable) {
            Zoomable zoomable = (Zoomable) plot;
            handleZoomable(zoomable, e);
        }
        // TODO:  here we could handle non-zoomable plots in interesting
        // ways (for example, the wheel could rotate a PiePlot or just zoom
        // in on the whole panel).
    }
    
    private void handleZoomable(Zoomable zoomable, MouseWheelEvent e) {
        Plot plot = (Plot) zoomable;
        ChartRenderingInfo info = this.chartPanel.getChartRenderingInfo();
        PlotRenderingInfo pinfo = info.getPlotInfo();
        Point2D p = this.chartPanel.translateScreenToJava2D(e.getPoint());
        if (!pinfo.getDataArea().contains(p)) {
            return;
        }
        int clicks = e.getWheelRotation();
        int direction = 0;
        if (clicks < 0) {
            direction = -1;
        }
        else if (clicks > 0) {
            direction = 1;
        }

        boolean old = plot.isNotify();

        // do not notify while zooming each axis
        plot.setNotify(false);
        double increment = 1.0 + this.zoomFactor;
        if (direction > 0) {
            zoomable.zoomDomainAxes(increment, pinfo, p, true);
            zoomable.zoomRangeAxes(increment, pinfo, p, true);
        }
        else if (direction < 0) {
            zoomable.zoomDomainAxes(1.0 / increment, pinfo, p, true);
            zoomable.zoomRangeAxes(1.0 / increment, pinfo, p, true);
        }
        // set the old notify status
        plot.setNotify(old);

    }
    
    public void chartMouseClicked(ChartMouseEvent e) {
    MouseEvent me = e.getTrigger();
    if ( me.getModifiers() == 16 ) { //Left Button ?
        if ( me.getClickCount() == 2) { //Double Click?
            me.consume();
            XYItemEntity entity = (XYItemEntity) e.getEntity();
            XYDataset dataset = entity.getDataset();
            int series = entity.getSeriesIndex();
            int item = entity.getItem();
            Comparable seriesKey = dataset.getSeriesKey(series);
        }
    }
}
    public void chartMouseMoved(final ChartMouseEvent event){
                    int newX = event.getTrigger().getX();
                    int newY = event.getTrigger().getY();
                    System.out.println("chartMouseMoved to " + newX + " " + newY);
                }
  
    
    
   
    
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    private XYDataset createDataset() {
        
        final XYSeries series1 = new XYSeries("First");
        int i;
        for(i = 0; i < this.xy.size();i++){
            //for(i = 0; i < 35;i++){
           // series1.add(Double.parseDouble(this.xy.get(i).x), Double.parseDouble(this.xy.get(i).y));
           // System.out.println(i);
             //series1.add(Double.parseDouble(this.xy.get(i).x), Double.parseDouble(this.xy.get(i).y));
             series1.add(i, Double.parseDouble(this.xy.get(i).y));
        }
        

        final XYSeries series2 = new XYSeries("Second");
         for(i = 0; i < this.xy.size();i++){
           // series2.add(Double.parseDouble(this.xy.get(i).x), Double.parseDouble(this.xy.get(i).y));
        }
        
       

        final XYSeries series3 = new XYSeries("Third");
        series3.add(3.0, 4.0);
        series3.add(4.0, 3.0);
        series3.add(5.0, 2.0);
        series3.add(6.0, 3.0);
        series3.add(7.0, 6.0);
        series3.add(8.0, 3.0);
        series3.add(9.0, 4.0);
        series3.add(10.0, 3.0);

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);
                
        return dataset;
        
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {
        
      
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            "Hugo ",      // chart title
            "X",                      // x axis label
            "Y",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

//        final StandardLegend legend = (StandardLegend) chart.getLegend();
  //      legend.setDisplaySeriesShapes(true);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        
       
//        Rectangle2D plotArea = chartPanel.getScreenDataArea();
        double[] a = {0.0, 0.0, 1.0};
            
        Function2D p = new PolynomialFunction2D(a);
        double lrange = -20.0;
        double rrange = 20.0;
        XYDataset ooo = DatasetUtilities.sampleFunction2D(p, lrange, rrange, 1000, "y = f(x)");
       
        
        
        
        plot.setBackgroundPaint(Color.lightGray);
    //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        return chart;
        
    }
    
    public class CustomListener implements MouseListener {

          public void mouseClicked(MouseEvent e) {

          }

          public void mouseEntered(MouseEvent e) {

          }

          public void mouseExited(MouseEvent e) {

          }

          public void mousePressed(MouseEvent e) {
              flag = 1;
              px = chartpx;
              py = chartpy;
              System.out.println("Mouse Pressed! xpos = " + px + "; py = " + py);
          }

          public void mouseReleased(MouseEvent e) {
              flag = 1;
              prx = chartX;
              pry = chartY;
              System.out.println("Mouse Released! xpos = " + prx + "; py = " + pry);
          }
     }
    
   
    
    
   /* 
    public static void main(final String[] args) {
        ArrayList<Coordinates> aux = new ArrayList<Coordinates>();
        final plotData demo = new plotData("CENAS",aux);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }*/

    
}
