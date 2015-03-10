

function like(likeElementId,contentId) {
	
	
    var http = new XMLHttpRequest();
    var url = "/wiki/rs/changelog/likecontent";
    var params = "contentId="+contentId;
    http.open("POST", url, true);

    //Send the proper header information along with the request
    http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    http.setRequestHeader("Content-length", params.length);
    http.setRequestHeader("Connection", "close");
    

    http.onreadystatechange = function() {//Call a function when the state changes.
        if(http.readyState == 4 && http.status == 200) {
        	var str = "Visit Microsoft!";
        	var disLikeElementId = likeElementId.replace("like", "dislike"); 
        	document.getElementById(likeElementId).style.display = "none";
        	document.getElementById(disLikeElementId).style.display = "inline";
        }
       
    }
    http.send(params);
}




function dislike(dislikeElementId,contentId) {
	
	
    var http = new XMLHttpRequest();
    var url = "/wiki/rs/changelog/dislikecontent";
    var params = "contentId="+contentId;
    http.open("POST", url, true);

    //Send the proper header information along with the request
    http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    http.setRequestHeader("Content-length", params.length);
    http.setRequestHeader("Connection", "close");
    

    http.onreadystatechange = function() {//Call a function when the state changes.
        if(http.readyState == 4 && http.status == 200) {
        	var str = "Visit Microsoft!";
        	var likeElementId = dislikeElementId.replace("dislike","like"); 
        	document.getElementById(dislikeElementId).style.display = "none";
        	document.getElementById(likeElementId).style.display = "inline";
        }
       
    }
    http.send(params);
}

