package es.udc.lbd.tfm.restservice.model.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "theUser")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
  @SequenceGenerator(name = "user_generator", sequenceName = "user_seq")
  private Long id;

  @Column(unique = true)
  private String login;

  private String password;
  
  private String name;
  
  private String lastName;
  
  private LocalDate birthDate;
  
  private String language;
  
  @Column(unique = true)
  private String email;
  
  private String center;
  
  private LocalDateTime registrationDate;
  
  private Boolean unsubscribe;
  
  private String linkImage;

  @Enumerated(EnumType.STRING)
  private UserAuthority authority;
  
  private LocalDate emailExpirationDate; //fecha para saber la validez del correo enviado para recuperar contraseña o configurar login
  
  private String validationString; //Cadena para validar cuando quiera recuperar la contraseña o configurar login

  public User() {
  }
  
  public User(String name, String lastName, String email, UserAuthority auth, LocalDateTime registrationDate,LocalDate birthdate) {
	  this.name = name;
	  this.email = email;
	  this.lastName = lastName;
	  this.authority = auth;
	  this.birthDate = birthdate;
	  this.registrationDate = registrationDate;
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

  public UserAuthority getAuthority() {
    return authority;
  }

  public void setAuthority(UserAuthority authority) {
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

	public LocalDateTime getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDateTime registrationDate) {
		this.registrationDate = registrationDate;
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

	public LocalDate getEmailExpirationDate() {
		return emailExpirationDate;
	}

	public void setEmailExpirationDate(LocalDate emailExpirationDate) {
		this.emailExpirationDate = emailExpirationDate;
	}

	public String getValidationString() {
		return validationString;
	}

	public void setValidationString(String validationString) {
		this.validationString = validationString;
	}
}
