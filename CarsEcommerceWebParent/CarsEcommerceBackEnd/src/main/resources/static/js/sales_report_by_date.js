var data;
var chartOptions;
var totalGrossSales;
var totalOrders;

$(document).ready(function() {
	$(".button-sales-by-date").on("click", function() {

		$(".button-sales-by-date").each(function(e) {
			$(this).removeClass('btn-primary').addClass('btn-light');
		});

		$(this).removeClass('btn-light').addClass('btn-primary');

		period = $(this).attr("period");		
		loadSalesReportByDate(period);
	});
});

function loadSalesReportByDate(period) {
	requestURL = contextPath + "reports/sales_by_date/" + period;

	$.get(requestURL, function(responseJSON) {
		prepareChartData(responseJSON);
		customizeChart(period);
		drawChart(period);
	});
}

function prepareChartData(responseJSON) {
	data = new google.visualization.DataTable();
	data.addColumn('string', 'Data');
	data.addColumn('number', 'Shitjet');
	data.addColumn('number', 'Porosit');

	totalGrossSales = 0.0;
	totalOrders = 0;

	$.each(responseJSON, function(index, reportItem) {
		data.addRows([[reportItem.identifier, reportItem.grossSales, reportItem.ordersCount]]);
		totalGrossSales += parseFloat(reportItem.grossSales);
		totalOrders += parseInt(reportItem.ordersCount);
	});
}

function customizeChart(period) {
	chartOptions = {
		title: getChartTitle(period),
		'height': 360,
		legend: {position: 'top'},

		series: {
			0: {targetAxisIndex: 0},
			1: {targetAxisIndex: 0},
		},

		vAxes: {
			0: {title: 'Shuma e Shitjeve', format: 'currency'},
			1: {title: 'Numri i Porosive'}
		}
	};

	var formatter = new google.visualization.NumberFormat({
		prefix: '€'
	});

	formatter.format(data, 1);
	formatter.format(data, 0);
}

function drawChart(period) {
	var salesChart = new google.visualization.ColumnChart(document.getElementById('chart_sales_by_date'));
	salesChart.draw(data, chartOptions);

	$("#textTotalGrossSales").text($.number(totalGrossSales, 2)+ "€");

	denominator = getDenominator(period);

	$("#textAvgGrossSales").text($.number(totalGrossSales / denominator, 2)+ "€");
	$("#textTotalOrders").text(totalOrders);
}

function getChartTitle(period) {
	if (period == "last_7_days") return "Shitjet ne periudhen 7 Ditore";
	if (period == "last_28_days") return "Shitjet ne periudhen 28 Ditore";
	if (period == "last_6_months") return "Shitjet ne periudhen 6 Mujore";
	if (period == "last_year") return "Shitjet ne periudhen 1 vjeqare";

	return "";
}

function getDenominator(period) {
	if (period == "last_7_days") return 7;
	if (period == "last_28_days") return 28;
	if (period == "last_6_months") return 6;
	if (period == "last_year") return 12;

	return 7;
}