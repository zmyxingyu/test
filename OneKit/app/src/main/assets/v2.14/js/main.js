var Order = {
    init: function () {
        var that = this;
        that.bindEvent();
    },
    bindEvent: function () {
        var that = this;
        $('#pay').on('click',  function () {
            that.showPay();
        });
        $('.backdrop').on('click',  function () {
            that.hidePay();
        });
        $('.pay-method-item').on('click', function () {
            $(this).find('.radio').addClass('selected');
            $(this).siblings('.pay-method-item').find('.radio').removeClass('selected');
        });
        $('.packages li').on('click', function () {
            var price = $(this).data('real');
            $('.realPayMoney').text(price);
            $('.pay-total').text(price + '元');
            $(this).addClass('selected');
            $(this).siblings('li').removeClass('selected');
        });
    },
    showPay: function () {
        var that = this;
        $('#J-pay-mod').removeClass('slideOutDown').addClass('slideInUp');
        $('.backdrop').show();
    },
    hidePay: function () {
        var that = this;
        $('#J-pay-mod').removeClass('slideInUp').addClass('slideOutDown');
        $('.backdrop').hide();
    }
}