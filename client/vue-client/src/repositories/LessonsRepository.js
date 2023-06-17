import HTTP from "@/common/http";

const resource = "lessons";

export default {
    async getById(lessonId){
        return (await HTTP.get(`${resource}/${lessonId}`)).data;   
    },
    async getByBookIdUnitId(bookId,unitId){
        return (await HTTP.get(`${resource}/${bookId}/${unitId}`)).data;
    },
    async getByUnitId(unitId){
        return (await HTTP.get(`${resource}/unit/${unitId}`)).data;
    },
    async save(unitId,formData){
        return (await HTTP.post(`${resource}/${unitId}`, formData,
        { headers: {
            'Content-Type': 'multipart/form-data'
        } } )).data;
    },
    async update(lesson){
        return (await HTTP.put(`${resource}/${lesson.id}`, lesson)).data;
    },
    async deleteById(lessonId){
        (await HTTP.delete(`${resource}/${lessonId}`));
    }
}