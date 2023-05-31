var MILLISECONDS_A_DAY = 24 * 60 * 60 * 1000;

function setupButtonEventHandlers(reportType, callbackFunction) {

	$(".button-sales-by" + reportType).on("click", function() {
		$(".button-sales-by" + reportType).each(function(e) {
			$(this).removeClass('btn-primary').addClass('btn-light');
		});

		$(this).removeClass('btn-light').addClass('btn-primary');

		period = $(this).attr("period");
		if (period) {
			callbackFunction(period);
			$("#divCustomDateRange" + reportType).addClass("d-none");
		} else {
			$("#divCustomDateRange" + reportType).removeClass("d-none");
		}		
	});

	initCustomDateRange(reportType);

	$("#buttonViewReportByDateRange" + reportType).on("click", function(e) {
		validateDateRange(reportType, callbackFunction);
	});	
}

function validateDateRange(reportType, callbackFunction) {
	startDateField = document.getElementById('startDate' + reportType);
	days = calculateDays(reportType);

	startDateField.setCustomValidity("");

	if (days >= 7 && days <= 30) {
		callbackFunction("custom");
	} else {
		startDateField.setCustomValidity("Data duhet të jetë në rangun 7 deri ne 30 ditë");
		startDateField.reportValidity();
	}
}

function calculateDays(reportType) {
	startDateField = document.getElementById('startDate' + reportType);
	endDateField = document.getElementById('endDate' + reportType);

	startDate = startDateField.valueAsDate;
	endDate = endDateField.valueAsDate;

	differenceInMilliseconds = endDate - startDate;
	return differenceInMilliseconds / MILLISECONDS_A_DAY;
}

function initCustomDateRange(reportType) {
	startDateField = document.getElementById('startDate' + reportType);
	endDateField = document.getElementById('endDate' + reportType);

	toDate = new Date();
	endDateField.valueAsDate = toDate;

	fromDate = new Date();
	fromDate.setDate(toDate.getDate() - 30);
	startDateField.valueAsDate = fromDate;
}	

function getChartTitle(period) {
	if (period == "last_7_days") return "Shitjet ne periudhen 7 Ditore";
	if (period == "last_28_days") return "Shitjet ne periudhen 28 Ditore";
	if (period == "last_6_months") return "Shitjet ne periudhen 6 Mujore";
	if (period == "last_year") return "Shitjet ne periudhen 1 vjeqare";
	if (period == "custom") return "Sipas Dates";
	
	return "";
}

function getDenominator(period, reportType) {
	if (period == "last_7_days") return 7;
	if (period == "last_28_days") return 28;
	if (period == "last_6_months") return 6;
	if (period == "last_year") return 12;
	if (period == "custom") return calculateDays(reportType);

	return 7;
}

function setSalesAmount(period, reportType, labelTotalItems) {
	$("#textTotalGrossSales" + reportType).text($.number(totalGrossSales, 2)+ " €");

	denominator = getDenominator(period, reportType);

	$("#textAvgGrossSales" + reportType).text($.number(totalGrossSales / denominator, 2)+ " €");
	$("#labelTotalItems" + reportType).text(labelTotalItems);
	$("#textTotalItems" + reportType).text(totalItems);	
}

function formatChartData(data) {
	var formatter = new google.visualization.NumberFormat({
		prefix: '€'
	});

	formatter.format(data, 1);
	formatter.format(data, 0);	
}