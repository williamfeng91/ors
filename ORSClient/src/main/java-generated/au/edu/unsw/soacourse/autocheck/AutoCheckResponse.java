
package au.edu.unsw.soacourse.autocheck;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="pdvResult" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="crvResult" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "pdvResult",
    "crvResult"
})
@XmlRootElement(name = "AutoCheckResponse")
public class AutoCheckResponse {

    @XmlElement(required = true)
    protected String pdvResult;
    @XmlElement(required = true)
    protected String crvResult;

    /**
     * Gets the value of the pdvResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPdvResult() {
        return pdvResult;
    }

    /**
     * Sets the value of the pdvResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPdvResult(String value) {
        this.pdvResult = value;
    }

    /**
     * Gets the value of the crvResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCrvResult() {
        return crvResult;
    }

    /**
     * Sets the value of the crvResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCrvResult(String value) {
        this.crvResult = value;
    }

}
