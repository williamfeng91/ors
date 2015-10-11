package au.edu.unsw.soacourse.ors.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "autoCheckResults")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoCheckResultList {
	
	@XmlElement(name = "autoCheckResult")
	private List<AutoCheckResult> autoCheckResultList;

	public List<AutoCheckResult> getAutoCheckResultList() {
		return autoCheckResultList;
	}

	public void setAutoCheckResultList(List<AutoCheckResult> autoCheckResultList) {
		this.autoCheckResultList = autoCheckResultList;
	}

}
