<template>
	<view>
		<uni-search-bar placeholder="请输入搜索内容" @confirm="search" @cancel="cancel" bgColor="#fff" />
		<view style="display: flex;">
			<view style="width: 50%; text-align: center;">
				<picker @change="typePicker" :value="typeIndex" :range="typeArray" :range-key="'value'">
					<text style="font-size: 14px;">{{typeArray[typeIndex].value}}</text>
					<uni-icons type="bottom" />
				</picker>
			</view>
			<view style="width: 50%; text-align: center;">
				<picker @change="sortPicker" :value="sortIndex" :range="sortArray" :range-key="'value'">
					<text style="font-size: 14px;">{{sortArray[sortIndex].value}}</text>
					<uni-icons type="bottom" />
				</picker>
			</view>
		</view>
		<uni-section title="活动列表" type="line">
			<uni-list>
				<block v-for="(item, index) in pageList" :key="index">
					<view @click="goDetail(item)">
						<uni-list-item ellipsis="1" :title="item.title" :note="item.createTime" showArrow
							:thumb="item.cover" thumb-size="lg" :rightText="typeDict[item.actType]" />
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
				typeIndex: 0,
				sortIndex: 0,
				searchData: {},
				typeDict: {},
				typeArray: [{}],
				sortArray: [{
					key: 'false',
					value: '最新发布'
				}, {
					key: 'true',
					value: '最久发布'
				}],
				pageList: [],
				pageSize: 5,
				currentPage: 1,
				status: 'more',
				sortAsc: 'false'
			}
		},
		onLoad() {
			this.getDict();
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
			typePicker(e) {
				this.typeIndex = e.target.value;
				this.searchData.actType = this.typeArray[e.target.value].key;
				this.pageList = [];
				this.currentPage = 1;
				this.getPageList();
			},
			sortPicker(e) {
				this.sortIndex = e.target.value;
				this.sortAsc = this.sortArray[e.target.value].key;
				this.pageList = [];
				this.currentPage = 1;
				this.getPageList();
			},
			goDetail(data) {
				uni.navigateTo({
					url: './detail?activityId=' + data.activityId
				});
			},
			toArray(dict) {
				let list = [];
				list.push({
					key: '',
					value: '全部类型'
				});
				for (let key in dict) {
					list.push({
						key: key,
						value: dict[key]
					});
				}
				return list;
			},
			handleDataList(dataList) {
				let list = [];
				dataList.forEach(data => {
					list.push({
						activityId: data.activityId,
						cover: this.url + '/image/' + data.cover,
						title: data.title,
						content: data.content,
						actType: data.actType,
						createTime: dateUtil.format(data.createTime)
					});
				});
				return list;
			},
			getDict() {
				uni.request({
					method: 'POST',
					url: this.url + '/dict',
					data: ["act_type"],
					success: resp => {
						if (resp.data.code == 200) {
							this.typeDict = resp.data.data.act_type;
							this.typeArray = this.toArray(resp.data.data.act_type);
						}
					},
					fail: (resp) => {}
				});
			},
			getPageList() {
				this.status = 'loading';
				uni.request({
					method: 'POST',
					url: this.url + '/activity/list?size=' + this.pageSize + '&current=' + this.currentPage +
						'&column=create_time&asc=' + this.sortAsc,
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

</style>
