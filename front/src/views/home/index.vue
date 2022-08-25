<template>
  <el-row :gutter="24">
    <el-col :span="12">
      <el-card align="middle">
        <div slot="header">
          <span>活动场数</span>
        </div>
        <el-descriptions :column="4" direction="vertical" border>
          <el-descriptions-item label="等待审核">
            <span style="font-size: 20px">{{ activityData.wait }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="审核失败">
            <span style="font-size: 20px">{{ activityData.fail }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="审核通过">
            <span style="font-size: 20px">{{ activityData.pass }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="总活动数">
            <span style="font-size: 20px">{{ activityData.total }}</span>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card align="middle">
        <div slot="header">
          <span>活动人数</span>
        </div>
        <el-descriptions :column="2" direction="vertical" border>
          <el-descriptions-item label="报名">
            <span style="font-size: 20px">{{ enrollData.enroll }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="参加">
            <span style="font-size: 20px">{{ enrollData.join }}</span>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card align="middle">
        <div slot="header">
          <span>义工人数</span>
        </div>
        <el-descriptions :column="2" direction="vertical" border>
          <el-descriptions-item label="男">
            <span style="font-size: 20px">{{ volunteerData.man }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="女">
            <span style="font-size: 20px">{{ volunteerData.woman }}</span>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
export default {
  data() {
    return {
      activityData: {},
      enrollData: {},
      volunteerData: {},
    };
  },
  methods: {
    getActivityData() {
      return this.axios({
        method: "get",
        url: this.url + "/data/activity",
      }).then((resp) => {
        this.activityData = resp.data.data;
      });
    },
    getEnrollData() {
      return this.axios({
        method: "get",
        url: this.url + "/data/enroll",
      }).then((resp) => {
        this.enrollData = resp.data.data;
      });
    },
    getVolunteerData() {
      return this.axios({
        method: "get",
        url: this.url + "/data/volunteer",
      }).then((resp) => {
        this.volunteerData = resp.data.data;
      });
    },
  },
  created() {
    this.getActivityData();
    this.getEnrollData();
    this.getVolunteerData();
  },
};
</script>

<style>
.el-descriptions__body .el-descriptions__table .el-descriptions-item__cell {
  text-align: center;
}
</style>
