
package au.edu.unsw.soacourse.crv;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CRVCheckFaultType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CRVCheckFaultType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="InvalidLicenseNo"/&gt;
 *     &lt;enumeration value="ProgramError"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CRVCheckFaultType")
@XmlEnum
public enum CRVCheckFaultType {

    @XmlEnumValue("InvalidLicenseNo")
    INVALID_LICENSE_NO("InvalidLicenseNo"),
    @XmlEnumValue("ProgramError")
    PROGRAM_ERROR("ProgramError");
    private final String value;

    CRVCheckFaultType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CRVCheckFaultType fromValue(String v) {
        for (CRVCheckFaultType c: CRVCheckFaultType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
