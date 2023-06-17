package es.udc.lbd.tfm.restservice.model.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "book")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_generator")
	@SequenceGenerator(name = "book_generator", sequenceName = "books_seq")
	private Long id;
	
	private String title;
	
	private String course;
	
	private Integer level;
	
	private BookState state;
	
	private String linkImage;
	
	public Book() {
	}
	
	public Book(String title, String course, Integer level, BookState state, String linkImage) {
		this.title = title;
		this.course = course;
		this.level = level;
		this.state = state;
		this.linkImage = linkImage;
	}
	
	public Book(String title, String course, Integer level, BookState state) {
		this.title = title;
		this.course = course;
		this.level = level;
		this.state = state;
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

	public String getLinkImage() {
		return linkImage;
	}

	public void setImage(String linkImage) {
		this.linkImage = linkImage;
	}
	

}
