<template>
  <div class="login">
    <el-card style="width: 500px" align="middle">
      <div slot="header">义工服务报名系统后台</div>
      <el-form ref="loginForm" :model="loginForm" :rules="rules">
        <el-form-item label="用户名称" prop="userName">
          <el-input v-model="loginForm.userName"></el-input>
        </el-form-item>
        <el-form-item label="用户密码" prop="password">
          <el-input v-model="loginForm.password" show-password></el-input>
        </el-form-item>
        <div style="width: 100%; margin-top: 40px">
          <el-button type="primary" @click="onSubmit" style="width: 100%"
            >登录</el-button
          >
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loginForm: {},
      rules: {
        userName: [
          { required: true, trigger: "blur", message: "请输入您的账号" },
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" },
        ],
      },
    };
  },
  methods: {
    onSubmit() {
      this.$refs["loginForm"].validate((valid) => {
        if (valid) {
          this.axios({
            method: "post",
            url: this.url + "/login/user",
            data: this.loginForm,
          })
            .then((resp) => {
              if (resp.data.code == 200) {
                window.localStorage.setItem("satoken", resp.data.data);
                window.localStorage.setItem('time', new Date().getTime() + 86400000);
                this.$router.push("/index");
                this.$message({
                  message: "登录成功",
                  type: "success",
                  center: true,
                });
              } else {
                this.$message({
                  message: "用户名称或密码错误",
                  type: "error",
                  center: true,
                });
              }
            })
            .catch(() => {
              this.$message({
                message: "网络异常",
                type: "error",
                center: true,
              });
            });
        }
      });
    },
  },
};
</script>

<style>
.login {
  background: linear-gradient(to right, #a0cfff, #f0f8ff);
  justify-content: center;
  align-items: center;
  position: fixed;
  display: flex;
  height: 100%;
  width: 100%;
  top: 0;
  left: 0;
}
</style>
