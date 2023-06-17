package es.udc.lbd.tfm.restservice.model.service.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import es.udc.lbd.tfm.restservice.model.domain.Subscription;
import es.udc.lbd.tfm.restservice.model.domain.SubscriptionState;

public class SubscriptionDTO {
	
	private Long id;
	@NotNull
	private LocalDateTime entrydate;
	@NotNull
	private UserDTOPublic user;
	@NotNull
	private SubscriptionState state;	
//	@NotNull
	private CodeDTO code;
	
	private ClassGroupDTO classGroup;
	
	public SubscriptionDTO() {
		
	}
	
	public SubscriptionDTO(Subscription subs) {
		this.id = subs.getId();
		this.entrydate = subs.getEntryDate();
		this.user = new UserDTOPublic(subs.getUser());
		if(subs.getClassGroup()!=null) {
			this.classGroup = new ClassGroupDTO(subs.getClassGroup());
		}
		if(subs.getCode()!=null) {
			this.code = new CodeDTO(subs.getCode());
		}
		this.state = subs.getState();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDateTime getEntrydate() {
		return entrydate;
	}
	public void setEntrydate(LocalDateTime entrydate) {
		this.entrydate = entrydate;
	}
	public UserDTOPublic getUser() {
		return user;
	}
	public void setUser(UserDTOPublic user) {
		this.user = user;
	}
	public SubscriptionState getState() {
		return state;
	}

	public void setState(SubscriptionState state) {
		this.state = state;
	}
	public CodeDTO getCode() {
		return code;
	}
	public void setCode(CodeDTO code) {
		this.code = code;
	}

	public ClassGroupDTO getClassGroup() {
		return classGroup;
	}

	public void setClassgroup(ClassGroupDTO classGroup) {
		this.classGroup = classGroup;
	}
	
}
