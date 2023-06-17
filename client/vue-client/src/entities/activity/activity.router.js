import AddActivity from "./AddActivity";

const routes = [
  {
    name: "AddActivity",
    path: "/books/edit/:bookId(\\d+)/:lessonId(\\d+)/activity/:activityId(\\d+)?",
    component: AddActivity,
    meta: { public: true }
  }
];

export default routes;