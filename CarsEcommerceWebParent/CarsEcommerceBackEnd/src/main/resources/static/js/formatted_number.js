$(document).ready(function() {
    var price = $('#formatted-price').text();
    $('#price-input').on('change', function() {
        price = $(this).val();
        $('#formatted-price').text($.formatCurrency(price));
    });
});