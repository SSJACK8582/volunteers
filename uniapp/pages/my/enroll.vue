<template>
	<view>
		<view class="sticky">
			<uni-segmented-control :current="current" :values="items" style-type="text" active-color="#007aff"
				@clickItem="onClickItem" />
		</view>
		<block v-for="key of 3" :key="key">
			<view v-if="current + 1 === key">
				<block v-for="(item, index) in pageList" :key="index">
					<uni-card :title="item.title" :extra="statusDict[item.enrollStatus]" @click="onClick(item)">
						<text class="uni-body">{{item.actTime}}</text>
						<view slot="actions" class="card-actions">
							<view class="card-actions-item">
								<text>查看详情</text>
							</view>
						</view>
					</uni-card>
				</block>
				<uni-load-more :status="status" :icon-size="16" />
			</view>
		</block>
	</view>
</template>

<script>
	import {
		dateUtil
	} from '../../util.js';
	export default {
		data() {
			return {
				items: ['全部', '已通过', '已取消'],
				current: 0,
				searchData: {},
				statusDict: {},
				pageList: [],
				pageSize: 5,
				currentPage: 1,
				status: 'more'
			}
		},
		onLoad(data) {
			this.getDict();
			this.getPageList();
		},
		onReachBottom() {
			if (this.status == 'more') {
				this.getPageList();
			}
		},
		methods: {
			onClickItem(e) {
				if (this.current !== e.currentIndex) {
					this.current = e.currentIndex;
					if (e.currentIndex === 0) {
						this.searchData.enrollStatus = '';
						this.pageList = [];
						this.currentPage = 1;
						this.getPageList();
					} else if (e.currentIndex === 1) {
						this.searchData.enrollStatus = '2';
						this.pageList = [];
						this.currentPage = 1;
						this.getPageList();
					} else if (e.currentIndex === 2) {
						this.searchData.enrollStatus = '3';
						this.pageList = [];
						this.currentPage = 1;
						this.getPageList();
					}
				}
			},
			onClick(item) {
				uni.navigateTo({
					url: '../activity/detail?activityId=' + item.activityId
				});
			},
			handleDataList(dataList) {
				let list = [];
				dataList.forEach(data => {
					list.push({
						enrollId: data.enrollId,
						activityId: data.activityId,
						title: data.title,
						enrollStatus: data.enrollStatus,
						actTime: dateUtil.format(data.actStartTime) + ' ~ ' + dateUtil.format(data
							.actEndTime)
					});
				});
				return list;
			},
			getDict() {
				uni.request({
					method: 'POST',
					url: this.url + '/dict',
					data: ["enroll_status"],
					success: resp => {
						if (resp.data.code == 200) {
							this.statusDict = resp.data.data.enroll_status;
						}
					},
					fail: (resp) => {}
				});
			},
			getPageList() {
				this.status = 'loading';
				uni.request({
					method: 'POST',
					url: this.url + '/enroll/list?size=' + this.pageSize + '&current=' + this.currentPage +
						'&column=create_time&asc=false',
					data: this.searchData,
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
	.sticky {
		position: sticky;
		top: var(--window-top);
		z-index: 2;
		background: #fff;
	}

	.card-actions {
		display: flex;
		flex-direction: row;
		justify-content: space-around;
		align-items: center;
		height: 60rpx;
		border-top: 1px #ddd solid;
	}

	.card-actions-item {
		display: flex;
		flex-direction: row;
		align-items: center;
		/* 		border: 1px #ccc solid;
		border-radius: 10px;
		padding: 0 20px; */
		color: #666;
	}
</style>
