
package sd.tp1.client.ws;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "ParseException", targetNamespace = "http://server.tp1.sd/")
public class ParseException_Exception
    extends java.lang.Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private ParseException faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public ParseException_Exception(String message, ParseException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public ParseException_Exception(String message, ParseException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: sd.tp1.client.ws.ParseException
     */
    public ParseException getFaultInfo() {
        return faultInfo;
    }

}
