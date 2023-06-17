package es.udc.lbd.tfm.restservice.model.repository;

import java.util.List;

import es.udc.lbd.tfm.restservice.model.domain.User;

public interface UserDao {
  List<User> findAll();
  
  List<User> findAllEditors();

  User findById(Long id);
  
  User findBySubscriptionId(Long subscriptionId);

  User findByLogin(String login);
  
  User findByEmail(String email);

  void create(User user);
  
  void update(User user);
  
  void deleteById(Long id);
}
