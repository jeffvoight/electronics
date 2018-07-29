/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.onebeartoe.rpi.rgb.led.matrix.queue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.onebeartoe.rpi.rgb.led.matrix.RaspberryPiRgbLedMatrix;

/**
 *
 * @author jeff
 */
public class ScrollQueue extends Thread {
    private final int MAX_ENTRIES=50;
    private final static List<ScrollItem> items=Collections.synchronizedList(new ArrayList<>());
    private ScrollItem item=new ScrollItem();
    private ScrollItem currentItem;
    private Iterator<ScrollItem> iterator;
    private boolean running=false;
    private RaspberryPiRgbLedMatrix ledMatrix;
    Logger logger=Logger.getLogger("ScrollQueue");

    public ScrollQueue(){
        iterator=items.iterator();
    }
    
    public void setLedMatrix(RaspberryPiRgbLedMatrix ledMatrix){
        this.ledMatrix=ledMatrix;
    }

    public void clear(){
        logger.log(Level.INFO, "Clear");
        items.clear();
        iterator=items.iterator();
    }
    
    public void run(){
        running=true;
        while(running){
            yield();
            synchronized(items){
            if(!items.isEmpty()){
                    ScrollItem item=nextItem();
                    if(item!=null){
                        currentItem=item;
                        if(currentItem.getActive()){
                            try {
                                logger.log(Level.INFO, "Current Item Processing "+currentItem.getText());
                                ledMatrix.setColor(currentItem.getColor());
                                ledMatrix.setScrollingText(currentItem.getText());
                                ledMatrix.startScrollingTextCommand(currentItem.getText());
                            } catch (IOException ex) {
                                Logger.getLogger(ScrollQueue.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (NullPointerException npe){
                                Logger.getLogger(ScrollQueue.class.getName()).log(Level.SEVERE, "LedMatrix not yet initialized.");
                            }
                        }
                    }
                    
                }
            }
        }
    }
    
    
    public void stopIt(){
        running=false;     
    }
    
    public void addItem(ScrollItem item){
        logger.log(Level.INFO, "Adding "+item.getText());
        synchronized(items){
                items.add(item);
            }
    }
    
    public void removeItem(ScrollItem item){
        synchronized(items){
            items.remove(item);
            iterator=items.iterator();
        }
        
    }
    
    public ScrollItem nextItem(){
        logger.log(Level.INFO, "Next! ************");
            if(items.isEmpty()){
                return null;
            }

            if(!iterator.hasNext()){ // The end of the iterator is here. Start over.
                logger.log(Level.INFO, "Start over!");
                iterator=items.iterator();
            }
        
            synchronized(items){
                return iterator.next();
            }
        
    }
    
    public List<ScrollItem> getItems(){
        return items;
    }
    
}
