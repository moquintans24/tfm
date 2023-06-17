package es.udc.lbd.tfm.restservice.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.model.service.UnitService;
import es.udc.lbd.tfm.restservice.model.service.dto.UnitDTO;

@RestController
@RequestMapping("/api/units")
public class UnitResource {
	
	@Autowired
	private UnitService unitService;
	
	@GetMapping("/{bookId}")
	public List<UnitDTO> findByBookId(@PathVariable Long bookId) throws NotFoundException{
		return unitService.findByBookId(bookId);
	}
	
	@PostMapping("/{bookId}")
	public UnitDTO create(@PathVariable Long bookId, @RequestParam("name")String name, @RequestParam("number")String number) throws NumberFormatException, NotFoundException {
		return unitService.create(bookId, name, Integer.parseInt(number));
	}
	
	@DeleteMapping("/{unitId}")
	public void delete(@PathVariable Long unitId) throws NotFoundException {
		unitService.delete(unitId);
	}

}
