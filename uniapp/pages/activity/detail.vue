<template>
	<view>
		<uni-card padding="0" spacing="0">
			<view class="custom-cover">
				<image class="cover-image" :src="data.cover" />
				<view class="cover-content">
					<text style="margin: auto;">{{data.title}}</text>
				</view>
			</view>
			<uni-list>
				<uni-list-item :title="'活动要求：' + data.demand" />
				<uni-list-item :title="'活动地址：' + data.address" />
				<uni-list-item :title="'活动人数：' + data.peopleNum" />
				<uni-list-item :title="'活动类型：' + typeDict[data.actType]" />
				<uni-list-item title="查看评论" showArrow @click="goComment" link />
			</uni-list>
		</uni-card>
		<uni-card title="活动内容" spacing="0">
			<text>{{data.content}}</text>
		</uni-card>
		<uni-card spacing="0">
			<view style="flex-direction:column; display: flex;">
				<text>活动：{{data.actTime}}</text>
				<text>报名：{{data.enrollTime}}</text>
			</view>
		</uni-card>
		<view style="display: flex;">
			<button v-if="!fromMy" type="primary" size="mini" style="width: 40%;" @click="submitEnroll">提交报名</button>
			<button v-if="fromMy" type="primary" size="mini" style="width: 40%;" @click="cancelEnroll">取消报名</button>
		</view>
	</view>
</template>

<script>
	import {
		dateUtil
	} from '../../util.js';
	export default {
		data() {
			return {
				fromMy: false,
				activityId: 0,
				typeDict: {},
				data: {}
			}
		},
		onLoad(data) {
			var pages = getCurrentPages();
			var page = pages[0];
			this.fromMy = page.route.indexOf('my') != -1
			this.activityId = data.activityId;
			this.getDict();
			this.getData();
		},
		methods: {
			goComment() {
				uni.navigateTo({
					url: './comment?activityId=' + this.data.activityId
				});
			},
			handleData(data) {
				return {
					activityId: data.activityId,
					cover: this.url + '/image/' + data.cover,
					title: data.title,
					demand: data.demand,
					content: data.content,
					address: data.address,
					peopleNum: data.peopleNum,
					actType: data.actType,
					actTime: dateUtil.format(data.actStartTime) + ' ~ ' + dateUtil.format(data.actEndTime),
					enrollTime: dateUtil.format(data.enrollStartTime) + ' ~ ' + dateUtil.format(data.enrollEndTime)
				}
			},
			getDict() {
				uni.request({
					method: 'POST',
					url: this.url + '/dict',
					data: ["act_type"],
					success: resp => {
						if (resp.data.code == 200) {
							this.typeDict = resp.data.data.act_type;
						}
					},
					fail: (resp) => {}
				});
			},
			getData() {
				uni.request({
					url: this.url + '/activity/' + this.activityId,
					success: resp => {
						if (resp.data.code == 200) {
							this.data = this.handleData(resp.data.data);
						}
					},
					fail: (resp) => {}
				});
			},
			submitEnroll() {
				uni.request({
					method: 'POST',
					url: this.url + '/enroll/insert',
					data: {
						'activityId': this.activityId
					},
					success: resp => {
						if (resp.data.code == 200) {
							uni.showToast({
								title: '报名成功',
								icon: 'none'
							});
						} else {
							uni.showToast({
								title: '报名失败',
								icon: 'none'
							});
						}
					},
					fail: (resp) => {}
				});
			},
			cancelEnroll() {
				uni.request({
					method: 'PUT',
					url: this.url + '/enroll/cancel',
					data: this.activityId,
					success: resp => {
						if (resp.data.code == 200) {
							uni.showToast({
								title: '取消成功',
								icon: 'none'
							});
						} else {
							uni.showToast({
								title: '取消失败',
								icon: 'none'
							});
						}
					},
					fail: (resp) => {}
				});
			}
		}
	}
</script>

<style lang="scss">
	.sticky {
		position: sticky;
		bottom: 0;
	}

	.custom-cover {
		flex: 1;
		flex-direction: row;
		position: relative;
	}

	.cover-content {
		position: absolute;
		bottom: 0;
		left: 0;
		right: 0;
		height: 60rpx;
		background-color: rgba($color: #000000, $alpha: 0.4);
		display: flex;
		flex-direction: row;
		align-items: center;
		font-size: 28rpx;
		color: #fff;
	}

	.cover-image {
		flex: 1;
		height: 350rpx;
		width: 100%;
	}

	.card-text {
		display: flex;
		justify-content: space-around;
		align-items: center;
		height: 60rpx;
	}
</style>
