package au.edu.unsw.soacourse.ors.dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import au.edu.unsw.soacourse.ors.beans.User;
import au.edu.unsw.soacourse.ors.beans.UserList;


public enum UsersDao {
	instance;
    
    private final String RESOURCE_LOCATION = getClass().getResource("/").getPath().replace("classes/", "resources/");
    private final String DATASOURCE = RESOURCE_LOCATION + "RegisteredUsers.xml";
    JAXBContext context;

    private UsersDao() {
    	try {
			context = JAXBContext.newInstance(UserList.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    }
    
    public User login(String username, String password) {
    	List<User> list = getAll();
    	for (User item : list) {
    		if (item.get_uid().equals(username) && item.get_pwd().equals(password)) {
    			return item;
    		}
    	}
    	return null;
    }
    
    public User getById(String id) {
    	List<User> list = getAll();
    	for (User item : list) {
    		if (item.get_uid().equals(id)) {
    			return item;
    		}
    	}
    	return null;
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
    
    public List<User> getAll() {
    	List<User> list = null;
    	try {
			Unmarshaller um = context.createUnmarshaller();
			UserList jl = (UserList) um.unmarshal(new FileReader(DATASOURCE));
			list = jl.getUserList();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return list;
    }
    
    public List<String> getAllTeams() {
    	List<String> result = new ArrayList<String>();
    	List<User> list = getAll();
		for (User item : list) {
			if (item.getRole().equals("reviewer") && !result.contains(item.getDepartment())) {
				result.add(item.getDepartment());
			}
		}
		return result;
    }

}
