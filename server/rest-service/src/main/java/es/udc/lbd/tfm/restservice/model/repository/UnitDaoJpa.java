package es.udc.lbd.tfm.restservice.model.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.lbd.tfm.restservice.model.domain.Unit;
import es.udc.lbd.tfm.restservice.model.repository.util.GenericDaoJpa;

@Repository
public class UnitDaoJpa extends GenericDaoJpa implements UnitDao {

	@Override
	public List<Unit> findAll() {
		return entityManager.createQuery("from Unit",Unit.class).getResultList();
	}

	@Override
	public Unit findById(Long id) {
		return entityManager.find(Unit.class, id);
	}

	@Override
	public List<Unit> findByBookId(Long bookId) {
		return entityManager.createQuery("from Unit u where u.book.id = :bookId", Unit.class).setParameter("bookId", bookId).getResultList();
	}
	
	@Override
	public void create(Unit unit) {
		entityManager.persist(unit);

	}
	
	private void delete(Unit unit) {
		entityManager.remove(unit);
	}

	@Override
	public void deleteById(Long id) {
		Unit unit = findById(id);
		delete(unit);

	}

	

}
