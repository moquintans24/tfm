import HTTP from "@/common/http";

const resource = "activities";


export default {
    async getById(activityId){
        return (await HTTP.get(`${resource}/${activityId}`)).data;
    },
    async getByLessonId(lessonId){
        return (await HTTP.get(`${resource}/lesson/${lessonId}`)).data;
    },
    async getByBookId(bookId){
        return (await HTTP.get(`${resource}/book/${bookId}`)).data;
    },
    async getTotalStudentsRealizedActivity(classGroupId){
        return (await HTTP.get(`${resource}/students/${classGroupId}`)).data;
    },
    async getByIdWithInteractions(activityId){
        return (await HTTP.get(`${resource}/withInteractions/${activityId}`)).data;
    },
    async getAvailablesByClassGroupId(classGroupId){
        return (await HTTP.get(`${resource}/available/${classGroupId}`)).data;
    },
    async getByIdH5PFile(idH5PFile){
        return (await HTTP.get(`${resource}/h5p-file/${idH5PFile}`)).data;
    },
    async save(formData){
        return (await HTTP.post(`${resource}`, formData,
        { headers: {
            'Content-Type': 'multipart/form-data'
        } })).data; 
    },
    async update(activity){
        return (await HTTP.put(`${resource}/${activity.id}`, activity)).data;
    },
    async updateImage(formData){
        return  (await HTTP.post(`/upload`, formData,
                    { headers: {
                        'Content-Type': 'multipart/form-data'
                    } } )).data;
    },
    async deleteById(activityId){
        await HTTP.delete(`${resource}/${activityId}`);
    }
};