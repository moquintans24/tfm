package es.udc.lbd.tfm.restservice.model.repository;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.tfm.restservice.model.domain.BookState;
import es.udc.lbd.tfm.restservice.model.domain.Subscription;
import es.udc.lbd.tfm.restservice.model.domain.SubscriptionState;
import es.udc.lbd.tfm.restservice.model.repository.util.GenericDaoJpa;

@Repository
public class SubscriptionDaoJpa extends GenericDaoJpa implements SubscriptionDao {
	
	private static final String bookState = "state";
	private static final String userIdStr = "userId";

	@Override
	public List<Subscription> findAll() {
		return entityManager.createQuery("from theSubscription", Subscription.class).getResultList();
	}

	@Override
	public Subscription findById(Long id) {
		return entityManager.find(Subscription.class, id);
	}
	
	@Override
	public List<Subscription> findByUserId(Long id) {
		return entityManager.createQuery(" from Subscription where user.id = :id and code.book.state != :state",Subscription.class).setParameter("id", id)
				.setParameter(bookState, BookState.ARCHIVED).getResultList();
	}
	
	@Override
	public Subscription findByCodeAndUserId(String code, Long userId) {
		TypedQuery<Subscription> query = entityManager.createQuery(" from Subscription s where s.code.code = :code and s.user.id = :userId and code.book.state != :state",
				Subscription.class)
				.setParameter("code", code).setParameter(userIdStr, userId).setParameter(bookState, BookState.ARCHIVED);
		return DataAccessUtils.singleResult(query.getResultList());
		
	}
	
	@Override
	public Subscription findByBookIdAndUserId(Long bookId, Long userId) {
		TypedQuery<Subscription> query = entityManager.createQuery(" from Subscription s where s.code.book.id = :bookId and s.user.id = :userId and code.book.state != :state",
				Subscription.class)
				.setParameter("bookId", bookId).setParameter(userIdStr, userId).setParameter(bookState, BookState.ARCHIVED);
		return DataAccessUtils.singleResult(query.getResultList());
		
	}

	@Override
	public void create(Subscription subscription) {
		entityManager.persist(subscription);

	}
	
	@Override
	public void update(Subscription subscription) {
		entityManager.merge(subscription);
	}

	@Override
	public void deleteById(Long id) {
		Subscription subs = findById(id);
		subs.setState(SubscriptionState.CANCELLED);
	}

	@Override
	public List<Subscription> findByClassGroupId(Long id) {
		return entityManager.createQuery(" from Subscription where classGroup.id = :id and code.book.state != :state",Subscription.class).setParameter("id", id)
				.setParameter(bookState, BookState.ARCHIVED).getResultList();
	}

	@Override
	public void cancelSubscriptionToClassGroup(Long subscriptionId) {
		Subscription subs = findById(subscriptionId);
		subs.setState(SubscriptionState.CANCELLED);
		subs.setClassGroup(null);
		
	}

	@Override
	public void acceptSubscriptionToClassGroup(Long subscriptionId) {
		Subscription subs = findById(subscriptionId);
		subs.setState(SubscriptionState.COMPLETED);
		
	}

	@Override
	public void deleteSubscriptionFromClassGroup(Long subscriptionId) {
		Subscription subs = findById(subscriptionId);
		subs.setClassGroup(null);
	
	}

	@Override
	public List<Subscription> findByCompletedStateAndClassGroupId(Long classgroupId) {
		return entityManager.createQuery(" from Subscription where classGroup.id = :classgroupId and state = :state and code.book.state != :bookState",Subscription.class)
				.setParameter("classgroupId", classgroupId).setParameter("state", SubscriptionState.COMPLETED).setParameter("bookState", BookState.ARCHIVED).getResultList();
	}

	@Override
	public Subscription findByClassGroupIdAndUserId(Long classGroupId, Long userId) {
		return entityManager.createQuery(" from Subscription where classGroup.id = :classGroupId and user.id = :userId and code.book.state != :state", Subscription.class)
				.setParameter("classGroupId", classGroupId).setParameter(userIdStr, userId).setParameter(bookState, BookState.ARCHIVED).getSingleResult();
	}

	@Override
	public Long findByClassGroupCodeAndUserId(String classGroupCode, Long userId) {
		String query = "with class_group_code_book as ("
				+ "	select c.book_id"
				+ "	from class_group cg"
				+ "	inner join the_subscription ts on cg.subscription_id = ts.id"
				+ "	inner join code c on c.id = ts.code_id"
				+ " inner join book b on b.id = c.book_id"
				+ "	where cg.code = :classGroupCode and b.state != 3)" //State = 3 => Archived
				+ " select ts2.id"
				+ " from the_subscription ts2"
				+ " inner join code cd on ts2.code_id = cd.id"
				+ " where cd.book_id = (select * from class_group_code_book)"
				+ " and user_id = :userId ";
		if(entityManager.createNativeQuery(query).setParameter(userIdStr, userId).setParameter("classGroupCode", classGroupCode).getResultList().isEmpty()) {
			return Long.parseLong(String.valueOf(-1));
		}else {
			return Long.parseLong(entityManager.createNativeQuery(query).setParameter(userIdStr, userId).setParameter("classGroupCode", classGroupCode).getSingleResult().toString());
		}
	}

}
