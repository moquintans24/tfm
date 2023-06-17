package es.udc.lbd.tfm.restservice.model.service.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import es.udc.lbd.tfm.restservice.model.domain.User;

public class UserDTOPrivate {
  private Long id;
  private String login;
  private String password;
  private String name;
  private String lastName;
  private LocalDate birthDate;
  private String language;
  private String center;
  private String email;
  private String rol;
  private String authority;
  private LocalDateTime registerDate;
  private String linkImage;

  public UserDTOPrivate() {
  }

  public UserDTOPrivate(User user) {
    this.id = user.getId();
    this.login = user.getLogin();
    // la contraseña no se rellena, nunca se envía al cliente
    this.name = user.getName();
    this.lastName = user.getLastName();
    this.birthDate = user.getBirthDate();
    this.language = user.getLanguage();
    this.center = user.getCenter();
    this.email = user.getEmail();
    this.authority = user.getAuthority().name();
    this.registerDate = user.getRegistrationDate();
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
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

public LocalDate getBirthDate() {
	return birthDate;
}

public void setBirthDate(LocalDate birthDate) {
	this.birthDate = birthDate;
}

public String getLanguage() {
	return language;
}

public void setLanguage(String language) {
	this.language = language;
}

public String getCenter() {
	return center;
}

public void setCenter(String center) {
	this.center = center;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getRol() {
	return rol;
}

public void setRol(String rol) {
	this.rol = rol;
}

public LocalDateTime getRegisterDate() {
	return registerDate;
}

public void setRegisterDate(LocalDateTime registerDate) {
	this.registerDate = registerDate;
}

public String getLinkImage() {
	return linkImage;
}

public void setLinkImage(String linkImage) {
	this.linkImage = linkImage;
}
}
