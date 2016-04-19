$(document).ready(function() {

	// get the height and width of the page
	var window_width = $(window).width();
	var window_height = $(window).height();

	// vertical and horizontal centering of modal window(s)
	/*
	 * we will use each function so if we have more then 1 modal window we
	 * center them all
	 */
	$('#modal_window').each(function() {

		// get the height and width of the modal
		var modal_height = $(this).outerHeight();
		var modal_width = $(this).outerWidth();

		// calculate top and left offset needed for centering
		var top = (window_height - modal_height) / 3;
		var left = (window_width - modal_width) / 2;

		// apply new top and left css values
		$(this).css({
			'top' : top,
			'left' : left
		});

	});

	$('#wait_modal').click(function() {
		// use the function to show it
		show_modal('modal_window');
	});

	$('#mask').click(function() {
		// use the function to close it
		// we don't want to close the modal window until contents loaded.
		// close_modal();
	});
});

// THE FUNCTIONS

function close_modal() {
	// hide the mask
	$('#mask').fadeOut(500);

	// hide modal window(s)
	$('#modal_window').fadeOut(500);

};
function show_modal(modal_id) {
	// get document width and height
	var window_width = $(document).width();
	var window_height = $(document).height();
	// set display to block and opacity to 0 so we can use fadeTo
	$('#mask').css({
		display : 'block',
		opacity : '0',
		'width' : window_width,
		'height' : window_height
	});
	// fade in the mask to opacity 0.8
	$('#mask').fadeTo(500, 0.5);
	// show the modal window
	$('#' + modal_id).fadeIn(500);
};
