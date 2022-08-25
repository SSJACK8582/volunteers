<template>
	<view style="margin: 20rpx 0;">
		<uni-card :title="data.title" isFull :sub-title="data.createTime" :extra="typeDict[data.noticeType]">
			<image :src="data.cover" style="width: 100%;" />
			<text class="uni-body">{{data.content}}</text>
		</uni-card>
	</view>
</template>

<script>
	import {
		dateUtil
	} from '../../util.js';
	export default {
		data() {
			return {
				noticeId: 0,
				typeDict: {},
				data: {}
			}
		},
		onLoad(data) {
			this.noticeId = data.noticeId;
			this.getDict();
			this.getData();
		},
		methods: {
			handleData(data) {
				return {
					noticeId: data.noticeId,
					cover: this.url + '/image/' + data.cover,
					title: data.title,
					content: data.content,
					noticeType: data.noticeType,
					createTime: dateUtil.format(data.createTime)
				}
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
			getData() {
				uni.request({
					url: this.url + '/notice/' + this.noticeId,
					success: resp => {
						if (resp.data.code == 200) {
							this.data = this.handleData(resp.data.data);
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
		position: sticky;
		display: flex;
		bottom: 0;
		background: #fff;
	}
</style>
