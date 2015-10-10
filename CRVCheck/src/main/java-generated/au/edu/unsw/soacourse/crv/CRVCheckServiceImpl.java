package au.edu.unsw.soacourse.crv;

import java.util.List;

import javax.jws.WebService;

import au.edu.unsw.soacourse.crvModel.*;

@WebService(endpointInterface = "au.edu.unsw.soacourse.crv.CRVCheckService")
public class CRVCheckServiceImpl implements CRVCheckService {
	
	ObjectFactory factory = new ObjectFactory();

	@Override
	public CRVCheckResponse crvCheck(CRVCheckRequest parameters)
		throws CRVCheckFaultMsg {
		String msg = "Error message";
		CRVCheckFault fault = factory.createCRVCheckFault();
		
		String licenseNo = parameters.getLicenseNo();
		if (licenseNo == null || licenseNo.isEmpty()) {
			msg = "Invalid license number";
			fault.setFaultMessage(msg);
			fault.setFaultType(CRVCheckFaultType.INVALID_LICENSE_NO);
			throw new CRVCheckFaultMsg(msg, fault);
		}
		
		CRVCheckResponse response = new CRVCheckResponse();
		
		try {
			List<Person> list = PersonsDao.instance.getAll();
			for (Person p : list) {
				if (p.getLicenseNo().equals(licenseNo)) {
					if (p.getCriminalRecords() != null && p.getCriminalRecords().size() > 0) {
						response.setMessage("the given license number is known in our database, a further discussion is recommended");
					} else {
						response.setMessage("the given license number has no records to report");
					}
					return response;
				}
			}
			response.setMessage("the given license number is not found in our records");
			return response;
		} catch (DatabaseException e) {
			msg = e.getMessage();
			fault.setFaultMessage(msg);
			fault.setFaultType(CRVCheckFaultType.PROGRAM_ERROR);
			throw new CRVCheckFaultMsg(msg, fault);
		}
	}
}
