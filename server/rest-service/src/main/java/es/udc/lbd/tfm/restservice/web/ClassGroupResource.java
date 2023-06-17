package es.udc.lbd.tfm.restservice.web;

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
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.tfm.restservice.web.exception.IdAndBodyNotMatchingOnUpdateException;
import es.udc.lbd.tfm.restservice.web.exception.RequestBodyNotValidException;
import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.model.service.ClassGroupService;
import es.udc.lbd.tfm.restservice.model.service.dto.ClassGroupDTO;
import es.udc.lbd.tfm.restservice.model.service.dto.LessonDTO;

@RestController
@RequestMapping("/api/classGroups")
public class ClassGroupResource {
	
	@Autowired
	private ClassGroupService classGroupService;
	
	
	@GetMapping
	public List<ClassGroupDTO> findAll(){
		return classGroupService.findAll();
	}
	
	@GetMapping("/{id}")
	public ClassGroupDTO findById(@PathVariable Long id) throws NotFoundException {
		return classGroupService.findById(id);
	}
	
	@GetMapping("/user/{userId}")
	public List<ClassGroupDTO> findByUserId(@PathVariable Long userId) throws NotFoundException{
		return classGroupService.findByUserId(userId);
	}
	
	@GetMapping("/book/{bookId}/user/{userId}")
	public List<ClassGroupDTO> findByBookIdAndUserId(@PathVariable Long bookId, @PathVariable Long userId) throws NotFoundException {
		return classGroupService.findByBookIdAndUserId(bookId, userId);
	}
	
	@GetMapping("/book/{bookId}/teacherSubscription/{subscriptionId}")
	public List<ClassGroupDTO> findByBookIdAndSubscriptionId(@PathVariable Long bookId, @PathVariable Long subscriptionId) throws NotFoundException{
		return classGroupService.findByBookIdAndTeacherSubscriptionId(bookId, subscriptionId);
	}
	
	@PostMapping
	public ClassGroupDTO create(@RequestBody @Valid ClassGroupDTO classGroup, Errors errors) throws RequestBodyNotValidException {
		errorHandler(errors);
		return classGroupService.create(classGroup);
	}
	
	@PutMapping("/show-hide-lesson/{type}/{classGroupId}")
	public ClassGroupDTO showHideLesson(@PathVariable Long classGroupId,@PathVariable String type,
			@RequestBody @Valid LessonDTO lesson, Errors errors) throws RequestBodyNotValidException, NotFoundException {
		errorHandler(errors);
		return classGroupService.showHideLessonByClassGroupId(classGroupId,lesson,type);
	}
	
	@PutMapping("/{id}")
	public ClassGroupDTO update(@PathVariable Long id, @RequestBody @Valid ClassGroupDTO classGroup, Errors errors) throws RequestBodyNotValidException, IdAndBodyNotMatchingOnUpdateException {
		errorHandler(errors);
		if(!id.equals(classGroup.getId())) {
			throw new IdAndBodyNotMatchingOnUpdateException("Path id "+id+" and class group id "+classGroup.getId()+" must be equals!");
		}
		return classGroupService.update(classGroup);
	}
	
	@DeleteMapping("/{id}")
	public void delete (@PathVariable Long id) throws NotFoundException {
		classGroupService.deleteById(id);
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
