package es.udc.lbd.tfm.restservice.model.service.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

import es.udc.lbd.tfm.restservice.model.domain.InteractionActivity;

public class InteractionActivityDTO {
	
	private Long id;
	
	private LocalTime duration;
	
	private Double score;
	
	private LocalDateTime homeworkDate;
	
	private ActivityDTO activity;
	
	private SubscriptionDTO subscription;
	
	private SubscriptionDTO teacherSubscription;
	
	public InteractionActivityDTO() {}
	
	public InteractionActivityDTO(InteractionActivity interactionActivity) {
		this.id = interactionActivity.getId();
		this.duration = interactionActivity.getDuration();
		this.score = interactionActivity.getScore();
		this.homeworkDate = interactionActivity.getHomeworkDate();
		this.activity = new ActivityDTO(interactionActivity.getActivity());
		this.subscription = new SubscriptionDTO(interactionActivity.getSubscription());
		this.teacherSubscription = new SubscriptionDTO(interactionActivity.getTeacherSubscription());
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

	public ActivityDTO getActivity() {
		return activity;
	}

	public void setActivity(ActivityDTO activity) {
		this.activity = activity;
	}

	public SubscriptionDTO getSubscription() {
		return subscription;
	}

	public void setSubscription(SubscriptionDTO subscription) {
		this.subscription = subscription;
	}

	public SubscriptionDTO getTeacherSubscription() {
		return teacherSubscription;
	}

	public void setTeacherSubscription(SubscriptionDTO teacherSubscription) {
		this.teacherSubscription = teacherSubscription;
	}

}
