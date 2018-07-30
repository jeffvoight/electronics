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
    private final static List<ScrollItem> items = Collections.synchronizedList(new ArrayList<>());
    private ScrollItem currentItem;
    private Iterator<ScrollItem> iterator;
    private boolean running = false;
    private RaspberryPiRgbLedMatrix ledMatrix;
    Logger logger = Logger.getLogger("ScrollQueue");

    public ScrollQueue() {
        items.clear();
        iterator = items.iterator();
    }

    public void setLedMatrix(RaspberryPiRgbLedMatrix ledMatrix) {
        this.ledMatrix = ledMatrix;
    }

    public void clear() {
        logger.log(Level.INFO, "Clear");
        items.clear();
        iterator = items.iterator();
    }

    public void run() {
        running = true;
        while (running) {
            ScrollItem item;

            synchronized (items) {
                item = nextItem();
            }

            if (item != null) {
                currentItem = item;
                if (currentItem.getActive()) {
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
                }
            } else {
                try {
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

    public void addItem(ScrollItem item) {
        logger.log(Level.INFO, "Adding {0}", item.getText());
        synchronized (items) {
            items.add(item);
            iterator = items.iterator();
        }
    }

    public void removeItem(ScrollItem item) {
        synchronized (items) {
            items.remove(item);
            iterator = items.iterator();
        }
    }

    public ScrollItem nextItem() {
        synchronized (items) {
            if (items.isEmpty()) {
                return null;
            }
            logger.log(Level.INFO, "Next!");
            if (!iterator.hasNext()) { // The end of the iterator is here. Start over.
                logger.log(Level.INFO, "Start over!");
                iterator = items.iterator();
            }
            return iterator.next();
        }
    }

    public ScrollItem[] getItems() {
        ScrollItem[] returnItems = new ScrollItem[items.size()];
        return items.toArray(returnItems);
    }
}
