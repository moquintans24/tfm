package es.udc.lbd.tfm.restservice.model.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "classGroup")
public class ClassGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "classGroup_generator")
	@SequenceGenerator(name="classGroup_generator", sequenceName = "classes_seq")
	private Long id;
	@Column(length = 16)
	private String code;
	
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Subscription subscription; //teacher
	
	@OneToOne(fetch = FetchType.LAZY)
	private Lesson lesson; //para saber hasta que lección están visibles las actividades
	
	
	public ClassGroup() {
	
	}
	
	public ClassGroup(String code, String name, Subscription subs, Lesson lesson) {
		this.code = code;
		this.name = name;
		this.subscription = subs;
		this.lesson = lesson;
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

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

}
