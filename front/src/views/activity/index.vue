<template>
  <div>
    <el-form :inline="true" :model="searchForm" size="small">
      <el-form-item label="标题">
        <el-input v-model="searchForm.title" placeholder="标题" />
      </el-form-item>
      <el-form-item label="要求">
        <el-input v-model="searchForm.demand" placeholder="要求" />
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="searchForm.address" placeholder="地址" />
      </el-form-item>
      <el-form-item label="活动类型">
        <el-select v-model="searchForm.actType" placeholder="活动类型">
          <template v-for="(value, key, index) of typeDict">
            <el-option :label="value" :value="key" />
          </template>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          @click="onSubmit"
          type="primary"
          icon="el-icon-search"
          size="small"
          >查询</el-button
        >
        <el-button
          @click="onReset"
          type="primary"
          icon="el-icon-refresh"
          size="small"
          >重置</el-button
        >
      </el-form-item>
    </el-form>
    <el-row style="margin-bottom: 5px">
      <el-button
        @click="handleAdd"
        type="primary"
        icon="el-icon-plus"
        size="small"
        plain
        >新增</el-button
      >
      <el-button
        @click="handleEdit"
        type="success"
        icon="el-icon-edit-outline"
        size="small"
        plain
        :disabled="single"
        >审核</el-button
      >
      <el-button
        @click="handleDelete"
        type="danger"
        icon="el-icon-delete"
        size="small"
        plain
        :disabled="multiple"
        >删除</el-button
      >
      <el-button
        @click="openUpload = true"
        type="info"
        icon="el-icon-upload2"
        size="small"
        plain
        >导入</el-button
      >
      <el-button
        @click="handleExport"
        type="warning"
        icon="el-icon-download"
        size="small"
        plain
        >导出</el-button
      >
    </el-row>
    <el-table
      ref="multipleTable"
      tooltip-effect="dark"
      style="width: 100%"
      :data="tableData"
      :header-cell-style="{ background: '#fafafa' }"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" align="center" />
      <el-table-column prop="title" label="标题" align="center" />
      <el-table-column prop="demand" label="要求" align="center" />
      <el-table-column prop="content" label="内容" align="center" />
      <el-table-column prop="address" label="地址" align="center" />
      <el-table-column prop="peopleNum" label="人数" align="center" />
      <el-table-column label="活动类型" align="center">
        <template slot-scope="scope">
          {{ typeDict[scope.row.actType] }}
        </template>
      </el-table-column>
      <el-table-column prop="actStatus" label="状态" align="center">
        <template slot-scope="scope">
          <el-tag>{{ statusDict[scope.row.actStatus] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" align="center">
        <template slot-scope="scope">
          <el-button
            @click="onEdit(scope.row)"
            type="text"
            icon="el-icon-edit-outline"
            size="small"
            :disabled="scope.row.actStatus != '0'"
            >审核</el-button
          >
          <el-button
            @click="createQrCode(scope.row)"
            type="text"
            icon="el-icon-document-checked"
            size="small"
            :disabled="scope.row.actStatus < '3'"
            >签到</el-button
          >
          <!-- <el-button
            @click="onDelete(scope.row)"
            type="text"
            icon="el-icon-delete"
            size="small"
            >删除</el-button
          > -->
        </template>
      </el-table-column>
    </el-table>
    <div style="float: right; padding-bottom: 2px">
      <el-pagination
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="pageSize"
        :current-page="currentPage"
        :total="total"
      >
      </el-pagination>
    </div>

    <el-dialog :title="title" :visible.sync="openForm">
      <el-form :inline="true" :model="dialogForm" :disabled="isEdit">
        <el-form-item label="标题" label-width="100px">
          <el-input v-model="dialogForm.title" />
        </el-form-item>
        <el-form-item label="要求" label-width="100px">
          <el-input v-model="dialogForm.demand"> </el-input>
        </el-form-item>
        <el-form-item label="内容" label-width="100px">
          <el-input v-model="dialogForm.content"> </el-input>
        </el-form-item>
        <el-form-item label="地址" label-width="100px">
          <el-input v-model="dialogForm.address" />
        </el-form-item>
        <el-form-item label="活动类型" label-width="100px">
          <el-select v-model="dialogForm.actType" placeholder="活动类型">
            <template v-for="(value, key, index) of typeDict">
              <el-option :label="value" :value="key" />
            </template>
          </el-select>
        </el-form-item>
        <el-form-item label="人数" label-width="85px">
          <el-input-number v-model="dialogForm.peopleNum" :min="1" />
        </el-form-item>
        <el-form-item label="报名时间" label-width="100px">
          <el-date-picker
            v-model="enrollTime"
            type="datetimerange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :default-time="['08:00:00', '20:00:00']"
            @change="handleEnrollTime"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="活动时间" label-width="100px">
          <el-date-picker
            v-model="actTime"
            type="datetimerange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :default-time="['08:00:00', '20:00:00']"
            @change="handleActTime"
          >
          </el-date-picker>
        </el-form-item>
        <el-row>
          <el-form-item label="封面" label-width="100px">
            <el-upload
              class="avatar"
              :action="url + '/upload'"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
            >
              <img
                class="avatar"
                v-if="dialogForm.cover"
                :src="url + '/image/' + dialogForm.cover"
              />
              <i v-else class="el-icon-plus avatar-icon"></i>
            </el-upload>
          </el-form-item>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="openForm = false" v-if="!isEdit">取 消</el-button>
        <el-button
          type="danger"
          @click="verifyActivityDialog('1')"
          v-if="isEdit"
          >拒 绝</el-button
        >
        <el-button type="primary" @click="submitDialogForm" v-if="!isEdit"
          >确 定</el-button
        >
        <el-button
          type="primary"
          @click="verifyActivityDialog('2')"
          v-if="isEdit"
          >通 过</el-button
        >
      </div>
    </el-dialog>
    <el-dialog title="导入活动" :visible.sync="openUpload" width="400px">
      <el-upload
        ref="upload"
        accept=".xlsx, .xls"
        :headers="headers"
        :action="url + '/activity/import'"
        :auto-upload="false"
        :limit="1"
        :on-success="handleSuccess"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">拖入或点击上传文件</div>
        <div class="el-upload__tip" slot="tip">仅允许导入xls/xlsx格式文件</div>
      </el-upload>
      <div slot="footer">
        <el-button @click="openUpload = false">取 消</el-button>
        <el-button type="primary" @click="submitUpload">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="活动签到"
      width="440px"
      :visible.sync="openQrCode"
      :before-close="closeQrCode"
    >
      <div id="qrCode" ref="qrCode" />
    </el-dialog>
  </div>
</template>

<script>
import FileSaver from "@/FileSaver";
import QRCode from "qrcodejs2";
export default {
  data() {
    return {
      pageSize: 10,
      currentPage: 1,
      total: 0,
      title: "",
      activityIds: "",
      qrCode: "",
      multipleSelection: [],
      tableData: [],
      typeDict: {},
      statusDict: {},
      searchForm: {},
      dialogForm: {},
      openForm: false,
      openUpload: false,
      openQrCode: false,
      single: true,
      multiple: true,
      isEdit: true,
      enrollTime: [],
      actTime: [],
    };
  },
  methods: {
    handleEnrollTime() {
      this.dialogForm.enrollStartTime = this.enrollTime[0];
      this.dialogForm.enrollEndTime = this.enrollTime[1];
    },
    handleActTime() {
      this.dialogForm.actStartTime = this.actTime[0];
      this.dialogForm.actEndTime = this.actTime[1];
    },
    handleAvatarSuccess(val) {
      this.$set(this.dialogForm, "cover", val.data);
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      if (!isJPG) {
        this.$message.error("仅允许上传JPG格式图片");
      }
      return isJPG;
    },
    onSubmit() {
      this.currentPage = 1;
      this.getTableData();
    },
    onReset() {
      this.searchForm = {};
      this.currentPage = 1;
      this.getTableData();
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.activityIds = val.map((item) => item.activityId);
      this.single = val.length != 1 || val[0].actStatus != "0";
      this.multiple = !val.length;
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getTableData();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getTableData();
    },
    handleAdd() {
      this.title = "新增活动";
      this.dialogForm = {};
      this.enrollTime = [];
      this.actTime = [];
      this.isEdit = false;
      this.openForm = true;
    },
    handleEdit() {
      this.title = "审核活动";
      this.dialogForm = this.multipleSelection[0];
      this.activityIds = this.multipleSelection[0].activityId;
      this.$set(this.enrollTime, 0, this.multipleSelection[0].enrollStartTime);
      this.$set(this.enrollTime, 1, this.multipleSelection[0].enrollEndTime);
      this.$set(this.actTime, 0, this.multipleSelection[0].actStartTime);
      this.$set(this.actTime, 1, this.multipleSelection[0].actStartTime);
      this.isEdit = true;
      this.openForm = true;
    },
    handleDelete() {
      this.$confirm("是否确认删除活动数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return this.deleteActivity();
        })
        .then(() => {
          this.getTableData();
        })
        .catch(() => {});
    },
    handleSuccess(res) {
      if (res.code == 200) {
        this.successMsg();
      } else {
        this.errorMsg();
      }
    },
    onEdit(val) {
      this.title = "审核活动";
      this.dialogForm = val;
      this.activityIds = val.activityId;
      this.$set(this.enrollTime, 0, val.enrollStartTime);
      this.$set(this.enrollTime, 1, val.enrollEndTime);
      this.$set(this.actTime, 0, val.actStartTime);
      this.$set(this.actTime, 1, val.actStartTime);
      this.isEdit = true;
      this.openForm = true;
    },
    onDelete(val) {
      this.activityIds = val.activityId;
      this.handleDelete();
    },
    submitUpload() {
      this.$refs.upload.submit();
      this.openUpload = false;
    },
    submitDialogForm() {
      this.openForm = false;
      this.addActivity().then(() => {
        this.getTableData();
      });
    },
    verifyActivityDialog(val) {
      this.openForm = false;
      this.verifyActivity(val).then(() => {
        this.getTableData();
      });
    },
    createQrCode(val) {
      this.openQrCode = true;
      var text = this.url + "/enroll/sign/" + val.activityId;
      this.$nextTick(() => {
        this.qrCode = new QRCode("qrCode", {
          text: text,
          width: 400,
          height: 400,
        });
      });
    },
    closeQrCode() {
      this.openQrCode = false;
      this.qrCode = "";
      document.getElementById("qrCode").innerHTML = "";
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
    getDict() {
      return this.axios({
        method: "post",
        url: this.url + "/dict",
        data: ["act_type", "act_status"],
      }).then((resp) => {
        this.typeDict = resp.data.data.act_type;
        this.statusDict = resp.data.data.act_status;
      });
    },
    getTableData() {
      return this.axios({
        method: "post",
        url: this.url + "/activity/list",
        data: this.searchForm,
        params: {
          size: this.pageSize,
          current: this.currentPage,
          column: "create_time",
          asc: "false",
        },
      }).then((resp) => {
        this.tableData = resp.data.data.records;
        this.total = resp.data.data.total;
      });
    },
    addActivity() {
      return this.axios({
        method: "post",
        url: this.url + "/activity/insert",
        data: this.dialogForm,
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
    verifyActivity(val) {
      return this.axios({
        method: "put",
        url: this.url + "/activity/verify/" + this.activityIds,
        params: { status: val },
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
    editActivity() {
      return this.axios({
        method: "put",
        url: this.url + "/activity",
        data: this.dialogForm,
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
    deleteActivity() {
      return this.axios({
        method: "delete",
        url: this.url + "/activity/" + this.activityIds,
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
    handleExport() {
      return this.axios({
        method: "get",
        url: this.url + "/activity/export",
        responseType: "blob",
      }).then((resp) => {
        FileSaver.saveAs(new Blob([resp.data]), "export.xlsx");
      });
    },
  },
  created() {
    this.getDict();
    this.getTableData();
  },
};
</script>

<style>
.avatar .el-upload {
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  position: relative;
  overflow: hidden;
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
