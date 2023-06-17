
const routes = [
  {
    name: "UserList",
    path: "/userList",
    component: () => import("./UserList"),
    meta: { authority: "ADMIN" }
  },
  {
    name: "UserProfile",
    path: "/userProfile/:id",
    component: () => import("./UserProfile"),
    meta: { public: true }
  },
  {
    name: "UserDetail",
    path: "/userList/userProfile/:id",
    component: () => import("./UserProfile"),
    meta: { public: true }
  }
];

export default routes;