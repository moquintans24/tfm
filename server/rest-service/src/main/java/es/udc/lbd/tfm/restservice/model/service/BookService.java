package es.udc.lbd.tfm.restservice.model.service;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import es.udc.lbd.tfm.restservice.model.domain.Activity;
import es.udc.lbd.tfm.restservice.model.domain.Book;
import es.udc.lbd.tfm.restservice.model.domain.BookState;
import es.udc.lbd.tfm.restservice.model.domain.UserAuthority;
import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.model.repository.ActivityDao;
import es.udc.lbd.tfm.restservice.model.repository.BookDao;
import es.udc.lbd.tfm.restservice.model.repository.LessonDao;
import es.udc.lbd.tfm.restservice.model.service.dto.ActivityDTO;
import es.udc.lbd.tfm.restservice.model.service.dto.BookDTO;
import es.udc.lbd.tfm.restservice.model.service.dto.LessonDTO;
import es.udc.lbd.tfm.restservice.model.service.dto.UnitDTO;
import es.udc.lbd.tfm.restservice.storage.StorageException;
import es.udc.lbd.tfm.restservice.storage.StorageService;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class BookService {
	
	@Autowired
	private BookDao bookDAO;
	
	@Autowired
	private LessonDao lessonDAO;
	
	@Autowired
	private ActivityDao activityDAO;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LessonService lessonService;
	
	@Autowired 
	private UnitService unitService;
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
    private StorageService storageService;
	
	public List<BookDTO> findAll() {
		if(userService.getCurrentUserWithAuthority().getAuthority().equals(UserAuthority.ADMIN.toString()) || 
				userService.getCurrentUserWithAuthority().getAuthority().equals(UserAuthority.EDITOR.toString())){
			return bookDAO.findAll().stream().map(book -> new BookDTO(book)).collect(Collectors.toList());
		}
		return bookDAO.findAllWithReadyState().stream().map(book -> new BookDTO(book)).collect(Collectors.toList());
	}
	
	public BookDTO findById(Long id) throws NotFoundException {
		Book book = bookDAO.findById(id);
		if(book == null) {
			throw new NotFoundException("Book with id "+ id + " not found!");
		}
		return new BookDTO(book);
	}
	
	public List<?> statisticsAllBooks(){
		return bookDAO.statisticsAllBooks();
	}

	@Transactional(readOnly = false)
	public BookDTO create(BookDTO book) {
		Book bdBook = new Book(book.getTitle(),book.getCourse(),book.getLevel(),book.getState());
		bookDAO.create(bdBook);
		return new BookDTO(bdBook);
	}
	
	@Transactional(readOnly = false)
	public BookDTO create(String title, String course, Integer level, MultipartFile file) throws NotFoundException, StorageException {
		Book bdBook = new Book(title,course,level,BookState.PENDING,file.getOriginalFilename());
		bookDAO.create(bdBook);
		
		BookDTO book = new BookDTO(bdBook);
		storageService.store(file,title,"book",book.getId());
		return new BookDTO(bdBook);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN','EDITOR')")
	@Transactional(readOnly = false)
	public BookDTO duplicate(Long id) throws NotFoundException, IOException {
		Book bdBook = bookDAO.findById(id);
		if(bdBook == null) {
			throw new NotFoundException("Book with id "+id+" not found!");
		}
		Book newBook = new Book();
		newBook.setCourse(bdBook.getCourse());
		newBook.setLevel(bdBook.getLevel());
		newBook.setState(bdBook.getState());
		
		if(bookDAO.findByTitle(bdBook.getTitle())!= null) {
			if(Character.isDigit(bdBook.getTitle().charAt(bdBook.getTitle().length() - 1))) {
				
					newBook.setTitle("Duplicate "+bdBook.getTitle().substring(0, bdBook.getTitle().length() - 1)
							+Integer.sum(Integer.parseInt(bdBook.getTitle().substring(bdBook.getTitle().length() - 1)),1));

			}else {
				newBook.setTitle("Duplicate "+bdBook.getTitle()+" 2");
			}
		}else {
			newBook.setTitle("Duplicate "+bdBook.getTitle());
		}
		bookDAO.create(newBook);
		
		BookDTO book = new BookDTO(newBook);
		String imagePath = storageService.copy("book", id, book.getId());
		newBook.setImage(imagePath);
		bookDAO.update(newBook);
		
		List<UnitDTO> units = unitService.findByBookId(bdBook.getId());
		for(int i=0;i<units.size();i++) {
			UnitDTO bdUnit = unitService.create(book.getId(), units.get(i).getName(), units.get(i).getNumber());

			List<LessonDTO> lessons = lessonService.findByUnitId(units.get(i).getId());
			for(int j=0;j<lessons.size();j++) {
				LessonDTO bdLesson = lessonService.create(bdUnit.getId(), lessons.get(j).getName(), lessons.get(j).getNumber());
				
				List<ActivityDTO> activities = activityService.findByLessonId(lessons.get(j).getId());
				for(int k=0;k<activities.size();k++) {
					Activity activity = new Activity();
					activity.setCode(activities.get(k).getCode());
					activity.setDescription(activities.get(k).getDescription());
					activity.setIdH5Pfile(activities.get(k).getIdH5Pfile());
					activity.setImagePath(activities.get(k).getImagePath());
					activity.setLesson(lessonDAO.findById(bdLesson.getId()));
					activity.setNumber(activities.get(k).getNumber());
					activity.setTitle(activities.get(k).getTitle());
					activity.setTypeH5P(activities.get(k).getTypeH5P());
					
					activityDAO.create(activity);
				}
			}
			
			
		}
		
		return new BookDTO(newBook);
	}
	
	@Transactional(readOnly = false)
	public BookDTO updateLinkImage(BookDTO book) {
		Book bdBook = bookDAO.findById(book.getId());
		bdBook.setImage(book.getImage());
		bookDAO.update(bdBook);
		return new BookDTO(bdBook);
	}
	
	
	@PreAuthorize("hasAnyAuthority('ADMIN','EDITOR')")
	@Transactional(readOnly = false)
	public void deleteById(Long id) throws NotFoundException, IOException {
		List<UnitDTO> units = unitService.findByBookId(id);
		for(int i=0;i<units.size();i++) {
			List<LessonDTO> lessons = lessonService.findByUnitId(units.get(i).getId());
			for(int j=0;j<lessons.size();j++) {
				lessonService.delete(lessons.get(j).getId()); //ya se están borrando las actividades en la función lessonService.delete
			}
			unitService.delete(units.get(i).getId());
		}
		storageService.delete("book", id);
		bookDAO.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public BookDTO update(BookDTO book) throws NotFoundException {
		Book bdBook = bookDAO.findById(book.getId());
		if(bdBook == null) {
			throw new NotFoundException("Book with id "+book.getId()+" not found!");
		}
		bdBook.setLevel(book.getLevel());
		bdBook.setCourse(book.getCourse());
		bdBook.setTitle(book.getTitle());
		bdBook.setState(book.getState());
		
		bookDAO.update(bdBook);
		return new BookDTO(bdBook);
	}
	
	public List<BookDTO> findByImage(String image){
		return bookDAO.findByImage(image).stream().map(book -> new BookDTO(book)).collect(Collectors.toList());
	}
}
