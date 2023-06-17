package es.udc.lbd.tfm.restservice.model.repository;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.tfm.restservice.model.domain.BookState;
import es.udc.lbd.tfm.restservice.model.domain.ClassGroup;
import es.udc.lbd.tfm.restservice.model.repository.util.GenericDaoJpa;

@Repository
public class ClassGroupDaoJpa extends GenericDaoJpa implements ClassGroupDao {
	private static final String bookState = "bookState";
	private static final String userId_ = "userId";
	
	@Override
	public List<ClassGroup> findAll() {
		return entityManager.createQuery("from ClassGroup", ClassGroup.class).getResultList();
	}

	@Override
	public ClassGroup findById(Long id) {
		return entityManager.find(ClassGroup.class, id);
	}
	
	@Override 
	public ClassGroup findByCode(String code) {
		TypedQuery<ClassGroup> query = entityManager.createQuery("from ClassGroup where code = :code and subscription.code.book.state != :bookState",
				ClassGroup.class).setParameter("code", code).setParameter(bookState, BookState.ARCHIVED);
		return DataAccessUtils.singleResult(query.getResultList());
	}
	
	@Override
	public List<ClassGroup> findByUserId(Long userId) {
		return entityManager.createQuery("select cg from ClassGroup cg inner join cg.subscription s inner join s.user u"
				+ " where u.id = :userId and cg.subscription.code.book.state != :bookState", ClassGroup.class).setParameter(userId_, userId)
				.setParameter(bookState, BookState.ARCHIVED).getResultList();
	}

	@Override
	public List<ClassGroup> findByStudentId(Long userId) {
		return entityManager.createQuery("select cg from Subscription s inner join s.classGroup cg "
				+ "where s.user.id = :userId and cg.subscription.code.book.state != :bookState", ClassGroup.class).setParameter(userId_, userId)
				.setParameter(bookState, BookState.ARCHIVED).getResultList();
	}

	@Override
	public List<ClassGroup> findByBookIdAndUserId(Long bookId, Long userId) {
		return entityManager.createQuery("select cg from Subscription s inner join s.code c inner join s.classGroup cg "
				+ "where s.user.id = :userId and c.book.id = :bookId and s.code.book.state != :bookState", ClassGroup.class).setParameter(userId_, userId)
				.setParameter("bookId", bookId).setParameter(bookState, BookState.ARCHIVED).getResultList();
	}
	
	@Override
	public List<ClassGroup> findByBookIdAndTeacherSubscriptionId(Long bookId, Long teacherSubscriptionId) {
		return entityManager.createQuery(" select cg from ClassGroup cg inner join cg.subscription s inner join s.code c inner join c.book b "
				+ "where b.id = :bookId and s.id = :teacherSubscriptionId and cg.subscription.code.book.state != :bookState", ClassGroup.class).setParameter("bookId", bookId)
				.setParameter("teacherSubscriptionId", teacherSubscriptionId).setParameter(bookState, BookState.ARCHIVED)
				.getResultList();
	}
	
	@Override
	public void create(ClassGroup classGroup) {
		entityManager.persist(classGroup);
	}
	
	@Override
	public void update(ClassGroup classGroup) {
		entityManager.merge(classGroup);
	}
	
	private void delete(ClassGroup classGroup) {
		entityManager.remove(classGroup);
	}
	
	@Override
	public void deleteById(Long id) {
		ClassGroup classGroup = findById(id);
		delete(classGroup);

	}


}
