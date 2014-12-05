/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chart;

/**
 *
 * @author Hugo.Leite
 */
public class Coordinates {
    
    String x;
    String y;
    
    public Coordinates(String x, String y){
        this.x = x;
        this.y = y; 
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public void setX(String x) {
        this.x = x;
    }

    public void setY(String y) {
        this.y = y;
    }
    
    
    
}
