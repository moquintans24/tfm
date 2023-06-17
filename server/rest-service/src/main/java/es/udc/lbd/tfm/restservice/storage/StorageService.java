package es.udc.lbd.tfm.restservice.storage;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;

public interface StorageService {
	
	void init() throws StorageException;
	
	String store(MultipartFile file, String name, String type, Long id) throws StorageException, NotFoundException;
	
	void delete(String type, Long id) throws NotFoundException, IOException;
	
	String copy(String type, Long originalId,Long duplicateId) throws NotFoundException, IOException;

}
