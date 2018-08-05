/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.onebeartoe.rpi.rgb.led.matrix.queue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.onebeartoe.io.ObjectRetriever;
import org.onebeartoe.rpi.rgb.led.matrix.RaspberryPiRgbLedMatrix;

/**
 *
 * @author jeff
 */
public class ScrollQueue extends Thread implements Serializable {

    private final int MAX_ENTRIES = 50;
    private final static List<ScrollItem> items = new ArrayList<>(); //Collections.synchronizedList(new ArrayList<>());
    private ScrollItem currentItem;
    private boolean running = false;
    private RaspberryPiRgbLedMatrix ledMatrix;
    Logger logger = Logger.getLogger("ScrollQueue");
    private final Object lock = new Object();
    private int currentIndex = 0;

    public ScrollQueue() {
    }

    public void setLedMatrix(RaspberryPiRgbLedMatrix ledMatrix) {
        this.ledMatrix = ledMatrix;
    }

    public void clear() {
        logger.log(Level.INFO, "Clear");
        items.clear();
        currentIndex = 0;
    }

    public void run() {
        running = true;
        while (running) {
            ScrollItem item = nextItem();

            if (item != null && item.getActive().equals("checked") && !item.getText().trim().equals("")) {
                currentItem = item;

                try {
                    logger.log(Level.INFO, "Current Item Processing {0}", currentItem.getText());
                    ledMatrix.setColor(currentItem.getColor());
                    ledMatrix.setScrollingText(currentItem.getText());
                    ledMatrix.startScrollingTextCommand(currentItem.getText());
                } catch (IOException ex) {
                    Logger.getLogger(ScrollQueue.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NullPointerException npe) {
                    Logger.getLogger(ScrollQueue.class.getName()).log(Level.SEVERE, "LedMatrix not yet initialized.");
                }

            } else {
                try {
                    logger.log(Level.INFO, "Sleeping for 1 second.");
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ScrollQueue.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    public void stopIt() {
        running = false;
    }

    public void addAll(ScrollItem[] items) {
        synchronized (lock) {
            for (int i = 0; i < items.length; i++) {
                this.items.add(items[i]);
            }
        }
    }

    public void addItem(ScrollItem item) {
        logger.log(Level.INFO, "Adding {0}", item.getText());
        synchronized (lock) {
            items.add(item);
        }
    }

    public void removeItem(ScrollItem item) {
        synchronized (lock) {
            items.remove(item);
        }
    }

    public ScrollItem nextItem() {
        synchronized (lock) {
            if (items.isEmpty()) {
                return null;
            }
            //logger.log(Level.INFO, "Next!");
            if (this.currentIndex++ >= items.size() - 1) { // The end of the iterator is here. Start over.
                logger.log(Level.INFO, "Start over!");
                currentIndex = 0;
            }
            return items.get(currentIndex);
        }
    }

    public List<ScrollItem> getItems() {
        synchronized (lock) {
            int diff = 10 - items.size();
            if (diff == 0) { // If we're at max, add one
                items.add(new ScrollItem());
            }
            for (; diff > 0; diff--) {
                items.add(new ScrollItem());
            }
            return items;
        }
        //return items.toArray(returnItems);
    }
}
