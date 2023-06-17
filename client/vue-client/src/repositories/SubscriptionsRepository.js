import HTTP from "@/common/http";

const resource = "subscriptions";

export default {
    async getById(subscriptionId){
        return (await HTTP.get(`${resource}/subscription/${subscriptionId}`)).data;
    },
    async getByUserId(userId) {
        return (await HTTP.get(`${resource}/${userId}`)).data;
    },
    async getByClassGroupId(classGroupId){
        return (await HTTP.get(`${resource}/classGroup/${classGroupId}`)).data;
    },
    async deleteClassGroupId(subscriptionId){
        await HTTP.post(`${resource}/classGroup/${subscriptionId}`);
    },
    async cancelSubscriptionToClassGroup(subscriptionId){
        await HTTP.post(`${resource}/cancelClassGroupSubscription/${subscriptionId}`);
    },
    async acceptSubscriptionToClassGroup(subscriptionId){
        await HTTP.post(`${resource}/acceptClassGroupSubscription/${subscriptionId}`);
    },
    async deleteById(id){
        await HTTP.delete(`${resource}/${id}`);
    },
    async save(formData){
        await(HTTP.post(`${resource}`,formData,
        { headers: {
            'Content-Type': 'multipart/form-data'
        } }));
    },
    async getByClassGroupIdAndUserId(classGroupId,userId){
        return (await HTTP.get(`${resource}/classGroup/${classGroupId}/user/${userId}`)).data;
    },
    async updateUserSubscriptionsAddUserToClassGroup(classGroupCode, userId,bookCode){
        await HTTP.post(`${resource}/add-user-to-class-group/${classGroupCode}/${userId}/${bookCode}`);
    },
    async getByBookIdAndUserId(bookId,userId){
        return (await HTTP.get(`${resource}/${userId}/${bookId}`)).data;
    }
  };