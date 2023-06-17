package es.udc.lbd.tfm.restservice.model.service.dto;

import javax.validation.constraints.NotNull;

import es.udc.lbd.tfm.restservice.model.domain.ClassGroup;

public class ClassGroupDTO {
	
	private Long id;
	
	private String code;
	@NotNull
	private String name;
	@NotNull
	private SubscriptionDTO teacher;
	
	private LessonDTO lesson;

	public ClassGroupDTO() {}

	public ClassGroupDTO(ClassGroup classGroup) {
		this.id = classGroup.getId();
		this.code = classGroup.getCode();
		this.name = classGroup.getName();
		this.teacher = new SubscriptionDTO(classGroup.getSubscription());
		if(classGroup.getLesson()!=null) {
			this.lesson = new LessonDTO(classGroup.getLesson());
		}
		
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SubscriptionDTO getTeacher() {
		return teacher;
	}

	public void setTeacher(SubscriptionDTO teacher) {
		this.teacher = teacher;
	}

	public LessonDTO getLesson() {
		return lesson;
	}

	public void setLesson(LessonDTO lesson) {
		this.lesson = lesson;
	}

//	public SubscriptionDTO getStudent() {
//		return student;
//	}
//
//	public void setStudent(SubscriptionDTO student) {
//		this.student = student;
//	}

}
