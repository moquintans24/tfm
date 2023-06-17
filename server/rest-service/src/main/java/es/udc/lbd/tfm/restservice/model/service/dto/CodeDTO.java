package es.udc.lbd.tfm.restservice.model.service.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import es.udc.lbd.tfm.restservice.model.domain.Code;
import es.udc.lbd.tfm.restservice.model.domain.CodeState;

public class CodeDTO {

	private Long id;
	@NotNull
	private String code;
	@NotNull
	private LocalDate creationDate;
	@NotNull
	private LocalDate expirationDate;
	@NotNull
	private CodeState state;
	@NotNull
	private UserDTOPrivate generatedBy;
	private BookDTO book;
	
	public CodeDTO() {
		
	}
	
	public CodeDTO(Code code) {
		this.id = code.getId();
		this.code = code.getCode();
		this.creationDate = code.getCreationDate();
		this.expirationDate = code.getExpirationDate();
		this.state = code.getState();
		this.generatedBy = new UserDTOPrivate(code.getGeneratedBy());
		this.book = new BookDTO(code.getBook());
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}
	public CodeState getState() {
		return state;
	}
	public void setState(CodeState state) {
		this.state = state;
	}
	public UserDTOPrivate getGeneratedBy() {
		return generatedBy;
	}
	public void setGeneratedBy(UserDTOPrivate generatedBy) {
		this.generatedBy = generatedBy;
	}

	public BookDTO getBook() {
		return book;
	}

	public void setBook(BookDTO book2) {
		this.book = book2;
	}
	
	
}

