/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chart;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.function.Function2D;
import org.jfree.data.function.PolynomialFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;


public class ParabolaDemo extends ApplicationFrame {

    /*
     * @param title  the frame title.
     */
    int flag = 0;
    double delta = 0;
    double px = 0.0, py = 0.0, prx = 0.0, pry = 0.0, chartpx = 0.0, chartpy = 0.0, 
            chartX = 0.0, chartY = 0.0;
    int windowheight = 270;
    public ParabolaDemo(final String title) {

        super(title);
        double[] a = {0.0, 0.0, 1.0};
        Function2D p = new PolynomialFunction2D(a);
        double lrange = -20.0;
        double rrange = 20.0;
        XYDataset dataset = DatasetUtilities.sampleFunction2D(p, lrange, rrange, 1000, "y = f(x)");
        
        final XYSeries series3 = new XYSeries("Third");
        series3.add(3.0, 4.0);
        series3.add(4.0, 3.0);
        series3.add(5.0, 2.0);
        series3.add(6.0, 3.0);
        series3.add(7.0, 6.0);
        series3.add(8.0, 3.0);
        series3.add(9.0, 4.0);
        series3.add(10.0, 3.0);
        
       final XYSeriesCollection dataset1 = new XYSeriesCollection();
       dataset1.addSeries(series3);
        
        
        double max = 0.0;

        if (a[2] < 0) {
            max = p.getValue(-a[1] / 2*a[2]);
        }
        else {
            if (p.getValue(lrange) > p.getValue(rrange))
                max = p.getValue(lrange);
            else
                max = p.getValue(rrange);
        }
        System.out.println("max = " + max);
        delta = max / 16.4;

        final JFreeChart chart = ChartFactory.createXYLineChart(
            "Parabola",
            "X", 
            "Y", 
            dataset1,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.addMouseListener(new CustomListener());
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
        chartPanel.setPreferredSize(new java.awt.Dimension(500, windowheight));
        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(false);
        setContentPane(chartPanel);
    }

    public static void main(final String[] args) {

        final ParabolaDemo demo = new ParabolaDemo("Parabola Plot Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
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
}