var data;
var chartOptions;

$(document).ready(function() {
	setupButtonEventHandlers("_category", loadSalesReportByDateForCategory);	
});

function loadSalesReportByDateForCategory(period) {
	if (period == "custom") {
		startDate = $("#startDate_category").val();
		endDate = $("#endDate_category").val();

		requestURL = contextPath + "reports/category/" + startDate + "/" + endDate;
	} else {
		requestURL = contextPath + "reports/category/" + period;		
	}

	$.get(requestURL, function(responseJSON) {
		prepareChartDataForSalesReportByCategory(responseJSON);
		customizeChartForSalesReportByCategory();
		formatChartData(data, 1, 2);
		drawChartForSalesReportByCategory(period);
		setSalesAmount(period, '_category', "Totali i Produkteve");
	});
}

function prepareChartDataForSalesReportByCategory(responseJSON) {
	data = new google.visualization.DataTable();
	data.addColumn('string', 'Kategorit');
	data.addColumn('number', 'Shitjet');

	totalGrossSales = 0.0;
	totalItems = 0;

	$.each(responseJSON, function(index, reportItem) {
		data.addRows([[reportItem.identifier, reportItem.grossSales]]);
		totalGrossSales += parseFloat(reportItem.grossSales);
		totalItems += parseInt(reportItem.productsCount);
	});
}

function customizeChartForSalesReportByCategory() {
	chartOptions = {
		height: 360, legend: {position: 'right'}
	};
}

function drawChartForSalesReportByCategory() {
	var salesChart = new google.visualization.PieChart(document.getElementById('chart_sales_by_category'));
	salesChart.draw(data, chartOptions);
}