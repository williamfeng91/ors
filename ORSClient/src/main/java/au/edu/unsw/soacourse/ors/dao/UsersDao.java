package au.edu.unsw.soacourse.ors.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import au.edu.unsw.soacourse.ors.beans.User;
import au.edu.unsw.soacourse.ors.beans.UserList;


public class UsersDao {

	private static final String DATASOURCE = "/WEB-INF/RegisteredUsers.xml";
    private static final String REST_URI = "http://localhost:8080/ORSRestAPI";
    private String dataLocation;
    private WebClient client;
    private List<Object> providers;
    private ServletContext servletContext;
    private JAXBContext jaxbContext;

    public UsersDao(ServletContext servletContext) {
    	this.servletContext = servletContext;
    	dataLocation = servletContext.getRealPath(DATASOURCE);
    	providers = new ArrayList<Object>();
        providers.add( new JacksonJaxbJsonProvider() );
        try {
        	jaxbContext = JAXBContext.newInstance(UserList.class);
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
			Unmarshaller um = jaxbContext.createUnmarshaller();
			UserList jl = (UserList) um.unmarshal(new FileReader(dataLocation));
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
    
//    public User getById(String id) {
//        client = WebClient.create(REST_URI, providers);
//    	client.path("/users/" + id).accept(MediaType.APPLICATION_JSON);
//    	User j = client.get(User.class);
//    	return j;
//    }
//    
//    public List<User> getByHireTeam(String team) {
//        client = WebClient.create(REST_URI, providers);
//    	client.path("/users").accept(MediaType.APPLICATION_JSON).query("hireTeam", team);
//    	List<User> list = (List<User>) client.getCollection(User.class);
//		return list;
//    }
//
//    public List<User> getAll() {
//        client = WebClient.create(REST_URI, providers);
//    	client.path("/users").accept(MediaType.APPLICATION_JSON);
//		List<User> list = (List<User>) client.getCollection(User.class);
//		return list;
//    }

}
