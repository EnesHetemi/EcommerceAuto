var iconNames = {
	'PROCESSING':'fa-spinner',	
	'PACKAGED':'fa-people-carry',
	'REFUNDED':'fa-undo',
	'PAID':'fa-check',
};



var confirmText;  
var confirmModalDialog;
var yesButton;
var noButton;

$(document).ready(function() {
	confirmText = $("#confirmText");
	confirmModalDialog = $("#confirmModal");
	yesButton = $("#yesButton");
	noButton = $("#noButton");

	$(".linkUpdateStatus").on("click", function(e) {
		e.preventDefault();
		link = $(this);
		showUpdateConfirmModal(link);
	});

	addEventHandlerForYesButton();
});

function addEventHandlerForYesButton() {
	yesButton.click(function(e) {
		e.preventDefault();
		sendRequestToUpdateOrderStatus($(this));
	});
}

function sendRequestToUpdateOrderStatus(button) {
	requestURL = button.attr("href");

	$.ajax({
		type: 'POST',
		url: requestURL,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response) {
		showMessageModal("Porosia u përditësua me sukses");
		updateStatusIconColor(response.orderId, response.status);

		console.log(response);
	}).fail(function(err) {
		showMessageModal("Gabim gjatë përditësimit të statusit të porosisë");
	})
}

function updateStatusIconColor(orderId, status) {
	link = $("#link" + status + orderId);
}

function showUpdateConfirmModal(link) {
	noButton.text("NO");
	yesButton.show();

	orderId = link.attr("orderId");
	status = link.attr("status");
	yesButton.attr("href", link.attr("href"));

	confirmText.text("Jeni të sigurt që dëshironi të përditësoni statusin e porosisë ID #" + orderId
					 + " në statusin " + status + "?");

	confirmModalDialog.modal();
}

function showMessageModal(message) {
	noButton.text("Close");
	yesButton.hide();
	confirmText.text(message);
}