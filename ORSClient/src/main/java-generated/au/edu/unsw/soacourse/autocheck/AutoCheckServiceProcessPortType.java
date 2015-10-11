package au.edu.unsw.soacourse.autocheck;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;

/**
 * This class was generated by Apache CXF 3.0.4
 * 2015-10-11T21:07:18.212+11:00
 * Generated source version: 3.0.4
 * 
 */
@WebService(targetNamespace = "http://soacourse.unsw.edu.au/autocheck", name = "AutoCheckServiceProcessPortType")
@XmlSeeAlso({au.edu.unsw.soacourse.pdv.ObjectFactory.class, au.edu.unsw.soacourse.crv.ObjectFactory.class, ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface AutoCheckServiceProcessPortType {

    @WebResult(name = "AutoCheckResponse", targetNamespace = "http://soacourse.unsw.edu.au/autocheck", partName = "parameters")
    @Action(input = "http://soacourse.unsw.edu.au/autocheck/check", output = "http://soacourse.unsw.edu.au/autocheck/AutoCheckService/checkResponse")
    @WebMethod(action = "http://soacourse.unsw.edu.au/autocheck/check")
    public AutoCheckResponse check(
        @WebParam(partName = "parameters", name = "AutoCheckRequest", targetNamespace = "http://soacourse.unsw.edu.au/autocheck")
        AutoCheckRequest parameters
    );
}