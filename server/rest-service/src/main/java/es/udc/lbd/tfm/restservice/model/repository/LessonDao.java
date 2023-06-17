package es.udc.lbd.tfm.restservice.model.repository;

import java.util.List;

import es.udc.lbd.tfm.restservice.model.domain.Lesson;

public interface LessonDao {
	
	List<Lesson> findAll();
	
	List<Lesson> findByUnitId(Long unitId);
	
	Lesson findById(Long id);
	
	List<Lesson> findByBookIdUnitId(Long bookId, Long unitId);
	
	List<Lesson> findByBookId(Long bookId);
	
	void create(Lesson lesson);
	
	void deleteById(Long id);
	
	void update(Lesson lesson);

}
