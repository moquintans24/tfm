package es.udc.lbd.tfm.restservice.model.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.lbd.tfm.restservice.model.domain.Book;
import es.udc.lbd.tfm.restservice.model.domain.BookState;
import es.udc.lbd.tfm.restservice.model.repository.util.GenericDaoJpa;

@Repository
public class BookDaoJpa extends GenericDaoJpa implements BookDao{

	@Override
	public List<Book> findAll() {
		return entityManager.createQuery("from Book", Book.class).getResultList();
	}
	
	@Override
	public List<Book> findAllWithReadyState() {
		return entityManager.createQuery("from Book where state = :state", Book.class).setParameter("state", BookState.READY).getResultList();
	}

	@Override
	public Book findById(Long id) {
		return entityManager.find(Book.class, id);
	}

	@Override
	public void create(Book book) {
		entityManager.persist(book);
		
	}
	
	@Override
	public void update(Book book) {
	  entityManager.merge(book);
	}
	
	private void delete(Book book) {
		entityManager.remove(book);
	}
	
	@Override
	public void deleteById(Long id) {
		Book book = findById(id);
		delete(book);
		
	}
	
	@Override
	public List<?> statisticsAllBooks() {
		return entityManager.createQuery("select b,count(distinct s.id) as total_users, count(distinct center) as total_centers,count(distinct cg.id) as total_class_groups,"
				+ " b.state"
				+ " from Subscription s right outer join s.user u right join s.code c "
				+ " left outer join s.classGroup cg"
				+ " right outer join c.book b"
				+ " where b.state = :state or b.state = :archivedState"
				+ " group by b.title, b.id,b.state").setParameter("state", BookState.READY).setParameter("archivedState", BookState.ARCHIVED).getResultList();	
	}

	@Override
	public Book findByTitle(String title) {
		return entityManager.createQuery("from Book where title = :title", Book.class).setParameter("title", title).getSingleResult();
	}

	@Override
	public List<Book> findByImage(String image) {
		return entityManager.createQuery("from Book a where a.linkImage =:linkImage",Book.class).setParameter("linkImage", image).getResultList();
	}

	

}
