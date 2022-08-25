import axios from "axios";
import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    redirect: "/index",
  },
  {
    path: "/login",
    component: () => import("../views/login.vue"),
  },
  {
    path: "/index",
    component: () => import("../views/index.vue"),
    redirect: "/home",
    children: [
      {
        path: "/home",
        name: "首页",
        meta: {
          icon: "el-icon-s-home",
        },
        component: () => import("../views/home/index.vue"),
      },
      {
        path: "/user",
        name: "用户管理",
        meta: {
          icon: "el-icon-user",
        },
        component: () => import("../views/user/index.vue"),
      },
      {
        path: "/volunteer",
        name: "义工管理",
        meta: {
          icon: "el-icon-s-custom",
        },
        component: () => import("../views/volunteer/index.vue"),
      },
      {
        path: "/activity",
        name: "活动管理",
        meta: {
          icon: "el-icon-date",
        },
        component: () => import("../views/activity/index.vue"),
      },
      {
        path: "/enroll",
        name: "报名审核",
        meta: {
          icon: "el-icon-s-order",
        },
        component: () => import("../views/enroll/index.vue"),
      },
      {
        path: "/score",
        name: "评分评论",
        meta: {
          icon: "el-icon-star-off",
        },
        component: () => import("../views/score/index.vue"),
      },
      {
        path: "/notice",
        name: "通知公告",
        meta: {
          icon: "el-icon-s-comment",
        },
        component: () => import("../views/notice/index.vue"),
      },
      {
        path: "/permission",
        name: "权限管理",
        meta: {
          icon: "el-icon-setting",
        },
        component: () => import("../views/permission/index.vue"),
      },
      {
        path: "/userPerm",
        name: "用户权限",
        meta: {
          icon: "el-icon-s-tools",
        },
        component: () => import("../views/userPerm/index.vue"),
      },
      {
        path: "/dict",
        name: "字典管理",
        meta: {
          icon: "el-icon-notebook-2",
        },
        component: () => import("../views/dict/index.vue"),
      },
      {
        path: "/person",
        name: "个人中心",
        meta: {
          icon: "el-icon-menu",
        },
        component: () => import("../views/person/index.vue"),
      },
    ],
  },
];

const router = new VueRouter({
  routes,
});

router.beforeEach((to, from, next) => {
  var satoken = localStorage.getItem("satoken");
  var time = localStorage.getItem("time");
  var date = new Date();
  if (satoken && date.getTime() < time) {
    if (to.path == "/login") {
      next("/");
    }
    next();
  } else {
    if (to.path != "/login") {
      next("/login");
    }
    next();
  }
});

export default router;
