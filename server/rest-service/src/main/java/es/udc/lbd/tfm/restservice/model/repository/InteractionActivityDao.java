package es.udc.lbd.tfm.restservice.model.repository;

import java.util.List;

import es.udc.lbd.tfm.restservice.model.domain.InteractionActivity;

public interface InteractionActivityDao {
	
	void create(InteractionActivity interactionActivity);
	
	InteractionActivity findById(Long id);
	
	List<InteractionActivity> findByClassGroupIdAndTeacherSubscriptionId(Long teacherSubscriptionId, Long classGroupId);
	
	List<InteractionActivity> findByClassGroupIdAndSubscriptionId(Long classGroupId, Long subscriptionId); //Student subscription
	
	List<InteractionActivity> findByActivityIdAndClassGroupIdAndTeacherSubscriptionId(Long id,Long classGroupId, Long teacherSubscriptionId);
	
	List<InteractionActivity> findBySubscriptionId(Long id);
	
	List<InteractionActivity> findPendingsBySubscriptionId(Long subscriptionId);
	
	InteractionActivity findByActivityIdAndUserId(Long activityId, Long userId);
	
	List<InteractionActivity> findByActivityIdAndClassGroupId(Long activityId, Long classGroupId);
	
	List<?> averageScoreStudentsByClassGroupId(Long classGroupId);
	
	List<?> averageScoreStudentsByClassGroupIdAndUnitId(Long classGroupId, Long unitId);
	
	void deleteById(Long id);
	
	void update(InteractionActivity interactionActivity);

}
