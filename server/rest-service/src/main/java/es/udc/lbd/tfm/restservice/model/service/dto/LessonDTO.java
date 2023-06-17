package es.udc.lbd.tfm.restservice.model.service.dto;

import javax.validation.constraints.NotNull;

import es.udc.lbd.tfm.restservice.model.domain.Lesson;

public class LessonDTO {
	
	private Long id;
	@NotNull
	private String name;
	@NotNull
	private Integer number;
	@NotNull
	private UnitDTO unit;
	
	public LessonDTO() {}
	
	public LessonDTO(Lesson lesson) {
		this.id = lesson.getId();
		this.name = lesson.getName();
		this.number = lesson.getNumber();
		this.unit = new UnitDTO(lesson.getUnit());
		
	}
	
	public LessonDTO(Long id, @NotNull String name, @NotNull Integer number) {
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

	public UnitDTO getUnit() {
		return unit;
	}

	public void setUnit(UnitDTO unit) {
		this.unit = unit;
	}
	
	

}
