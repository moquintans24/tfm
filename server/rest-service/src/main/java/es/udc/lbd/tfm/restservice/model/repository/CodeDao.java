package es.udc.lbd.tfm.restservice.model.repository;

import java.time.LocalDate;
import java.util.List;

import es.udc.lbd.tfm.restservice.model.domain.Code;

public interface CodeDao {

	List<Code> findAll();
	
	Code findById(Long id);
	
	Code findByCode(String code);
	
	List<Code> findByBookId(Long bookId);
	
	List<Code> findByCreationDate(Long bookId, LocalDate creationDate1, LocalDate creationDate2);
	
	void create(Code code);
	
	void update(Code code);
}
