//Create my namespace
var merc = {};

merc.AjaxFileUpload = function(dobefore, doafter) {

	if (dobefore && typeof (dobefore) == 'function') {
		dobefore();
	}

	// Global variable needed to monitor upload progress
	var uploadStartTime = new Date().getTime() / 1000; // seconds start time
	// //time it started
	var totalFileSize = 0; // size of file
	var updateRate = 1000;
	var lastProgressUpdate = uploadStartTime; // last time the data changed
	var lastUploadedSize = -1; // current upload size
	var barwidth = 450; // size of the progress bar
	var fileName;
	var initialisingTxt = "Initialising, please wait.....";
	var timeoutId;

	// GUI Elements
	var fileNameTxt = jQuery("#uploadFilename");
	var progressBarDiv = jQuery("#progress-bar");
	var progressBarTextDiv = jQuery("#progress-text");
	var progressAreaDiv = jQuery("#fileuploadProgress");
	var progressBarGraphic = jQuery("#progress-bgrd");
	var docField = jQuery("#upload");
	var theForm = jQuery("#ajaxFileUploadForm");
	var theSubmitButton = jQuery("#fileUpload input[type='submit']");

	// file importing message div block
	var fileSuccessMsgDiv = jQuery(".file_success_msg_div");
	var fileErrorMsgDiv = jQuery(".file_error_msg_div");
	var successMsg = jQuery("#success_msg");
	var errorMsg = jQuery("#error_msg");
	successMsg.html("");
	errorMsg.html("");
	fileSuccessMsgDiv.hide();
	fileErrorMsgDiv.hide();

	// remove previouse error or success message if any
	var normalErrorMsgSection = jQuery(".error_msg_section");
	normalErrorMsgSection.remove();
	var panSection = jQuery(".pane");
	panSection.remove();

	// Initialise/Reset GUI elements
	theSubmitButton.attr("disabled", "disabled");
	fileNameTxt.html(initialisingTxt);
	progressBarGraphic.width(0);
	progressBarTextDiv.html("");

	// Start the Ajax File Upload...........
	initialise(doafter);

	function initialise(doafter) {
		var action = theForm.attr("action");
		var f = getFileName(docField.val());
		if (f) {
			fileName = f;
			// getProgress() will recurse until dowload is over
			timeoutId = setTimeout(getProgress, updateRate);

			// This is blocked until above method completes
			ajaxFileUpload(action, doafter);
			progressAreaDiv.show();
		} else {
			// show the error message.
			errorMsg.html("File must be provided");
			fileErrorMsgDiv.show();
		}
		return false;
	}

	/**
	 * Gets the upload progress from the server and then recursively calls
	 * itself until the upload is complete
	 */
	function getProgress() {
		jQuery.ajax({
			type : "POST",
			url : "uploadprogress.action?rnd=" + new Date().getTime(),
			dataType : "json",
			success : function(jsonData) {
				var aborted = jsonData.aborted;
				if (aborted) {
					abort();
				} else {
					var percent = jsonData.percentComplete;
					totalFileSize = jsonData.bytesTotal;
					var sent = jsonData.bytesSent;

					if (fileNameTxt.html() == initialisingTxt) {
						fileNameTxt.html('<b>Importing:</b> ' + fileName);
					}

					if (sent > 0) {
						updateProgressBarGUI(sent);
					}

					if (percent == "100") {
						// The end
					} else {
						setTimeout(getProgress, updateRate);
					}
				}

			},
			error : function(request, error, exception) {
				// show the error
				//var err = "Error detected: " + error + " and exception "+ exception;
				errorMsg.html("Failed to get importing progress");
				fileErrorMsgDiv.show();
			}
		});
	}

	/**
	 * This method performs the upload of the file
	 * 
	 * @param action
	 *            the url to call
	 * @param callback
	 *            is the javascript method to call when the upload is complete
	 */
	function ajaxFileUpload(actionurl, doafter) {
		jQuery.ajaxFileUpload({
			url : actionurl,
			secureuri : false,
			fileElementId : 'upload',
			extElementId : 'extract',
			coElementId : 'col',
			ownerElementId : 'colowner',
			viewTypeElementId : 'viewtype',
			dataType : 'json',
			success : function(data, status) {
				if (data.success == 'true') {
					// show the file importing successful message
					successMsg.html(data.message);
					fileSuccessMsgDiv.show();
					complete(true);
				} else {
					// show the file importing failed message
					errorMsg.html(data.message);
					fileErrorMsgDiv.show();
					// finally set the complet div
					complete(false);
				}

			},
			error : function(data, status, e) {
				// alert(e);
				errorMsg.html("Failed to import dataset file");
				fileErrorMsgDiv.show();
				if (doafter && typeof (doafter) == 'function') {
					doafter(true);
				}
			}
		})
		return false;
	}

	function abort() {
		complete(false);
	}

	function complete(success) {
		progressAreaDiv.hide();
		theSubmitButton.attr("disabled", "");
		clearTimeout(timeoutId);
		if (doafter && typeof (doafter) == 'function') {
			doafter(success);
		}
	}

	var previousPercentComplete = 0;

	/**
	 * This is called every 2 seconds to update the progress bar
	 */
	function updateProgressBarGUI(bytesTransferred) {
		// if (bytesTransferred > lastUploadedSize) {
		var now = new Date().getTime() / 1000.0; // seconds
		var uploadTransferRate = bytesTransferred / (now - uploadStartTime);
		var timeRemaining = (totalFileSize - lastUploadedSize)
				/ uploadTransferRate;

		// if (totalFileSize > 5242880) //if greater than 5 megabytes - slow
		// down checks to every 5 seconds
		// currentRate = 500;

		lastUploadedSize = bytesTransferred;
		lastProgressUpdate = now;
		var progress = (0.0 + lastUploadedSize) / totalFileSize;
		var percentComplete = Math.round(100 * progress);

		var timeLeft = timeRemaining;

		/*
		 * This modifies the server connection frequency based on the progress
		 * rate of the upload So for large files it will slow down progress
		 * update checks - or for network glitches which slow and speed up
		 * transfer, it will adjust the rate accordingly.
		 */
		var rateModifier = 3;
		if (previousPercentComplete > 0) {
			if ((percentComplete - previousPercentComplete) < rateModifier) {
				// If less than 3% change - slow down the checks
				updateRate += 500;
			} else if (((percentComplete - previousPercentComplete) > rateModifier)
					&& (updateRate > 1000)) {
				updateRate -= 500;
			}
		}
		previousPercentComplete = percentComplete;

		var totalStr = Math.round(totalFileSize / 1024); // changed to
															// kilobytes
		if (progress && progress > 0) {
			progressBarGraphic.width(barwidth * progress);
		}
		var ptext = percentComplete + '% completed ('
				+ formatSize(lastUploadedSize) + ' of '
				+ formatSize(totalFileSize) + ')';
		ptext += '&nbsp; &nbsp; Time remaining: ' + formatTime(timeLeft);
		ptext += '&nbsp; &nbsp; Transfer rate: '
				+ formatSize(uploadTransferRate) + " / sec";
		progressBarTextDiv.html(ptext);
	}

	/**
	 * If bigger than 1MB output MB, else output KB
	 */
	function formatSize(byteCount) {
		var str = Math.round(byteCount / 1024);
		if (str > 1024) {
			str = Math.round(100 * str / 1024) / 100.0;
			str += ' MB';
		} else {
			str += ' KB';
		}
		return str;
	}

	/**
	 * Format seconds into a string readable format
	 */
	function formatTime(seconds) {
		var str;
		if (seconds > 3600) {
			var h = Math.floor(seconds / 3600);
			seconds -= h * 3600;
			var m = Math.round(seconds / 60);
			str = h + ' hr. ' + ((m < 10) ? '0' + m : m) + ' min.';
		} else if (seconds > 60) {
			var m = Math.floor(seconds / 60);
			seconds -= m * 60;
			seconds = Math.round(seconds);
			str = m + ' min. ' + ((seconds < 10) ? '0' + seconds : seconds)
					+ ' sec.';
		} else {
			str = Math.round(seconds) + ' sec.';
		}
		return str;
	}

	/**
	 * Gets the file name from a path
	 */
	function getFileName(path) {
		if (path) {
			var windows = path.indexOf("\\");
			if (windows && windows > -1) {
				var lastIndex = path.lastIndexOf("\\");
				var filename = path.substring(lastIndex + 1, path.length);
				return filename;
			} else {
				// linux
				var lastIndex = path.lastIndexOf("/");
				var filename = path.substring(lastIndex + 1, path.length);
				return filename;
			}
		} else {
			return null;
		}
	}

};

/**
 * Static factory - sort of.
 */
merc.AjaxFileUpload.initialise = function(dobefore, doafter) {
	return new merc.AjaxFileUpload(dobefore, doafter);
}