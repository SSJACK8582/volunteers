<template>
	<view>
		<uni-card>
			<uni-forms ref="infoForm" :rules="rules" :modelValue="data">
				<uni-forms-item label="姓名" required name="realName">
					<uni-easyinput v-model="data.realName" placeholder="请输入姓名" />
				</uni-forms-item>
				<uni-forms-item label="性别" required name="sex">
					<uni-data-checkbox v-model="data.sex" :localdata="array" />
				</uni-forms-item>
				<uni-forms-item label="年龄" required name="age">
					<uni-easyinput v-model="data.age" placeholder="请输入年龄" />
				</uni-forms-item>
				<uni-forms-item label="专业" required name="major">
					<uni-easyinput v-model="data.major" placeholder="请输入专业" />
				</uni-forms-item>
				<uni-forms-item label="班级" required name="grade">
					<uni-easyinput v-model="data.grade" placeholder="请输入班级" />
				</uni-forms-item>
				<uni-forms-item label="电话" required name="phoneNumber">
					<uni-easyinput v-model="data.phoneNumber" placeholder="请输入电话" />
				</uni-forms-item>
				<uni-forms-item label="邮箱" required name="email">
					<uni-easyinput v-model="data.email" placeholder="请输入邮箱" />
				</uni-forms-item>
				<uni-forms-item label="自我介绍">
					<uni-easyinput v-model="data.introduction" placeholder="请输入自我介绍" />
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
					realName: {
						rules: [{
							required: true,
							errorMessage: '姓名不能为空'
						}]
					},
					sex: {
						rules: [{
							required: true,
							errorMessage: '性别不能为空'
						}]
					},
					age: {
						rules: [{
								required: true,
								errorMessage: '年龄不能为空'
							},
							{
								format: 'number',
								errorMessage: '年龄只能为数字'
							}
						]
					},
					major: {
						rules: [{
							required: true,
							errorMessage: '专业不能为空'
						}]
					},
					grade: {
						rules: [{
							required: true,
							errorMessage: '班级不能为空'
						}]
					},
					phoneNumber: {
						rules: [{
								required: true,
								errorMessage: '电话不能为空'
							},
							{
								format: 'number',
								errorMessage: '电话只能为数字'
							}
						]
					},
					email: {
						rules: [{
							required: true,
							errorMessage: '邮箱不能为空'
						}]
					},
				},
				data: {},
				array: [{}]
			}
		},
		onLoad(data) {
			this.getDict();
			this.getData();
		},
		methods: {
			toArray(dict) {
				let list = [];
				for (let key in dict) {
					list.push({
						value: key,
						text: dict[key]
					});
				}
				return list;
			},
			submit(ref) {
				this.$refs[ref].validate().then(res => {
					uni.request({
						method: 'PUT',
						url: this.url + '/volunteer/update',
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
			},
			getDict() {
				uni.request({
					method: 'POST',
					url: this.url + '/dict',
					data: ["sex"],
					success: resp => {
						if (resp.data.code == 200) {
							this.array = this.toArray(resp.data.data.sex);
						}
					},
					fail: (resp) => {}
				});
			},
			getData() {
				uni.request({
					url: this.url + '/volunteer/info',
					success: resp => {
						if (resp.data.code == 200) {
							this.data = resp.data.data;
						}
					},
					fail: (resp) => {}
				});
			}
		}
	}
</script>

<style>

</style>
