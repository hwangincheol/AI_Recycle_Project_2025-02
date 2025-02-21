
    function page(page){
        var status = new URLSearchParams(window.location.search).get('status');
        if (status) {
            location.href = "/orderList/" + page + "?status=" + status;
        } else {
            location.href = "/orderList/" + page;
        }
    }

    function confirmCancel(orderId) {
        confirmShow('취소 확인', '주문을 취소하시겠습니까?', orderId);
    }

    function confirmOk(orderId){
        cancelOrder(orderId);
    }

    function cancelOrder(orderId) {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        var url = "/orderCancel/" + orderId;
        var paramData = {
            orderId : orderId
        };
        var order_num = $('#order_num').val();
        var param = JSON.stringify(paramData);

        $.ajax({
            url      : url,
            type     : "POST",
            contentType : "application/json",
            data     : param,
            beforeSend : function(xhr){
                xhr.setRequestHeader(header, token);
            },
            dataType : "json",
            cache   : false,
            success  : function(result, status){

                var page = order_num;  // Thymeleaf에서 현재 페이지 정보를 가져옵니다
                var status = new URLSearchParams(window.location.search).get('status');
                if (status) {
                    location.href = '/orderList/' + page + "?status=" + status;
                } else {
                    location.href = '/orderList/' + page;
                }
            },
            error : function(jqXHR, status, error){
                if(jqXHR.status == '401'){
                    alert('로그인 후 이용해주세요');
                    location.href='/login';
                } else{
                    alert(jqXHR.responseText);
                }
            }
        });
    }