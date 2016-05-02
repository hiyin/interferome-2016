 
var pw_done = false;
var pw_height = 400;
var pw_width = 400;

//popup window iframe
var pw_animation = true;
$(document).ready(function(){
  $("a.greybox").click(function(){
    var t = this.title || $(this).text() || this.href;
    pw_show(t,this.href,450,730);
    return false;
  });
});

$(document).keypress(function(e){ 
	if(pw_done){
		if (e.which == 13) { 
			 //do nothing,
		} 
		if (e.keyCode == 27) {  
			 $("#pw_window,#pw_overlay").hide();
		}  
	}
});


function pw_show(caption, url, height, width) {
  pw_height = height || 400;
  pw_width = width || 400;
  if(!pw_done) {
    $(document.body)
      .append("<div id='pw_overlay'></div><div id='pw_window'><div id='pw_caption'></div>"
        + "<img src='../images/close.png' alt='Close window'/></div>");
    $("#pw_window img").click(pw_hide);    
    $("#pw_overlay").click(pw_hide);
    $(window).resize(pw_position);
    pw_done = true;
 }

  $("#pw_frame").remove();
  $("#pw_window").append("<iframe id='pw_frame' src='"+url+"'></iframe>");

  $("#pw_caption").html(caption);
  $("#pw_overlay").show();
  pw_position();

  if(pw_animation)
    $("#pw_window").slideDown("slow");
  else
    $("#pw_window").show();
}

function pw_hide() {
  $("#pw_window,#pw_overlay").hide();
}

function pw_position() {
  var de = document.documentElement;
  var w = self.innerWidth || (de&&de.clientWidth) || document.body.clientWidth;
  $("#pw_window").css({width:pw_width+"px",height:pw_height+"px",
    left: ((w - pw_width)/2)+"px" });
  $("#pw_frame").css("height",pw_height - 32 +"px");
}

