<template>
  <div>
    <el-form :inline="true" :model="searchForm" size="small">
      <el-form-item label="用户名称">
        <el-input v-model="searchForm.userName" placeholder="用户名称" />
      </el-form-item>
      <el-form-item label="用户类型">
        <el-select v-model="searchForm.userType" placeholder="用户类型">
          <template v-for="(value, key, index) of typeDict">
            <el-option :label="value" :value="key" />
          </template>
        </el-select>
      </el-form-item>
      <el-form-item label="权限名称">
        <el-input v-model="searchForm.permName" placeholder="权限名称" />
      </el-form-item>
      <el-form-item label="权限标识">
        <el-input v-model="searchForm.permCode" placeholder="权限标识" />
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
      <el-table-column prop="userName" label="用户名称" align="center" />
      <el-table-column prop="userType" label="用户类型" align="center">
        <template slot-scope="scope">
          <el-tag>{{ typeDict[scope.row.userType] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="permName" label="权限名称" align="center" />
      <el-table-column prop="permCode" label="权限标识" align="center" />
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
        :page-sizes="[10, 20, 30, 40]"
        :page-size="pageSize"
        :current-page="currentPage"
        :total="total"
      >
      </el-pagination>
    </div>

    <el-dialog :title="title" :visible.sync="openForm">
      <el-form :inline="true" :model="dialogForm">
        <el-form-item label="用户名称" label-width="100px">
          <el-input v-model="dialogForm.userName" />
        </el-form-item>
        <el-form-item label="权限标识" label-width="100px">
          <el-input v-model="dialogForm.permCode" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="openForm = false">取 消</el-button>
        <el-button type="primary" @click="submitDialogForm">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="导入权限" :visible.sync="openUpload" width="400px">
      <el-upload
        ref="upload"
        accept=".xlsx, .xls"
        :headers="headers"
        :action="url + '/userPerm/import'"
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
      pageSize: 10,
      currentPage: 1,
      total: 0,
      title: "",
      userPermIds: "",
      multipleSelection: [],
      tableData: [],
      typeDict: {},
      searchForm: {},
      dialogForm: {},
      openForm: false,
      openUpload: false,
      single: true,
      multiple: true,
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
      this.userPermIds = val.map((item) => item.userPermId);
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
      this.title = "新增权限";
      this.dialogForm = {};
      this.openForm = true;
    },
    handleEdit() {
      this.title = "修改权限";
      this.dialogForm = this.multipleSelection[0];
      this.openForm = true;
    },
    handleDelete() {
      this.$confirm("是否确认删除权限数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return this.deletePermission();
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
      this.title = "修改权限";
      this.dialogForm = val;
      this.openForm = true;
    },
    onDelete(val) {
      this.userPermIds = val.userPermId;
      this.handleDelete();
    },
    submitUpload() {
      this.$refs.upload.submit();
      this.openUpload = false;
    },
    submitDialogForm() {
      if (this.title == "新增权限") {
        this.openForm = false;
        this.addPermission().then(() => {
          this.getTableData();
        });
      } else if (this.title == "修改权限") {
        this.openForm = false;
        this.editPermission().then(() => {
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
        data: ["user_type"],
      }).then((resp) => {
        this.typeDict = resp.data.data.user_type;
      });
    },
    getTableData() {
      return this.axios({
        method: "post",
        url: this.url + "/userPerm/list",
        data: this.searchForm,
        params: { size: this.pageSize, current: this.currentPage },
      }).then((resp) => {
        this.tableData = resp.data.data.records;
        this.total = resp.data.data.total;
      });
    },
    addPermission() {
      return this.axios({
        method: "post",
        url: this.url + "/userPerm/insert",
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
    editPermission() {
      return this.axios({
        method: "put",
        url: this.url + "/userPerm",
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
    deletePermission() {
      return this.axios({
        method: "delete",
        url: this.url + "/userPerm/" + this.userPermIds,
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
        url: this.url + "/userPerm/export",
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
