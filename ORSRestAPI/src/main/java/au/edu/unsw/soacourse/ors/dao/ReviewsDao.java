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

import au.edu.unsw.soacourse.ors.model.Review;
import au.edu.unsw.soacourse.ors.model.ReviewList;


public enum ReviewsDao {
    instance;
    
    private final String RESOURCE_LOCATION = getClass().getResource("/").getPath().replace("classes/", "resources/");
    private final String DATASOURCE = RESOURCE_LOCATION + "Reviews.xml";
    JAXBContext context;

    private ReviewsDao() throws InternalServerErrorException {
    	try {
			context = JAXBContext.newInstance(ReviewList.class);
		} catch (JAXBException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		}
    }
    
    public void create(Review newReview) throws InternalServerErrorException {
    	try {
			List<Review> list;
			ReviewList rl;
			
			// create directory if not exist
    		File resourceDirectory = new File(RESOURCE_LOCATION);
    		if (!resourceDirectory.exists()) {
    			resourceDirectory.mkdir();
    		}
    		// create file if not exist
	        File dataSourceFile = new File(DATASOURCE);
	        if (!dataSourceFile.exists()) {
	        	dataSourceFile.createNewFile();
				list = new ArrayList<Review>();
				rl = new ReviewList();
				rl.setReviewList(list);
	        } else {
				Unmarshaller um = context.createUnmarshaller();
				rl = (ReviewList) um.unmarshal(new FileReader(DATASOURCE));
				list = rl.getReviewList();
	        }
			list.add(newReview);
			
			// write to database
			Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	        m.marshal(rl, new File(DATASOURCE));
		} catch (JAXBException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		} catch (FileNotFoundException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		} catch (IOException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		}
    }
    
    public Review getById(String id) throws NotFoundException {
    	List<Review> list = getAll();
    	for (Review item : list) {
    		if (item.get_reviewId().equals(id)) {
    			return item;
    		}
    	}
    	throw new NotFoundException("Review not found");
    }
    
    public List<Review> getByApplication(String applicationId) {
    	List<Review> result = new ArrayList<Review>();
    	List<Review> list = getAll();
    	for (Review item : list) {
    		if (item.get_appId().equals(applicationId)) {
    			result.add(item);
    		}
    	}
    	return result;
    }
    
    public List<Review> getByReviewer(String reviewerId) {
    	List<Review> result = new ArrayList<Review>();
    	List<Review> list = getAll();
    	for (Review item : list) {
    		if (item.get_uId().equals(reviewerId)) {
    			result.add(item);
    		}
    	}
    	return result;
    }
    
    public List<Review> getAll()
    		throws InternalServerErrorException, NotFoundException {
    	List<Review> list = null;
    	try {
			Unmarshaller um = context.createUnmarshaller();
			ReviewList rl = (ReviewList) um.unmarshal(new FileReader(DATASOURCE));
			list = rl.getReviewList();
		} catch (JAXBException e) {
			throw new InternalServerErrorException("Failed to connect to database");
		} catch (FileNotFoundException e) {
			throw new NotFoundException("Review list not found");
		}
		return list;
    }

}
