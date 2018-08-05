package org.onebeartoe.rpi.rgb.led.matrix.webapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.onebeartoe.io.ObjectRetriever;
import org.onebeartoe.rpi.rgb.led.matrix.RaspberryPiRgbLedMatrix;
import org.onebeartoe.rpi.rgb.led.matrix.queue.ScrollItem;
import org.onebeartoe.rpi.rgb.led.matrix.queue.ScrollQueue;
import org.onebeartoe.system.OperatingSystem;

/**
 *
 * @author Roberto Marquez <https://www.youtube.com/user/onebeartoe>
 */
public abstract class RaspberryPiRgbLedMatrixServlet extends HttpServlet {

    protected OperatingSystem os = new OperatingSystem();
    ;

    protected Logger logger;

    public static final String LED_MATRIX_HAT_CONTEXT_KEY = "LED_MATRIX_HAT_CONTEXT_KEY";

    protected static RaspberryPiRgbLedMatrix ledMatrix;
    protected static ScrollQueue scrollQueue;

    String configDirPath = os.currentUserHome() + "/.onebeartoe/rpi-rgb-led-matrix-webapp/";
    File configDir = new File(configDirPath);
    static protected File configFile;
    static protected File queueFile;
    String rpiLgbLedMatrixHome = "/home/pi/rpi-rgb-led-matrix";
    String badUserhome = "/home/pi/";
    String animationsPath = "/home/pi/rpi-rgb-led-matrix-images/animations/";
    String stillImagesPath = "/home/pi/rpi-rgb-led-matrix-images/stills/";
    String[] stillImagesCommandLineFlags = {"--led-cols=64", "--led-chain=2", "--led-slowdown-gpio=2", "--led-gpio-mapping=adafruit-hat"};
    String[] commandLineFlags = {"--led-cols=64", "--led-chain=2", "--led-slowdown-gpio=2", "--led-gpio-mapping=adafruit-hat"};

    private void adjustIfOnWindows() {
        if (os.seemsLikeMsWindows()) {
            // adjust the target host from explicit paths on Raspberry Pi to 
            // development development location (user home).
            String userhome = os.currentUserHome() + "/";
            animationsPath = ledMatrix.getAnimationsPath().replace(badUserhome, userhome);
            stillImagesPath = ledMatrix.getStillImagesPath().replace(badUserhome, userhome);

            ledMatrix.setAnimationsPath(animationsPath);
            ledMatrix.setStillImagesPath(stillImagesPath);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description for " + getClass().getName();
    }

    /**
     * This initializes the RaspberryPiRgbLedMatrix object referenced throughout
     * the application.
     *
     * If the RaspberryPiRgbLedMatrix object retrieved from persistence has a
     * null value for either the animations or still images directory, the
     * defaults values are used and directories for the default values are
     * created.
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        super.init();

        logger = Logger.getLogger(getClass().getName());
        configDir.mkdirs();
        configFile = new File(configDir, "led-matrix-webapp.xml");
        queueFile = new File(configDir, "scrollQueue.xml");
        if (ledMatrix == null || scrollQueue == null) {
            initializeLedMatix();
        }
    }

    private void initializeLedMatix() {
        ServletContext servletContext = getServletContext();
        ledMatrix = (RaspberryPiRgbLedMatrix) servletContext.getAttribute(LED_MATRIX_HAT_CONTEXT_KEY);
        if (ledMatrix == null) { // this is the first time the application loads this servlet
            restoreLedMatrixFromPersistence();
            loadDefaults();
            adjustIfOnWindows();
            servletContext.setAttribute(LED_MATRIX_HAT_CONTEXT_KEY, ledMatrix);  // make the RaspberryPiRgbLedMatrix object available to the servlet context
        }
        if (scrollQueue == null) {
            restoreScrollQueueFromPersistence();
        }
    }

    private void loadDefaults() {

        ledMatrix.setRpiLgbLedMatrixHome(rpiLgbLedMatrixHome);
        ledMatrix.setStillImagesPath(stillImagesPath);
        ledMatrix.setAnimationsPath(animationsPath);
        File animationsDir = new File(animationsPath);
        animationsDir.mkdirs();
        File stillImagesDirectory = new File(stillImagesPath);
        stillImagesDirectory.mkdirs();

        ledMatrix.setStillImagesCommandLineFlags(stillImagesCommandLineFlags);

        // default to what worked with this panel and hat
        //      https://www.adafruit.com/products/1484
        //      https://www.adafruit.com/products/2345
        //
        ledMatrix.setCommandLineFlags(commandLineFlags);

    }

    private void restoreLedMatrixFromPersistence() {
        Object object = null;
        try {
            object = ObjectRetriever.decodeObject(configFile);
            ledMatrix = (RaspberryPiRgbLedMatrix) object;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RaspberryPiRgbLedMatrixServlet.class.getName()).log(Level.SEVERE, "LED NOT INITIALIZED PROPERLY * * * * * * * * * * * * * * * * * *", ex);
            ledMatrix = new RaspberryPiRgbLedMatrix();
        }
    }

    private void restoreScrollQueueFromPersistence() {
        Object restoreObject = null;
        scrollQueue = new ScrollQueue();

        try {
            restoreObject = ObjectRetriever.decodeObject(queueFile);
            scrollQueue.addAll((List<ScrollItem>) restoreObject);
        } catch (Exception ex) {
            Logger.getLogger(RaspberryPiRgbLedMatrixServlet.class.getName()).log(Level.SEVERE, "No ScrollQueue found. Starting fresh.");
            scrollQueue.getItems(); // side effect of filling the queue
        }
        
        logger.log(Level.INFO, "Starting scrollqueue.");
        if (!scrollQueue.isAlive()) {
            scrollQueue.start();        // set up the default image/animation paths
        }
    }
}
