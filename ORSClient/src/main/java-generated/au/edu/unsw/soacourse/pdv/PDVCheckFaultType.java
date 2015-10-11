
package au.edu.unsw.soacourse.pdv;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PDVCheckFaultType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PDVCheckFaultType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="InvalidLicenseNo"/&gt;
 *     &lt;enumeration value="InvalidFullName"/&gt;
 *     &lt;enumeration value="InvalidPostcode"/&gt;
 *     &lt;enumeration value="ProgramError"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PDVCheckFaultType")
@XmlEnum
public enum PDVCheckFaultType {

    @XmlEnumValue("InvalidLicenseNo")
    INVALID_LICENSE_NO("InvalidLicenseNo"),
    @XmlEnumValue("InvalidFullName")
    INVALID_FULL_NAME("InvalidFullName"),
    @XmlEnumValue("InvalidPostcode")
    INVALID_POSTCODE("InvalidPostcode"),
    @XmlEnumValue("ProgramError")
    PROGRAM_ERROR("ProgramError");
    private final String value;

    PDVCheckFaultType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PDVCheckFaultType fromValue(String v) {
        for (PDVCheckFaultType c: PDVCheckFaultType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
