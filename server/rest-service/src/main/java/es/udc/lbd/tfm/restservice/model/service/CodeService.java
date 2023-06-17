package es.udc.lbd.tfm.restservice.model.service;



import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.tfm.restservice.model.domain.Book;
import es.udc.lbd.tfm.restservice.model.domain.Code;
import es.udc.lbd.tfm.restservice.model.domain.CodeState;
import es.udc.lbd.tfm.restservice.model.domain.User;
import es.udc.lbd.tfm.restservice.model.exception.CodeExistsException;
import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.model.repository.BookDao;
import es.udc.lbd.tfm.restservice.model.repository.CodeDao;
import es.udc.lbd.tfm.restservice.model.repository.UserDao;
import es.udc.lbd.tfm.restservice.model.service.dto.CodeDTO;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CodeService {
	
	@Autowired
	private CodeDao codeDAO;
	
	@Autowired
	private BookDao bookDAO;
	
	@Autowired
	private UserDao userDAO;
	
	@PreAuthorize("hasAuthority('ADMIN')")
	public List<CodeDTO> findAll(){
		return  codeDAO.findAll().stream().map(code -> new CodeDTO(code)).collect(Collectors.toList());
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@Transactional(readOnly = false)
	public CodeDTO create(Code code){
		Code bdCode = new Code(code.getCode(),code.getCreationDate(), code.getExpirationDate(), code.getState(), code.getGeneratedBy(), code.getBook());
		
		codeDAO.create(bdCode);
		return new CodeDTO(bdCode);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@Transactional(readOnly = false)
	public List<CodeDTO> create(Integer number, LocalDate expirationDate, Long userId, Long bookId){
		User user = userDAO.findById(userId);
		Book book = bookDAO.findById(bookId);
		List<CodeDTO> codeList = new ArrayList<CodeDTO>();
		for(int i=0;i<number;i++) {
			String code = generateRandomCode();
			
			if(codeDAO.findByCode(code) == null) {
				Code bdCode = new Code(code, LocalDate.now(),expirationDate,CodeState.NEW,user,book);
				codeDAO.create(bdCode);
				codeList.add(new CodeDTO(bdCode));
			}	
		}
		return codeList;
		
	}
	
	private String generateRandomCode () {
		String characterSet = "0123456789abcdefghijklmnopqrstuvwxyz";
		StringBuffer sbuff = new StringBuffer(16);
		SecureRandom random = new SecureRandom();
		for(int j=0;j<16;j++) {
			int offset = random.nextInt(characterSet.length());
			sbuff.append(characterSet.substring(offset,offset+1));
		}
		return sbuff.toString();
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	public CodeDTO findById(Long id) throws NotFoundException{
		Code code = codeDAO.findById(id);
		if(code == null) {
			throw new NotFoundException("Code with id " + id + "not found!");
		}
		return new CodeDTO(codeDAO.findById(id));
	}
	
	public CodeDTO findByCode(String code) throws CodeExistsException {
		Code bdCode = codeDAO.findByCode(code);
		if(code != null) {
			throw new CodeExistsException("Number code " + code + " already exists!");
		}
		return new CodeDTO(bdCode);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	public List<CodeDTO> findByBookId(Long bookId) throws NotFoundException{
		Book book = bookDAO.findById(bookId);
		if(book == null) {
			throw new NotFoundException("Book with id " + bookId + " not found!");
		}
		return codeDAO.findByBookId(bookId).stream().map(code -> new CodeDTO(code)).collect(Collectors.toList());
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	public List<CodeDTO> findByCreationDate(Long bookId, LocalDate creationDate1, LocalDate creationDate2) throws NotFoundException{
		Book book = bookDAO.findById(bookId);
		if(book == null) {
			throw new NotFoundException("Book with id " + bookId + "not found!");
		}
		return codeDAO.findByCreationDate(bookId, creationDate1, creationDate2).stream().map(code -> new CodeDTO(code)).collect(Collectors.toList());
	}
}
