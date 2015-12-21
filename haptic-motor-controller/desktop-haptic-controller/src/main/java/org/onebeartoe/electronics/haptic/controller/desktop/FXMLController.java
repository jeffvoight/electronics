
package org.onebeartoe.electronics.haptic.controller.desktop;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.io.OutputStream;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import org.onebeartoe.electronics.haptic.controller.datasheet.waveform.list.parser.DatasheetWaveformListParser;
import org.onebeartoe.io.serial.SerialPorts;

/**
 * This was tested with the Adafruit EZ Link (adafruit.com/products/1628) using this command:
 * 
 * java -Djava.library.path="c:\opt\rxtx" -jar target/serial-plotter-0.0.1-SNAPSHOT.jar
 * 
 * @author Roberto Marquez
 */
public class FXMLController 
//        extends Application 
        implements SerialPortEventListener,
                   Initializable
{    
    @FXML
    private Label label;
    
    @FXML
    private ChoiceBox dropdown;
    
    private SerialPort serialPort;
    
    private Map<Integer, String> hardcodedWaveforms;
    
    Logger logger;
    
    private OutputStream serialOutstream;

    @FXML
    private void handleButtonAction(ActionEvent event) 
    {
        int waveformId = dropdown.getSelectionModel().getSelectedIndex();
        
        label.setText("Haptic Controller: Waveform " + waveformId);
        
        sendWaveformId(waveformId);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        logger = Logger.getLogger( getClass().getName() );
        
        hardcodedWaveforms = DatasheetWaveformListParser.hardcodedWaveforms();
        
        List<String> effectNames = new ArrayList();

        Set<Integer> keySet = hardcodedWaveforms.keySet();
        
        for(Integer i : keySet)
        {
            String name = hardcodedWaveforms.get(i);
            effectNames.add(name);
        }        

        ObservableList<String> ol = FXCollections.observableList(effectNames);
        
        dropdown.setItems(ol);
        dropdown.getSelectionModel().selectFirst();
        
        dropdown.getSelectionModel().selectedIndexProperty().addListener( new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                int i = newValue.intValue();
                sendWaveformId(i);
            }
        });
        
        System.out.println("items added");
        
        initializeSerialPort();
    }
    
    private void initializeSerialPort()
    {
        try
        {
            serialPort = SerialPorts.get();
            serialOutstream = serialPort.getOutputStream();
            
// we are not using serial events in this app, so far
//            serialPort.addEventListener(this);
        }
        catch (Exception | NoClassDefFoundError ex)
        {
            String message = "The serial port was not obtained.";
            logger.log(Level.SEVERE, message, ex);
        }
    }
    
    private Integer nameToId(String name)
    {
        Set<Integer> keySet = hardcodedWaveforms.keySet();
        
        Integer id = 0;
        
        for(Integer i : keySet)
        {
            String currentName = hardcodedWaveforms.get(i);
            
            if( name.equals(currentName) )
            {
                id = i;
                
                break;
            }
        }
        
        return id;
    }
    
    private void sendWaveformId(int waveformId)
    {
        System.out.println("sending " + waveformId + " to serial port");
        String message = waveformId + "" + '\n';

        try
        {
            serialOutstream.write( message.getBytes() );
        }
        catch (IOException ex)
        {
            message = "could not send >" + waveformId + "< to serial port";
            logger.log(Level.SEVERE, message, ex);
        }
    }
    
    @Override
    public void serialEvent(SerialPortEvent spe)
    {
        String message = "Serial event: " + 
                         spe.getEventType() + ", " + 
                         spe.getNewValue() + ", " + 
                         spe.getOldValue() + ", " + 
                         spe.getSource();
        
        System.out.println(message);
    }
    
    public void shutdown()
    {
        SerialPorts.shutdown(serialPort);
    }    
}
