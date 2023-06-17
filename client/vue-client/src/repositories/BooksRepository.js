import HTTP from "@/common/http";

const resource = "books";


export default {
  async getAll() {
    const response = await HTTP.get(resource);
    return response.data;
  },

  async getById(id) {
    return (await HTTP.get(`${resource}/${id}`)).data;
  },

  async getStatistics(){
    return (await HTTP.get(`${resource}/statistics`)).data;
  },

  async getRouteImage(linkImage) {
    return (await HTTP.get(`${resource}/images/${linkImage}`)).data;
  },

  async delete(id) {
     await HTTP.delete(`${resource}/${id}`);
  },

  async save(formData){
    await HTTP.post(`${resource}`, formData,
    { headers: {
        'Content-Type': 'multipart/form-data'
    } } );
  },

  async duplicate(id){
    return (await HTTP.post(`${resource}/duplicate/${id}`)).data
  },
  async updateImage(formData){
    return  (await HTTP.post(`/upload`, formData,
                { headers: {
                    'Content-Type': 'multipart/form-data'
                } } )).data;
  },
  async update(book){
    return (await HTTP.put(`${resource}/${book.id}`, book)).data;
  }
};
