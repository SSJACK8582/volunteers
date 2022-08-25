<template>
	<view>
		<uni-search-bar placeholder="请输入搜索内容" @confirm="search" @cancel="cancel" bgColor="#fff" />
		<view style="margin: 0 20rpx;">
			<uni-swiper-dot class="uni-swiper-dot-box" @clickItem=clickItem :info="bannerList"
				:current="swiperDotIndex">
				<swiper class="swiper-box" @change="changeSwiper" :current="swiperIndex" autoplay="true">
					<swiper-item v-for="(item, index) in bannerList" :key="index">
						<view class="swiper-item">
							<image :src="item.cover" style="width: 100%;" />
						</view>
					</swiper-item>
				</swiper>
			</uni-swiper-dot>
		</view>
		<view style="background: #fff; margin: 20rpx;">
			<uni-grid :column="4" :square="false" :highlight="false" :show-border="false" @change="changeGrid">
				<uni-grid-item v-for="(item, index) in gridList" :index="index" :key="index" style="width: 25%;">
					<view class="grid-item-box">
						<uni-icons :type="item.type" color="#007aff" size="30" />
						<text style="font-size: 12px;">{{item.text}}</text>
					</view>
				</uni-grid-item>
			</uni-grid>
		</view>
		<uni-section title="通知公告" type="line">
			<uni-list>
				<block v-for="(item, index) in pageList" :key="index">
					<view @click="goDetail(item)">
						<uni-list-item ellipsis="1" :title="item.title" :note="item.createTime" showArrow
							:thumb="item.cover" thumb-size="lg" :rightText="typeDict[item.noticeType]" />
					</view>
				</block>
			</uni-list>
			<uni-load-more :status="status" :icon-size="16" />
		</uni-section>
	</view>
</template>

<script>
	import {
		dateUtil
	} from '../../util.js';
	export default {
		data() {
			return {
				swiperDotIndex: 0,
				swiperIndex: 0,
				gridList: [{
						text: '活动',
						type: "calendar"
					},
					{
						text: '收藏',
						type: "heart"
					},
					{
						text: '评分',
						type: "star"
					},
					{
						text: '签到',
						type: "scan"
					},
					{
						text: '首页',
						type: "home"
					},
					{
						text: '报名',
						type: "compose"
					},
					{
						text: '消息',
						type: "chat"
					},
					{
						text: '我的',
						type: "person"
					}
				],
				searchData: {},
				typeDict: {},
				bannerList: [],
				pageList: [],
				pageSize: 5,
				currentPage: 1,
				status: 'more'
			}
		},
		onLoad() {
			this.getDict();
			this.getBannerList();
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
			search(res) {
				this.searchData.title = res.value;
				this.pageList = [];
				this.currentPage = 1;
				this.getPageList();
			},
			cancel() {
				this.searchData.title = '';
				this.pageList = [];
				this.currentPage = 1;
				this.getPageList();
			},
			changeSwiper(e) {
				this.swiperDotIndex = e.detail.current
			},
			clickItem(e) {
				this.swiperIndex = e
			},
			changeGrid(e) {
				uni.showToast({
					title: this.gridList[e.detail.index].text,
					icon: 'none'
				});
				if (e.detail.index === 3) {
					this.signEnroll();
				}
			},
			signEnroll() {
				uni.scanCode({
					success: function(res) {
						uni.request({
							url: res.result,
							success: resp => {
								if (resp.data.code == 200) {
									uni.showModal({
										title: '提示',
										content: '签到成功'
									});
								} else {
									uni.showModal({
										title: '提示',
										content: '签到失败'
									});
								}
							},
							fail: (resp) => {}
						});
					}
				});
			},
			goDetail(data) {
				uni.navigateTo({
					url: './detail?noticeId=' + data.noticeId
				});
			},
			handleDataList(dataList) {
				let list = [];
				dataList.forEach(data => {
					list.push({
						noticeId: data.noticeId,
						cover: this.url + '/image/' + data.cover,
						title: data.title,
						content: data.content,
						noticeType: data.noticeType,
						createTime: dateUtil.format(data.createTime)
					});
				});
				return list;
			},
			getDict() {
				uni.request({
					method: 'POST',
					url: this.url + '/dict',
					data: ["notice_type"],
					success: resp => {
						if (resp.data.code == 200) {
							this.typeDict = resp.data.data.notice_type;
						}
					},
					fail: (resp) => {}
				});
			},
			getBannerList() {
				uni.request({
					url: this.url + '/notice/banner',
					success: resp => {
						if (resp.data.code == 200) {
							let list = this.handleDataList(resp.data.data);
							this.bannerList = list;
						}
					},
					fail: (resp) => {}
				});
			},
			getPageList() {
				this.status = 'loading';
				uni.request({
					method: 'POST',
					url: this.url + '/notice/list?size=' + this.pageSize + '&current=' + this.currentPage +
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
	.uni-swiper-dot-box {
		backgroundColor: 'rgba(0, 0, 0, .3)';
		border: '1px rgba(0, 0, 0, .3) solid';
		color: '#fff';
		selectedBackgroundColor: 'rgba(0, 0, 0, .9)';
		selectedBorder: '1px rgba(0, 0, 0, .9) solid';
	}

	.swiper-box {
		height: 300rpx;
	}

	.swiper-item {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: column;
		justify-content: center;
		align-items: center;
		height: 300rpx;
		color: #fff;
	}

	.grid-item-box {
		flex: 1;
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 10rpx 0;
		margin-top: 10rpx;
	}
</style>
