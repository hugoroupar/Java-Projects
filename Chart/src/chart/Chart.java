/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chart;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author Hugo.Leite
 */
public class Chart {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        String filename = "D:\\Users\\hugo.leite\\Documents\\Moodle\\Common Errors\\data2.csv";
        File file = new File(filename);
        ArrayList<Coordinates> coordinatesList = new ArrayList<Coordinates>();
        
        int i = 0;
        
        try{
        Scanner inputStream = new Scanner(file);
        
        while(inputStream.hasNext()){
           
           Coordinates c = new Coordinates("","");
           String data = inputStream.nextLine();
          
           String[] values = data.split(";");
           java.util.Date time;
           time = new java.util.Date(Long.parseLong(values[0])*1000);
           //System.out.println(time);
            
           c.setX(values[0]);
           c.setY(values[1]);
           coordinatesList.add(c);
           //System.out.println(values[0]+" "+values[1]+" "+i++);
           
            
        }
     /*VERIFY COORDINATES
        for(i = 0; i < coordinatesList.size(); i++){
            System.out.println(coordinatesList.get(i).x+"@"+coordinatesList.get(i).y);
        }
        */
        
        
         plotMyChart(coordinatesList);
        
        
        
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
    
    public static void plotMyChart(ArrayList<Coordinates> aux){
        
        
        CrosshairOverlayDemo1 app = new CrosshairOverlayDemo1(
                        "Hugo Chart", aux);
                app.pack();
                app.setVisible(true);
        
        final plotData demo = new plotData("CENAS",aux);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
       // demo.setVisible(true);
    }
    
}
