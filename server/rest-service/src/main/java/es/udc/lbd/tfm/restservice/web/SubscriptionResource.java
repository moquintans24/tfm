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

import es.udc.lbd.tfm.restservice.model.exception.CodeExpiredException;
import es.udc.lbd.tfm.restservice.model.exception.InvalidBookCodeException;
import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.model.exception.SubscriptionCodeExistisException;
import es.udc.lbd.tfm.restservice.model.exception.SubscriptionException;
import es.udc.lbd.tfm.restservice.model.service.SubscriptionService;
import es.udc.lbd.tfm.restservice.model.service.dto.SubscriptionDTO;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionResource {
	
	@Autowired
	private SubscriptionService subscriptionService;
	
	@GetMapping("/subscription/{subscriptionId}")
	public SubscriptionDTO findById(@PathVariable Long subscriptionId) throws NotFoundException {
		return subscriptionService.findById(subscriptionId);
	}
	
	
	@GetMapping("/{userId}")
	public List<SubscriptionDTO> findByUserId(@PathVariable Long userId) throws NotFoundException {
		return subscriptionService.findByUserId(userId);
	}
	
	@GetMapping("/{userId}/{bookId}")
	public SubscriptionDTO findByBookIdAndUserId(@PathVariable Long userId, @PathVariable Long bookId) throws NotFoundException {
		return subscriptionService.findByBookIdAndUserId(bookId, userId);
	}
	
	@PostMapping
	public void create(@RequestParam("code") String code, @RequestParam("userId") Long userId) throws NotFoundException, SubscriptionCodeExistisException, SubscriptionException, CodeExpiredException {
		subscriptionService.create(userId, code);
	}
	
	@GetMapping("/classGroup/{classGroupId}")
	public List<SubscriptionDTO> findByClassGroupId(@PathVariable Long classGroupId) throws NotFoundException {
		return subscriptionService.findByClassGroupId(classGroupId);
	}
	
	@GetMapping("/classGroup/{classGroupId}/user/{userId}")
	public SubscriptionDTO findByClassGroupIdAndUserId(@PathVariable Long classGroupId, @PathVariable Long userId) throws NotFoundException {
		return subscriptionService.findByClassGroupAndUserId(classGroupId, userId);
	}
	
	@PostMapping("/classGroup/{subscriptionId}")
	public void deleteClassGroupId(@PathVariable Long subscriptionId) throws NotFoundException {
		subscriptionService.deleteClassGroupId(subscriptionId);
	}
	
	@PostMapping("/cancelClassGroupSubscription/{subscriptionId}")
	public void cancelSubscriptionToClassGroup(@PathVariable Long subscriptionId) throws NotFoundException {
		subscriptionService.cancelSubscriptionToClassGroup(subscriptionId);
	}
	
	@PostMapping("/acceptClassGroupSubscription/{subscriptionId}")
	public void acceptSubscriptionToClassGroup(@PathVariable Long subscriptionId) throws NotFoundException {
		subscriptionService.acceptSubscriptionToClassGroup(subscriptionId);
	}
	
	@PostMapping("/add-user-to-class-group/{classGroupCode}/{userId}/{bookCode}")
	public void updateUserSubscriptionsAddUserToClassGroup(@PathVariable String classGroupCode, @PathVariable Long userId,@PathVariable String bookCode) 
			throws NotFoundException, InvalidBookCodeException, SubscriptionCodeExistisException, CodeExpiredException {
		subscriptionService.updateUserSubscriptionsAddUserToClassGroup(classGroupCode, userId,bookCode);
	}
	
	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable Long id)  {
		subscriptionService.deleteById(id);
	}
	
}
