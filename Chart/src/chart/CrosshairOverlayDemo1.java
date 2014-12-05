/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chart;



import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.time.Day;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;

/**
 * A demo showing crosshairs that follow the data points on an XYPlot.
 */
public class CrosshairOverlayDemo1 extends JFrame implements ChartMouseListener {

    private ChartPanel chartPanel;

    private Crosshair xCrosshair;

    private Crosshair yCrosshair;
    
    private ArrayList<Coordinates> xy;

    public CrosshairOverlayDemo1(String title, ArrayList<Coordinates> xy) {
        super(title);
        this.xy = xy;
        setContentPane(createContent());
    }

    private JPanel createContent() {
        JFreeChart chart = createChart(createDataset());
        this.chartPanel = new ChartPanel(chart);
        this.chartPanel.setFillZoomRectangle(true);
        this.chartPanel.setMouseWheelEnabled(true);
        this.chartPanel.addChartMouseListener(this);
        CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
        this.xCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
        this.xCrosshair.setLabelVisible(true);
        this.yCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
        this.yCrosshair.setLabelVisible(true);
        crosshairOverlay.addDomainCrosshair(xCrosshair);
        crosshairOverlay.addRangeCrosshair(yCrosshair);
        chartPanel.addOverlay(crosshairOverlay);
        return chartPanel;
    }

    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart("Moodle Concurrency", 
                "X", "Y", dataset);
        return chart;
    }

    private XYDataset createDataset() {
        
        
        
        final XYSeries series1 = new XYSeries("Concurrency");
        int i;
        for(i = 0; i < this.xy.size();i++){
            //for(i = 0; i < 35;i++){
           // series1.add(Double.parseDouble(this.xy.get(i).x), Double.parseDouble(this.xy.get(i).y));
           // System.out.println(i);
             //series1.add(Double.parseDouble(this.xy.get(i).x), Double.parseDouble(this.xy.get(i).y));
             series1.add(Double.parseDouble(this.xy.get(i).x), Double.parseDouble(this.xy.get(i).y));
        }
        
        
        
        
        
       // final XYSeriesCollection dataset = new XYSeriesCollection();
        // dataset.addSeries(series1);
        
        
        
        ///TEST TIME/
       TimeSeries s1 = new TimeSeries("Hello");
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        
      for(i = 0; i < this.xy.size();i++){
             long l = Long.parseLong(this.xy.get(i).x);
             java.util.Date time=new java.util.Date((long)l*1000);
             System.out.println("Unix Time: "+Long.parseLong(this.xy.get(i).x)+" "+time);
             time.getDay();
             s1.add(new FixedMillisecond(time), Double.parseDouble(this.xy.get(i).y));
             //s1.add(time, Double.parseDouble(this.xy.get(i).y));
        }
        
        
        dataset.addSeries(s1);
        
        
        
        return dataset;
    }

    @Override
    public void chartMouseClicked(ChartMouseEvent event) {
        // ignore
    }

    @Override
    public void chartMouseMoved(ChartMouseEvent event) {
        Rectangle2D dataArea = this.chartPanel.getScreenDataArea();
        JFreeChart chart = event.getChart();
        XYPlot plot = (XYPlot) chart.getPlot();
        ValueAxis xAxis = plot.getDomainAxis();
        double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
                RectangleEdge.BOTTOM);
        double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
        this.xCrosshair.setValue(x);
        this.yCrosshair.setValue(y);
    }
/*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CrosshairOverlayDemo1 app = new CrosshairOverlayDemo1(
                        "Hugo Chart");
                app.pack();
                app.setVisible(true);
            }
        });
    }*/

}