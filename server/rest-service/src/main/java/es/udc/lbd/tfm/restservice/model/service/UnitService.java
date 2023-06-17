package es.udc.lbd.tfm.restservice.model.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.tfm.restservice.model.domain.Book;
import es.udc.lbd.tfm.restservice.model.domain.Lesson;
import es.udc.lbd.tfm.restservice.model.domain.Unit;
import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.model.repository.BookDao;
import es.udc.lbd.tfm.restservice.model.repository.LessonDao;
import es.udc.lbd.tfm.restservice.model.repository.UnitDao;
import es.udc.lbd.tfm.restservice.model.service.dto.UnitDTO;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class UnitService {
	
	@Autowired
	private UnitDao unitDAO;
	
	@Autowired
	private BookDao bookDAO;
	
	@Autowired
	private LessonDao lessonDAO;
	
	public UnitDTO findById(Long id) throws NotFoundException {
		Unit unit = unitDAO.findById(id);
		if(unit == null) {
			throw new NotFoundException("Unit with id "+ id + " not found!");
		}
		return new UnitDTO(unit);
	}
	
	public List<UnitDTO> findByBookId(Long bookId) throws NotFoundException{
		Book book = bookDAO.findById(bookId);
		
		if(book == null) {
			throw new NotFoundException("Book with id "+ bookId + " not found!");
		}
		return unitDAO.findByBookId(bookId).stream()
				.sorted(Comparator.comparing(Unit::getNumber, Comparator.nullsLast(Comparator.naturalOrder())))
				.map(unit -> new UnitDTO(unit)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = false)
	public UnitDTO create(Long bookId,String name,Integer number) throws NotFoundException {
		Book bdBook = bookDAO.findById(bookId);
		
		if(bdBook == null) {
			throw new NotFoundException("Book with id "+ bookId + " not found!");
		}
		Unit newUnit = new Unit();
		newUnit.setBook(bdBook);
		newUnit.setName(name);
		newUnit.setNumber(number);
		unitDAO.create(newUnit);
		return new UnitDTO(newUnit);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) throws NotFoundException {
		Unit bdUnit = unitDAO.findById(id);
		if(bdUnit == null) {
			throw new NotFoundException("Unit with id "+id+" not found!");
		}
		List<Lesson> lessons = lessonDAO.findByUnitId(id);
		for(int i = 0;i<lessons.size();i++) {
			lessonDAO.deleteById(lessons.get(i).getId());
		}
		unitDAO.deleteById(id);
	}

}
