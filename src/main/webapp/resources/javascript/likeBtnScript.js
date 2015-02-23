/**
 * 
 */

(function($) {
	$.fn.likeContent = function(rowId) {
		return this.each(function() {
			var likeLink =document.getElementById('contentsTable:'+rowId+':form:like');
			var likeCounter=likeLink.innerHTML.substring(9,likeLink.innerHTML.length-1);
			likeLink.style.display="none";
			var dislikeLink =document.getElementById('contentsTable:'+rowId+':form:dislike');
			dislikeLink.style.display="inline";
			var newValue=parseInt(likeCounter)+1;
			dislikeLink.innerHTML='نمی‌پسندم('+newValue+')';
			
		})
	};
})(jQuery);


(function($) {
	$.fn.dislikeContent = function(rowId) {
		return this.each(function() {
			var dislikeLink =document.getElementById('contentsTable:'+rowId+':form:dislike');
			dislikeLink.style.display="none";
			var likeLink =document.getElementById('contentsTable:'+rowId+':form:like');
			likeLink.style.display="inline";
		})
	};
})(jQuery);








(function($) {
	$.fn.test = function() {
		return this.each(function() {
			alert("naviiiiiiid");
		})
	};
})(jQuery);