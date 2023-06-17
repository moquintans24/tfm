package es.udc.lbd.tfm.restservice.model.service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import es.udc.lbd.tfm.restservice.model.domain.Activity;
import es.udc.lbd.tfm.restservice.model.domain.Book;
import es.udc.lbd.tfm.restservice.model.domain.ClassGroup;
import es.udc.lbd.tfm.restservice.model.domain.Lesson;
import es.udc.lbd.tfm.restservice.model.domain.Subscription;
import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.model.repository.ActivityDao;
import es.udc.lbd.tfm.restservice.model.repository.BookDao;
import es.udc.lbd.tfm.restservice.model.repository.ClassGroupDao;
import es.udc.lbd.tfm.restservice.model.repository.LessonDao;
import es.udc.lbd.tfm.restservice.model.repository.SubscriptionDao;
import es.udc.lbd.tfm.restservice.model.service.dto.ActivityDTO;
import es.udc.lbd.tfm.restservice.storage.StorageException;
import es.udc.lbd.tfm.restservice.storage.StorageService;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ActivityService {
	
	@Autowired
	private ActivityDao activityDAO;
	
	@Autowired
	private LessonDao lessonDAO;
	
	@Autowired
	private BookDao bookDAO;
	
	@Autowired
	private ClassGroupDao classGroupDAO;
	
	@Autowired
	private SubscriptionDao subscriptionDAO;
	
	@Autowired
	private StorageService storageService;
	
	public List<ActivityDTO> findByLessonId(Long lessonId) throws NotFoundException{
		Lesson lesson = lessonDAO.findById(lessonId);
		if(lesson == null) {
			throw new NotFoundException("Lesson with id "+ lessonId+" not found!");
		}
		return activityDAO.findByLessonId(lessonId).stream().map(activity -> new ActivityDTO(activity)).collect(Collectors.toList());
	}
	
	public List<ActivityDTO> findByBookId(Long bookId) throws NotFoundException{
		Book bdBook = bookDAO.findById(bookId);
		if(bdBook == null) {
			throw new NotFoundException("Book with id "+bookId+" not found!");
		}
		return activityDAO.findByBookId(bookId).stream().map(activity -> new ActivityDTO(activity)).collect(Collectors.toList());
	}
	
	public List<?> totalStudentsRealizedActivity(Long classGroupId)  throws NotFoundException{
		ClassGroup bdClassGroup = classGroupDAO.findById(classGroupId);
		if(bdClassGroup == null) {
			throw new NotFoundException("Class group with id "+classGroupId+" not found!");
		}
		Subscription bdSubscription = subscriptionDAO.findById(bdClassGroup.getSubscription().getId());
		return activityDAO.totalStudentsRealizedActivity(bdSubscription.getId(), classGroupId);
		
	}
	
	public Boolean findByIdWithInteractionActivity(Long activityId) throws NotFoundException{
		Activity bdActivity = activityDAO.findById(activityId);
		if(bdActivity == null) {
			throw new NotFoundException("Activity with id "+activityId+" not found!");
		}
		return activityDAO.findByIdWithInteractionActivity(activityId);
	}
	
	@Transactional(readOnly = false)
	public ActivityDTO create(Long lessonId, String code, String title, String description, Integer number,MultipartFile image,
			String idH5PFile) throws NotFoundException, StorageException {
		Lesson bdLesson = lessonDAO.findById(lessonId);
		if(bdLesson == null) {
			throw new NotFoundException("Lesson with id "+lessonId+" not found!");
		}
		String imagePath = "";
		if(image!=null) {
			imagePath = image.getOriginalFilename();
		}else {
			imagePath = "default_activity.png"; //A las actividades nuevas les asignamos una por defecto si el editor no añade ninguna
		}
		Activity bdActivity = new Activity(code, title, description, null,null, imagePath, bdLesson, number);
		
		bdActivity.setTypeH5P(true);

		activityDAO.create(bdActivity);
		
		ActivityDTO activity = new ActivityDTO(bdActivity);
		if(image!=null) {
			storageService.store(image, title, "activity", activity.getId());
		}
		return new ActivityDTO(bdActivity);
	}
	
	public ActivityDTO findById(Long id) throws NotFoundException {
		Activity bdActivity = activityDAO.findById(id);
		if(bdActivity == null) {
			throw new NotFoundException("Activity with id "+id+" not found!");
		}
		return new ActivityDTO(bdActivity);
	}
	
	@Transactional(readOnly = false)
	public ActivityDTO updateLinkImage(ActivityDTO activity) {
		Activity bdActivity = activityDAO.findById(activity.getId());
		bdActivity.setImagePath(activity.getImagePath());
		return new ActivityDTO(bdActivity);
	}
	
	@Transactional(readOnly = false)
	public void deleteById(Long activityId) throws NotFoundException, IOException {
		Activity bdActivity = activityDAO.findById(activityId);
		storageService.delete("activity", activityId);
		if(bdActivity == null) {
			throw new NotFoundException("Activity with id "+activityId+" not found!");
		}
		activityDAO.deleteById(activityId);
	}
	
	@Transactional(readOnly = false)
	public ActivityDTO update(ActivityDTO activity) throws NotFoundException {
		Activity bdActivity = activityDAO.findById(activity.getId());
		if(bdActivity == null) {
			throw new NotFoundException("Activity with id "+activity.getId()+" not found!");
		}
		bdActivity.setCode(activity.getCode());
		bdActivity.setDescription(activity.getDescription());
		bdActivity.setNumber(activity.getNumber());
		bdActivity.setTitle(activity.getTitle());
		
		bdActivity.setIdH5Pfile(activity.getIdH5Pfile());
		
		activityDAO.update(bdActivity);
		return new ActivityDTO(bdActivity);
		
	}
	
	public List<ActivityDTO> findAvailableByClassGroupId(Long classGroupId) throws NotFoundException {
		ClassGroup bdClassGroup = classGroupDAO.findById(classGroupId);
		if(bdClassGroup == null) {
			throw new NotFoundException("Class group with id "+classGroupId+" not found!");
		}
		return activityDAO.findAvailableByClassGroupId(classGroupId, bdClassGroup.getLesson().getNumber()).stream()
				.sorted(Comparator.comparing(Activity::getNumber, Comparator.nullsLast(Comparator.naturalOrder())))
				.map(activity -> new ActivityDTO(activity)).collect(Collectors.toList());
	}
	
	public List<ActivityDTO> findByIdH5PFile(String idH5PFile){
		return activityDAO.findByIdH5PFile(idH5PFile).stream().map(activity -> new ActivityDTO(activity)).collect(Collectors.toList());
	}
	
	public List<ActivityDTO> findByImage(String imagePath){
		return activityDAO.findByImagePath(imagePath).stream().map(activity -> new ActivityDTO(activity)).collect(Collectors.toList());
	}

}
