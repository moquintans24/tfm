package es.udc.lbd.tfm.restservice.model.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.lbd.tfm.restservice.model.domain.Lesson;
import es.udc.lbd.tfm.restservice.model.repository.util.GenericDaoJpa;

@Repository
public class LessonDaoJpa extends GenericDaoJpa implements LessonDao {

	@Override
	public List<Lesson> findAll() {
		return entityManager.createQuery("from Lesson", Lesson.class).getResultList();
	}

	@Override
	public Lesson findById(Long id) {
		return entityManager.find(Lesson.class, id);
	}
	
	@Override
	public List<Lesson> findByUnitId(Long unitId) {
		return entityManager.createQuery("from Lesson where unit.id = :unitId",Lesson.class).setParameter("unitId", unitId).getResultList();
	}
	
	@Override
	public List<Lesson> findByBookIdUnitId(Long bookId, Long unitId) {
		return entityManager.createQuery("from Lesson where unit.id = :unitId and unit.book.id = :bookId",Lesson.class).setParameter("bookId", bookId)
		.setParameter("unitId", unitId).getResultList();
	}

	@Override
	public List<Lesson> findByBookId(Long bookId){
		return entityManager.createQuery("select l from Lesson l inner join l.unit u where u.book.id = :bookId",Lesson.class)
				.setParameter("bookId", bookId).getResultList();
	}
	
	@Override
	public void create(Lesson lesson) {
		entityManager.persist(lesson);

	}
	
	private void delete(Lesson lesson) {
		entityManager.remove(lesson);
	}

	@Override
	public void deleteById(Long id) {
		Lesson lesson = findById(id);
		delete(lesson);

	}

	@Override
	public void update(Lesson lesson) {
		entityManager.persist(lesson);
		
	}

	

}
