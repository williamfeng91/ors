package au.edu.unsw.soacourse.ors.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import au.edu.unsw.soacourse.ors.model.AutoCheckResult;
import au.edu.unsw.soacourse.ors.model.AutoCheckResultList;


public enum AutoCheckResultsDao {
    instance;
    
    private final String RESOURCE_LOCATION = getClass().getResource("/").getPath().replace("classes/", "resources/");
    private final String DATASOURCE = RESOURCE_LOCATION + "AutoCheckResults.xml";
    JAXBContext context;

    private AutoCheckResultsDao() throws InternalServerErrorException {
    	try {
			context = JAXBContext.newInstance(AutoCheckResultList.class);
		} catch (JAXBException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		}
    }
    
    public void create(AutoCheckResult newAutoCheckResult) throws InternalServerErrorException {
    	try {
			List<AutoCheckResult> list;
			AutoCheckResultList al;
			
			// create directory if not exist
    		File resourceDirectory = new File(RESOURCE_LOCATION);
    		if (!resourceDirectory.exists()) {
    			resourceDirectory.mkdir();
    		}
    		// create file if not exist
	        File dataSourceFile = new File(DATASOURCE);
	        if (!dataSourceFile.exists()) {
	        	dataSourceFile.createNewFile();
				list = new ArrayList<AutoCheckResult>();
				al = new AutoCheckResultList();
				al.setAutoCheckResultList(list);
	        } else {
				Unmarshaller um = context.createUnmarshaller();
				al = (AutoCheckResultList) um.unmarshal(new FileReader(DATASOURCE));
				list = al.getAutoCheckResultList();
				if (list == null) {
					list = new ArrayList<AutoCheckResult>();
				}
	        }
			list.add(newAutoCheckResult);
			al.setAutoCheckResultList(list);
			
			// write to database
			Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	        m.marshal(al, new File(DATASOURCE));
		} catch (JAXBException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		} catch (FileNotFoundException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		} catch (IOException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		}
    }
    
    public AutoCheckResult getById(String id) throws NotFoundException {
    	List<AutoCheckResult> list = getAll();
    	for (AutoCheckResult item : list) {
    		if (item.get_autoCheckId().equals(id)) {
    			return item;
    		}
    	}
    	throw new NotFoundException("AutoCheckResult not found");
    }
    
    public AutoCheckResult getByApplication(String appId) {
    	List<AutoCheckResult> list = getAll();
    	for (AutoCheckResult item : list) {
    		if (item.get_appId().equals(appId)) {
    			return item;
    		}
    	}
    	throw new NotFoundException("AutoCheckResult not found");
    }
    
    public List<AutoCheckResult> getAll()
    		throws InternalServerErrorException, NotFoundException {
    	List<AutoCheckResult> list = null;
    	try {
			Unmarshaller um = context.createUnmarshaller();
			AutoCheckResultList al = (AutoCheckResultList) um.unmarshal(new FileReader(DATASOURCE));
			list = al.getAutoCheckResultList();
	    	if (list == null) {
	    		list = new ArrayList<AutoCheckResult>();
	    	}
			return list;
		} catch (JAXBException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		} catch (FileNotFoundException e) {
			throw new NotFoundException("AutoCheckResult list not found");
		}
    }
    
    public void update(AutoCheckResult updatedAutoCheckResult)
    		throws InternalServerErrorException, NotFoundException {
    	try {
			List<AutoCheckResult> list = getAll();
			for (int i = 0; i < list.size(); ++i) {
				AutoCheckResult item = list.get(i);
				if (item.get_appId().equals(updatedAutoCheckResult.get_appId())) {
					// make sure id is not modified
					updatedAutoCheckResult.set_autoCheckId(item.get_autoCheckId());
					list.set(i, updatedAutoCheckResult);
					
					// write to database
					AutoCheckResultList al = new AutoCheckResultList();
					al.setAutoCheckResultList(list);
					Marshaller m = context.createMarshaller();
					m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
					m.marshal(al, new File(DATASOURCE));
					return;
				}
			}
			throw new NotFoundException("AutoCheckResult not found");
		} catch (JAXBException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		}
    }

}
