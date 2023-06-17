package es.udc.lbd.tfm.restservice.model.repository;

import java.util.List;

import es.udc.lbd.tfm.restservice.model.domain.Subscription;

public interface SubscriptionDao {
	
	List<Subscription> findAll();
	
	Subscription findById(Long id);
	
	List<Subscription> findByUserId(Long id);
	
	Subscription findByCodeAndUserId(String code,Long userId);
	
	Subscription findByBookIdAndUserId(Long bookId, Long userId);
	
	List<Subscription> findByClassGroupId(Long id);
	
	List<Subscription> findByCompletedStateAndClassGroupId(Long classgroupId);
	
	Long findByClassGroupCodeAndUserId(String classGroupCode, Long userId);
	
	void cancelSubscriptionToClassGroup(Long subscriptionId);
	
	void acceptSubscriptionToClassGroup(Long subscriptionId);
	
	void create(Subscription subscription);
	
	void update(Subscription subscription);
	
	void deleteSubscriptionFromClassGroup(Long subscriptionId);
	
	void deleteById(Long id);
	
	Subscription findByClassGroupIdAndUserId(Long classGroupId, Long userId);

}
