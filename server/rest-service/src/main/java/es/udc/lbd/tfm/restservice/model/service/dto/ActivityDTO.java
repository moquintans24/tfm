package es.udc.lbd.tfm.restservice.model.service.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.validation.constraints.NotNull;

import es.udc.lbd.tfm.restservice.model.domain.Activity;

public class ActivityDTO {
	
	private Long id;
	@NotNull
	private String code;
	@NotNull
	private String title;
	
	private String description;
	@NotNull
	private Boolean typeH5P;
	
	private String imagePath;
	
	private String idH5Pfile;
	
	private LessonDTO lesson;
	
	private List<SubscriptionDTO> students;
	
	private Integer number;
	
	public ActivityDTO() {}
	
	public ActivityDTO(Activity activity) {
		this.id = activity.getId();
		this.code = activity.getCode();
		this.title = activity.getTitle();
		this.description = activity.getDescription();
		this.typeH5P = activity.getTypeH5P();
		this.idH5Pfile = activity.getIdH5Pfile();
		this.imagePath = activity.getImagePath();
		this.lesson = new LessonDTO(activity.getLesson());
		this.number = activity.getNumber();
		//this.students = activity.getSubscriptions();
	}

	public ActivityDTO(Long id, @NotNull String code, @NotNull String title, String description,
			@NotNull LocalTime duration, @NotNull Boolean typeH5P,String idH5PFile, LocalDateTime homeworkDate, Double score,
			LessonDTO lesson, List<SubscriptionDTO> students) {
		this.id = id;
		this.code = code;
		this.title = title;
		this.description = description;
		this.typeH5P = typeH5P;
		this.idH5Pfile = idH5PFile;
		this.lesson = lesson;
		this.students = students;
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

	public LessonDTO getLesson() {
		return lesson;
	}

	public void setLesson(LessonDTO lesson) {
		this.lesson = lesson;
	}

	public List<SubscriptionDTO> getStudents() {
		return students;
	}

	public void setStudents(List<SubscriptionDTO> students) {
		this.students = students;
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

	public void setNummber(Integer number) {
		this.number = number;
	}

}
