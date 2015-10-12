
package au.edu.unsw.soacourse.pdv;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.0.4
 * 2015-10-13T00:08:03.877+11:00
 * Generated source version: 3.0.4
 */

@WebFault(name = "PDVCheckFault", targetNamespace = "http://pdv.soacourse.unsw.edu.au")
public class PDVCheckFaultMsg extends Exception {
    
    private au.edu.unsw.soacourse.pdv.PDVCheckFault pdvCheckFault;

    public PDVCheckFaultMsg() {
        super();
    }
    
    public PDVCheckFaultMsg(String message) {
        super(message);
    }
    
    public PDVCheckFaultMsg(String message, Throwable cause) {
        super(message, cause);
    }

    public PDVCheckFaultMsg(String message, au.edu.unsw.soacourse.pdv.PDVCheckFault pdvCheckFault) {
        super(message);
        this.pdvCheckFault = pdvCheckFault;
    }

    public PDVCheckFaultMsg(String message, au.edu.unsw.soacourse.pdv.PDVCheckFault pdvCheckFault, Throwable cause) {
        super(message, cause);
        this.pdvCheckFault = pdvCheckFault;
    }

    public au.edu.unsw.soacourse.pdv.PDVCheckFault getFaultInfo() {
        return this.pdvCheckFault;
    }
}
