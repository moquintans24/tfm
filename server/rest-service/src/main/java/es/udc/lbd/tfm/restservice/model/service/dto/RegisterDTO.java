package es.udc.lbd.tfm.restservice.model.service.dto;

import javax.validation.constraints.NotNull;

public class RegisterDTO {

	@NotNull
	private UserDTOPrivate user;
	
	private String code;
	
	public RegisterDTO() {}

	public UserDTOPrivate getUser() {
		return user;
	}

	public void setUser(UserDTOPrivate user) {
		this.user = user;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
