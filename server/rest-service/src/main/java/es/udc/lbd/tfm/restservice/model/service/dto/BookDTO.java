package es.udc.lbd.tfm.restservice.model.service.dto;

import javax.validation.constraints.NotNull;

import es.udc.lbd.tfm.restservice.model.domain.Book;
import es.udc.lbd.tfm.restservice.model.domain.BookState;

public class BookDTO {
	
	private Long id;
	@NotNull
	private String title;
	@NotNull
	private String course;
	@NotNull
	private Integer level;
	@NotNull
	private BookState state;
	
	private String linkImage;
	
	public BookDTO() {
		
	}
	
	public BookDTO(Book book) {
		this.id = book.getId();
		this.title = book.getTitle();
		this.course = book.getCourse();
		this.level = book.getLevel();
		this.state = book.getState();
		this.linkImage = book.getLinkImage();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public BookState getState() {
		return state;
	}
	public void setState(BookState state) {
		this.state = state;
	}

	public String getImage() {
		return linkImage;
	}

	public void setImage(String linkImage) {
		this.linkImage = linkImage;
	}
	
	
	
}
