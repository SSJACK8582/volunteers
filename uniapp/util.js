var dateUtil = {
	parse: function(str) {
		var a = str.split(/[^0-9]/);
		return new Date(a[0], a[1] - 1, a[2], a[3], a[4], a[5]);
	},
	format: function(value) {
		let date = new Date(value)
		let yyyy = date.getFullYear()
		let mm = date.getMonth() + 1
		mm = mm < 10 ? ('0' + mm) : mm
		let dd = date.getDate()
		dd = dd < 10 ? ('0' + dd) : dd
		let HH = date.getHours()
		HH = HH < 10 ? ('0' + HH) : HH
		let MM = date.getMinutes()
		MM = MM < 10 ? ('0' + MM) : MM
		let ss = date.getSeconds()
		ss = ss < 10 ? ('0' + ss) : ss
		return yyyy + '/' + mm + '/' + dd + ' ' + HH + ':' + MM + ':' + ss
	}
};
export {
	dateUtil
}
