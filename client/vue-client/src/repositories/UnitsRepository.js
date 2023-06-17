import HTTP from "@/common/http";

const resource = "units";

export default {
    async getByBookId(bookId){
        return (await HTTP.get(`${resource}/${bookId}`)).data;
    },
    async save(bookId, formData){
        return (await HTTP.post(`${resource}/${bookId}`, formData,
        { headers: {
            'Content-Type': 'multipart/form-data'
        } } )).data;
    },
    async deleteById(unitId){
        (await HTTP.delete(`${resource}/${unitId}`));
    }
}