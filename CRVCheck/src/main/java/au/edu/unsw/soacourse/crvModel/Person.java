package au.edu.unsw.soacourse.crvModel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "person")
@XmlAccessorType(XmlAccessType.FIELD)
public class Person {
	
	@XmlElement(name = "licenseNo")
    private String licenseNo;
	@XmlElementWrapper(name = "criminalRecords")
	@XmlElement(name = "criminalRecord")
    private List<CriminalRecord> criminalRecords = new ArrayList<CriminalRecord>();

    public Person() {

    }

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public List<CriminalRecord> getCriminalRecords() {
		return criminalRecords;
	}

	public void setCriminalRecords(List<CriminalRecord> criminalRecords) {
		this.criminalRecords = criminalRecords;
	}
}
