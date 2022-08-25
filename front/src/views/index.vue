<template>
  <div class="container">
    <el-container style="height: 100%">
      <NavMenu />
      <el-container>
        <el-header>
          <NavBar />
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import NavMenu from "../components/NavMenu.vue";
import NavBar from "../components/NavBar.vue";
export default {
  data() {
    return {};
  },
  components: {
    NavMenu,
    NavBar,
  },
};
</script>

<style>
.container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.el-header {
  box-shadow: 0px 1px 2px #eee;
  color: #333;
  width: 100%;
  line-height: 60px;
}

.el-main {
  color: #333;
  width: 100%;
}
</style>
