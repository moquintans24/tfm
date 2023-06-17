import HTTP from "@/common/http";

const resource = "classGroups";


export default {
  async getByUserId(userId) {
    return (await HTTP.get(`${resource}/user/${userId}`)).data;
  },
  async getById(classGroupId){
    return (await HTTP.get(`${resource}/${classGroupId}`)).data;
  },
  async save(classGroup){
    if(classGroup.id){
      return (await HTTP.put(`${resource}/${classGroup.id}`, classGroup)).data;
    }else{
      return (await HTTP.post(`${resource}`, classGroup)).data;
    }
  },
  async delete(id){
    return (await HTTP.delete(`${resource}/${id}`));
  },
  async getByBookIdAndUserId(bookId, userId){
    return (await HTTP.get(`${resource}/book/${bookId}/user/${userId}`)).data;
  },
  async showHideLesson(classGroupId, lesson, type){
    return (await HTTP.put(`${resource}/show-hide-lesson/${type}/${classGroupId}`,lesson )).data;
  },
  async getByBookIdAndTeacherSubscriptionId(bookId, teacherSubscriptionId){
    return (await HTTP.get(`${resource}/book/${bookId}/teacherSubscription/${teacherSubscriptionId}`)).data;
  }
};