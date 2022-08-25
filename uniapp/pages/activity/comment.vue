<template>
	<view>
		<uni-section title="评论区" type="line">
			<uni-list>
				<block v-for="(item, index) in pageList" :key="index">
					<uni-list-item :title="item.comment" :note="item.commentTime" :thumb="item.avatar" thumb-size="lg"
						:rightText="item.realName" />
				</block>
			</uni-list>
			<uni-load-more :status="status" :icon-size="16" />
		</uni-section>
		<view class="sticky">
			<uni-easyinput v-model="comment" placeholder="输入评论" style="width: 80%;" />
			<button type="default" size="mini" style="width: 20%;" @click="onClick">评论</button>
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
				activityId: 0,
				comment: '',
				pageList: [],
				pageSize: 5,
				currentPage: 1,
				status: 'more'
			}
		},
		onLoad(data) {
			this.activityId = data.activityId;
			this.getPageList();
		},
		methods: {
			onClick() {
				uni.request({
					method: 'PUT',
					url: this.url + '/enroll/comment/' + this.activityId,
					data: this.comment,
					success: resp => {
						if (resp.data.code == 200) {
							uni.showToast({
								title: '评论成功',
								icon: 'none'
							});
							this.pageList = [];
							this.currentPage = 1;
							this.getPageList();
						} else {
							uni.showToast({
								title: '评论失败',
								icon: 'none'
							});
						}
					},
					fail: (resp) => {}
				});
			},
			handleDataList(dataList) {
				let list = [];
				dataList.forEach(data => {
					list.push({
						enrollId: data.enrollId,
						realName: data.realName,
						avatar: this.url + '/image/' + data.avatar,
						comment: data.comment,
						commentTime: dateUtil.format(data.commentTime)
					});
				});
				return list;
			},
			getPageList() {
				this.status = 'loading';
				uni.request({
					method: 'POST',
					url: this.url + '/activity/comment/list?size=' + this.pageSize + '&current=' + this
						.currentPage +
						'&column=create_time&asc=false',
					data: this.activityId,
					success: resp => {
						if (resp.data.code == 200) {
							let list = this.handleDataList(resp.data.data.records);
							this.pageList = this.pageList.concat(list);
							if (list.length == 0) {
								this.status = 'noMore';
							} else {
								this.status = 'more';
							}
						}
					},
					fail: (resp) => {}
				});
				this.currentPage += 1;
			},
			putComment() {
				uni.request({
					method: 'PUT',
					url: this.url + '/enroll/comment/' + this.activityId,
					data: this.comment,
					success: resp => {
						if (resp.data.code == 200) {
							uni.showToast({
								title: '评论成功',
								icon: 'none'
							});
						} else {
							uni.showToast({
								title: '评论失败',
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

<style>
	.sticky {
		display: flex;
		position: fixed;
		left: 0;
		right: 0;
		bottom: 0;
		background: #fff;
	}
</style>
