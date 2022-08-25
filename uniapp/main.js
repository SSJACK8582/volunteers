// #ifndef VUE3
import Vue from 'vue'
import App from './App'

Vue.config.productionTip = false
Vue.prototype.url = 'http://127.0.0.1:8000'

App.mpType = 'app'

const app = new Vue({
	...App
})
app.$mount()
// #endif

// #ifdef VUE3
import {
	createSSRApp
} from 'vue'
import App from './App.vue'
export function createApp() {
	const app = createSSRApp(App)
	return {
		app
	}
}
// #endif

uni.addInterceptor('request', {
	invoke(args) {
		args.header = {
			'satoken': uni.getStorageSync('satoken')
		}
	}
});

uni.addInterceptor('switchTab', {
	invoke(e) {
		let date = new Date();
		if (e.url.indexOf('m') != -1 && date.getTime() > uni.getStorageSync('time')) {
			uni.reLaunch({
				url: '/pages/login'
			});
			return false;
		}
		return true;
	}
});
