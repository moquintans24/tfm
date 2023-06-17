package es.udc.lbd.tfm.restservice.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.model.service.ActivityService;
import es.udc.lbd.tfm.restservice.model.service.dto.ActivityDTO;
import es.udc.lbd.tfm.restservice.storage.StorageException;
import es.udc.lbd.tfm.restservice.web.exception.IdAndBodyNotMatchingOnUpdateException;
import es.udc.lbd.tfm.restservice.web.exception.RequestBodyNotValidException;

@RestController
@RequestMapping("/api/activities")
public class ActivityResource {
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private ServletContext servletContext;
	
	@GetMapping("/{id}")
	public ActivityDTO findById(@PathVariable Long id) throws NotFoundException {
		return activityService.findById(id);
	}
	
	@GetMapping("/lesson/{lessonId}")
	public List<ActivityDTO> findByLessonId(@PathVariable Long lessonId) throws NotFoundException{
		return activityService.findByLessonId(lessonId);
	}
	
	@GetMapping("/book/{bookId}")
	public List<ActivityDTO> findByBookId(@PathVariable Long bookId) throws NotFoundException{
		return activityService.findByBookId(bookId);
	}
	
	@GetMapping("/students/{classGroupId}")
	public List<?> totalStudentsRealizedActivity(@PathVariable Long classGroupId) throws NotFoundException{
		return activityService.totalStudentsRealizedActivity(classGroupId);
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/images/{path}",method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] activityImage(@PathVariable String path) throws IOException {
		InputStream in = servletContext.getResourceAsStream("/WEB-INF/images/activities/"+ path);
	    return IOUtils.toByteArray(in);
	}
	
	@GetMapping("/withInteractions/{id}")
	public Boolean findByIdWithInteractionActivity(@PathVariable Long id) throws NotFoundException{
		return activityService.findByIdWithInteractionActivity(id);
	}
	
	@GetMapping("/available/{classGroupId}")
	public List<ActivityDTO> findAvailableByClassGroupId(@PathVariable Long classGroupId) throws NotFoundException{
		return activityService.findAvailableByClassGroupId(classGroupId);
	}
	
	@GetMapping("/h5p-file/{idH5PFile}")
	public List<ActivityDTO> findByIdH5PFile(@PathVariable String idH5PFile){
		return activityService.findByIdH5PFile(idH5PFile);
	}
	
	@PostMapping
	public ActivityDTO create(@RequestParam(name="image",required=false) MultipartFile image,@RequestParam("activityCode") String code,
			@RequestParam("activityTitle") String title,@RequestParam("activityDescription") String description,@RequestParam(name="idH5PFile",required = false) String idH5PFile,
			@RequestParam("activityNumber") String number,@RequestParam("lessonId") String lessonId) throws NumberFormatException, NotFoundException, StorageException {
		if(idH5PFile == null) {
			return activityService.create(Long.parseLong(lessonId) , code, title, description, Integer.parseInt(number), image, null);
		}else {
			return activityService.create(Long.parseLong(lessonId) , code, title, description, Integer.parseInt(number), image, idH5PFile);
		}
		
	}
	
	@PutMapping("/{id}")
	public ActivityDTO update(@PathVariable Long id, @RequestBody @Valid ActivityDTO activity, Errors errors) throws RequestBodyNotValidException, IdAndBodyNotMatchingOnUpdateException, NotFoundException {
		errorHandler(errors);
		if(!id.equals(activity.getId())) {
			throw new IdAndBodyNotMatchingOnUpdateException("Path id "+id+" and activity id "+activity.getId()+" must be equals!");
		}
		return activityService.update(activity);
	}
	
	@DeleteMapping("/{activityId}")
	public void deleteById(@PathVariable Long activityId) throws NotFoundException, IOException{
		activityService.deleteById(activityId);
	}

	private void errorHandler(Errors errors) throws RequestBodyNotValidException {
	    if (errors.hasErrors()) {
	      String errorMsg = errors.getFieldErrors().stream()
	          .map(fe -> String.format("%s.%s %s", fe.getObjectName(), fe.getField(), fe.getDefaultMessage()))
	          .collect(Collectors.joining("; "));
	      throw new RequestBodyNotValidException(errorMsg);
	    }
	  }
}
