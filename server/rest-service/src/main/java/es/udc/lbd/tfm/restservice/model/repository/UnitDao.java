package es.udc.lbd.tfm.restservice.model.repository;

import java.util.List;

import es.udc.lbd.tfm.restservice.model.domain.Unit;

public interface UnitDao {
	
	List<Unit> findAll();
	
	Unit findById(Long id);

	List<Unit> findByBookId(Long id);
	
	void create(Unit unit);
	
	void deleteById(Long id);

}
