
var removeClass = function (className) {
	var elems = document.getElementsByClassName(className);
	if (elems != undefined && elems.length > 0) {
		for (var i = 0; i < elems.length; ++i) {
			elems[i].parentElement.removeChild (elems[i]);
		}
	}
};

var removeId = function (idName) {
	var elem = document.getElementById(idName);
	if (elem != undefined) {
		elem.parentElement.removeChild (elem);
	}
};

removeId ('div-gpt-ad-1498030544317-0');
removeId ('div-gpt-ad-1498030412987-0');
removeId ('openApp');
removeId ('rightColumn');
removeId ('header');
removeId ('float-ads-tags');
removeId ('footer');
removeId ('google_center_div');

removeClass ('fullWidth newVideo');
removeClass ('buttons loadMoreHotPhoto');
removeClass ('tag-footer');
removeClass ('viewSwitcher');
removeClass ('box darkBox rightPage');
removeClass ('instant_item');
removeClass ('creative tall-square');
removeClass ('box_title');
removeClass ('videoListItem');
removeClass ('btn-social');
