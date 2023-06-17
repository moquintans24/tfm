package es.udc.lbd.tfm.restservice.model.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "code")
public class Code {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "code_generator")
	@SequenceGenerator(name="code_generator", sequenceName = "codes_seq")
	private Long id;
	
	@Column(unique = true,length = 16)
	private String code;
	
	private LocalDate creationDate;

	private LocalDate expirationDate;
	
	@Enumerated(EnumType.STRING)
	private CodeState state;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Book book;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User generatedBy;

    public Code() {
    	
    }
    
    public Code(String code,LocalDate creationDate,LocalDate expirationDate, CodeState state, User generatedBy, Book book) {
    	this.code = code;
    	this.creationDate = creationDate;
    	this.expirationDate = expirationDate;
    	this.state = state;
    	this.generatedBy = generatedBy;
    	this.book = book;
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

	public User getGeneratedBy() {
		return generatedBy;
	}

	public void setGeneratedBy(User generatedBy) {
		this.generatedBy = generatedBy;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	
	
	
}
