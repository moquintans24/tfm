package es.udc.lbd.tfm.restservice.web;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.model.service.InteractionActivityService;
import es.udc.lbd.tfm.restservice.model.service.dto.InteractionActivityDTO;

@RestController
@RequestMapping("/api/interactionsActivities")
public class InteractionActivityResource {
	
	@Autowired
	private InteractionActivityService interactionActivityService;
	
	@GetMapping("/classGroup/{id}")
	public List<InteractionActivityDTO> findByClassGroupId(@PathVariable Long id) throws NotFoundException{
		return interactionActivityService.findByClassGroupIdAndTeacherSubscriptionId(id);
	}
	
	@GetMapping("/classGroup/{classGroupId}/subscription/{subscriptionId}")
	public List<InteractionActivityDTO> findByClassGroupIdAndSubscriptionId(@PathVariable Long classGroupId, @PathVariable Long subscriptionId) throws NotFoundException{
		return interactionActivityService.findByClassGroupIdAndSubscriptionId(classGroupId, subscriptionId);
	}

	@GetMapping("/activity/{id}/{classGroupId}")
	public List<InteractionActivityDTO> findByActivityIdAndClassGroupId(@PathVariable Long id, @PathVariable Long classGroupId) throws NotFoundException{
		return interactionActivityService.findByActivityIdAndClassGroupId(id, classGroupId);
	}
	
	@GetMapping("/pendings/{classGroupId}/{userId}")
	public List<InteractionActivityDTO> findPendingsByClassGroupIdAndUserId(@PathVariable Long classGroupId, @PathVariable Long userId) throws NotFoundException{
		return interactionActivityService.findPendingsByClassGroupIdAndUserId(classGroupId,userId);
	}
	
	@GetMapping("/activity/{activityId}/user/{userId}")
	public InteractionActivityDTO findByActivityAndUserId(@PathVariable Long activityId, @PathVariable Long userId) throws NotFoundException {
		return interactionActivityService.findByActivityIdAndUserId(activityId, userId);
	}
	
	@GetMapping("/classGroup/{classGroupId}/average-score")
	public List<?> averageScoreStudentsByClassGroupId(@PathVariable Long classGroupId) throws NotFoundException{
		return interactionActivityService.averageScoreStudentsByClassGroupId(classGroupId);
	}

	@GetMapping("/classGroup/{classGroupId}/unit/{unitId}/average-score")
	public List<?> averageScoreStudentsByClassGroupIdAndUnitId(@PathVariable Long classGroupId, @PathVariable Long unitId) throws NotFoundException{
		return interactionActivityService.averageScoreStudentsByClassGroupIdAndUnitId(classGroupId, unitId);
	}
	
	@PutMapping("/update-homework-date/{classGroupId}/{teacherSubscriptionId}/{activityId}")
	public void updateHomeworkDateByClassGroupIdAndTeacherSubscriptionId(@PathVariable Long classGroupId,@PathVariable Long teacherSubscriptionId,
			@PathVariable Long activityId,
			@RequestParam(name="homeworkDate", required = false) String  homeworkDate) throws NotFoundException {
		if(homeworkDate != null) {
			interactionActivityService.updateHomeworkDateByClassGroupIdTeacherSubscriptionIdAndActivityId(classGroupId, teacherSubscriptionId, activityId,
					LocalDateTime.parse(homeworkDate,DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		}else {
			interactionActivityService.updateHomeworkDateByClassGroupIdTeacherSubscriptionIdAndActivityId(classGroupId, teacherSubscriptionId, activityId,null);
		}
		
	}
	
	@PostMapping("/activity/{activityId}/{classGroupId}")
	public void assignInteractionsByActivityIdAndClassGroupId(@PathVariable Long activityId, @PathVariable Long classGroupId,
			@RequestParam(name="homeworkDate", required = false) String  homeworkDate) throws NotFoundException{
		if(homeworkDate == null) {
			interactionActivityService.createInteractionsByActivityIdAndClassGroupId(activityId, classGroupId);
		}else {
			interactionActivityService.assignInteractionsByActivityIdAndClassGroupId(activityId, classGroupId,
					LocalDateTime.parse(homeworkDate,DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		}
		
	}
	
	@PutMapping("/save-activity-done/{interactionActivityId}")
	public void saveActivityDuractionAndScore(@PathVariable Long interactionActivityId, @RequestParam("duration") String duration, 
			@RequestParam("score") String score) throws NumberFormatException, NotFoundException {
			int hours = (int) (Double.parseDouble(duration) / 3600);
	        int minutes = (int) ((Double.parseDouble(duration) % 3600) / 60);
	        int secs = (int) (Double.parseDouble(duration) % 60);
		interactionActivityService.saveDurationAndScore(LocalTime.parse(String.format("%02d:%02d:%02d", hours, minutes, secs)), Double.parseDouble(score), interactionActivityId);
	}

}
