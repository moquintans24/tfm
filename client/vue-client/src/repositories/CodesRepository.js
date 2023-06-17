import HTTP from "@/common/http";

const resource = "codes";


export default {
    async getAll() {
        const response = await HTTP.get(resource);
        return response.data;
    },

    async getByBookId(id) {
        return (await HTTP.get(`${resource}/${id}`)).data;
    },

    async create(bookId, formData) {
        return (await HTTP.post(`${resource}/${bookId}`,formData,{
            headers: {
                'Content-Type': 'multipart/form-data'
            }})).data;
    },

    async getByCreationDate(bookId, creationDate1, creationDate2){
        return (await HTTP.get(`${resource}/${bookId}/exportCodes`,{
                params: {
                    creationDateA: creationDate1,
                    creationDateB: creationDate2
                }}
        )).data;
    }
};