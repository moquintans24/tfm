package es.udc.lbd.tfm.restservice.web;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.model.service.LessonService;
import es.udc.lbd.tfm.restservice.model.service.dto.LessonDTO;
import es.udc.lbd.tfm.restservice.web.exception.IdAndBodyNotMatchingOnUpdateException;
import es.udc.lbd.tfm.restservice.web.exception.RequestBodyNotValidException;

@RestController
@RequestMapping("/api/lessons")
public class LessonResource {
	
	@Autowired
	private LessonService lessonService;
	
	@GetMapping("/{lessonId}")
	public LessonDTO findById(@PathVariable Long lessonId) throws NotFoundException{
		return lessonService.findById(lessonId);
	}
	
	@GetMapping("/unit/{unitId}")
	public List<LessonDTO> findByUnitId(@PathVariable Long unitId) throws NotFoundException{
		return lessonService.findByUnitId(unitId);
	}
	
	@PostMapping("/{lessonId}")
	public LessonDTO create(@PathVariable Long lessonId, @RequestParam("name")String name, @RequestParam("number")String number) throws NumberFormatException, NotFoundException {
		return lessonService.create(lessonId, name, Integer.parseInt(number));
	}
	
	@PutMapping("/{id}")
	public LessonDTO update(@PathVariable Long id, @RequestBody @Valid LessonDTO lesson, Errors errors) throws RequestBodyNotValidException, 
															IdAndBodyNotMatchingOnUpdateException, NotFoundException {
		errorHandler(errors);
		if(!id.equals(lesson.getId())) {
			throw new IdAndBodyNotMatchingOnUpdateException("Path id "+id+" and lesson id "+lesson.getId()+" must be equals!");
		}
		return lessonService.update(lesson);
	}
	
	@DeleteMapping("/{lessonId}")
	public void delete(@PathVariable Long lessonId) throws NotFoundException, IOException {
		lessonService.delete(lessonId);
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
