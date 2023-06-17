import BookList from "./BookList";
import AddBook from "./AddBook";
import BookDetail from "./BookDetail";
import EditBook from "./EditBook";

const routes = [
  {
    name: "BookList",
    path: "/books/:code?",
    component: BookList,
    meta: { public: false }
  },
  {
    name: "AddBook",
    path: "/books/new",
    component: AddBook,
    meta: { public: false }
  },
  {
    name: "BookDetail",
    path: "/books/detail/:id(\\d+)",
    component: BookDetail,
    meta: { public: false }
  },
  {
    name: "EditBook",
    path: "/books/edit/:id(\\d+)",
    component: EditBook,
    meta: { public: false }
  }
];

export default routes;
