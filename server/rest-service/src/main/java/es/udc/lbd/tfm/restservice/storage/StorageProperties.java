package es.udc.lbd.tfm.restservice.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("storage")
public class StorageProperties {
	
	private String usersLocation = "./src/main/webapp/WEB-INF/images/users";
	private String booksLocation = "./src/main/webapp/WEB-INF/images/books";
	private String activitiesLocation = "./src/main/webapp/WEB-INF/images/activities";

	public String getUsersLocation() {
		return usersLocation;
	}

	public void setLocation(String usersLocation) {
		this.usersLocation = usersLocation;
	}

	public String getBooksLocation() {
		return booksLocation;
	}

	public void setBooksLocation(String booksLocation) {
		this.booksLocation = booksLocation;
	}

	public String getActivitiesLocation() {
		return activitiesLocation;
	}

	public void setActivitiesLocation(String activitiesLocation) {
		this.activitiesLocation = activitiesLocation;
	}

}
