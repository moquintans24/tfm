package es.udc.lbd.tfm.restservice.model.repository;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import es.udc.lbd.tfm.restservice.model.domain.Code;
import es.udc.lbd.tfm.restservice.model.domain.CodeState;
import es.udc.lbd.tfm.restservice.model.repository.util.GenericDaoJpa;

@Repository
public class CodeDaoJpa extends GenericDaoJpa implements CodeDao {

	@Override
	public List<Code> findAll() {
		return entityManager.createQuery("from Code", Code.class).getResultList();
	}

	@Override
	public Code findById(Long id) {
		return entityManager.find(Code.class, id);
	}
	
	@Override
	public Code findByCode(String code) {
		TypedQuery<Code> query = entityManager.createQuery("from Code c where c.code = :code",Code.class).setParameter("code", code);
		return DataAccessUtils.singleResult(query.getResultList());
	}
	
	@Override
	public List<Code> findByBookId(Long bookId){
		return entityManager.createQuery("from Code c where book.id = :bookId order by creationDate desc",Code.class)
				.setParameter("bookId", bookId).getResultList();
		
	}

	@Override
	public void create(Code code) {
		entityManager.persist(code);
		
	}

	@Override
	public List<Code> findByCreationDate(Long bookId, LocalDate creationDate1, LocalDate creationDate2) {
		return entityManager.createQuery("from Code c where book.id = :bookId and c.creationDate between :creationDate1 and :creationDate2 and state = :codeState", Code.class)
					.setParameter("bookId", bookId).setParameter("creationDate1", creationDate1).setParameter("creationDate2", creationDate2).setParameter("codeState", CodeState.NEW)
					.getResultList();
		
	}

	@Override
	public void update(Code code) {
		entityManager.merge(code);		
	}

}
