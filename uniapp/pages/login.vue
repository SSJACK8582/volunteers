<template>
	<view style="padding-top: 20%;">
		<uni-card>
			<uni-forms ref="loginForm" :rules="rules" :modelValue="data" labelPosition="top">
				<uni-forms-item label="学号" required name="studentNo">
					<uni-easyinput v-model="data.studentNo" placeholder="请输入学号" />
				</uni-forms-item>
				<uni-forms-item label="密码" required name="password">
					<uni-easyinput type="password" v-model="data.password" placeholder="请输入密码" />
				</uni-forms-item>
			</uni-forms>
		</uni-card>
		<view style="display: flex;">
			<button style="width: 40%;" type="primary" size="mini" @click="submit('loginForm')">登录</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				data: {},
				rules: {
					studentNo: {
						rules: [{
								required: true,
								errorMessage: '学号不能为空'
							},
							{
								format: 'number',
								errorMessage: '学号只能为数字'
							}
						]
					},
					password: {
						rules: [{
							required: true,
							errorMessage: '密码不能为空'
						}]
					}
				}
			}
		},
		methods: {
			submit(ref) {
				this.$refs[ref].validate().then(res => {
					uni.request({
						method: 'POST',
						url: this.url + '/login/volunteer',
						data: this.data,
						success: resp => {
							if (resp.data.code == 200) {
								uni.setStorageSync('satoken', resp.data.data);
								uni.setStorageSync('time', new Date().getTime() + 86400000);
								uni.showToast({
									title: '登录成功',
									icon: 'none'
								});
								uni.switchTab({
									url: '/pages/index/index'
								});
							} else {
								uni.showToast({
									title: '学号或密码错误',
									icon: 'none'
								});
							}
						},
						fail: (resp) => {
							uni.showToast({
								title: '网络错误',
								icon: 'none'
							});
						}
					});
				}).catch(err => {});
			},
		}
	}
</script>

<style>
</style>
