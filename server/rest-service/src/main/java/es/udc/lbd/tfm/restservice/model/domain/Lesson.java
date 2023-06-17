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
@Table(name = "lesson")
public class Lesson {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lesson_generator")
	@SequenceGenerator(name="lesson_generator", sequenceName = "lessons_seq")
	private Long id;
	
	private String name;
	
	private Integer number;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Unit unit;
	
	public Lesson() {
		
	}
	
	public Lesson(String name, Integer number, Unit unit ) {
		this.name = name;
		this.number = number;
		this.unit = unit;
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

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
	

}
