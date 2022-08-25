import Vue from "vue";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import axios from "axios";
import VueAxios from "vue-axios";
import App from "./App.vue";
import router from "./router";

axios.interceptors.request.use(
  (request) => {
    var satoken = window.localStorage.getItem("satoken");
    if (satoken) {
      request.headers.satoken = satoken;
    }
    return request;
  },
  (error) => {
    return Promise.reject(error);
  }
);

Vue.use(ElementUI);
Vue.use(VueAxios, axios);
Vue.config.productionTip = false;
Vue.prototype.url = "http://127.0.0.1:8000";
Vue.prototype.headers = { satoken: window.localStorage.getItem("satoken") };

new Vue({
  router,
  render: (h) => h(App),
}).$mount("#app");
