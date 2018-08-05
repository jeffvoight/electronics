/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.onebeartoe.rpi.rgb.led.matrix.queue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author jeff
 */
public class ScrollItem implements Comparable {
  
    private String text;
    private String color;
    private boolean isCommand;
    private boolean isActive;
    private int priority;
    
    public ScrollItem(){
        this.text="";
        this.color="#ff00ff";
        this.isCommand=false;
        this.isActive=false;
    }
    
    public ScrollItem(String text, String color, boolean isActive, boolean isCommand){
        this.text=text;
        this.color=color;
        this.isActive=isActive;
        this.isCommand=isCommand;
    }
    
    public String getActive(){
        return (isActive?"on":"off");
    }
    
    public void setActive(String isActive){
        this.isActive=isActive.equals("on");
    }
    
    public void setPriority(int priority){
        if(priority>=0 && priority < 20){
            this.priority=priority;
        }
    }
    
    public int getPriority(){
        return priority;
    }
    
    public String getText(){
        return text;
    }
    
    public void setText(String text){
        this.text=text;
    }
    
    public String getColor(){
        if(color==null){
            return "#aa00ff";
        }
        return color;
    }

    @Override
    public int compareTo(Object o) {
        return this.getText().compareTo(((ScrollItem)o).getText());
    }
}
