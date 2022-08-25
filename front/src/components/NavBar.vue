<template>
  <div class="navbar">
    <div style="float: left">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item v-for="(item, index) of breadList" :key="index">
          <strong>{{ item.name }}</strong>
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div style="float: right; display: flex">
      <el-avatar
        style="margin: 10px 0"
        shape="square"
        size="large"
        :src="url + '/image/' + userInfo.avatar"
        v-if="userInfo.avatar"
      />
      <span style="padding: 0 10px">
        {{ userInfo.userName }}
      </span>
      <el-button @click="onLogout" type="text">退出</el-button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      breadList: [],
      userInfo: {},
    };
  },
  methods: {
    getBreadList() {
      var matched = this.$route.matched.filter((item) => item.name);
      this.breadList = matched;
    },
    onLogout() {
      this.$confirm("是否确认退出系统?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.logout();
          window.localStorage.removeItem("satoken");
          this.$router.push("/login");
        })
        .catch(() => {});
    },
    getUserInfo() {
      return this.axios({
        method: "get",
        url: this.url + "/user/info",
      }).then((resp) => {
        this.userInfo = resp.data.data;
      });
    },
    logout() {
      return this.axios({
        method: "get",
        url: this.url + "/user/logout",
      });
    },
  },
  created() {
    this.getBreadList();
    this.getUserInfo();
  },
  watch: {
    $route() {
      this.getBreadList();
    },
  },
};
</script>

<style>
.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
}
</style>
