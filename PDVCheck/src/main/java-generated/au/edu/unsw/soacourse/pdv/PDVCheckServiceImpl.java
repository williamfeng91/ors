package au.edu.unsw.soacourse.pdv;

import java.util.List;

import javax.jws.WebService;

import au.edu.unsw.soacourse.pdvModel.*;

@WebService(endpointInterface = "au.edu.unsw.soacourse.pdv.PDVCheckService")
public class PDVCheckServiceImpl implements PDVCheckService {
	
	ObjectFactory factory = new ObjectFactory();

	@Override
	public PDVCheckResponse pdvCheck(PDVCheckRequest parameters)
		throws PDVCheckFaultMsg {
		String msg = "Error message";
		PDVCheckFault fault = factory.createPDVCheckFault();
		
		String licenseNo = parameters.getLicenseNo();
		String fullName = parameters.getFullName();
		String postcode = parameters.getPostcode();
		if (licenseNo == null || licenseNo.isEmpty()) {
			msg = "Invalid license number";
			fault.setFaultMessage(msg);
			fault.setFaultType(PDVCheckFaultType.INVALID_LICENSE_NO);
			throw new PDVCheckFaultMsg(msg, fault);
		}
		if (fullName == null || fullName.isEmpty()) {
			msg = "Invalid full name";
			fault.setFaultMessage(msg);
			fault.setFaultType(PDVCheckFaultType.INVALID_FULL_NAME);
			throw new PDVCheckFaultMsg(msg, fault);
		}
		if (postcode == null || postcode.isEmpty()) {
			msg = "Invalid postcode";
			fault.setFaultMessage(msg);
			fault.setFaultType(PDVCheckFaultType.INVALID_POSTCODE);
			throw new PDVCheckFaultMsg(msg, fault);
		}
		
		PDVCheckResponse response = new PDVCheckResponse();
		
		try {
			List<Person> list = PersonsDao.instance.getAll();
			for (Person p : list) {
				if (p.getLicenseNo().equals(licenseNo)) {
					if (p.getFullName().equals(fullName) && p.getPostcode().equals(postcode)) {
						response.setMessage("the given details are correct against our records");
					} else {
						response.setMessage("the given license number is correct, but details are different to our records");
					}
					return response;
				}
			}
			response.setMessage("the given license number is not found in our records");
			return response;
		} catch (DatabaseException e) {
			msg = e.getMessage();
			fault.setFaultMessage(msg);
			fault.setFaultType(PDVCheckFaultType.PROGRAM_ERROR);
			throw new PDVCheckFaultMsg(msg, fault);
		}
	}
}
