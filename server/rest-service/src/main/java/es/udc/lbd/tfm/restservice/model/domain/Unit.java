package es.udc.lbd.tfm.restservice.model.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "unit")
public class Unit {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unit_generator")
	@SequenceGenerator(name = "unit_generator", sequenceName = "units_seq")
	private Long id;
	
	private String name;
	
	private Integer number;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Book book;
	
	public Unit() {

	}
	
	public Unit(String name, Integer number, Book book) {
		this.name = name;
		this.number = number;
		this.book = book;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	
}
