<template>
  <el-menu
    :default-active="$route.path"
    :collapse="isCollapse"
    :collapse-transition="false"
    class="el-menu-vertical"
    background-color="#304156"
    text-color="#fff"
    active-text-color="#409eff"
    router
  >
    <el-menu-item @click="isCollapse = !isCollapse" v-if="!isCollapse">
      <i class="el-icon-s-fold"></i>
      <span slot="title">义工服务报名系统</span>
    </el-menu-item>
    <el-menu-item @click="isCollapse = !isCollapse" v-if="isCollapse">
      <i class="el-icon-s-unfold"></i>
    </el-menu-item>
    <template v-for="(item, index) in $router.options.routes">
      <el-menu-item
        v-for="(children, index) in item.children"
        :index="children.path"
        :key="index"
      >
        <i :class="children.meta.icon"></i>
        <span slot="title">{{ children.name }}</span>
      </el-menu-item>
    </template>
  </el-menu>
</template>

<script>
export default {
  data() {
    return {
      isCollapse: false,
    };
  },
};
</script>

<style>
.el-menu-vertical:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
</style>
