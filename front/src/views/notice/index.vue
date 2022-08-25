<template>
  <div>
    <el-form :inline="true" :model="searchForm" size="small">
      <el-form-item label="标题">
        <el-input v-model="searchForm.title" placeholder="标题" />
      </el-form-item>
      <el-form-item label="内容">
        <el-input v-model="searchForm.content" placeholder="内容" />
      </el-form-item>
      <el-form-item label="通知类型">
        <el-select v-model="searchForm.noticeType" placeholder="通知类型">
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
      <el-table-column prop="content" label="内容" align="center" />
      <el-table-column label="通知类型" align="center">
        <template slot-scope="scope">
          {{ typeDict[scope.row.noticeType] }}
        </template>
      </el-table-column>
      <el-table-column prop="noticeStatus" label="状态" align="center">
        <template slot-scope="scope">
          <el-tag>{{ statusDict[scope.row.noticeStatus] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" align="center">
        <template slot-scope="scope">
          <el-button
            @click="onEdit(scope.row)"
            type="text"
            icon="el-icon-edit-outline"
            size="small"
            :disabled="scope.row.noticeStatus != '0'"
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
        :page-sizes="[5, 10, 20, 30]"
        :page-size="pageSize"
        :current-page="currentPage"
        :total="total"
      >
      </el-pagination>
    </div>

    <el-dialog :title="title" :visible.sync="openForm">
      <el-form :model="dialogForm" :disabled="isEdit">
        <el-form-item label="标题" label-width="100px">
          <el-input v-model="dialogForm.title" />
        </el-form-item>
        <el-form-item label="内容" label-width="100px">
          <el-input
            :autosize="{ minRows: 4, maxRows: 4 }"
            type="textarea"
            placeholder="请输入内容"
            v-model="dialogForm.content"
          />
        </el-form-item>
        <el-form-item label="通知类型" label-width="100px">
          <el-select v-model="dialogForm.noticeType" placeholder="通知类型">
            <template v-for="(value, key, index) of typeDict">
              <el-option :label="value" :value="key" />
            </template>
          </el-select>
        </el-form-item>
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
      </el-form>
      <div slot="footer">
        <el-button @click="openForm = false" v-if="!isEdit">取 消</el-button>
        <el-button type="danger" @click="verifyNoticeDialog('1')" v-if="isEdit"
          >拒 绝</el-button
        >
        <el-button type="primary" @click="submitDialogForm" v-if="!isEdit"
          >确 定</el-button
        >
        <el-button type="primary" @click="verifyNoticeDialog('2')" v-if="isEdit"
          >通 过</el-button
        >
      </div>
    </el-dialog>
    <el-dialog title="导入通知" :visible.sync="openUpload" width="400px">
      <el-upload
        ref="upload"
        accept=".xlsx, .xls"
        :headers="headers"
        :action="url + '/notice/import'"
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
      noticeIds: "",
      multipleSelection: [],
      tableData: [],
      typeDict: {},
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
      this.noticeIds = val.map((item) => item.noticeId);
      this.single = val.length != 1 || val[0].noticeStatus != "0";
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
      this.title = "新增通知";
      this.dialogForm = {};
      this.isEdit = false;
      this.openForm = true;
    },
    handleEdit() {
      this.title = "审核通知";
      this.dialogForm = this.multipleSelection[0];
      this.noticeIds = this.multipleSelection[0].noticeId;
      this.isEdit = true;
      this.openForm = true;
    },
    handleDelete() {
      this.$confirm("是否确认删除通知数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return this.deleteNotice();
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
      this.title = "审核通知";
      this.dialogForm = val;
      this.noticeIds = val.noticeId;
      this.isEdit = true;
      this.openForm = true;
    },
    onDelete(val) {
      this.noticeIds = val.noticeId;
      this.handleDelete();
    },
    submitUpload() {
      this.$refs.upload.submit();
      this.openUpload = false;
    },
    submitDialogForm() {
      this.openForm = false;
      this.addNotice().then(() => {
        this.getTableData();
      });
    },
    verifyNoticeDialog(val) {
      this.openForm = false;
      this.verifyNotice(val).then(() => {
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
        data: ["notice_type", "notice_status"],
      }).then((resp) => {
        this.typeDict = resp.data.data.notice_type;
        this.statusDict = resp.data.data.notice_status;
      });
    },
    getTableData() {
      return this.axios({
        method: "post",
        url: this.url + "/notice/list",
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
    addNotice() {
      return this.axios({
        method: "post",
        url: this.url + "/notice/insert",
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
    verifyNotice(val) {
      return this.axios({
        method: "put",
        url: this.url + "/notice/verify/" + this.noticeIds,
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
    editNotice() {
      return this.axios({
        method: "put",
        url: this.url + "/notice",
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
    deleteNotice() {
      return this.axios({
        method: "delete",
        url: this.url + "/notice/" + this.noticeIds,
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
        url: this.url + "/notice/export",
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
