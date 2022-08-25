<template>
  <div>
    <el-form :inline="true" :model="searchForm" size="small">
      <el-form-item label="字典名称">
        <el-input v-model="searchForm.dictName" placeholder="字典名称" />
      </el-form-item>
      <el-form-item label="字典类型">
        <el-input v-model="searchForm.dictType" placeholder="字典类型" />
      </el-form-item>
      <el-form-item label="关键字">
        <el-input v-model="searchForm.dictKey" placeholder="关键字" />
      </el-form-item>
      <el-form-item label="关键值">
        <el-input v-model="searchForm.dictValue" placeholder="关键值" />
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
      <el-table-column prop="dictName" label="字典名称" align="center" />
      <el-table-column prop="dictType" label="字典类型" align="center" />
      <el-table-column prop="dictKey" label="关键字" align="center" />
      <el-table-column prop="dictValue" label="关键值" align="center" />
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
        <el-form-item label="字典名称" label-width="100px">
          <el-input v-model="dialogForm.dictName" />
        </el-form-item>
        <el-form-item label="字典类型" label-width="100px">
          <el-input v-model="dialogForm.dictType" />
        </el-form-item>
        <el-form-item label="关键字" label-width="100px">
          <el-input v-model="dialogForm.dictKey" />
        </el-form-item>
        <el-form-item label="关键值" label-width="100px">
          <el-input v-model="dialogForm.dictValue" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="openForm = false">取 消</el-button>
        <el-button type="primary" @click="submitDialogForm">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="导入字典" :visible.sync="openUpload" width="400px">
      <el-upload
        ref="upload"
        accept=".xlsx, .xls"
        :headers="headers"
        :action="url + '/dict/import'"
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
      dictIds: "",
      multipleSelection: [],
      tableData: [],
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
      this.dictIds = val.map((item) => item.dictId);
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
      this.title = "新增字典";
      this.dialogForm = {};
      this.openForm = true;
    },
    handleEdit() {
      this.title = "修改字典";
      this.dialogForm = this.multipleSelection[0];
      this.openForm = true;
    },
    handleDelete() {
      this.$confirm("是否确认删除字典数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return this.deleteDict();
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
      this.title = "修改字典";
      this.dialogForm = val;
      this.openForm = true;
    },
    onDelete(val) {
      this.dictIds = val.dictId;
      this.handleDelete();
    },
    submitUpload() {
      this.$refs.upload.submit();
      this.openUpload = false;
    },
    submitDialogForm() {
      if (this.title == "新增字典") {
        this.openForm = false;
        this.addDict().then(() => {
          this.getTableData();
        });
      } else if (this.title == "修改字典") {
        this.openForm = false;
        this.editDict().then(() => {
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
    getTableData() {
      return this.axios({
        method: "post",
        url: this.url + "/dict/list",
        data: this.searchForm,
        params: { size: this.pageSize, current: this.currentPage },
      }).then((resp) => {
        this.tableData = resp.data.data.records;
        this.total = resp.data.data.total;
      });
    },
    addDict() {
      return this.axios({
        method: "post",
        url: this.url + "/dict/insert",
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
    editDict() {
      return this.axios({
        method: "put",
        url: this.url + "/dict",
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
    deleteDict() {
      return this.axios({
        method: "delete",
        url: this.url + "/dict/" + this.dictIds,
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
        url: this.url + "/dict/export",
        responseType: "blob",
      }).then((resp) => {
        FileSaver.saveAs(new Blob([resp.data]), "export.xlsx");
      });
    },
  },
  created() {
    this.getTableData();
  },
};
</script>

<style></style>
