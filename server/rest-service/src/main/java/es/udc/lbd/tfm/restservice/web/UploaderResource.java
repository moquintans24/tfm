package es.udc.lbd.tfm.restservice.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.storage.StorageException;
import es.udc.lbd.tfm.restservice.storage.StorageService;

@RestController
@RequestMapping("/api")
public class UploaderResource {


	@Autowired
    private final StorageService storageService;

    @Autowired
    public UploaderResource(StorageService storageService) {
        this.storageService = storageService;
    }

    
    //Type user, book or activity
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("name") String name,
    		@RequestParam("type") String type, @RequestParam("id") Long id, RedirectAttributes redirectAttributes) throws NotFoundException, StorageException {
    	
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return storageService.store(file, name, type, id);
    }


}
