package es.udc.lbd.tfm.restservice.model.service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.tfm.restservice.model.domain.Activity;
import es.udc.lbd.tfm.restservice.model.domain.Book;
import es.udc.lbd.tfm.restservice.model.domain.Lesson;
import es.udc.lbd.tfm.restservice.model.domain.Unit;
import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.model.repository.ActivityDao;
import es.udc.lbd.tfm.restservice.model.repository.BookDao;
import es.udc.lbd.tfm.restservice.model.repository.LessonDao;
import es.udc.lbd.tfm.restservice.model.repository.UnitDao;
import es.udc.lbd.tfm.restservice.model.service.dto.LessonDTO;
import es.udc.lbd.tfm.restservice.storage.StorageService;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class LessonService {
	
	@Autowired
	private LessonDao lessonDAO;
	
	@Autowired
	private UnitDao unitDAO;
	
	@Autowired
	private ActivityDao activityDAO;
	
	@Autowired
	private BookDao bookDAO;
	
	@Autowired
	private StorageService storageService;
	
	public List<LessonDTO>findByUnitId(Long unitId) throws NotFoundException{
		Unit unit = unitDAO.findById(unitId);
		if(unit == null) {
			throw new NotFoundException("unit with id "+unitId+ " not found!");
		}
		return lessonDAO.findByUnitId(unitId).stream()
				.sorted(Comparator.comparing(Lesson::getNumber, Comparator.nullsLast(Comparator.naturalOrder())))
				.map(lesson -> new LessonDTO(lesson)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = false)
	public LessonDTO create(Long unitId, String name, Integer number) throws NotFoundException {
		Unit bdUnit = unitDAO.findById(unitId);
		
		if(bdUnit == null) {
			throw new NotFoundException("Unit with id "+unitId+" not found");
		}
		
		Lesson newLesson = new Lesson();
		newLesson.setUnit(bdUnit);
		newLesson.setName(name);
		newLesson.setNumber(number);
		lessonDAO.create(newLesson);
		
		return new LessonDTO(newLesson);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) throws NotFoundException, IOException {
		Lesson bdLesson = lessonDAO.findById(id);
		
		if(bdLesson == null) {
			throw new NotFoundException("Lesson with id "+id+" not found!");
		}
		
		List<Activity> activities = activityDAO.findByLessonId(id);
		for(int i = 0;i<activities.size();i++) {
			storageService.delete("activity", activities.get(i).getId());
			activityDAO.deleteById(activities.get(i).getId());
		}
		lessonDAO.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public LessonDTO update(LessonDTO lesson) throws NotFoundException {
		Lesson bdLesson = lessonDAO.findById(lesson.getId());
		if(bdLesson == null) {
			throw new NotFoundException("Lesson with id "+lesson.getId()+" not found!");
		}
		bdLesson.setName(lesson.getName());
		bdLesson.setNumber(lesson.getNumber());
		
		lessonDAO.update(bdLesson);
		return new LessonDTO(bdLesson);
	}
	
	public LessonDTO findById(Long id) throws NotFoundException {
		Lesson bdLesson = lessonDAO.findById(id);
		if(bdLesson == null) {
			throw new NotFoundException("Lesson with id "+id+" not found!");
		}
		return new LessonDTO(bdLesson);
	}
	
	public List<LessonDTO> findByBookId(Long bookId) throws NotFoundException {
		Book bdBook = bookDAO.findById(bookId);
		if(bdBook == null) {
			throw new NotFoundException("Book with id "+bookId+" not found!");
		}
		
		return lessonDAO.findByBookId(bookId).stream()
				.sorted(Comparator.comparing(Lesson::getNumber, Comparator.nullsLast(Comparator.naturalOrder())))
				.map(lesson -> new LessonDTO(lesson)).collect(Collectors.toList());
	}

}
