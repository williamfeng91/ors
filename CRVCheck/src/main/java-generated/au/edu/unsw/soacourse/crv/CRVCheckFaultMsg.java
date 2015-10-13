
package au.edu.unsw.soacourse.crv;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.0.4
 * 2015-10-13T11:27:23.725+11:00
 * Generated source version: 3.0.4
 */

@WebFault(name = "CRVCheckFault", targetNamespace = "http://crv.soacourse.unsw.edu.au")
public class CRVCheckFaultMsg extends Exception {
    
    private au.edu.unsw.soacourse.crv.CRVCheckFault crvCheckFault;

    public CRVCheckFaultMsg() {
        super();
    }
    
    public CRVCheckFaultMsg(String message) {
        super(message);
    }
    
    public CRVCheckFaultMsg(String message, Throwable cause) {
        super(message, cause);
    }

    public CRVCheckFaultMsg(String message, au.edu.unsw.soacourse.crv.CRVCheckFault crvCheckFault) {
        super(message);
        this.crvCheckFault = crvCheckFault;
    }

    public CRVCheckFaultMsg(String message, au.edu.unsw.soacourse.crv.CRVCheckFault crvCheckFault, Throwable cause) {
        super(message, cause);
        this.crvCheckFault = crvCheckFault;
    }

    public au.edu.unsw.soacourse.crv.CRVCheckFault getFaultInfo() {
        return this.crvCheckFault;
    }
}
