package es.udc.lbd.tfm.restservice.model.service.dto;

import javax.validation.constraints.NotNull;

import es.udc.lbd.tfm.restservice.model.domain.Unit;

public class UnitDTO {
	
	private Long id;
	@NotNull
	private String name;
	@NotNull
	private Integer number;
	@NotNull
	private BookDTO book;
	
	public UnitDTO(Unit unit) {
		this.id = unit.getId();
		this.name = unit.getName();
		this.number = unit.getNumber();
		this.book = new BookDTO(unit.getBook());
	}
	
	public UnitDTO(Long id, @NotNull String name, @NotNull Integer number) {
		this.id = id;
		this.name = name;
		this.number = number;
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

	public BookDTO getBook() {
		return book;
	}

	public void setBook(BookDTO book) {
		this.book = book;
	}
	
	

}
