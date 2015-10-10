package au.edu.unsw.soacourse.pdvModel;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


public enum PersonsDao {
    instance;
    
    private final String RESOURCE_LOCATION = getClass().getResource("/").getPath().replace("classes/", "resources/");
    private final String DATASOURCE = RESOURCE_LOCATION + "PeopleDB.xml";
    JAXBContext context;

    private PersonsDao() {
    	
    }
    
    public List<Person> getAll() throws DatabaseException {
    	List<Person> list = null;
    	try {
			context = JAXBContext.newInstance(PersonList.class);
			Unmarshaller um = context.createUnmarshaller();
			PersonList pl = (PersonList) um.unmarshal(new FileReader(DATASOURCE));
			list = pl.getPersonList();
			if (list == null) {
				System.out.println("abc");
			}
		} catch (JAXBException e) {
			throw new DatabaseException("Failed to connect to database");
		} catch (FileNotFoundException e) {
			throw new DatabaseException("Failed to connect to database");
		}
		return list;
    }

}
