package es.udc.lbd.tfm.restservice.model.repository;

import java.util.List;

import es.udc.lbd.tfm.restservice.model.domain.Activity;

public interface ActivityDao {
	
	Activity findById(Long id);
	
	List<Activity> findByLessonId(Long lessonId);
	
	List<Activity> findByBookId(Long bookId);
	
	List<?> totalStudentsRealizedActivity(Long teacherSubscriptionId, Long classGroupId);
	
	Boolean findByIdWithInteractionActivity(Long activityId);
	
	List<Activity> findAvailableByClassGroupId(Long classGroupId, Integer lessonNumber);
	
	List<Activity> findByIdH5PFile(String idH5PFile);
	
	List<Activity> findByImagePath(String imagePath);
	
	void create(Activity activity);
	
	void deleteById(Long id);
	
	void update(Activity activity);

}
