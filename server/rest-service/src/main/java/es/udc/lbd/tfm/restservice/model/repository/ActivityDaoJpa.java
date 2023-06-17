package es.udc.lbd.tfm.restservice.model.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.lbd.tfm.restservice.model.domain.Activity;
import es.udc.lbd.tfm.restservice.model.repository.util.GenericDaoJpa;

@Repository
public class ActivityDaoJpa extends GenericDaoJpa implements ActivityDao {

	@Override
	public Activity findById(Long id) {
		return entityManager.find(Activity.class, id);
	}
	
	@Override
	public List<Activity> findByLessonId(Long lessonId) {
		return entityManager.createQuery("from Activity a where a.lesson.id = :lessonId", Activity.class).setParameter("lessonId", lessonId).getResultList();
	}

	@Override
	public void create(Activity activity) {
		entityManager.persist(activity);

	}

	private void delete(Activity activity) {
		entityManager.remove(activity);
	}
	
	@Override
	public void deleteById(Long id) {
		Activity activity = findById(id);
		delete(activity);
	}

	@Override
	public List<Activity> findByBookId(Long bookId) {
		return entityManager.createQuery("select a from Activity a inner join a.lesson l inner join l.unit u inner join u.book b"
				+ " where b.id = :bookId", Activity.class).setParameter("bookId", bookId).getResultList();
	}

	@Override
	public List<?> totalStudentsRealizedActivity(Long teacherSubscriptionId, Long classGroupId) {
		return entityManager.createQuery("select a.id as activityId, count(s.id) as total_students from InteractionActivity ia inner join ia.activity a"
				+ " inner join ia.teacherSubscription ts inner join ia.subscription s"
				+ " where s.classGroup.id = :classGroupId and ts.id = :teacherSubscriptionId and ia.duration is not null"
				+ " group by a.id").setParameter("classGroupId", classGroupId).setParameter("teacherSubscriptionId", teacherSubscriptionId)
				.getResultList();
	}

	@Override
	public Boolean findByIdWithInteractionActivity(Long activityId) {
		List<Activity> activities =  entityManager.createQuery("select a from InteractionActivity ia inner join ia.activity a"
				+ " where a.id = :activityId", Activity.class).setParameter("activityId", activityId).getResultList();
		if(!activities.isEmpty()) {
			return true;
		}else {
			return false;
					
		}
	}

	@Override
	public void update(Activity activity) {
		entityManager.merge(activity);
	}

	@Override
	public List<Activity> findAvailableByClassGroupId(Long classGroupId, Integer lessonNumber) {
			    
		return entityManager.createQuery(" select a from ClassGroup cg inner join cg.subscription s inner join s.code c inner join c.book b"
				+ " inner join Unit u on u.book.id = b.id inner join Lesson l on l.unit.id = u.id "
				+ " inner join Activity a on a.lesson.id = l.id where cg.id = :classGroupId and l.number <= :lessonNumber",Activity.class)
				.setParameter("classGroupId", classGroupId).
				setParameter("lessonNumber", lessonNumber).getResultList();
	}

	@Override
	public List<Activity> findByIdH5PFile(String idH5PFile) {
		return entityManager.createQuery("from Activity a where a.idH5Pfile = :idH5PFile",Activity.class).setParameter("idH5PFile", idH5PFile).getResultList();
	}

	@Override
	public List<Activity> findByImagePath(String imagePath) {
		return entityManager.createQuery("from Activity a where a.imagePath =:imagePath",Activity.class).setParameter("imagePath", imagePath).getResultList();
	}

	

}
