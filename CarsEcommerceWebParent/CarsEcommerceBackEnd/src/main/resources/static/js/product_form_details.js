function addNextDetailSection() {
	allDivDetails = $("[id^='divDetail']");
	divDetailsCount = allDivDetails.length;

	htmlDetailSection = `
		<div class="form-inline d-flex justify-content-center" id="divDetail${divDetailsCount}">
			<label class="m-3">Emri:</label>
			<input type="text" class="form-control w-25 shadow-sm bg-light rounded" name="detailNames" maxlength="255" />
			<label class="m-3">Vlera:</label>
			<input type="text" class="form-control w-25 shadow-sm bg-light rounded" name="detailValues" maxlength="255" />
		</div>	
	`;

	$("#divProductDetails").append(htmlDetailSection);

	previousDivDetailSection = allDivDetails.last();
	previousDivDetailID = previousDivDetailSection.attr("id");

	htmlLinkRemove = `
		<a class="ml-3 btn btn-danger shadow rounded"
			href="javascript:removeDetailSectionById('${previousDivDetailID}')"
			title="Hiqeni këtë detaj">x</a>
	`;

	previousDivDetailSection.append(htmlLinkRemove);

	$("input[name='detailNames']").last().focus();
}

function removeDetailSectionById(id) {
	$("#" + id).remove();
}