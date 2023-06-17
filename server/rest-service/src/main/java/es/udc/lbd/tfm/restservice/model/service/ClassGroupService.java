package es.udc.lbd.tfm.restservice.model.service;

import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.tfm.restservice.model.domain.Activity;
import es.udc.lbd.tfm.restservice.model.domain.Book;
import es.udc.lbd.tfm.restservice.model.domain.ClassGroup;
import es.udc.lbd.tfm.restservice.model.domain.Lesson;
import es.udc.lbd.tfm.restservice.model.domain.Subscription;
import es.udc.lbd.tfm.restservice.model.domain.Unit;
import es.udc.lbd.tfm.restservice.model.domain.User;
import es.udc.lbd.tfm.restservice.model.domain.UserAuthority;
import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.model.repository.ActivityDao;
import es.udc.lbd.tfm.restservice.model.repository.BookDao;
import es.udc.lbd.tfm.restservice.model.repository.ClassGroupDao;
import es.udc.lbd.tfm.restservice.model.repository.LessonDao;
import es.udc.lbd.tfm.restservice.model.repository.SubscriptionDao;
import es.udc.lbd.tfm.restservice.model.repository.UnitDao;
import es.udc.lbd.tfm.restservice.model.repository.UserDao;
import es.udc.lbd.tfm.restservice.model.service.dto.ClassGroupDTO;
import es.udc.lbd.tfm.restservice.model.service.dto.LessonDTO;

import java.security.SecureRandom;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ClassGroupService {

	@Autowired
	private ClassGroupDao classGroupDAO;
	
	@Autowired
	private SubscriptionDao subscriptionDAO;
	
	@Autowired
	private UserDao userDAO;
	
	@Autowired
	private BookDao bookDAO;
	
	@Autowired
	private LessonDao lessonDAO;
	
	@Autowired
	private UnitDao unitDAO;
	
	@Autowired
	private ActivityDao activityDAO;
	
	@Autowired
	private InteractionActivityService interactionActivityService;
	
	public List<ClassGroupDTO> findAll(){
		return classGroupDAO.findAll().stream().map(classGroup -> new ClassGroupDTO(classGroup)).collect(Collectors.toList());
	}
	
	public List<ClassGroupDTO> findByUserId(Long userId) throws NotFoundException{
		User bdUser = userDAO.findById(userId);
		if(bdUser == null) {
			throw new NotFoundException("User with id "+userId+" not found!");
		}
		if(bdUser.getAuthority()==UserAuthority.TEACHER) {
			return classGroupDAO.findByUserId(userId).stream().sorted(Comparator.comparing(ClassGroup::getName))
					.map(classGroup -> new ClassGroupDTO(classGroup)).collect(Collectors.toList());
		}else {
			return classGroupDAO.findByStudentId(userId).stream().sorted(Comparator.comparing(ClassGroup::getName))
					.map(classGroup -> new ClassGroupDTO(classGroup)).collect(Collectors.toList());
		}
		
	}
	
	public ClassGroupDTO findById(Long id) throws NotFoundException {
		ClassGroup classGroup = classGroupDAO.findById(id);
		if(classGroup == null) {
			throw new NotFoundException("Class group with id "+ id + " not found!");
		}
		return new ClassGroupDTO(classGroup);
	}
	
	@PreAuthorize("hasAuthority('TEACHER')")
	@Transactional(readOnly = false)
	public ClassGroupDTO create(ClassGroupDTO classGroup) {
		ClassGroup bdClassGroup = new ClassGroup();
		bdClassGroup.setName(classGroup.getName());
		String classCode = generateRandomCode();

		bdClassGroup.setSubscription(subscriptionDAO.findById(classGroup.getTeacher().getId()));

		if(classGroupDAO.findByCode(classCode) == null) {
			bdClassGroup.setCode(classCode);
			classGroupDAO.create(bdClassGroup);
		}
		return new ClassGroupDTO(bdClassGroup);
	}
	
	private String generateRandomCode () {
		String characterSet = "0123456789abcdefghijklmnopqrstuvwxyz";
		StringBuffer sbuff = new StringBuffer(16);
		SecureRandom random = new SecureRandom();
		for(int j=0;j<14;j++) {
			int offset = random.nextInt(characterSet.length());
			sbuff.append(characterSet.substring(offset,offset+1));
		}
		return "CL"+sbuff.toString();
	}
	
	@PreAuthorize("hasAuthority('TEACHER')")
	@Transactional(readOnly = false)
	public ClassGroupDTO update(ClassGroupDTO classGroup) {
		ClassGroup bdClassGroup = classGroupDAO.findById(classGroup.getId());
		bdClassGroup.setName(classGroup.getName());
		bdClassGroup.setSubscription(subscriptionDAO.findById(classGroup.getTeacher().getId()));
		
		classGroupDAO.update(bdClassGroup);
		return new ClassGroupDTO(bdClassGroup);
	}
	
	@PreAuthorize("hasAuthority('TEACHER')")
	@Transactional(readOnly = false)
	public void deleteById(Long id) throws NotFoundException {
		
		ClassGroup bdClassGroup = classGroupDAO.findById(id);
		
		if(bdClassGroup == null) {
			throw new NotFoundException("Class group with id "+id+" not found!");
		}
		//Al eliminar una clase se eliminan, de todas las suscripciones asociadas el id de clase que se va a eliminar
		List<Subscription> subscriptions = subscriptionDAO.findByClassGroupId(id);
		for(int i=0;i<subscriptions.size();i++) {
			subscriptions.get(i).setClassGroup(null);
		}
		classGroupDAO.deleteById(id);
	}
	
	public List<ClassGroupDTO> findByBookIdAndUserId(Long bookId, Long userId) throws NotFoundException {
		Book bdBook = bookDAO.findById(bookId);
		if(bdBook == null) {
			throw new NotFoundException("Book with id "+bookId+" not found!");
		}
		User bdUser = userDAO.findById(userId);
		if(bdUser == null) {
			throw new NotFoundException("User with id "+userId+" not found!");
		}
		
		return classGroupDAO.findByBookIdAndUserId(bookId, userId).stream().map(classGroup -> new ClassGroupDTO(classGroup)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = false)
	public ClassGroupDTO showHideLessonByClassGroupId(Long classGroupId, LessonDTO lesson,String type) throws NotFoundException {
		ClassGroup bdClassgroup = classGroupDAO.findById(classGroupId);
		if(bdClassgroup == null) {
			throw new NotFoundException("Class group with id "+classGroupId+" not found!");
		}
		if(type.equals("SHOW")) {
			bdClassgroup.setLesson(lessonDAO.findById(lesson.getId()));
			
			List<Lesson> lessons = lessonDAO.findByBookId(lesson.getUnit().getBook().getId());
			
			for(int i=0;i<lessons.size();i++) {
				List<Activity> activities = activityDAO.findByLessonId(lessons.get(i).getId());
				if(activities.size() > 0) {
					if(lessons.get(i).getNumber() <= lesson.getNumber()) {
						for(int j= 0;j<activities.size();j++) { 
							//En el momento en el que se pone una lección visible se genera una interacción de actividad para los alumnos de la clase excepto que ya tengan
							//Se genera una interacción de actividad para la lección que se habilita y para todas las anteriores
							if(interactionActivityService.findByActivityIdAndClassGroupId(activities.get(j).getId(), classGroupId).size()==0) {
								interactionActivityService.createInteractionsByActivityIdAndClassGroupId(activities.get(j).getId(), classGroupId);
							}
						}
					}
				}
				
			}
			
			
		}else {
			List<Lesson> lessons = lessonDAO.findByUnitId(lesson.getUnit().getId());
			List<Unit> units = unitDAO.findByBookId(bdClassgroup.getSubscription().getCode().getBook().getId());
			
			for(int i=0;i<lessons.size();i++) {
				if(lessons.get(i).getId().equals(lesson.getId())) {
					
					if(i == 0 && units.indexOf(lessons.get(i).getUnit()) == 0) { //si es la primera lección del libro ocultamos todo el contenido
						bdClassgroup.setLesson(null);
					}else { //sino mostramos hasta la lección anterior a la que se oculta
						bdClassgroup.setLesson(lessonDAO.findById(lessons.get(i).getId()-1)); 
					}
				}
			}
		}
		
		classGroupDAO.update(bdClassgroup);
		return new ClassGroupDTO(bdClassgroup);
	}
	
	public List<ClassGroupDTO> findByBookIdAndTeacherSubscriptionId(Long bookId, Long teacherSubscriptionId) throws NotFoundException{
		Book bdBook = bookDAO.findById(bookId);
		if(bdBook == null) {
			throw new NotFoundException("Book with id "+bookId+" not found!");
		}
		Subscription bdSubscription = subscriptionDAO.findById(teacherSubscriptionId);
		if(bdSubscription == null ) {
			throw new NotFoundException("Subscription with id "+teacherSubscriptionId+" not found!");
		}
		return classGroupDAO.findByBookIdAndTeacherSubscriptionId(bookId, teacherSubscriptionId).stream().sorted(Comparator.comparing(ClassGroup::getName))
		.map(classGroup -> new ClassGroupDTO(classGroup)).collect(Collectors.toList());
	}

}
