package es.udc.lbd.tfm.restservice.web;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.model.service.CodeService;
import es.udc.lbd.tfm.restservice.model.service.dto.CodeDTO;

@RestController
@RequestMapping("/api/codes")
public class CodeResource {
	
	@Autowired
	private CodeService codeService;
	
	@GetMapping
	public List<CodeDTO> findAll(){
		return codeService.findAll();
	}
	
	@GetMapping("/{bookId}")
	public List<CodeDTO> findByBookId(@PathVariable Long bookId) throws NotFoundException{
		return codeService.findByBookId(bookId);
	}
	
	@PostMapping("/{bookId}")
	public List<CodeDTO> create(@PathVariable Long bookId, @RequestParam("number") Integer number, @RequestParam("expirationDate") String expirationDate,
			@RequestParam("userId") String userId) {
		return codeService.create(number, LocalDate.parse(expirationDate), Long.parseLong(userId), bookId);
	}
	
	@GetMapping("/{bookId}/exportCodes")
	public List<CodeDTO> findByCreationDate(@PathVariable Long bookId, @RequestParam("creationDateA") String creationDate1, 
			@RequestParam("creationDateB") String creationDate2) throws NotFoundException{
		return codeService.findByCreationDate(bookId, LocalDate.parse(creationDate1), LocalDate.parse(creationDate2));
	}
}
