
package org.onebeartoe.rpi.rgb.led.matrix.webapp.scrolling.text;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.onebeartoe.io.ObjectSaver;
import org.onebeartoe.rpi.rgb.led.matrix.queue.ScrollItem;
import org.onebeartoe.rpi.rgb.led.matrix.queue.ScrollQueue;
import org.onebeartoe.rpi.rgb.led.matrix.webapp.RaspberryPiRgbLedMatrixServlet;

/**
 * Here is a sample command with the --text (-t) option to specify the text to 
 * scroll on the RGB LED matrix:
 * 
 *      sudo ./runtext.py -t "I love-you! <3"
 * 
 * @author Roberto Marquez <https://www.youtube.com/user/onebeartoe>
 */
@WebServlet(name = "ScrollingTextServet", urlPatterns = {"/scrolling-text/*"})
public class ScrollingTextServlet extends RaspberryPiRgbLedMatrixServlet
{
    private static ScrollQueue scrollQueue;
    
    public ScrollingTextServlet(){
        super();
        if(scrollQueue==null){
 
            scrollQueue=new ScrollQueue();
            scrollQueue.start();
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {      
        doResponse(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String text = request.getParameter("text");
        String color = request.getParameter("color");
        logger.log(Level.INFO, "Color: {0}", color);
        boolean isCommand=false;
        boolean isActive=true;
        String saveMessages;
        scrollQueue.setLedMatrix(ledMatrix);
        try
        {
            scrollQueue.addItem(new ScrollItem(text, color, isActive, isCommand));
            scrollQueue.addItem(new ScrollItem("sample1", "#ff00ff", true, false));
            scrollQueue.addItem(new ScrollItem("sample2************************", "#ff00ff", false, false));
            scrollQueue.addItem(new ScrollItem("sample3", "#ff00ff", true, false));
            saveMessages = "The scrolling text was updated.";
        }
        catch(Exception e)
        {
            saveMessages = "An error occured while updating the scrolling text: "
                            + e.getMessage();
            
            logger.log(Level.SEVERE, saveMessages, e);
            
            e.printStackTrace();
        }

        // save the updated scrolling text configuration
        File outfile = RaspberryPiRgbLedMatrixServlet.configFile;
        ObjectSaver.encodeObject(ledMatrix, outfile);
        
        request.setAttribute("responseMessages", saveMessages);
        doResponse(request, response);
    }
    
    private void doResponse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setAttribute("ledMatrix", ledMatrix);
                
        ServletContext c = getServletContext();
        RequestDispatcher rd = c.getRequestDispatcher("/WEB-INF/jsp/scrolling-text/index.jsp");
        rd.forward(request, response);        
    }    
}
