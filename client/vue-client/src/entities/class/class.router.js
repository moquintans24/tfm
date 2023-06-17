import ClassList from "./ClassList";
import ClassDetail from "./ClassDetail";
import ClassDetailStudent from "./ClassDetailStudent";
import ActivityListByUser from "../activity/ActivityListByUser";
import ActivityList from "../activity/ActivityList";
import ClassUserList from "./ClassUserList";
import ClassUserRatings from "./ClassUserRatings";
import BookDetail from "../book/BookDetail";

const routes = [
  {
    name: "ClassList",
    path: "/classes",
    component: ClassList,
    meta: { public: false }
  },
  {
    name: "ClassDetail",
    // path: "/classes/:id(\\d+)/book",
    path: "/classes/:id(\\d+)/activities",
    component: ClassDetail,
    children: [
      {
        path: "/classes/:id(\\d+)/activities",
        component: ActivityList
      },
      {
        path: "/classes/:id(\\d+)/students",
        component: ClassUserList
      },
      {
        path: "/classes/:id(\\d+)/ratings",
        component: ClassUserRatings
      },
      {
        path: "/classes/:id(\\d+)/book",
        component: BookDetail
      }
    ],
    meta: { public: false }
  },
  {
    name: "ClassDetailStudent",
    path: "/classes/:id(\\d+)/student/:subscriptionId(\\d+)/pending-activities",
    component: ClassDetailStudent,
    children: [
      {
        path: "/classes/:id(\\d+)/student/:subscriptionId(\\d+)/activities",
        component: ActivityListByUser
      }
    ],
    meta: { public: false }
  }
];

export default routes;