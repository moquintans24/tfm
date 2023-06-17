package es.udc.lbd.tfm.restservice.model.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.tfm.restservice.model.domain.Activity;
import es.udc.lbd.tfm.restservice.model.domain.ClassGroup;
import es.udc.lbd.tfm.restservice.model.domain.InteractionActivity;
import es.udc.lbd.tfm.restservice.model.domain.Subscription;
import es.udc.lbd.tfm.restservice.model.domain.Unit;
import es.udc.lbd.tfm.restservice.model.domain.User;
import es.udc.lbd.tfm.restservice.model.domain.UserAuthority;
import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.model.repository.ActivityDao;
import es.udc.lbd.tfm.restservice.model.repository.ClassGroupDao;
import es.udc.lbd.tfm.restservice.model.repository.InteractionActivityDao;
import es.udc.lbd.tfm.restservice.model.repository.SubscriptionDao;
import es.udc.lbd.tfm.restservice.model.repository.UnitDao;
import es.udc.lbd.tfm.restservice.model.repository.UserDao;
import es.udc.lbd.tfm.restservice.model.service.dto.InteractionActivityDTO;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class InteractionActivityService {

	@Autowired
	private InteractionActivityDao interactionActivityDAO;
	
	@Autowired
	private ActivityDao activityDAO;
	
	@Autowired
	private SubscriptionDao subscriptionDAO;
	
	@Autowired
	private ClassGroupDao classGroupDAO;
	
	@Autowired
	private UserDao userDAO;
	
	@Autowired
	private UnitDao unitDAO;
	
	@Autowired
	private UserService userService;
	
	public List<InteractionActivityDTO> findByClassGroupIdAndTeacherSubscriptionId(Long classGroupId) throws NotFoundException{
		ClassGroup bdClassGroup = classGroupDAO.findById(classGroupId);
		if(bdClassGroup == null) {
			throw new NotFoundException("Class group with id "+classGroupId+" not found!");
		}
		Subscription bdSubscription = subscriptionDAO.findById(bdClassGroup.getSubscription().getId());
		return interactionActivityDAO.findByClassGroupIdAndTeacherSubscriptionId(bdSubscription.getId(), classGroupId).stream()
				.sorted(Comparator.comparing(InteractionActivity::getHomeworkDate, Comparator.nullsLast(Comparator.naturalOrder()))).map(interaction -> new InteractionActivityDTO(interaction))
				.collect(Collectors.toList());
	}
	
	public List<InteractionActivityDTO> findByClassGroupIdAndSubscriptionId(Long classGroupId, Long subscriptionId) throws NotFoundException{
		ClassGroup bdClassGroup = classGroupDAO.findById(classGroupId);
		if(bdClassGroup == null) {
			throw new NotFoundException("Class group with id "+classGroupId+" not found!");
		}
		Subscription bdSubscription = subscriptionDAO.findById(subscriptionId);
		if(bdSubscription == null) {
			throw new NotFoundException("Subscription with id "+subscriptionId+" not found!");
		}
		return interactionActivityDAO.findByClassGroupIdAndSubscriptionId(classGroupId,subscriptionId).stream()
				.sorted(Comparator.comparing(InteractionActivity::getHomeworkDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed()).map(interaction -> new InteractionActivityDTO(interaction))
				.collect(Collectors.toList());
	}
	
	public List<InteractionActivityDTO> findByActivityIdAndClassGroupId(Long id, Long classGroupId) throws NotFoundException{
		ClassGroup bdClassGroup = classGroupDAO.findById(classGroupId);
		if(bdClassGroup == null) {
			throw new NotFoundException("Class group with id "+classGroupId+" not found!");
		}
		Subscription bdSubscription = subscriptionDAO.findById(bdClassGroup.getSubscription().getId());
		return interactionActivityDAO.findByActivityIdAndClassGroupIdAndTeacherSubscriptionId(id,classGroupId,bdSubscription.getId()).stream()
				.sorted(Comparator.comparing(InteractionActivity::getHomeworkDate, Comparator.nullsLast(Comparator.naturalOrder()))).map(interaction -> new InteractionActivityDTO(interaction))
				.collect(Collectors.toList());
	}
	
	@Transactional(readOnly = false)
	public void createInteractionsByActivityIdAndClassGroupId(Long activityId, Long classGroupId) throws NotFoundException{
		ClassGroup bdClassGroup = classGroupDAO.findById(classGroupId);
		if(bdClassGroup == null) {
			throw new NotFoundException("Class group with id "+classGroupId+" not found!");
		}
		Activity bdActivity = activityDAO.findById(activityId);
		if(bdActivity == null) {
			throw new NotFoundException("Activity with id "+activityId+" not found!");
		}
		Subscription bdTeacherSubscription = subscriptionDAO.findById(bdClassGroup.getSubscription().getId());
		List<Subscription> students = subscriptionDAO.findByCompletedStateAndClassGroupId(classGroupId);

		for(int i=0;i<students.size();i++) {
			InteractionActivity bdInteraction = new InteractionActivity();
			bdInteraction.setActivity(activityDAO.findById(activityId));
			bdInteraction.setTeacherSubscription(bdTeacherSubscription);
			bdInteraction.setSubscription(students.get(i));
			interactionActivityDAO.create(bdInteraction);
		}
	}
	
	@Transactional(readOnly = false)
	public InteractionActivityDTO create(InteractionActivityDTO interactionActivity) {
		InteractionActivity bdInteraction = new InteractionActivity();
		bdInteraction.setDuration(interactionActivity.getDuration());
		bdInteraction.setScore(interactionActivity.getScore());
		bdInteraction.setActivity(activityDAO.findById(interactionActivity.getActivity().getId()));
		bdInteraction.setSubscription(subscriptionDAO.findById(interactionActivity.getSubscription().getId()));
		bdInteraction.setTeacherSubscription(subscriptionDAO.findById(interactionActivity.getTeacherSubscription().getId()));
		
		interactionActivityDAO.create(bdInteraction);
		return new InteractionActivityDTO(bdInteraction);
		
	}
	
	@Transactional(readOnly = false)
	public void assignInteractionsByActivityIdAndClassGroupId(Long activityId, Long classGroupId, LocalDateTime homeworkDate) throws NotFoundException{
		ClassGroup bdClassGroup = classGroupDAO.findById(classGroupId);
		if(bdClassGroup == null) {
			throw new NotFoundException("Class group with id "+classGroupId+" not found!");
		}
		Activity bdActivity = activityDAO.findById(activityId);
		if(bdActivity == null) {
			throw new NotFoundException("Activity with id "+activityId+" not found!");
		}
		Subscription bdTeacherSubscription = subscriptionDAO.findById(bdClassGroup.getSubscription().getId());
		List<Subscription> students = subscriptionDAO.findByCompletedStateAndClassGroupId(classGroupId);

		for(int i=0;i<students.size();i++) {
			InteractionActivity bdInteraction = new InteractionActivity();
			bdInteraction.setActivity(activityDAO.findById(activityId));
			bdInteraction.setTeacherSubscription(bdTeacherSubscription);
			bdInteraction.setSubscription(students.get(i));
			if(homeworkDate!=null) {
				bdInteraction.setHomeworkDate(homeworkDate);
			}
			interactionActivityDAO.create(bdInteraction);
		}
	}
	
	public List<InteractionActivityDTO> findPendingsByClassGroupIdAndUserId(Long classGroupId, Long userId) throws NotFoundException{
		ClassGroup bdClassGroup = classGroupDAO.findById(classGroupId);
		if(bdClassGroup == null) {
			throw new NotFoundException("Subscription with id "+classGroupId+" not found!");
		}
		User bdUser = userDAO.findById(userId);
		if(bdUser == null) {
			throw new NotFoundException("User with id "+userId+" not found!");
		}
		Subscription bdSubscription = subscriptionDAO.findByClassGroupIdAndUserId(classGroupId, userId);
		return interactionActivityDAO.findPendingsBySubscriptionId(bdSubscription.getId()).stream()
				.sorted(Comparator.comparing(InteractionActivity::getHomeworkDate, Comparator.nullsLast(Comparator.naturalOrder())))
				.map(interaction -> new InteractionActivityDTO(interaction))
				.collect(Collectors.toList());
	}
	
	@Transactional(readOnly = false)
	public void updateHomeworkDateByClassGroupIdTeacherSubscriptionIdAndActivityId(Long classGroupId, Long subscriptionId,Long activityId,LocalDateTime homeworkDate) throws NotFoundException {
		ClassGroup bdClassGroup = classGroupDAO.findById(classGroupId);
		if(bdClassGroup == null) {
			throw new NotFoundException("Subscription with id "+classGroupId+" not found!");
		}
		Subscription bdSubscription = subscriptionDAO.findById(subscriptionId);
		if(bdSubscription == null) {
			throw new NotFoundException("Subscription with id "+subscriptionId+" not found!");
		}
		Activity bdActivity = activityDAO.findById(activityId);
		if(bdActivity == null) {
			throw new NotFoundException("Activity with id "+activityId+" not found!");
		}
		List<InteractionActivity> interactions = interactionActivityDAO.findByActivityIdAndClassGroupIdAndTeacherSubscriptionId(activityId,classGroupId, subscriptionId);
		for(int i=0;i<interactions.size();i++) {
			
			if(homeworkDate!=null) {
				interactions.get(i).setHomeworkDate(homeworkDate);
			}else {
				interactions.get(i).setHomeworkDate(null);
			}
			interactionActivityDAO.update(interactions.get(i));
		}
		
	}
	
	@Transactional(readOnly = false)
	public void saveDurationAndScore(LocalTime duration, Double score, Long interactionActivityId) throws NotFoundException {
		InteractionActivity bdInteraction = interactionActivityDAO.findById(interactionActivityId);
		if(bdInteraction == null) {
			throw new NotFoundException("Interaction activity with id "+interactionActivityId+" not found!");
		}
		if(userService.getCurrentUserWithAuthority().getAuthority().equals(UserAuthority.USER.toString()) ) {
			if(bdInteraction.getHomeworkDate()!=null) {
				if(LocalDateTime.now().isBefore(bdInteraction.getHomeworkDate()) ) {
					if(bdInteraction.getDuration()== null) {
						bdInteraction.setDuration(duration);
					}
					if(bdInteraction.getScore() == null) {
						bdInteraction.setScore(score);
					}
					interactionActivityDAO.update(bdInteraction);
				}
			}else {
				if(bdInteraction.getDuration()== null) {
					bdInteraction.setDuration(duration);
				}
				if(bdInteraction.getScore() == null) {
					bdInteraction.setScore(score);
				}
				interactionActivityDAO.update(bdInteraction);
			}
		}
	}
	
	public InteractionActivityDTO findByActivityIdAndUserId(Long activityId, Long userId) throws NotFoundException {
		User bdUser = userDAO.findById(userId);
		if(bdUser == null) {
			throw new NotFoundException("User with id "+userId+" not found!");
		}
		Activity bdActivity = activityDAO.findById(activityId);
		if(bdActivity == null) {
			throw new NotFoundException("Activity with id "+activityId+" not found!");
		}
		return new InteractionActivityDTO(interactionActivityDAO.findByActivityIdAndUserId(activityId, userId)) ;
	}
	
	public List<?> averageScoreStudentsByClassGroupId(Long classGroupId) throws NotFoundException{
		ClassGroup bdClassGroup = classGroupDAO.findById(classGroupId);
		
		if(bdClassGroup == null) {
			throw new NotFoundException("Class group with id "+classGroupId+" not found!");
		}
		return interactionActivityDAO.averageScoreStudentsByClassGroupId(classGroupId);
	}
	public List<?> averageScoreStudentsByClassGroupIdAndUnitId(Long classGroupId, Long unitId) throws NotFoundException{
		ClassGroup bdClassGroup = classGroupDAO.findById(classGroupId);
		if(bdClassGroup == null) {
			throw new NotFoundException("Class group with id "+classGroupId+" not found!");
		}
		Unit bdUnit = unitDAO.findById(unitId);
		if(bdUnit == null) {
			throw new NotFoundException("Unit with id "+unitId+" not found!");
		}
		
		return interactionActivityDAO.averageScoreStudentsByClassGroupIdAndUnitId(classGroupId, unitId);
	}
}
