package es.udc.lbd.tfm.restservice.model.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.tfm.restservice.model.domain.Code;
import es.udc.lbd.tfm.restservice.model.domain.CodeState;
import es.udc.lbd.tfm.restservice.model.domain.Subscription;
import es.udc.lbd.tfm.restservice.model.domain.User;
import es.udc.lbd.tfm.restservice.model.domain.UserAuthority;
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
import es.udc.lbd.tfm.restservice.model.repository.CodeDao;
import es.udc.lbd.tfm.restservice.model.repository.SubscriptionDao;
import es.udc.lbd.tfm.restservice.model.repository.UserDao;
import es.udc.lbd.tfm.restservice.model.service.dto.UserDTOPrivate;
import es.udc.lbd.tfm.restservice.model.service.dto.UserDTOPublic;
import es.udc.lbd.tfm.restservice.security.SecurityUtils;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserDao userDAO;
	
	@Autowired
	private SubscriptionDao subscriptionDAO;
	
	@Autowired
	private CodeDao codeDAO;
	
	@Autowired
	private SubscriptionService subscriptionService;

	@PreAuthorize("hasAuthority('ADMIN')")
	public List<UserDTOPrivate> findAll() {
		return userDAO.findAll().stream().sorted(Comparator.comparing(User::getRegistrationDate).reversed()).map(user -> new UserDTOPrivate(user)).collect(Collectors.toList());
	}
	
	public List<UserDTOPublic> findAllEditors() {
		
		return userDAO.findAllEditors().stream().sorted(Comparator.comparing(User::getRegistrationDate).reversed()).map(user -> new UserDTOPublic(user)).collect(Collectors.toList());
	}
	
	public UserDTOPublic findById(Long id) throws NotFoundException {
		User user = userDAO.findById(id);
		if(user == null) {
			throw new NotFoundException("User with id "+ id +" not found!");
		}
		return new UserDTOPublic(user);
	}
	
	public UserDTOPublic findByLogin(String login) throws NotFoundException {
		User bdUser = userDAO.findByLogin(login);
		if(bdUser == null) {
			throw new NotFoundException("User with login "+login+" not found!");
		}
		
		return new UserDTOPublic(bdUser);
	}

	public UserDTOPublic findBySubscriptionId(Long subscriptionId) throws NotFoundException {
		Subscription bdSubscription = subscriptionDAO.findById(subscriptionId);
		if(bdSubscription == null) {
			throw new NotFoundException("Subscriptiond with id "+subscriptionId+" not found!");
		}
		return new UserDTOPublic(userDAO.findBySubscriptionId(subscriptionId));
	}

	
	public void registerUser(String name, String lastName, String email, String login,String password, LocalDate birthDate,
			String language,boolean isAdmin,String image) throws UserLoginExistsException {
		if (userDAO.findByLogin(login) != null) {
			throw new UserLoginExistsException("User login " + login + " already exists");
		}
		User user = new User();
		String encryptedPassword = passwordEncoder.encode(password);
		
		user.setLogin(login);
		user.setName(name);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setLogin(login);
		user.setRegistrationDate(LocalDateTime.now());
		user.setPassword(encryptedPassword);
		user.setUnsubscribe(false);
		user.setBirthDate(birthDate);
		user.setLinkImage(image);
		user.setLanguage(language);
		if (isAdmin) {
			user.setAuthority(UserAuthority.ADMIN);
		}else {
			user.setAuthority(UserAuthority.EDITOR);
		}

		userDAO.create(user);
	}
	
	@Transactional(readOnly = false)
	public UserDTOPublic registerUserWithImage(String login, String password,String name, String lastName, 
		LocalDate birthDate, String language, String email, String center, String rol,String image) throws UserLoginExistsException, UserEmailExistsException {
		if (userDAO.findByLogin(login) != null) {
			throw new UserLoginExistsException("User login " + login + " already exists");
		}
		if(userDAO.findByEmail(email) != null) {
			throw new UserEmailExistsException("User email " + email + " already exists" );
		}

		User user = new User();
		String encryptedPassword = passwordEncoder.encode(password);

		user.setLogin(login);
		user.setPassword(encryptedPassword);
		user.setName(name);
		user.setLastName(lastName);
		user.setBirthDate(birthDate);
		user.setCenter(center);
		user.setLanguage(language);
		user.setEmail(email);
		user.setRegistrationDate(LocalDateTime.now());
		user.setUnsubscribe(false);
		user.setLinkImage(image);
		if(rol.toString().equals("Profesorado")  || rol.equals("Profesorado")) {
		user.setAuthority(UserAuthority.TEACHER);
		}else if(rol.toString().equals("Alumnado")  || rol.equals("Alumnado")) {
		user.setAuthority(UserAuthority.USER);
		}else if (rol.toString().equals("Editor") || rol.equals("Editor") ){
				user.setAuthority(UserAuthority.EDITOR);
			}else {
				user.setAuthority(UserAuthority.ADMIN);
			}
		userDAO.create(user);
		return new UserDTOPublic(user);
	}

	public void registerUser(String login, String password) throws UserLoginExistsException, UserEmailExistsException {
		registerUser(login, password, false);
	}

	public void registerUser(String login, String password, boolean isAdmin) throws UserLoginExistsException, UserEmailExistsException {
		if (userDAO.findByLogin(login) != null) {
			throw new UserLoginExistsException("User login " + login + " already exists");
		}

		User user = new User();
		String encryptedPassword = passwordEncoder.encode(password);

		user.setLogin(login);
		user.setPassword(encryptedPassword);
		user.setAuthority(UserAuthority.USER);
		user.setUnsubscribe(false);
		if (isAdmin) {
			user.setAuthority(UserAuthority.ADMIN);
		}

		userDAO.create(user);
	}
	
	@Transactional(readOnly = false)
	public void registerUser(String login, String password,String name, String lastName, 
		LocalDate birthDate, String language, String email, String center, String rol, String code) throws NotFoundException, UserLoginExistsException, UserEmailExistsException, SubscriptionCodeExistisException, CodeUsedException, SubscriptionException, CodeExpiredException {
		UserDTOPublic newUser = registerUserWithImage(login, password, name, lastName, birthDate, language, email, center, rol,"default_user_image.jpg");
		Code bdCode = codeDAO.findByCode(code);
		if(bdCode == null) {
			throw new NotFoundException("Code "+code+" not found!");
		}
		if(bdCode.getState().equals(CodeState.USED)) {
			throw new CodeUsedException("Code "+code+" is already used");
		}
		subscriptionService.create(newUser.getId(), code);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@Transactional(readOnly = false)
	public UserDTOPublic create(UserDTOPublic user) throws UserEmailExistsException {
		//El admin solo va a dar de alta a editores de contenido
		if (userDAO.findByEmail(user.getEmail()) != null) {
			throw new UserEmailExistsException("User email " + user.getEmail() + " already exists");
		}
		
		User bdUser = new User(user.getName(), user.getLastName(), user.getEmail(), UserAuthority.EDITOR, LocalDateTime.now(),user.getBirthDate());
		bdUser.setUnsubscribe(false);
		bdUser.setLanguage("es-ES");
		bdUser.setLinkImage("default_user_image.jpg");
		userDAO.create(bdUser);
		return new UserDTOPublic(bdUser);
	}

	public UserDTOPrivate getCurrentUserWithAuthority() {
		String currentUserLogin = SecurityUtils.getCurrentUserLogin();
		if (currentUserLogin != null) {
		return new UserDTOPrivate(userDAO.findByLogin(currentUserLogin));
		}
		return null;
	}
	
	@Transactional(readOnly = false)
	public UserDTOPublic update(UserDTOPublic user, Long userId) throws UserEmailExistsException, NotFoundException {
		User bdUser = userDAO.findById(userId);
		if(!bdUser.getEmail().equalsIgnoreCase(user.getEmail())) {
			if (userDAO.findByEmail(user.getEmail()) != null) {
				throw new UserEmailExistsException("User email " + user.getEmail() + " already exists");
			}
			bdUser.setEmail(user.getEmail());
		}

		bdUser.setBirthDate(user.getBirthDate());
		bdUser.setName(user.getName());
		bdUser.setCenter(user.getCenter());
		bdUser.setLastName(user.getLastName());
		bdUser.setLanguage(user.getLanguage());

		bdUser.setUnsubscribe(false);
		userDAO.update(bdUser);
		return new UserDTOPublic(bdUser);
	}
	
	@Transactional(readOnly = false)
	public UserDTOPublic updateLinkImage(UserDTOPublic user) {
		User bdUser = userDAO.findById(user.getId());
		bdUser.setLinkImage(user.getLinkImage());
		userDAO.update(bdUser);
		return new UserDTOPublic(bdUser);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@Transactional(readOnly = false)
	public void deleteById(Long id) {
		userDAO.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void resetPassword(Long id, String password, String validationString) throws NotFoundException, CanNotChangePasswordException, EmailExpirationDateException {
		User bdUser = userDAO.findById(id);
		if(bdUser == null) {
			throw new NotFoundException("User with login "+id+"not found!");
		}
		
		if(!(validationString.equals(bdUser.getValidationString()))) {
			throw new CanNotChangePasswordException("User with id "+id+" can not change password! Validation error!");
		}
		if(LocalDate.now().isAfter(bdUser.getEmailExpirationDate())) {
			throw new EmailExpirationDateException("Date to change your password has expired!");
		}
		String encryptedPassword = passwordEncoder.encode(password);
		bdUser.setPassword(encryptedPassword);
		
		userDAO.update(bdUser);
	
	}
	
	@Transactional(readOnly = false)
	public void saveLoginPassword(Long id, String login, String password,String validationString) throws NotFoundException, UserLoginExistsException, EmailExpirationDateException, CanNotSaveLoginAndPasswordException {
		User bdUser = userDAO.findById(id);
		if(bdUser == null) {
			throw new NotFoundException("User with id "+id+" not found!");
		}
		if(userDAO.findByLogin(login) != null) {
			throw new UserLoginExistsException("Login "+login+"already exists!");
		}
		if(!(validationString.equals(bdUser.getValidationString()))) {
			throw new CanNotSaveLoginAndPasswordException("User with id "+id+" can not save password and login! Validation error!");
		}
		if(LocalDate.now().isAfter(bdUser.getEmailExpirationDate())) {
			throw new EmailExpirationDateException("Date to change your password has expired!");
		}
		String encryptedPassword = passwordEncoder.encode(password);
		bdUser.setPassword(encryptedPassword);
		bdUser.setLogin(login);
		
		userDAO.update(bdUser);
	}
}
