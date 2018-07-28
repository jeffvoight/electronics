
package org.onebeartoe.rpi.rgb.led.matrix.webapp.settings;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.onebeartoe.io.ObjectSaver;
import org.onebeartoe.rpi.rgb.led.matrix.webapp.RaspberryPiRgbLedMatrixServlet;

/**
 *
 * @author Roberto Marquez <https://www.youtube.com/user/onebeartoe>
 */
@WebServlet(name = "SettingsServet", urlPatterns = {"/settings/*"})
public class SettingsServlet extends RaspberryPiRgbLedMatrixServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {      
        doResponse(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String stillsDirtory = request.getParameter("stillImagesDirectory");
        ledMatrix.setStillImagesPath(stillsDirtory);
        
        String animationsDirectory = request.getParameter("animationsDirectory");
        ledMatrix.setAnimationsPath(animationsDirectory);
        
        String rpiRgbLedMatrixHome = request.getParameter("rpiRgbLedMatrixHome");
        ledMatrix.setRpiLgbLedMatrixHome(rpiRgbLedMatrixHome);
        
        String s = request.getParameter("commandLineFlags");
        if(s == null)
        {
            s = "";
        }
        String[] flags = s.split("\\s+");
        ledMatrix.setCommandLineFlags(flags);

        String stillImagesCommandLineFlags = request.getParameter("stillImagesCommandLineFlags");
        if(stillImagesCommandLineFlags == null)
        {
            s = "";
        }
        String[] stillImagesCommandLineflags = stillImagesCommandLineFlags.split("\\s+");
        ledMatrix.setStillImagesCommandLineFlags(stillImagesCommandLineflags);

        
        File outfile = RaspberryPiRgbLedMatrixServlet.configFile;
        ObjectSaver.encodeObject(ledMatrix, outfile);
        
        String saveMessages = "Settings changes were saved.";        
        request.setAttribute("saveMessages", saveMessages);
        doResponse(request, response);
    }

    private void doResponse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setAttribute("ledMatrix", ledMatrix);
        
        List list = Arrays.asList( ledMatrix.getCommandLineFlags() );
        List list2 = Arrays.asList(ledMatrix.getStillImagesCommandLineFlags());
        String rpiRgbLedMatrix2 = String.join(" ", list2);
        String rpiRgbLedMatrixHome = String.join(" ", list);                
        request.setAttribute("commandLineFlags", rpiRgbLedMatrixHome);
        request.setAttribute("stillImagesCommandLineFlags", rpiRgbLedMatrix2);        
        ServletContext c = getServletContext();
        RequestDispatcher rd = c.getRequestDispatcher("/WEB-INF/jsp/settings/index.jsp");
        rd.forward(request, response);        
    }
}
