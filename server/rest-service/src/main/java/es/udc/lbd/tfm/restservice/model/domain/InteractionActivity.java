package es.udc.lbd.tfm.restservice.model.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "interactionActivity")
public class InteractionActivity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "interation_generator")
	@SequenceGenerator(name="interation_generator", sequenceName = "interation_seq")
	private Long id;

	private LocalTime duration;
	
	private Double score;
	
	private LocalDateTime homeworkDate;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Activity activity;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Subscription subscription; //student subscription
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Subscription teacherSubscription;
	
	
	public InteractionActivity() {
		
	}

	public InteractionActivity(LocalTime duration, Double score, LocalDateTime homeworkDate, Activity activity, Subscription student, Subscription teacherSubscription ) {
		this.duration = duration;
		this.score = score;
		this.homeworkDate = homeworkDate;
		this.activity = activity;
		this.subscription = student;
		this.teacherSubscription = teacherSubscription;
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public LocalTime getDuration() {
		return duration;
	}


	public void setDuration(LocalTime duration) {
		this.duration = duration;
	}


	public Double getScore() {
		return score;
	}


	public void setScore(Double score) {
		this.score = score;
	}


	public LocalDateTime getHomeworkDate() {
		return homeworkDate;
	}


	public void setHomeworkDate(LocalDateTime homeworkDate) {
		this.homeworkDate = homeworkDate;
	}


	public Activity getActivity() {
		return activity;
	}


	public void setActivity(Activity activity) {
		this.activity = activity;
	}


	public Subscription getSubscription() {
		return subscription;
	}


	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}


	public Subscription getTeacherSubscription() {
		return teacherSubscription;
	}


	public void setTeacherSubscription(Subscription teacherSubscription) {
		this.teacherSubscription = teacherSubscription;
	}
	
	
}
