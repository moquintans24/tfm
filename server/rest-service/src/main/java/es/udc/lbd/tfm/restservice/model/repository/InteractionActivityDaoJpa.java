package es.udc.lbd.tfm.restservice.model.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.lbd.tfm.restservice.model.domain.InteractionActivity;
import es.udc.lbd.tfm.restservice.model.repository.util.GenericDaoJpa;

@Repository
public class InteractionActivityDaoJpa extends GenericDaoJpa  implements InteractionActivityDao {
	
	private static final String classGroupIdStr = "classGroupId";

	@Override
	public void create(InteractionActivity interactionActivity) {
		entityManager.persist(interactionActivity);

	}

	@Override
	public InteractionActivity findById(Long id) {
		return entityManager.find(InteractionActivity.class, id);
	}
	
	@Override
	public List<InteractionActivity> findByClassGroupIdAndTeacherSubscriptionId(Long teacherSubsId, Long classGroupId){
		return entityManager.createQuery("select ia from InteractionActivity ia inner join ia.teacherSubscription s"
				+ " inner join ia.subscription st"
				+ " where st.classGroup.id = :classGroupId and s.id = :tSubscriptionId", InteractionActivity.class).setParameter("tSubscriptionId", teacherSubsId)
				.setParameter(classGroupIdStr, classGroupId).getResultList();
	}


	@Override
	public List<InteractionActivity> findByActivityIdAndClassGroupIdAndTeacherSubscriptionId(Long id,Long classGroupId, Long teacherSubscriptionId) {
		return entityManager.createQuery("select ia from InteractionActivity ia inner join ia.activity a "
				+ "inner join ia.teacherSubscription s inner join ia.subscription st"
				+ " where a.id = :id and st.classGroup.id = :classGroupId and s.id = :teacherSubscriptionId", InteractionActivity.class)
				.setParameter("id", id).setParameter(classGroupIdStr, classGroupId).setParameter("teacherSubscriptionId", teacherSubscriptionId).getResultList();
	}

	@Override 
	public List<InteractionActivity> findBySubscriptionId(Long id){
		return entityManager.createQuery("select ia from InteractionActivity ia inner join ia.subscription s "
				+ " where s.id = :id", InteractionActivity.class).setParameter("id", id).getResultList();
	}
	
	@Override
	public void deleteById(Long id) {
		InteractionActivity iact = findById(id);
		delete(iact);
		
	}
	
	private void delete(InteractionActivity iact) {
	    entityManager.remove(iact);
	}

	@Override
	public List<InteractionActivity> findByClassGroupIdAndSubscriptionId(Long classGroupId, Long subscriptionId) {
		return entityManager.createQuery(" select ia from InteractionActivity ia inner join ia.subscription s"
				+ " where s.id = :subscriptionId and s.classGroup.id = :classGroupId", InteractionActivity.class).setParameter("subscriptionId", subscriptionId)
				.setParameter(classGroupIdStr, classGroupId).getResultList();
	}

	@Override
	public List<InteractionActivity> findPendingsBySubscriptionId(Long subscriptionId) {
		return entityManager.createQuery(" select ia from InteractionActivity ia inner join ia.subscription s"
				+ " where s.id = :subscriptionId and ia.duration is null and"
				+ " (ia.homeworkDate >= :currentDate or ia.homeworkDate is null)", InteractionActivity.class).setParameter("subscriptionId", subscriptionId)
				.setParameter("currentDate", LocalDateTime.now()).getResultList();
	}

	@Override
	public void update(InteractionActivity interactionActivity) {
		entityManager.merge(interactionActivity);
	}

	@Override
	public InteractionActivity findByActivityIdAndUserId(Long activityId, Long userId) {
		return entityManager.createQuery("select ia from InteractionActivity ia inner join ia.subscription s "
				+ "where s.user.id = :userId and ia.activity.id = :activityId", InteractionActivity.class).setParameter("activityId", activityId)
				.setParameter("userId", userId).getSingleResult();
	}

	@Override
	public List<InteractionActivity> findByActivityIdAndClassGroupId(Long activityId, Long classGroupId) {
		return entityManager.createQuery(" select ia from InteractionActivity ia inner join ia.subscription s"
				+ " where ia.activity.id = :activityId and s.classGroup.id = :classGroupId"
				+ " and ia.homeworkDate is not null",InteractionActivity.class).setParameter("activityId", activityId)
				.setParameter(classGroupIdStr, classGroupId).getResultList();
	}
	
	@Override
	public List<?> averageScoreStudentsByClassGroupId(Long classGroupId) {
		String query = "select ts.id,ts.user_id,avg(coalesce(score,0)) as avg_score, "
				+ "avg(date_part('hour', coalesce(duration,'00:00:00'))*60+ date_part('minute', coalesce(duration,'00:00:00'))), "
				+ "coalesce(count(case when duration is null then activity_id end),0) activities_undone, "
				+ "coalesce(count(case when duration is not null then activity_id end),0) activities_done, "
				+ "avg(case when homework_date is not null then coalesce(score,0) end) avg_homework_score, "
				+ "avg(case when homework_date is null then coalesce(score,0) end) avg_not_assigned_act_score "
				+ "from the_subscription ts "
				+ "inner join interaction_activity ia on ts.id = ia.subscription_id "
				+ "where ts.class_group_id =:classGroupId "
				+ "group by ts.id, ts.user_id ";
		return entityManager.createNativeQuery(query).setParameter(classGroupIdStr, classGroupId).getResultList();
		
	}
	@Override
	public List<?> averageScoreStudentsByClassGroupIdAndUnitId(Long classGroupId, Long unitId) {
		String query = "select ts.id as subscriptionId,ts.user_id,u.id as unit_id, "
				+ "avg(coalesce(score,0)) avg_score,avg(date_part('hour', coalesce(duration,'00:00:00'))*60+ date_part('minute', coalesce(duration,'00:00:00'))), "
				+ "coalesce(count(case when duration is null then activity_id end),0) activities_undone, "
				+ "coalesce(count(case when duration is not null then activity_id end),0) activities_done, "
				+ "avg(case when homework_date is not null then coalesce(score,0) end) avg_homework_score, "
				+ "avg(case when homework_date is null then coalesce(score,0) end) avg_not_assigned_act_score "
				+ "from the_subscription ts "
				+ "inner join interaction_activity ia on ts.id = ia.subscription_id "
				+ "inner join activity a on a.id = ia.activity_id "
				+ "inner join lesson l on l.id = a.lesson_id "
				+ "inner join unit u on u.id = l.unit_id "
				+ "where ts.class_group_id =:classGroupId and u.id=:unitId "
				+ "group by ts.id, ts.user_id, u.id ";
		return entityManager.createNativeQuery(query).setParameter(classGroupIdStr, classGroupId).setParameter("unitId", unitId).getResultList();
		
	}
	
}
