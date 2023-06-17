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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.model.service.BookService;
import es.udc.lbd.tfm.restservice.model.service.dto.BookDTO;
import es.udc.lbd.tfm.restservice.storage.StorageException;
import es.udc.lbd.tfm.restservice.web.exception.IdAndBodyNotMatchingOnUpdateException;
import es.udc.lbd.tfm.restservice.web.exception.RequestBodyNotValidException;

@RestController
@RequestMapping("/api/books")
public class BookResource {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private ServletContext servletContext;
	
	@GetMapping
	public List<BookDTO> findAll() {
		return bookService.findAll();
	}
	
	@GetMapping("/statistics")
	public List<?> statisticsBooks(){
		return bookService.statisticsAllBooks();
	}
	
	@GetMapping("/{id}")
	public BookDTO findById(@PathVariable Long id) throws NotFoundException {
		return bookService.findById(id);
	}
	
	@ResponseBody
	@RequestMapping(value = "/images/{ruta}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] bookImage(@PathVariable String ruta) throws IOException {
	    InputStream in = servletContext.getResourceAsStream("/WEB-INF/images/books/"+ ruta);
	    return IOUtils.toByteArray(in);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) throws NotFoundException, IOException {
		bookService.deleteById(id);
	}
	
	@PostMapping
	public void create(@RequestParam("file") MultipartFile file,@RequestParam("bookTitle") String bookTitle,
			@RequestParam("bookCourse") String bookCourse,@RequestParam("bookLevel") Integer bookLevel) throws NotFoundException, StorageException{
	    	
	     bookService.create(bookTitle,bookCourse,bookLevel,file);
	}
	
	@PostMapping("/duplicate/{id}")
	public BookDTO duplicate(@PathVariable Long id) throws NotFoundException, IOException {
	   return bookService.duplicate(id);
	}

	@PutMapping("/{id}")
	public BookDTO update(@PathVariable Long id, @RequestBody @Valid BookDTO book, Errors errors) throws IdAndBodyNotMatchingOnUpdateException, RequestBodyNotValidException, NotFoundException {
		errorHandler(errors);
		if(!id.equals(book.getId())) {
			throw new IdAndBodyNotMatchingOnUpdateException("Path id "+id+" and book id "+book.getId()+" must be equals!");
		}
		return bookService.update(book);
		
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
