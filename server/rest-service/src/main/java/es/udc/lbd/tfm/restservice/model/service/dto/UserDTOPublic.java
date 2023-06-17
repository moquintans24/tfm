package es.udc.lbd.tfm.restservice.model.service.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import es.udc.lbd.tfm.restservice.model.domain.User;

public class UserDTOPublic {
  private Long id;
  private String login;
  private String name;
  private String lastName;
  private String language;
  private LocalDate birthDate;
  private String email;
  private String center;
  private LocalDateTime registerDate;
  private Boolean unsubscribe;
  private String linkImage;

  public UserDTOPublic() {
  }

  public UserDTOPublic(User user) {
    this.id = user.getId();
    this.login = user.getLogin();
    this.name = user.getName();
    this.lastName = user.getLastName();
    this.birthDate = user.getBirthDate();
    this.language = user.getLanguage();
    this.center = user.getCenter();
    this.email = user.getEmail();
    this.setRegisterDate(user.getRegistrationDate());
    this.unsubscribe = user.getUnsubscribe();
    this.linkImage = user.getLinkImage();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public LocalDate getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getCenter() {
		return center;
	}
	
	public void setCenter(String center) {
		this.center = center;
	}

	public LocalDateTime getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(LocalDateTime registerDate) {
		this.registerDate = registerDate;
	}

	public Boolean getUnsubscribe() {
		return unsubscribe;
	}

	public void setUnsubscribe(Boolean unsubscribe) {
		this.unsubscribe = unsubscribe;
	}

	public String getLinkImage() {
		return linkImage;
	}

	public void setLinkImage(String linkImage) {
		this.linkImage = linkImage;
	}
	
	
  
}
