<template>
	<view>
		<uni-card>
			<uni-forms ref="infoForm" :rules="rules" :modelValue="data">
				<uni-forms-item label="旧密码" required name="oldPassword">
					<uni-easyinput type="password" v-model="data.oldPassword" placeholder="请输入旧密码" />
				</uni-forms-item>
				<uni-forms-item label="新密码" required name="newPassword">
					<uni-easyinput type="password" v-model="data.newPassword" placeholder="请输入新密码" />
				</uni-forms-item>
				<uni-forms-item label="重复密码" required name="rePassword">
					<uni-easyinput type="password" v-model="data.rePassword" placeholder="请输入新密码" />
				</uni-forms-item>
			</uni-forms>
		</uni-card>
		<view style="display: flex;">
			<button style="width: 40%;" type="primary" size="mini" @click="submit('infoForm')">保存</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				rules: {
					oldPassword: {
						rules: [{
							required: true,
							errorMessage: '旧密码不能为空'
						}]
					},
					newPassword: {
						rules: [{
							required: true,
							errorMessage: '新密码不能为空'
						}]
					},
					rePassword: {
						rules: [{
							required: true,
							errorMessage: '重复密码不能为空'
						}]
					}
				},
				data: {}
			}
		},
		methods: {
			submit(ref) {
				this.$refs[ref].validate().then(res => {
					uni.request({
						method: 'PUT',
						url: this.url + '/volunteer/password',
						data: this.data,
						success: resp => {
							if (resp.data.code == 200) {
								uni.showToast({
									title: '保存成功',
									icon: 'none'
								});
							} else {
								uni.showToast({
									title: '保存失败',
									icon: 'none'
								});
							}
						},
						fail: (resp) => {}
					});
				}).catch(err => {});
			}
		}
	}
</script>

<style>

</style>
