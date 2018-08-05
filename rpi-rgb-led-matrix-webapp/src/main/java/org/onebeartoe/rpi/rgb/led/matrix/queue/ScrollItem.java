/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.onebeartoe.rpi.rgb.led.matrix.queue;


/**
 *
 * @author jeff
 */
public class ScrollItem implements Comparable {
  
    private String text;
    private String color;
    private boolean isCommand;
    private String isActive;
    private int priority;
    
    public ScrollItem(){
        this.text="";
        this.color="#999999";
        this.isCommand=false;
        this.isActive="";
    }
    
    public ScrollItem(String text, String color, String isActive, boolean isCommand){
        this.text=text;
        this.color=color;
        this.isActive=isActive;
        this.isCommand=isCommand;
    }
    
    public String getActive(){
        return isActive;
    }
    
    public void setActive(String isActive){
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
        return text;
    }
    
    public void setText(String text){
        this.text=text;
    }
    
    public void setColor(String color){
        this.color=color;
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
