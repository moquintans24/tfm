package es.udc.lbd.tfm.restservice.web;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.tfm.restservice.model.exception.CanNotChangePasswordException;
import es.udc.lbd.tfm.restservice.model.exception.CanNotSaveLoginAndPasswordException;
import es.udc.lbd.tfm.restservice.model.exception.CodeExpiredException;
import es.udc.lbd.tfm.restservice.model.exception.CodeUsedException;
import es.udc.lbd.tfm.restservice.model.exception.EmailExpirationDateException;
import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.model.exception.SubscriptionCodeExistisException;
import es.udc.lbd.tfm.restservice.model.exception.SubscriptionException;
import es.udc.lbd.tfm.restservice.model.exception.UserEmailExistsException;
import es.udc.lbd.tfm.restservice.model.exception.UserLoginExistsException;
import es.udc.lbd.tfm.restservice.model.service.EmailService;
import es.udc.lbd.tfm.restservice.model.service.UserService;
import es.udc.lbd.tfm.restservice.model.service.dto.LoginDTO;
import es.udc.lbd.tfm.restservice.model.service.dto.RegisterDTO;
import es.udc.lbd.tfm.restservice.model.service.dto.UserDTOPrivate;
import es.udc.lbd.tfm.restservice.model.service.dto.UserDTOPublic;
import es.udc.lbd.tfm.restservice.security.JWTConfigurer;
import es.udc.lbd.tfm.restservice.security.JWTToken;
import es.udc.lbd.tfm.restservice.security.TokenProvider;
import es.udc.lbd.tfm.restservice.web.exception.CredentialsAreNotValidException;
import es.udc.lbd.tfm.restservice.web.exception.UnsubscribeUserException;

/**
 * Este controlador va por separado que el UserResource porque se encarga de
 * tareas relacionadas con la autenticación, registro, etc.
 *
 * <p>
 * También permite a cada usuario logueado en la aplicación obtener información
 * de su cuenta
 */
@RestController
@RequestMapping("/api")
public class AccountResource {
  private final Logger logger = LoggerFactory.getLogger(AccountResource.class);

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserService userService;
  
  @Autowired
  private EmailService emailService;

  @PostMapping("/authenticate")
  public JWTToken authenticate(@Valid @RequestBody LoginDTO loginDTO) throws CredentialsAreNotValidException, NotFoundException, UnsubscribeUserException {

	UserDTOPublic user = userService.findByLogin(loginDTO.getLogin());
	if(user.getUnsubscribe()) {
		throw new UnsubscribeUserException("User with login "+loginDTO.getLogin()+" has been unsubscribed!");
	}
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        loginDTO.getLogin(), loginDTO.getPassword());
    try {
      Authentication authentication = authenticationManager.authenticate(authenticationToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = tokenProvider.createToken(authentication);
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
      return new JWTToken(jwt);
    } catch (AuthenticationException e) {
      logger.warn(e.getMessage(), e);
      throw new CredentialsAreNotValidException(e.getMessage());
    }
  }

  @GetMapping("/account")
  public UserDTOPrivate getAccount() {
    return userService.getCurrentUserWithAuthority();
  }

  @PostMapping("/register")
   public void registerAccount(@Valid @RequestBody RegisterDTO account) throws UserLoginExistsException,UserEmailExistsException, NotFoundException, SubscriptionCodeExistisException, CodeUsedException, SubscriptionException, CodeExpiredException {
	  userService.registerUser(account.getUser().getLogin(), account.getUser().getPassword(), account.getUser().getName(), account.getUser().getLastName(), 
			  account.getUser().getBirthDate(), 
			  account.getUser().getLanguage(), account.getUser().getEmail(), account.getUser().getCenter(), account.getUser().getRol(),
			  account.getCode());
  }
  
  @PostMapping("/forgot-password")
  public void forgotPassword(@RequestParam("email_login") String email_login) throws NotFoundException {
	 System.out.println(email_login);
	 emailService.send(email_login);
  }
  
  @PostMapping("/reset-password/{id}")
  public void resetPassword(@PathVariable Long id,@RequestParam("password") String password, @RequestParam("validationString") String validationString) 
		  throws NotFoundException, CanNotChangePasswordException, EmailExpirationDateException {
	  userService.resetPassword(id, password, validationString);
  }
  
  @PostMapping("/new-login-password/{id}")
  public void saveLoginPassword(@PathVariable Long id,@RequestParam("login") String login,@RequestParam("password") String password,
		  @RequestParam("validationString") String validationString) throws NotFoundException, UserLoginExistsException, EmailExpirationDateException, CanNotSaveLoginAndPasswordException {
	  userService.saveLoginPassword(id, login, password,validationString);
  }
}
