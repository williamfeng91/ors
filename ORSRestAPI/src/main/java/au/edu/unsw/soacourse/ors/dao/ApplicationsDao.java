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

import au.edu.unsw.soacourse.ors.model.Application;
import au.edu.unsw.soacourse.ors.model.ApplicationList;
import au.edu.unsw.soacourse.ors.model.ApplicationStatus;


public enum ApplicationsDao {
    instance;
    
    private final String RESOURCE_LOCATION = getClass().getResource("/").getPath().replace("classes/", "resources/");
    private final String DATASOURCE = RESOURCE_LOCATION + "Applications.xml";
    JAXBContext context;

    private ApplicationsDao() throws InternalServerErrorException {
    	try {
			context = JAXBContext.newInstance(ApplicationList.class);
		} catch (JAXBException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		}
    }
    
    public void create(Application newApplication) throws InternalServerErrorException {
    	try {
			List<Application> list;
			ApplicationList al;
			
			// create directory if not exist
    		File resourceDirectory = new File(RESOURCE_LOCATION);
    		if (!resourceDirectory.exists()) {
    			resourceDirectory.mkdir();
    		}
    		// create file if not exist
	        File dataSourceFile = new File(DATASOURCE);
	        if (!dataSourceFile.exists()) {
	        	dataSourceFile.createNewFile();
				list = new ArrayList<Application>();
				al = new ApplicationList();
				al.setApplicationList(list);
	        } else {
				Unmarshaller um = context.createUnmarshaller();
				al = (ApplicationList) um.unmarshal(new FileReader(DATASOURCE));
				list = al.getApplicationList();
				if (list == null) {
					list = new ArrayList<Application>();
				}
	        }
			list.add(newApplication);
			al.setApplicationList(list);
			
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
    
    public Application getById(String id) throws NotFoundException {
    	List<Application> list = getAll();
    	for (Application item : list) {
    		if (item.get_appId().equals(id)) {
    			return item;
    		}
    	}
    	throw new NotFoundException("Application not found");
    }
    
    public List<Application> getByJob(String jobId) {
    	List<Application> result = new ArrayList<Application>();
    	List<Application> list = getAll();
    	for (Application item : list) {
    		if (item.get_jobId().equals(jobId)) {
    			result.add(item);
    		}
    	}
    	return result;
    }
    
    public List<Application> getAll()
    		throws InternalServerErrorException, NotFoundException {
    	List<Application> list = null;
    	try {
			Unmarshaller um = context.createUnmarshaller();
			ApplicationList al = (ApplicationList) um.unmarshal(new FileReader(DATASOURCE));
			list = al.getApplicationList();
	    	if (list == null) {
	    		list = new ArrayList<Application>();
	    	}
			return list;
		} catch (JAXBException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		} catch (FileNotFoundException e) {
			throw new NotFoundException("Application list not found");
		}
    }
    
    public void update(Application updatedApplication)
    		throws InternalServerErrorException, NotFoundException {
    	try {
			List<Application> list = getAll();
			for (int i = 0; i < list.size(); ++i) {
				Application item = list.get(i);
				if (item.get_appId().equals(updatedApplication.get_appId())) {
					// do not update archived application
					if (item.getStatus().equals(ApplicationStatus.ARCHIVED)) {
						continue;
					}
					list.set(i, updatedApplication);
					
					// write to database
					ApplicationList al = new ApplicationList();
					al.setApplicationList(list);
					Marshaller m = context.createMarshaller();
					m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
					m.marshal(al, new File(DATASOURCE));
					return;
				}
			}
			throw new NotFoundException("Application not found");
		} catch (JAXBException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		}
    }
    
    public void delete(String id)
    		throws InternalServerErrorException, NotFoundException {
    	try {
			List<Application> list = getAll();
			for (Application item : list) {
				if (item.get_appId().equals(id)) {
//					list.remove(item);
					item.setStatus(ApplicationStatus.ARCHIVED);
					
					// write to database
					ApplicationList al = new ApplicationList();
					al.setApplicationList(list);
					Marshaller m = context.createMarshaller();
					m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
					m.marshal(al, new File(DATASOURCE));
					return;
				}
			}
			throw new NotFoundException("Application not found");
		} catch (JAXBException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		}
    }

}
