import Vue from 'vue'
import VueRouter from 'vue-router'
import CodeA from "../views/CodeA";
import Home from "../views/Home";

Vue.use(VueRouter)


const routes = [
    {
        path: "/",
        component: Home,
        children:[
            {
                path: "common/a",
                component: CodeA
            }
        ]
    },


];
console.log(routes)

const router = new VueRouter({
    routes
})

export default router
