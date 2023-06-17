package es.udc.lbd.tfm.restservice.model.repository;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.tfm.restservice.model.domain.User;
import es.udc.lbd.tfm.restservice.model.domain.UserAuthority;
import es.udc.lbd.tfm.restservice.model.repository.util.GenericDaoJpa;

@Repository
public class UserDaoJpa extends GenericDaoJpa implements UserDao {

  @Override
  public List<User> findAll() {
    return entityManager.createQuery("from User u where u.authority != :authority and u.unsubscribe = FALSE", User.class).setParameter("authority", UserAuthority.ADMIN)
    		.getResultList();
  }
  
  @Override
  public List<User> findAllEditors(){
	  return entityManager.createQuery("from User u where u.authority = :authority and u.unsubscribe = FALSE", User.class).setParameter("authority", UserAuthority.EDITOR).getResultList();
  }

  @Override
  public User findById(Long id) {
    return entityManager.find(User.class, id);
  }
  
  @Override
  public User findBySubscriptionId(Long subscriptionId) {
	  TypedQuery<User> query =  entityManager.createQuery("select u from Subscription s inner join s.user u"
	  		+ " where s.id = :subscriptionId", User.class).setParameter("subscriptionId", subscriptionId);
	  return DataAccessUtils.singleResult(query.getResultList());
  }

  @Override
  public User findByLogin(String login) {
    TypedQuery<User> query = entityManager.createQuery("from User u where u.login = :login", User.class)
        .setParameter("login", login);
    return DataAccessUtils.singleResult(query.getResultList());
  }

  @Override
  public void create(User user) {
    entityManager.persist(user);
  }
  
  @Override
  public void update(User user) {
	  entityManager.merge(user);
  }
   @Override
	public User findByEmail(String email) {
		TypedQuery<User> query = entityManager.createQuery("from User u where u.email = :email", User.class)
		        .setParameter("email",email);
			    return DataAccessUtils.singleResult(query.getResultList());
		}

	
	@Override
	public void deleteById(Long id) {
		User user = findById(id);
		user.setUnsubscribe(true);
	}

	
}
