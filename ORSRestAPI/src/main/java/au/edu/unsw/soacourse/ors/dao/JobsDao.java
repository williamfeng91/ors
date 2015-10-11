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
import au.edu.unsw.soacourse.ors.model.RecruitmentStatus;


public enum JobsDao {
    instance;

    private final String RESOURCE_LOCATION = getClass().getResource("/").getPath().replace("classes/", "resources/");
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
				if (list == null) {
					list = new ArrayList<Job>();
				}
	        }
			list.add(newJob);
			jl.setJobList(list);

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
	    	if (list == null) {
	    		list = new ArrayList<Job>();
	    	}
			return list;
		} catch (JAXBException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		} catch (FileNotFoundException e) {
			throw new NotFoundException("Job posting list not found");
		}
    }

    public List<Job> search(
			String closingDateFrom,
			String closingDateTo,
			int salaryFrom,
			int salaryTo,
			String positionType,
			String location,
			String description,
			String status,
			String assignedTeam)
    		throws BadRequestException {
    	List<Job> result = new ArrayList<Job>();
    	List<Job> list = getAll();
		for (Job item : list) {
			if ((closingDateFrom == null || closingDateFrom.compareTo(item.getClosingDate()) <= 0)
					&& (closingDateTo == null || closingDateTo.compareTo(item.getClosingDate()) >= 0)
					&& (salaryFrom <= item.getSalary())
					&& (salaryTo >= item.getSalary())
					&& (positionType == null || item.getPositionType().toLowerCase().contains(positionType.toLowerCase()))
					&& (location == null || item.getLocation().toLowerCase().contains(location.toLowerCase()))
					&& (description == null || item.getDescription().toLowerCase().contains(description.toLowerCase()))
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
					// do not update archived job
					if (item.getStatus().equals(RecruitmentStatus.ARCHIVED)) {
						continue;
					}
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
//					list.remove(item);
					item.setStatus(RecruitmentStatus.ARCHIVED);

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
