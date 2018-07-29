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
public class ScrollItem {
  
    private final String text;
    private final String color;
    private final boolean isCommand;
    private boolean isActive;
    private int priority;
    
    public ScrollItem(String text, String color, boolean isActive, boolean isCommand){
        this.text=text;
        this.color=color;
        this.isActive=isActive;
        this.isCommand=isCommand;
    }
    
    public boolean getActive(){
        return isActive;
    }
    
    public void setActive(boolean isActive){
        this.isActive=isActive;
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
        if(isCommand){
            try {
                Process exec = Runtime.getRuntime().exec(text);
                BufferedReader buff=new BufferedReader(new InputStreamReader(exec.getInputStream()));
                int waitFor = exec.waitFor();
                if(buff.ready()){
                    return buff.readLine();
                }
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(ScrollItem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return text;
    }
    
    public String getColor(){
        return color;
    }
}
