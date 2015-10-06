package au.edu.unsw.soacourse.ors.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import au.edu.unsw.soacourse.ors.model.Job;
import au.edu.unsw.soacourse.ors.model.JobList;


public enum JobsDao {
    instance;

    private final String RESOURCE_LOCATION = System.getProperty("catalina.home") + File.separator + "webapps"
			+ File.separator + "ROOT/resources/";
    private final String DATASOURCE = RESOURCE_LOCATION + "Jobs.xml";
    JAXBContext context;

    private JobsDao() throws InternalServerErrorException {
    	try {
			context = JAXBContext.newInstance(JobList.class);
		} catch (JAXBException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		}
    }

    public void create(Job newJob) throws InternalServerErrorException {
    	try {
			List<Job> list;
			JobList jl;

			// create directory if not exist
    		File resourceDirectory = new File(RESOURCE_LOCATION);
    		if (!resourceDirectory.exists()) {
    			resourceDirectory.mkdir();
    		}
    		// create file if not exist
	        File dataSourceFile = new File(DATASOURCE);
	        if (!dataSourceFile.exists()) {
	        	dataSourceFile.createNewFile();
				list = new ArrayList<Job>();
				jl = new JobList();
				jl.setJobList(list);
	        } else {
				Unmarshaller um = context.createUnmarshaller();
				jl = (JobList) um.unmarshal(new FileReader(DATASOURCE));
				list = jl.getJobList();
	        }
			list.add(newJob);

			// write to database
			Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	        m.marshal(jl, new File(DATASOURCE));
		} catch (JAXBException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		} catch (FileNotFoundException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		} catch (IOException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		}
    }

    public Job getById(String id) throws NotFoundException {
    	List<Job> list = getAll();
    	for (Job item : list) {
    		if (item.get_jobId().equals(id)) {
    			return item;
    		}
    	}
    	throw new NotFoundException("Job posting not found");
    }

    public List<Job> getAll()
    		throws InternalServerErrorException, NotFoundException {
    	List<Job> list = null;
    	try {
			Unmarshaller um = context.createUnmarshaller();
			JobList jl = (JobList) um.unmarshal(new FileReader(DATASOURCE));
			list = jl.getJobList();
		} catch (JAXBException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		} catch (FileNotFoundException e) {
			throw new NotFoundException("Job posting list not found");
		}
		return list;
    }

    public List<Job> search(
    		String closingDate,
    		String salary,
    		String positionType,
    		String location,
    		String description,
    		String status,
            String assignedTeam)
    		throws BadRequestException {
    	List<Job> result = new ArrayList<Job>();
    	List<Job> list = getAll();
		for (Job item : list) {
			if ((closingDate == null || closingDate.equals(item.getClosingDate()))
					&& (salary == null || salary.equals(""+item.getSalary()))
					&& (positionType == null || positionType.equals(item.getPositionType()))
					&& (location == null || location.equals(item.getLocation()))
					&& (description == null || description.equals(item.getDescription()))
					&& (status == null || status.equals(item.getStatus().toString()))
                    && (assignedTeam == null || assignedTeam.equals(item.getAssignedTeam()))
			) {
				result.add(item);
			}
		}
		return result;
    }

    public void update(Job updatedJob)
    		throws InternalServerErrorException, NotFoundException {
    	try {
			List<Job> list = getAll();
			for (int i = 0; i < list.size(); ++i) {
				Job item = list.get(i);
				if (item.get_jobId().equals(updatedJob.get_jobId())) {
					list.set(i, updatedJob);

					// write to database
					JobList jl = new JobList();
					jl.setJobList(list);
					Marshaller m = context.createMarshaller();
					m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
					m.marshal(jl, new File(DATASOURCE));
					return;
				}
			}
			throw new NotFoundException("Job posting not found");
		} catch (JAXBException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		}
    }

    public void delete(String id)
    		throws InternalServerErrorException, NotFoundException {
    	try {
			List<Job> list = getAll();
			for (Job item : list) {
				if (item.get_jobId().equals(id)) {
					list.remove(item);

					// write to database
					JobList jl = new JobList();
					jl.setJobList(list);
					Marshaller m = context.createMarshaller();
					m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
					m.marshal(jl, new File(DATASOURCE));
					return;
				}
			}
			throw new NotFoundException("Job posting not found");
		} catch (JAXBException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		}
    }

}
