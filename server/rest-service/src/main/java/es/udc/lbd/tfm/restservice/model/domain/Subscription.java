package es.udc.lbd.tfm.restservice.model.domain;

import java.time.LocalDateTime;

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
@Table(name = "theSubscription")
public class Subscription {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscription_generator")
	@SequenceGenerator(name="subscription_generator", sequenceName = "subscriptions_seq")
	private Long id;
	
	private LocalDateTime entryDate;
	
	private SubscriptionState state;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private ClassGroup classGroup;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Code code;
	
	public Subscription() {
	
	}
	
	public Subscription(LocalDateTime entryDate, User user, Code code, SubscriptionState state) {
		this.entryDate = entryDate;
		this.user = user;
		this.code = code;
		this.state = state;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(LocalDateTime entryDate) {
		this.entryDate = entryDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Code getCode() {
		return code;
	}

	public ClassGroup getClassGroup() {
		return classGroup;
	}

	public void setClassGroup(ClassGroup classGroup) {
		this.classGroup = classGroup;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public SubscriptionState getState() {
		return state;
	}

	public void setState(SubscriptionState state) {
		this.state = state;
	}

}
