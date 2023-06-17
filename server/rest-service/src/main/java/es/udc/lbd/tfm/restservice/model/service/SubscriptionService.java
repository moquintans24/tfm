package es.udc.lbd.tfm.restservice.model.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.tfm.restservice.model.domain.Book;
import es.udc.lbd.tfm.restservice.model.domain.ClassGroup;
import es.udc.lbd.tfm.restservice.model.domain.Code;
import es.udc.lbd.tfm.restservice.model.domain.CodeState;
import es.udc.lbd.tfm.restservice.model.domain.InteractionActivity;
import es.udc.lbd.tfm.restservice.model.domain.Lesson;
import es.udc.lbd.tfm.restservice.model.domain.Subscription;
import es.udc.lbd.tfm.restservice.model.domain.SubscriptionState;
import es.udc.lbd.tfm.restservice.model.domain.User;
import es.udc.lbd.tfm.restservice.model.exception.CodeExpiredException;
import es.udc.lbd.tfm.restservice.model.exception.InvalidBookCodeException;
import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.model.exception.SubscriptionCodeExistisException;
import es.udc.lbd.tfm.restservice.model.exception.SubscriptionException;
import es.udc.lbd.tfm.restservice.model.repository.ActivityDao;
import es.udc.lbd.tfm.restservice.model.repository.BookDao;
import es.udc.lbd.tfm.restservice.model.repository.ClassGroupDao;
import es.udc.lbd.tfm.restservice.model.repository.CodeDao;
import es.udc.lbd.tfm.restservice.model.repository.InteractionActivityDao;
import es.udc.lbd.tfm.restservice.model.repository.LessonDao;
import es.udc.lbd.tfm.restservice.model.repository.SubscriptionDao;
import es.udc.lbd.tfm.restservice.model.repository.UserDao;
import es.udc.lbd.tfm.restservice.model.service.dto.ActivityDTO;
import es.udc.lbd.tfm.restservice.model.service.dto.SubscriptionDTO;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class SubscriptionService {

	@Autowired
	private SubscriptionDao subscriptionDAO;
	
	@Autowired
	private UserDao userDAO;
	
	@Autowired
	private CodeDao codeDAO;
	
	@Autowired
	private ClassGroupDao classGroupDAO;
	
	@Autowired
	private ActivityDao activityDAO;
	
	@Autowired
	private InteractionActivityDao interactionActivityDAO;
	
	@Autowired
	private LessonDao lessonDAO;
	
	@Autowired
	private BookDao bookDAO;
	
	@Autowired
	private ActivityService activityService;

	
	public SubscriptionDTO findById(Long id) throws NotFoundException {
		Subscription subs = subscriptionDAO.findById(id);
		if(subs == null) {
			throw new NotFoundException("Subscription with id " + id + " not found!" );
		}
		return new SubscriptionDTO(subs);
	}
	
	public List<SubscriptionDTO> findByUserId(Long userId) throws NotFoundException {
		User user = userDAO.findById(userId);
		if(user == null) {
			throw new NotFoundException("User with id "+ userId + " not found!");
		}
		return subscriptionDAO.findByUserId(userId).stream().map(subscription -> new SubscriptionDTO(subscription)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = false)
	public void create(Long userId, String code) throws NotFoundException, SubscriptionCodeExistisException, SubscriptionException, CodeExpiredException {
		User bdUser = userDAO.findById(userId);
		if (bdUser == null) {
			throw new NotFoundException("User with id " + userId + " not found!");
		}
		Code bdCode = codeDAO.findByCode(code);
		if (bdCode == null) {
			throw new NotFoundException("Code " + code + " is not valid!");
		}
		if(subscriptionDAO.findByCodeAndUserId(code, userId)!= null) {
			throw new SubscriptionCodeExistisException("Subscription with code "+code+" already exists!");
		}
		if(subscriptionDAO.findByBookIdAndUserId(bdCode.getBook().getId(), userId)!=null) {
			throw new SubscriptionException("You already have a subscription to the book "+bdCode.getBook().getTitle());
		}
		//Comprobamos si el código introducido es válido (no caducó)
		if(LocalDate.now().isAfter(bdCode.getExpirationDate())) {
			throw new CodeExpiredException("Code "+code+" has expired!");
		}
		bdCode.setState(CodeState.USED);
		codeDAO.update(bdCode);
		Subscription bdSubscription = new Subscription();
		bdSubscription.setCode(bdCode);
		bdSubscription.setEntryDate(LocalDateTime.now());
		bdSubscription.setUser(bdUser);
		subscriptionDAO.create(bdSubscription);
	}
	
	public List<SubscriptionDTO> findByClassGroupId(Long classGroupId) throws NotFoundException{
		ClassGroup bdClassGroup = classGroupDAO.findById(classGroupId);
		if (bdClassGroup == null) {
			throw new NotFoundException("Class group with id "+classGroupId+" not found!");
		}
		return subscriptionDAO.findByClassGroupId(classGroupId).stream().map(subs -> new SubscriptionDTO(subs)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = false)
	public void deleteClassGroupId(Long subsId) throws NotFoundException {
		Subscription bdSubscription = subscriptionDAO.findById(subsId);
		if(bdSubscription == null) {
			throw new NotFoundException("Subscription with id "+subsId+" not found!");
		}
		bdSubscription.setClassGroup(null);
	}
	
	public void deleteById(Long id) {
		subscriptionDAO.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void cancelSubscriptionToClassGroup(Long subscriptionId) throws NotFoundException {
		Subscription bdSubscription = subscriptionDAO.findById(subscriptionId);
		if(bdSubscription == null) {
			throw new NotFoundException("Subscription with id "+subscriptionId+" not found!");
		}
		subscriptionDAO.cancelSubscriptionToClassGroup(subscriptionId);
	}
	
	@Transactional(readOnly = false)
	public void acceptSubscriptionToClassGroup(Long subscriptionId) throws NotFoundException {
		Subscription bdSubscription = subscriptionDAO.findById(subscriptionId);
		if(bdSubscription == null) {
			throw new NotFoundException("Subscription with id "+subscriptionId+" not found!");
		}
		//Al aceptar la suscripción de un usuario a una clase, le generamos las mismas interacciones de actividad que el resto de alumnos
		ClassGroup bdClassGroup = classGroupDAO.findById(bdSubscription.getClassGroup().getId());
		if(bdClassGroup.getLesson()!=null) {
			List<Lesson> lessons = lessonDAO.findByBookId(bdClassGroup.getLesson().getUnit().getBook().getId());
			for(int i=0;i<lessons.size();i++) {
				List<ActivityDTO> activities = new ArrayList<>();
				activities = activityService.findByLessonId(lessons.get(i).getId());
				if(activities.size() > 0) {
					for(int j = 0;j<activities.size();j++) {
						if(activities.get(j).getLesson().getNumber() <= bdClassGroup.getLesson().getNumber()) {
							InteractionActivity bdInteraction = new InteractionActivity();
							bdInteraction.setActivity(activityDAO.findById(activities.get(j).getId()));
							bdInteraction.setTeacherSubscription(bdSubscription.getClassGroup().getSubscription());
							bdInteraction.setSubscription(bdSubscription);
							List<InteractionActivity> interactions = interactionActivityDAO.findByActivityIdAndClassGroupId(activities.get(j).getId(), 
									bdSubscription.getClassGroup().getId());
							if(interactions.size()>0) {
								if(interactions.get(1).getHomeworkDate()!=null) {
									bdInteraction.setHomeworkDate(interactions.get(1).getHomeworkDate());
								}
							}
							interactionActivityDAO.create(bdInteraction);
						}
						
					}
					
					
				}
				
			}
		}
		
	
		subscriptionDAO.acceptSubscriptionToClassGroup(subscriptionId);
	}
	
	public SubscriptionDTO findByClassGroupAndUserId(Long classGroupId, Long userId) throws NotFoundException {
		ClassGroup bdClassGroup = classGroupDAO.findById(classGroupId);
		if(bdClassGroup == null) {
			throw new NotFoundException("Class group with id "+classGroupId+" not found!");
		}
		User bdUser = userDAO.findById(userId);
		if(bdUser == null) {
			throw new NotFoundException("User with id "+ userId +" not found!");
		}
		return new SubscriptionDTO(subscriptionDAO.findByClassGroupIdAndUserId(classGroupId, userId));
		
	}
	
	@Transactional(readOnly = false)
	public void updateUserSubscriptionsAddUserToClassGroup(String classGroupCode,Long userId, String bookCode) 
			throws NotFoundException, InvalidBookCodeException, SubscriptionCodeExistisException, CodeExpiredException {
		User bdUser = userDAO.findById(userId);
		if(bdUser == null) {
			throw new NotFoundException("User with id "+ userId +" not found!");
		}
		ClassGroup bdClassGroup = classGroupDAO.findByCode(classGroupCode);
		if(bdClassGroup == null) {
			throw new NotFoundException("Class group with code "+classGroupCode+" not found!");
		}
		Code bdCode = codeDAO.findByCode(bookCode);
		if(bdCode == null) {
			throw new NotFoundException("Book code "+bookCode+" not found!");
		}
		if(LocalDate.now().isAfter(bdCode.getExpirationDate())){
			throw new CodeExpiredException("Book code "+bookCode+" has expired!");
		}
		//Validamos que el código del libro introducido por el usuario se corresponde con el libro que está asociado al grupo de clase
		if(!bdCode.getBook().getId().equals(bdClassGroup.getSubscription().getCode().getBook().getId())) {
			throw new InvalidBookCodeException("Book code "+bookCode+ " is not valid for the class group "+bdClassGroup.getName());
		}
		Long subscriptionId = subscriptionDAO.findByClassGroupCodeAndUserId(classGroupCode, userId);
		//Si no tiene ninguna suscripción asociada al libro de la clase se crea una nueva, sino se actualiza la que ya tiene
		if(subscriptionId == (long) -1) {
			Subscription newSubscription = new Subscription();
			newSubscription.setCode(bdCode); //código de libro indicado por el usuario
			newSubscription.setClassGroup(bdClassGroup);
			newSubscription.setEntryDate(LocalDateTime.now());
			newSubscription.setState(SubscriptionState.PENDING);
			newSubscription.setUser(bdUser);
			subscriptionDAO.create(newSubscription);			
		}else{
			Subscription bdSubscription = subscriptionDAO.findById(subscriptionId);
			if(bdSubscription.getState().equals(SubscriptionState.COMPLETED)) {
				throw new SubscriptionCodeExistisException("You already have a subscription to class group "+bdClassGroup.getName());
			}
			bdSubscription.setState(SubscriptionState.PENDING);
			bdSubscription.setClassGroup(bdClassGroup);
			subscriptionDAO.update(bdSubscription);

		}
		
	}
	
	public SubscriptionDTO findByBookIdAndUserId(Long bookId,Long userId) throws NotFoundException {
		User bdUser = userDAO.findById(userId);
		if(bdUser==null) {
			throw new NotFoundException("User with id "+userId+" not found!");
		}
		Book bdBook = bookDAO.findById(bookId);
		if(bdBook == null) {
			throw new NotFoundException("Book with id "+bookId+" not found!");
		}
		return new SubscriptionDTO(subscriptionDAO.findByBookIdAndUserId(bookId, userId));
	}
	
}
