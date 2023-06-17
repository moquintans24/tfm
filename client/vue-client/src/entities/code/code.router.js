const router = [
    {
        name: 'CodeList',
        path: '/books/:id(\\d+)/codes',
        component: () => import("./CodeList"),
        meta: { authority:"ADMIN" }
    }
]

export default router;