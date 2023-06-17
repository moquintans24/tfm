package es.udc.lbd.tfm.restservice.model.repository;

import java.util.List;

import es.udc.lbd.tfm.restservice.model.domain.ClassGroup;

public interface ClassGroupDao {

	List<ClassGroup> findAll();
	
	ClassGroup findById(Long id);
	
	List<ClassGroup> findByUserId(Long userId); //teacher id
	
	List<ClassGroup> findByStudentId(Long userId);
	
	ClassGroup findByCode(String code);
	
	List<ClassGroup> findByBookIdAndUserId(Long bookId, Long userId);
	
	List<ClassGroup> findByBookIdAndTeacherSubscriptionId(Long bookId, Long teacherSubscriptionId);
	
	void create(ClassGroup classGroup);
	
	void update (ClassGroup classGroup);
	
	void deleteById(Long id);
}
