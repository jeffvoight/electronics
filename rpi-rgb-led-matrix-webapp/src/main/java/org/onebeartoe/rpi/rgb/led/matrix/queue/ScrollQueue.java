/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.onebeartoe.rpi.rgb.led.matrix.queue;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.onebeartoe.rpi.rgb.led.matrix.RaspberryPiRgbLedMatrix;

/**
 *
 * @author jeff
 */
public class ScrollQueue extends Thread {
    private final static LinkedHashMap<String,ScrollItem> items=new LinkedHashMap();
    private ScrollItem currentItem;
    private Iterator<Map.Entry<String, ScrollItem>> iterator=items.entrySet().iterator();
    private boolean running=false;
    private RaspberryPiRgbLedMatrix ledMatrix;
    
    public ScrollQueue(RaspberryPiRgbLedMatrix ledMatrix){
        this.ledMatrix=ledMatrix;
    }
    
    public void run(){
        running=true;
        while(running){
            ScrollItem item=nextItem();
            if(item.getActive()){
                try {
                    ledMatrix.setColor(item.getColor());
                    ledMatrix.setScrollingText(item.getText());
                    ledMatrix.startScrollingTextCommand(item.getText());
                } catch (IOException ex) {
                    Logger.getLogger(ScrollQueue.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    
    public void stopIt(){
        running=false;     
    }
    
    public void addItem(ScrollItem item){
        if(items.isEmpty()){
            currentItem=item;
        }
        items.put(item.getText(), item);
    }
    
    public void removeItem(ScrollItem item){
        items.remove(item.getText());
    }
    
    public ScrollItem nextItem(){
        if(items.isEmpty()){
            return null;
        }
        
        if(!iterator.hasNext()){ // The end of the iterator is here. Start over.
            iterator=items.entrySet().iterator();
        }
        
        return iterator.next().getValue();
    }
    
    public LinkedHashMap<String,ScrollItem> getItems(){
        return items;
    }
    
}
