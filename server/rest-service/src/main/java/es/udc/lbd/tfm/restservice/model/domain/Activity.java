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
@Table(name = "activity")
public class Activity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_generator")
	@SequenceGenerator(name="activity_generator", sequenceName = "activities_seq")
	private Long id;
	
	private String code;
	
	private String title;
	
	private String description;
	
	private Boolean typeH5P;
	
	private String imagePath;
	
	private String idH5Pfile;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Lesson lesson;
	
	private Integer number;
	
	public Activity() {
		
	}
	
	public Activity(String code, String title, String description, Boolean typeH5P, String idH5PFile, String imagePath, Lesson lesson, Integer number) {
		this.code = code;
		this.title = title;
		this.description = description;
		this.typeH5P = typeH5P;
		this.idH5Pfile = idH5PFile;
		this.imagePath = imagePath;
		this.lesson = lesson;
		this.number = number;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getTypeH5P() {
		return typeH5P;
	}

	public void setTypeH5P(Boolean typeH5P) {
		this.typeH5P = typeH5P;
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getIdH5Pfile() {
		return idH5Pfile;
	}

	public void setIdH5Pfile(String idH5Pfile) {
		this.idH5Pfile = idH5Pfile;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

}
