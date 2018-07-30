
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
        String[] texts=request.getParameterValues("text");
        String[] colors=request.getParameterValues("color");
        String[] actives=request.getParameterValues("active");
        //String text = request.getParameter("text");
        //String color = request.getParameter("color");
        //boolean isCommand=false;
        //boolean isActive=true;
        String saveMessages;
        scrollQueue.setLedMatrix(ledMatrix);
        scrollQueue.clear();
        try {
            for(int i=0;i<texts.length;i++){
                if(!texts[i].trim().equals("")){
                    scrollQueue.addItem(new ScrollItem(texts[i], colors[i], actives[i].equals("checked"), false));
                }
            }
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
        //ObjectSaver.encodeObject(scrollQueue, outfile);
        
        request.setAttribute("responseMessages", saveMessages);
        doResponse(request, response);
    }
    
    private void doResponse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setAttribute("ledMatrix", ledMatrix);
        //request.setAttribute("scrollQueue", scrollQueue);
        ServletContext c = getServletContext();
        RequestDispatcher rd = c.getRequestDispatcher("/WEB-INF/jsp/scrolling-text/index.jsp");
        rd.forward(request, response);        
    }    
}
