<template>
	<view>
		<view style="margin: 20rpx 0;">
			<uni-card :isFull="true" :title="data.realName" :sub-title="data.studentNo" :extra="data.major"
				:thumbnail="avatar">
				<view style="display: flex; text-align: center;">
					<view style="width: 50%; border-right: solid #ddd 1px;">
						<text style="margin-right: 10rpx;">义工时长</text>
						<text style="font-size: 30rpx;">{{data.workTime}}</text>
					</view>
					<view style="width: 50%; border-left: solid #ddd 1px;">
						<text style="margin-right: 10rpx;">评分</text>
						<text style="font-size: 30rpx;">{{data.score}}</text>
					</view>
				</view>
			</uni-card>
		</view>
		<uni-list>
			<block v-for="(item, index) in list" :key="index">
				<uni-list-item show-extra-icon="true" showArrow :extra-icon="item.extraIcon" :title="item.title"
					:to="item.to" />
			</block>
		</uni-list>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				list: [{
					extraIcon: {
						color: 'deepskyblue',
						size: '22',
						type: 'calendar'
					},
					title: "我的报名",
					to: "./enroll"
				}, {
					extraIcon: {
						color: 'deepskyblue',
						size: '22',
						type: 'person'
					},
					title: "个人信息",
					to: "./info"
				}, {
					extraIcon: {
						color: 'deepskyblue',
						size: '22',
						type: 'compose'
					},
					title: "修改密码",
					to: "./password"
				}, {
					extraIcon: {
						color: 'deepskyblue',
						size: '22',
						type: 'contact'
					},
					title: "修改头像",
					to: ""
				}, {
					extraIcon: {
						color: 'deepskyblue',
						size: '22',
						type: 'email'
					},
					title: "问题反馈",
					to: ""
				}, {
					extraIcon: {
						color: 'deepskyblue',
						size: '22',
						type: 'undo'
					},
					title: "退出登录",
					to: ""
				}],
				data: {
					studentNo: ' ',
					realName: ' ',
					major: ' ',
				},
				avatar: ' '
			}
		},
		onShow() {
			let date = new Date();
			if (date > uni.getStorageSync('time')) {
				uni.reLaunch({
					url: '/pages/login'
				});
			}
		},
		onLoad() {
			this.getData();
		},
		onPullDownRefresh() {
			this.getData();
			setTimeout(function() {
				uni.stopPullDownRefresh();
			}, 500);
		},
		methods: {
			getData() {
				uni.request({
					url: this.url + '/volunteer/info',
					success: resp => {
						if (resp.data.code == 200) {
							this.data = resp.data.data;
							this.avatar = this.url + '/image/' + resp.data.data.avatar;
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
