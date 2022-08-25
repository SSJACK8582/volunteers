<template>
  <div>
    <el-form :inline="true" :model="searchForm" size="small">
      <el-form-item label="学号">
        <el-input v-model="searchForm.studentNo" placeholder="学号" />
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="searchForm.realName" placeholder="姓名" />
      </el-form-item>
      <el-form-item label="专业">
        <el-input v-model="searchForm.major" placeholder="专业" />
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="searchForm.phoneNumber" placeholder="电话" />
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
        >修改</el-button
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
      <el-table-column prop="studentNo" label="学号" align="center" />
      <el-table-column prop="realName" label="姓名" align="center" />
      <el-table-column label="性别" align="center">
        <template slot-scope="scope">
          {{ sexDict[scope.row.sex] }}
        </template>
      </el-table-column>
      <el-table-column prop="age" label="年龄" align="center" />
      <el-table-column prop="major" label="专业" align="center" />
      <el-table-column prop="grade" label="班级" align="center" />
      <el-table-column prop="phoneNumber" label="电话" align="center" />
      <el-table-column prop="email" label="邮箱" align="center" />
      <el-table-column prop="workTime" label="时长" align="center" />
      <el-table-column prop="score" label="评分" align="center" />
      <el-table-column fixed="right" label="操作" align="center">
        <template slot-scope="scope">
          <el-button
            @click="onEdit(scope.row)"
            type="text"
            icon="el-icon-edit-outline"
            size="small"
            >修改</el-button
          >
          <el-button
            @click="onDelete(scope.row)"
            type="text"
            icon="el-icon-delete"
            size="small"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <div style="float: right; padding-bottom: 2px">
      <el-pagination
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :page-sizes="[5, 10, 20, 30]"
        :page-size="pageSize"
        :current-page="currentPage"
        :total="total"
      >
      </el-pagination>
    </div>

    <el-dialog :title="title" :visible.sync="openForm">
      <el-form :inline="true" :model="dialogForm">
        <el-form-item label="学号" label-width="100px">
          <el-input v-model="dialogForm.studentNo" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="密码" label-width="100px">
          <el-input v-model="dialogForm.password" v-if="!isEdit" />
          <el-button
            @click="resetPassword"
            type="primary"
            icon="el-icon-refresh"
            size="small"
            v-if="isEdit"
            >重置</el-button
          >
        </el-form-item>
        <el-form-item label="姓名" label-width="100px">
          <el-input v-model="dialogForm.realName" />
        </el-form-item>
        <el-form-item label="性别" label-width="100px">
          <el-radio-group v-model="dialogForm.sex">
            <template v-for="(value, key, index) of sexDict">
              <el-radio :label="key" :value="key">{{ value }}</el-radio>
            </template>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄" label-width="100px">
          <el-input v-model="dialogForm.age" />
        </el-form-item>
        <el-form-item label="专业" label-width="100px">
          <el-input v-model="dialogForm.major" />
        </el-form-item>
        <el-form-item label="班级" label-width="100px">
          <el-input v-model="dialogForm.grade" />
        </el-form-item>
        <el-form-item label="状态" label-width="100px">
          <el-radio-group v-model="dialogForm.volStatus">
            <template v-for="(value, key, index) of statusDict">
              <el-radio :label="key" :value="key">{{ value }}</el-radio>
            </template>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="电话" label-width="100px">
          <el-input v-model="dialogForm.phoneNumber" />
        </el-form-item>
        <el-form-item label="邮箱" label-width="100px">
          <el-input v-model="dialogForm.email" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="openForm = false">取 消</el-button>
        <el-button type="primary" @click="submitDialogForm">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="导入用户" :visible.sync="openUpload" width="400px">
      <el-upload
        ref="upload"
        accept=".xlsx, .xls"
        :headers="headers"
        :action="url + '/volunteer/import'"
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
  </div>
</template>

<script>
import FileSaver from "@/FileSaver";
export default {
  data() {
    return {
      pageSize: 5,
      currentPage: 1,
      total: 0,
      title: "",
      volunteerIds: "",
      multipleSelection: [],
      tableData: [],
      sexDict: {},
      statusDict: {},
      searchForm: {},
      dialogForm: {},
      openForm: false,
      openUpload: false,
      single: true,
      multiple: true,
      isEdit: true,
    };
  },
  methods: {
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
      this.volunteerIds = val.map((item) => item.volunteerId);
      this.single = val.length != 1;
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
      this.title = "新增义工";
      this.dialogForm = {};
      this.isEdit = false;
      this.openForm = true;
    },
    handleEdit() {
      this.title = "修改义工";
      this.dialogForm = this.multipleSelection[0];
      this.isEdit = true;
      this.openForm = true;
    },
    handleDelete() {
      this.$confirm("是否确认删除义工数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return this.deleteVolunteer();
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
      this.title = "修改义工";
      this.dialogForm = val;
      this.isEdit = true;
      this.openForm = true;
    },
    onDelete(val) {
      this.volunteerIds = val.volunteerId;
      this.handleDelete();
    },
    submitUpload() {
      this.$refs.upload.submit();
      this.openUpload = false;
    },
    submitDialogForm() {
      if (!this.isEdit) {
        this.openForm = false;
        this.addVolunteer().then(() => {
          this.getTableData();
        });
      } else if (this.isEdit) {
        this.openForm = false;
        this.editVolunteer().then(() => {
          this.getTableData();
        });
      }
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
        data: ["sex", "vol_status"],
      }).then((resp) => {
        this.sexDict = resp.data.data.sex;
        this.statusDict = resp.data.data.vol_status;
      });
    },
    getTableData() {
      return this.axios({
        method: "post",
        url: this.url + "/volunteer/list",
        data: this.searchForm,
        params: { size: this.pageSize, current: this.currentPage },
      }).then((resp) => {
        this.tableData = resp.data.data.records;
        this.total = resp.data.data.total;
      });
    },
    addVolunteer() {
      return this.axios({
        method: "post",
        url: this.url + "/volunteer/insert",
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
    editVolunteer() {
      return this.axios({
        method: "put",
        url: this.url + "/volunteer",
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
    deleteVolunteer() {
      return this.axios({
        method: "delete",
        url: this.url + "/volunteer/" + this.volunteerIds,
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
    resetPassword() {
      return this.axios({
        method: "put",
        url: this.url + "/volunteer/password/" + this.dialogForm.volunteerId,
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
        url: this.url + "/volunteer/export",
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

<style></style>
