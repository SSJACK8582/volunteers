<template>
	<view>
		<block v-for="(item, index) in pageList" :key="index">
			<uni-card :title="item.title" :extra="item.createTime">
				<text>{{item.content}}</text>
			</uni-card>
		</block>
		<uni-load-more :status="status" :icon-size="16" />
	</view>
</template>

<script>
	import {
		dateUtil
	} from '../../util.js';
	export default {
		data() {
			return {
				pageList: [],
				pageSize: 5,
				currentPage: 1,
				status: 'more'
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
			this.getPageList();
		},
		onPullDownRefresh() {
			this.pageList = [];
			this.currentPage = 1;
			this.getPageList();
			setTimeout(function() {
				uni.stopPullDownRefresh();
			}, 500);
		},
		onReachBottom() {
			if (this.status == 'more') {
				this.getPageList();
			}
		},
		methods: {
			handleDataList(dataList) {
				let list = [];
				dataList.forEach(data => {
					list.push({
						messageId: data.messageId,
						title: data.title,
						content: data.content,
						createTime: dateUtil.format(data.createTime)
					});
				});
				return list;
			},
			getPageList() {
				this.status = 'loading';
				uni.request({
					method: 'POST',
					url: this.url + '/message/list?size=' + this.pageSize + '&current=' + this.currentPage +
						'&column=create_time&asc=false',
					data: {},
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
			}
		}
	}
</script>

<style>

</style>
