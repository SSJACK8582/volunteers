<template>
  <el-row :gutter="20">
    <el-col :span="8">
      <el-card align="middle">
        <div slot="header">
          <el-upload
            class="avatar"
            :action="url + '/upload'"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img
              class="avatar"
              v-if="userInfo.avatar"
              :src="url + '/image/' + userInfo.avatar"
            />
            <i v-else class="el-icon-plus avatar-icon"></i>
          </el-upload>
        </div>
        <el-descriptions :column="1" border>
          <el-descriptions-item>
            <template slot="label"> 用户名称 </template>
            {{ userInfo.userName }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"> 真实姓名 </template>
            {{ userInfo.realName }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"> 性别 </template>
            {{ sexDict[userInfo.sex] }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"> 用户类型 </template>
            {{ typeDict[userInfo.userType] }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"> 手机号码 </template>
            {{ userInfo.phoneNumber }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"> 邮箱号码 </template>
            {{ userInfo.email }}
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
    </el-col>
    <el-col :span="16">
      <el-tabs type="border-card">
        <el-tab-pane label="修改信息">
          <el-form :model="userInfo">
            <el-form-item label="用户名称" label-width="100px">
              <el-input v-model="userInfo.userName" disabled />
            </el-form-item>
            <el-form-item label="姓名" label-width="100px">
              <el-input v-model="userInfo.realName" />
            </el-form-item>
            <el-form-item label="性别" label-width="100px">
              <el-radio-group v-model="userInfo.sex">
                <template v-for="(value, key, index) of sexDict">
                  <el-radio :label="key" value="key">{{ value }}</el-radio>
                </template>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="电话" label-width="100px">
              <el-input v-model="userInfo.phoneNumber" />
            </el-form-item>
            <el-form-item label="邮箱" label-width="100px">
              <el-input v-model="userInfo.email" />
            </el-form-item>
          </el-form>
          <div align="center">
            <el-button type="primary" @click="editUser">保 存</el-button>
          </div>
        </el-tab-pane>
        <el-tab-pane label="修改密码">
          <el-form :model="data">
            <el-form-item label="旧密码" label-width="100px">
              <el-input v-model="data.oldPassword"  show-password/>
            </el-form-item>
            <el-form-item label="新密码" label-width="100px">
              <el-input v-model="data.newPassword"  show-password/>
            </el-form-item>
            <el-form-item label="重复密码" label-width="100px">
              <el-input v-model="data.rePassword"  show-password/>
            </el-form-item>
          </el-form>
          <div align="center">
            <el-button type="primary" @click="editPassword">保 存</el-button>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-col>
  </el-row>
</template>

<script>
export default {
  data() {
    return {
      sexDict: {},
      typeDict: {},
      userInfo: {},
      data: {},
    };
  },
  methods: {
    handleAvatarSuccess(val) {
      this.$set(this.userInfo, "avatar", val.data);
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      if (!isJPG) {
        this.$message.error("仅允许上传JPG格式图片");
      }
      return isJPG;
    },
    successMsg() {
      this.$message({
        message: "操作成功",
        type: "success",
        center: true,
      });
    },
    errorMsg() {
      this.$message({
        message: "操作失败",
        type: "error",
        center: true,
      });
    },
    editUser() {
      return this.axios({
        method: "put",
        url: this.url + "/user/update",
        data: this.userInfo,
      })
        .then((resp) => {
          if (resp.data.code == 200) {
            this.successMsg();
          } else {
            this.errorMsg();
          }
        })
        .catch(() => {
          this.errorMsg();
        });
    },
    editPassword() {
      return this.axios({
        method: "put",
        url: this.url + "/user/password",
        data: this.data,
      })
        .then((resp) => {
          if (resp.data.code == 200) {
            this.successMsg();
          } else {
            this.errorMsg();
          }
        })
        .catch(() => {
          this.errorMsg();
        });
    },
    getDict() {
      return this.axios({
        method: "post",
        url: this.url + "/dict",
        data: ["sex", "user_type"],
      }).then((resp) => {
        this.sexDict = resp.data.data.sex;
        this.typeDict = resp.data.data.user_type;
      });
    },
    getUserInfo() {
      return this.axios({
        method: "get",
        url: this.url + "/user/info",
      }).then((resp) => {
        this.userInfo = resp.data.data;
      });
    },
  },
  created() {
    this.getDict();
    this.getUserInfo();
  },
};
</script>

<style>
.avatar .el-upload {
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  position: relative;
  overflow: hidden;
  width: 80px;
  height: 80px;
}
.avatar .el-upload:hover {
  border-color: #409eff;
}
.avatar-icon {
  font-size: 20px;
  color: #8c939d;
  width: 80px;
  height: 80px;
  line-height: 80px;
  text-align: center;
}
.avatar {
  width: 80px;
  height: 80px;
  display: block;
}
</style>
