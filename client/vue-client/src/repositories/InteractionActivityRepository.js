import HTTP from "@/common/http";

const resource = "interactionsActivities";


export default {
    async getByClassGroupId(subsId){
        return (await HTTP.get(`${resource}/classGroup/${subsId}`)).data;
    },

    async getByTeacherSubscriptionId(subsId){
        return (await HTTP.get(`${resource}/teacherSubscription/${subsId}`)).data;
    },

    async getByActivityIdAndClassGroupId(activityId,classGroupId){
        return (await HTTP.get(`${resource}/activity/${activityId}/${classGroupId}`)).data;
    },

    async getByClassGroupIdAndSubscriptionId(classGroupId,subscriptionId){
        return (await HTTP.get(`${resource}/classGroup/${classGroupId}/subscription/${subscriptionId}`)).data;
    },
    
    async saveInteractionByActivityIdAndClassGroupId(activityId, classGroupId){
        (await HTTP.post(`${resource}/activity/${activityId}/${classGroupId}`));
    },

    async assignInteractionByActivityIdAndClassGroupId(activityId, classGroupId,homeworkDate){
        (await HTTP.post(`${resource}/activity/${activityId}/${classGroupId}`,{},
        {params: {
                homeworkDate
            }}
        ));
    },

    async getPendingsByClassGroupIdAndUserId(classGroupId,userId){
        return (await HTTP.get(`${resource}/pendings/${classGroupId}/${userId}`)).data;
    },
    async updateHomeworkDateByClassGroupIdTeacherSubscriptionIdAndActivityId(classGroupId,teacherSubscriptionId,activityId,homeworkDate){
        (await HTTP.put(`${resource}/update-homework-date/${classGroupId}/${teacherSubscriptionId}/${activityId}`,{},
        {params: {
                homeworkDate
            }}
        ));
    },
    async saveDurationAndScore(interactionActivityId,duration,score){
        (await HTTP.put(`${resource}/save-activity-done/${interactionActivityId}`,{},
        {
            params: {
                duration: duration,
                score: score
            }}
        ));
    },
    async getByActivityIdAndUserId(activityId, userId){
        return (await HTTP.get(`${resource}/activity/${activityId}/user/${userId}`)).data;
    },
    async getAverageScoreStudentsByClassGroupId(classGroupId){
        return (await HTTP.get(`${resource}/classGroup/${classGroupId}/average-score`)).data;
    },
    async getAverageScoreStudentsByClassGroupIdAndUnitId(classGroupId, unitId){
        return (await HTTP.get(`${resource}/classGroup/${classGroupId}/unit/${unitId}/average-score`)).data;
    }
};