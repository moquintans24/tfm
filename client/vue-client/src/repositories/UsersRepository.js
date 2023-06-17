import HTTP from "@/common/http";

const resource = "users";

export default {
  async getAll() {
    const response = await HTTP.get(resource);
    return response.data;
  },
  async getAllEditors(){
    return (await HTTP.get(`${resource}/editors`)).data;
  },
  async getById(userId){
    return (await HTTP.get(`${resource}/${userId}`)).data;
  },
  async getBySubscriptionId(subscriptionId){
    return (await HTTP.get(`${resource}/subscription/${subscriptionId}`)).data;
  },
  async getRouteImage(linkImage) {
    return (await HTTP.get(`${resource}/images/${linkImage}`)).data;
  },
  async updateUser(user, userId){
    return (await HTTP.put(`${resource}/profile/${userId}`,user)).data
  },
  async updateImage(formData){
    return  (await HTTP.post(`/upload`, formData,
                { headers: {
                    'Content-Type': 'multipart/form-data'
                } } )).data;
  },
  //Crear usuario nuevo
  async save(user) { 
    (await HTTP.post(`${resource}`, user));
  },
  async deleteById(id){
    await HTTP.delete(`${resource}/${id}`);
  },
  //registrar usuario
  async registerUser(register) { 
    (await HTTP.post(`/register`, register));
  }
};
