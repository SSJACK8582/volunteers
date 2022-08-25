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
        @click="handleEdit"
        type="primary"
        size="small"
        plain
        :disabled="multiple"
        >批量评分</el-button
      >
      <el-button
        @click="handleEdit"
        type="success"
        icon="el-icon-edit-outline"
        size="small"
        plain
        :disabled="single"
        >评分</el-button
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
      <el-table-column prop="realName" label="姓名" align="center" />
      <el-table-column prop="major" label="专业" align="center" />
      <el-table-column prop="score" label="评分" align="center" />
      <el-table-column prop="comment" label="评论" align="center" />
      <el-table-column fixed="right" label="操作" align="center">
        <template slot-scope="scope">
          <el-button
            @click="onEdit(scope.row)"
            type="text"
            icon="el-icon-edit-outline"
            size="small"
            :disabled="scope.row.score != null"
            >评分</el-button
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

    <el-dialog title="评分" :visible.sync="openScore" width="400px">
      <el-rate
        v-model="score"
        :texts="texts"
        :max="10"
        show-text
        style="margin-left: 50px"
      />
      <div slot="footer">
        <el-button type="danger" @click="openScore = false" size="small"
          >取 消</el-button
        >
        <el-button type="primary" @click="submitScore" size="small"
          >确 定</el-button
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
      score: 0,
      enrollIds: "",
      multipleSelection: [],
      tableData: [],
      texts: ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10"],
      searchForm: {},
      openScore: false,
      single: true,
      multiple: true,
    };
  },
  methods: {
    selectable(row) {
      if (row.score == null) return true;
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
    handleEdit() {
      this.openScore = true;
    },
    onEdit(val) {
      this.enrollIds = val.enrollId;
      this.openScore = true;
    },
    submitScore() {
      this.openScore = false;
      this.scoreEnroll().then(() => {
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
    getTableData() {
      return this.axios({
        method: "post",
        url: this.url + "/enroll/score/list",
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
    scoreEnroll() {
      return this.axios({
        method: "put",
        url: this.url + "/enroll/score/" + this.enrollIds,
        params: { score: this.score },
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
    this.getTableData();
  },
};
</script>

<style></style>
