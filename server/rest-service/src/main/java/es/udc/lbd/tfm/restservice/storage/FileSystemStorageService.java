package es.udc.lbd.tfm.restservice.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.model.service.ActivityService;
import es.udc.lbd.tfm.restservice.model.service.BookService;
import es.udc.lbd.tfm.restservice.model.service.UserService;
import es.udc.lbd.tfm.restservice.model.service.dto.ActivityDTO;
import es.udc.lbd.tfm.restservice.model.service.dto.BookDTO;
import es.udc.lbd.tfm.restservice.model.service.dto.UserDTOPublic;

@Service
public class FileSystemStorageService implements StorageService{
	
	private final Path usersLocation;
	private final Path booksLocation;
	private final Path activitiesLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.usersLocation = Paths.get(properties.getUsersLocation());
        this.booksLocation = Paths.get(properties.getBooksLocation());
        this.activitiesLocation = Paths.get(properties.getActivitiesLocation());
    }
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private ActivityService activityService;
    
	@Override
	public String store(MultipartFile file, String name, String type, Long id) throws StorageException, NotFoundException {
		String result = null;
		String filename = (id.toString()+name.toLowerCase()+ "." + FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase()).replaceAll(" ", "");
		
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                
                if(type.contentEquals("usuario")){
                	
                	
	               	UserDTOPublic bdUser = userService.findById(id);
	               	UUID uuid = UUID.randomUUID();
	        		String extension = "";

	        		int i = bdUser.getLinkImage().lastIndexOf('.');

	        		//Eliminar la imagen del usuario siempre que no sea la imagen por defecto
	        		if(!bdUser.getLinkImage().equalsIgnoreCase("default_user_image.jpg")) {
	        			Files.delete(this.usersLocation.resolve(bdUser.getLinkImage())); 
	        		}
//	        		
        		    extension = bdUser.getLinkImage().substring(i+1);
        		    bdUser.setLinkImage(uuid.toString().concat(".").concat(extension));
//	        		
	        		Files.copy(inputStream, this.usersLocation.resolve(uuid.toString().concat(".").concat(extension)),
	                          StandardCopyOption.REPLACE_EXISTING);
	        
	               	 userService.updateLinkImage(bdUser);
	               	 return uuid.toString().concat(".").concat(extension);
               }
               if(type.contentEquals("book")){
                	
                	
	               	BookDTO bdBook = bookService.findById(id);
        		    bdBook.setImage(filename);

	        		Files.copy(inputStream, this.booksLocation.resolve(filename),StandardCopyOption.REPLACE_EXISTING);
	        
	               	 bookService.updateLinkImage(bdBook);
	               	 
	               	 return filename;
               }
               if(type.contentEquals("activity")){
            	   
            	   	ActivityDTO bdActivity = activityService.findById(id);
            	   	UUID uuid = UUID.randomUUID();
	        		String extension = "";
	        	
	        		int i = bdActivity.getImagePath().lastIndexOf('.');
	        		
	        		if(!bdActivity.getImagePath().equalsIgnoreCase("default_activity.png")) {
	        			Files.delete(this.activitiesLocation.resolve(bdActivity.getImagePath())); 
	        		}
	        		
	       		    extension = bdActivity.getImagePath().substring(i+1);
	       		    bdActivity.setImagePath(uuid.toString().concat(".").concat(extension));

	        		Files.copy(inputStream, this.activitiesLocation.resolve(uuid.toString().concat(".").concat(extension)),
	                          StandardCopyOption.REPLACE_EXISTING);
	        
	               	 activityService.updateLinkImage(bdActivity);
	               	 
	               	 return uuid.toString().concat(".").concat(extension);
              }
                
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        if(type.contentEquals("usuario")) {
        	result = userService.findById(id).getLinkImage();
        }
        if(type.contentEquals("book")) {
        	result = bookService.findById(id).getImage();
        }
        if(type.contentEquals("activity")) {
        	result = activityService.findById(id).getImagePath();
        }
        
		return result;
    }



    @Override
    public void init() throws StorageException {
        try {
            Files.createDirectories(usersLocation);
            Files.createDirectories(booksLocation);
            Files.createDirectories(activitiesLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }



	@Override
	public void delete(String type, Long id) throws NotFoundException, IOException {
		if(type.equals("activity")) {
			ActivityDTO bdActivity = activityService.findById(id);
			List<ActivityDTO> activities = activityService.findByImage(bdActivity.getImagePath());
			//Eliminar la imagen de la actividad siempre que no sea la imagen por defecto y que la imagen no esté asociada a otra actividad
    		if(!bdActivity.getImagePath().equalsIgnoreCase("default_activity.png")&& activities.size()<=1) {
    			Files.delete(this.activitiesLocation.resolve(bdActivity.getImagePath()));
    		}
		}
		if(type.equals("book")) {
			BookDTO bdBook = bookService.findById(id);
			//Eliminar imagen del libro si no está asociada a otros libros
			List<BookDTO> books = bookService.findByImage(bdBook.getImage());
			if(books.size() > 1) {
				Files.delete(this.booksLocation.resolve(bdBook.getImage()));
			}
			
		}
		
		
	}

	@Override
	public String copy(String type, Long originalId, Long duplicateId) throws NotFoundException, IOException {
		UUID uuid = UUID.randomUUID();
		String extension = "";
		String imagePath = "";
		if(type.equals("activity")) {
			ActivityDTO bdActivity = activityService.findById(originalId);
			int i = bdActivity.getImagePath().lastIndexOf('.');
			//duplicamos la imagen siempre que no sea la imagen por defecto
			if(!bdActivity.getImagePath().equalsIgnoreCase("default_activity.png")) {
				 extension = bdActivity.getImagePath().substring(i+1);
				Files.copy(this.activitiesLocation.resolve(bdActivity.getImagePath()),this.activitiesLocation.resolve(uuid.toString().concat(".").concat(extension)),
						StandardCopyOption.REPLACE_EXISTING);
				imagePath = uuid.toString().concat(".").concat(extension);
			}
		   
		}
		if(type.equals("book")) {
			BookDTO bdBook = bookService.findById(originalId);
			int i = bdBook.getImage().lastIndexOf('.');
			
		    extension = bdBook.getImage().substring(i+1);
		    
			Files.copy(this.booksLocation.resolve(bdBook.getImage()),this.booksLocation.resolve(uuid.toString().concat(".").concat(extension)),
					StandardCopyOption.REPLACE_EXISTING);
			imagePath = uuid.toString().concat(".").concat(extension);
		}
		return imagePath;
		
	}

}
