$(document).ready(function() {
	$(".link-delete").on("click", function(e) {
		e.preventDefault();
		showDeleteConfirmModal($(this), entityName);
	});
});
	
function clearFilter() {
	window.location = moduleURL;	
}

function showDeleteConfirmModal(link, entityName) {
	entityId = link.attr("entityId");
	
	$("#yesButton").attr("href", link.attr("href"));	
	$("#confirmText").text("Jeni të sigurt që dëshironi të fshini këtë "
							 + entityName + " ID " + entityId + "?");
	$("#confirmModal").modal();	
}