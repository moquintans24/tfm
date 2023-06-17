package es.udc.lbd.tfm.restservice.model.repository;

import java.util.List;

import es.udc.lbd.tfm.restservice.model.domain.Book;

public interface BookDao {
	
	List<Book> findAll();
	
	List<Book> findAllWithReadyState();
	
	List<?> statisticsAllBooks();
	
	Book findById(Long id);
	
	Book findByTitle(String title);
	
	List<Book> findByImage(String image);
	
	void create(Book book);
	
	void deleteById(Long id);
	
	void update(Book book);

}
