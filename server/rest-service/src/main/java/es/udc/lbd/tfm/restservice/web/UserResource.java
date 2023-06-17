package es.udc.lbd.tfm.restservice.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.tfm.restservice.model.service.EmailService;
import es.udc.lbd.tfm.restservice.model.service.UserService;
import es.udc.lbd.tfm.restservice.model.service.dto.UserDTOPrivate;
import es.udc.lbd.tfm.restservice.model.service.dto.UserDTOPublic;
import es.udc.lbd.tfm.restservice.web.exception.RequestBodyNotValidException;
import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.model.exception.UserEmailExistsException;
import es.udc.lbd.tfm.restservice.model.exception.UserLoginExistsException;

@RestController
@RequestMapping("/api/users")
public class UserResource {

  @Autowired
  private UserService userService;
  
  @Autowired
  private ServletContext servletContext;
  
  @Autowired
  private EmailService emailService;

  @GetMapping
  public List<UserDTOPrivate> findAll() {
    return userService.findAll();
  }

  
  @GetMapping("/{id}")
  public UserDTOPublic findById(@PathVariable Long id) throws NotFoundException {
	  return userService.findById(id);
  }
  
  @GetMapping("/subscription/{id}")
  public UserDTOPublic findBySubscriptionId(@PathVariable Long id) throws NotFoundException {
	  return userService.findBySubscriptionId(id);
  }

  @GetMapping("/editors")
  public List<UserDTOPublic> findAllEditors() {
	  return userService.findAllEditors();
  }
  
  @ResponseBody
  @RequestMapping(value = "/images/{ruta}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
  public byte[] userImage(@PathVariable String ruta) throws IOException {
	    InputStream in = servletContext.getResourceAsStream("/WEB-INF/images/users/"+ ruta);
	    return IOUtils.toByteArray(in);
  }
  
  @PutMapping("/profile/{id}")
  public UserDTOPublic update(@RequestBody @Valid UserDTOPublic user, @PathVariable Long id, Errors errors) throws RequestBodyNotValidException,UserEmailExistsException, NotFoundException {
	  errorHandler(errors);
	  return userService.update(user,id);
  }
  
  @PostMapping
	public void create(@RequestBody @Valid UserDTOPublic user, Errors errors) throws RequestBodyNotValidException, UserLoginExistsException, UserEmailExistsException {
	    errorHandler(errors);
	     userService.create(user);
	     emailService.sendToCreateLogin(user);
	}
  
  @DeleteMapping("/{id}")
  public void deleteById(@PathVariable Long id)  {
	  userService.deleteById(id);
  }
  private void errorHandler(Errors errors) throws RequestBodyNotValidException {
    if (errors.hasErrors()) {
      String errorMsg = errors.getFieldErrors().stream()
          .map(fe -> String.format("%s.%s %s", fe.getObjectName(), fe.getField(), fe.getDefaultMessage()))
          .collect(Collectors.joining("; "));
      throw new RequestBodyNotValidException(errorMsg);
    }
  }
}
