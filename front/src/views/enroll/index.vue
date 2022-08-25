<template>
  <div>
    <el-form :inline="true" :model="searchForm" size="small">
      <el-form-item label="标题">
        <el-input v-model="searchForm.title" placeholder="标题" />
      </el-form-item>
      <el-form-item label="要求">
        <el-input v-model="searchForm.demand" placeholder="要求" />
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="searchForm.realName" placeholder="姓名" />
      </el-form-item>
      <el-form-item label="专业">
        <el-input v-model="searchForm.major" placeholder="专业" />
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
        @click="handlePass"
        type="primary"
        size="small"
        plain
        :disabled="multiple"
        >批量通过</el-button
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
        @click="handleRefuse"
        type="info"
        size="small"
        plain
        :disabled="multiple"
        >批量拒绝</el-button
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
      <el-table-column
        type="selection"
        align="center"
        :selectable="selectable"
      />
      <el-table-column prop="title" label="标题" align="center" />
      <el-table-column prop="demand" label="要求" align="center" />
      <el-table-column prop="peopleNum" label="人数" align="center" />
      <el-table-column prop="realName" label="姓名" align="center" />
      <el-table-column prop="major" label="专业" align="center" />
      <el-table-column prop="enrollStatus" label="状态" align="center">
        <template slot-scope="scope">
          <el-tag>{{ statusDict[scope.row.enrollStatus] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" align="center">
        <template slot-scope="scope">
          <el-button
            @click="onEdit(scope.row)"
            type="text"
            icon="el-icon-edit-outline"
            size="small"
            :disabled="scope.row.enrollStatus != '0'"
            >审核</el-button
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

    <el-dialog title="审核报名" :visible.sync="openForm">
      <el-form :model="dialogForm" disabled>
        <el-form-item label="标题" label-width="100px">
          <el-input v-model="dialogForm.title" />
        </el-form-item>
        <el-form-item label="要求" label-width="100px">
          <el-input v-model="dialogForm.demand" />
        </el-form-item>
        <el-form-item label="人数" label-width="100px">
          <el-input v-model="dialogForm.peopleNum" />
        </el-form-item>
        <el-form-item label="姓名" label-width="100px">
          <el-input v-model="dialogForm.realName" />
        </el-form-item>
        <el-form-item label="专业" label-width="100px">
          <el-input v-model="dialogForm.major" />
        </el-form-item>
        <el-form-item label="简介" label-width="100px">
          <el-input v-model="dialogForm.introduction" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="danger" @click="verifyEnrollDialog('1')"
          >拒 绝</el-button
        >
        <el-button type="primary" @click="verifyEnrollDialog('2')"
          >通 过</el-button
        >
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
      enrollIds: "",
      multipleSelection: [],
      tableData: [],
      statusDict: {},
      searchForm: {},
      dialogForm: {},
      openForm: false,
      single: true,
      multiple: true,
    };
  },
  methods: {
    selectable(row) {
      if (row.enrollStatus == "0") return true;
      else return false;
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
      this.enrollIds = val.map((item) => item.enrollId);
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
    handlePass() {
      this.$confirm("是否确认审核通过报名数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return this.verifyEnroll("2");
        })
        .then(() => {
          this.getTableData();
        })
        .catch(() => {});
    },
    handleEdit() {
      this.dialogForm = this.multipleSelection[0];
      this.enrollIds = this.multipleSelection[0].enrollId;
      this.openForm = true;
    },
    handleDelete() {
      this.$confirm("是否确认删除报名数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return this.deleteEnroll();
        })
        .then(() => {
          this.getTableData();
        })
        .catch(() => {});
    },
    handleRefuse() {
      this.$confirm("是否确认拒绝审核报名数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return this.verifyEnroll("1");
        })
        .then(() => {
          this.getTableData();
        })
        .catch(() => {});
    },
    onEdit(val) {
      this.dialogForm = val;
      this.enrollIds = val.enrollId;
      this.openForm = true;
    },
    onDelete(val) {
      this.enrollIds = val.enrollId;
      this.handleDelete();
    },
    verifyEnrollDialog(val) {
      this.openForm = false;
      this.verifyEnroll(val).then(() => {
        this.getTableData();
      });
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
        data: ["enroll_status"],
      }).then((resp) => {
        this.statusDict = resp.data.data.enroll_status;
      });
    },
    getTableData() {
      return this.axios({
        method: "post",
        url: this.url + "/enroll/list",
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
    verifyEnroll(val) {
      return this.axios({
        method: "put",
        url: this.url + "/enroll/verify/" + this.enrollIds,
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
    editEnroll() {
      return this.axios({
        method: "put",
        url: this.url + "/enroll",
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
    deleteEnroll() {
      return this.axios({
        method: "delete",
        url: this.url + "/enroll/" + this.enrollIds,
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
        url: this.url + "/enroll/export",
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
