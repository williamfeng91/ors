package au.edu.unsw.soacourse.ors.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import au.edu.unsw.soacourse.ors.model.User;
import au.edu.unsw.soacourse.ors.model.UserList;


public enum UsersDao {
    instance;
    
    private final String RESOURCE_LOCATION = System.getProperty("catalina.home") + File.separator + "webapps"
			+ File.separator + "ROOT/resources/";
    private final String DATASOURCE = RESOURCE_LOCATION + "RegisteredUsers.xml";
    JAXBContext context;

    private UsersDao() throws InternalServerErrorException {
    	try {
			context = JAXBContext.newInstance(UserList.class);
		} catch (JAXBException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		}
    }
    
    public User getById(String id) throws NotFoundException {
    	List<User> list = getAll();
    	for (User item : list) {
    		if (item.get_uid().equals(id)) {
    			return item;
    		}
    	}
    	throw new NotFoundException("User not found");
    }
    
    public List<User> getByHireTeam(String team) {
    	List<User> result = new ArrayList<User>();
    	List<User> list = getAll();
		for (User item : list) {
			if (team == null || team.equals(item.getDepartment())) {
				result.add(item);
			}
		}
		return result;
    }
    
    public List<User> getAll()
    		throws InternalServerErrorException, NotFoundException {
    	List<User> list = null;
    	try {
			Unmarshaller um = context.createUnmarshaller();
			UserList jl = (UserList) um.unmarshal(new FileReader(DATASOURCE));
			list = jl.getUserList();
		} catch (JAXBException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		} catch (FileNotFoundException e) {
			throw new NotFoundException("User list not found");
		}
		return list;
    }

}
