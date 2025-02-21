$(document).ready(function() {
    var selectedItem = null;

    // 품목 클릭 시 선택된 품목 표시
    $('.item-btn').on('click', function() {
        $('.item-btn').removeClass('bg-success text-white');
        $(this).addClass('bg-success text-white');
        selectedItem = $(this).attr('id');
        $('.select-btn').text($(this).text() + ' 선택됨 (품목 추가)');
    });

    // "추가할 품목을 선택하세요" 버튼 클릭 시 선택된 품목 추가
    $('.select-btn').on('click', function() {
        if (selectedItem) {
            var itemName = $('#' + selectedItem).text();
            var itemPrice = $('#' + selectedItem).data('price');
            var existingItem = $('#selectedItemsContainer .selected-item[data-item-id="' + selectedItem + '"]');

            if (existingItem.length > 0) {
                var countInput = existingItem.find('.item-count');
                countInput.val(parseInt(countInput.val()) + 1);
                updatePrice(existingItem, itemPrice);
            } else {
                var selectBox = $('<select class="form-select"></select>');
                // 품목 리스트를 셀렉트박스 옵션에 추가
                $('.item-btn').each(function() {
                    var itemOption = $('<option></option>');
                    itemOption.val($(this).attr('id')).text($(this).text());
                    selectBox.append(itemOption);
                });
                var countInput = $('<input type="number" class="form-control item-count" min="1" value="1" data-price="' + itemPrice + '">');
                var priceInput = $('<input type="text" class="form-control item-price" readonly placeholder="' + itemPrice + ' 원">');
                var deleteBtn = $('<button class="btn btn-danger btn-sm delete-btn">삭제</button>');

                selectBox.find('option').each(function() {
                    if ($(this).val() === selectedItem) {
                        $(this).prop('selected', true);
                    }
                });

                var itemContainer = $('<div class="selected-item" data-price="' + itemPrice + '" data-item-id="' + selectedItem + '"></div>');
                itemContainer.append(selectBox, countInput, priceInput, deleteBtn);

                $('#selectedItemsContainer').append(itemContainer);
                updatePrice(itemContainer, itemPrice);
            }

            $('.item-btn').removeClass('bg-success text-white');
            selectedItem = null;
            $('.select-btn').text('추가할 품목을 선택하세요');

            getOrderTotalPrice();
        }
    });

    // 수량 입력 시 1 미만 방지
    $(document).on('input', '.item-count', function() {
        var count = parseInt($(this).val());
        if (isNaN(count) || count < 1) {
            $(this).val(1);
        }
        var itemPrice = $(this).closest('.selected-item').data('price');  // 고유 가격 가져오기
        itemPrice = parseInt(itemPrice);  // 값이 문자열일 수 있으므로 숫자로 변환
        updatePrice($(this).closest('.selected-item'), itemPrice); // 가격 업데이트
    });

    // 삭제 버튼 기능
    $(document).on('click', '.delete-btn', function() {
        $(this).closest('.selected-item').remove();
        getOrderTotalPrice();
    });

    // 품목 변경 시, 선택된 품목을 업데이트
    $(document).on('change', '.form-select', function() {
        var newItemId = $(this).val();
        var itemContainer = $(this).closest('.selected-item');
        var itemPrice = $('#' + newItemId).data('price');

        itemContainer.remove(); // 기존 품목 삭제

        // 버튼 클릭처럼 처리: 품목 추가
        selectedItem = newItemId;  // 수동으로 선택된 품목을 설정
        $('.item-btn').removeClass('bg-success text-white'); // 기존 선택된 버튼 상태 해제
        $('#' + newItemId).addClass('bg-success text-white'); // 새로운 품목 버튼 선택
        $('.select-btn').text($('#' + newItemId).text() + ' 선택됨 (품목 추가)'); // 버튼 텍스트 업데이트

        // 버튼 클릭 이벤트 트리거
        $('.select-btn').trigger('click');
    });

    // 가격 업데이트 함수
    function updatePrice(itemContainer, itemPrice) {
        var count = parseInt(itemContainer.find('.item-count').val());  // 수량
        if (isNaN(count) || count < 1) {
            count = 1;  // 수량이 1 미만이면 1로 설정
        }
        var totalPrice = itemPrice * count;  // 총 가격 계산
        itemContainer.find('.item-price').val(totalPrice + ' 원');  // 가격 입력란에 업데이트
        getOrderTotalPrice();
    }

    //총 금액 계산
    function getOrderTotalPrice(){
        var orderTotalPrice = 0;
        $('.selected-item').each(function() {
            var price = $(this).find('.item-price').val().replace(' 원', '');
            orderTotalPrice += parseInt(price);
        });

        $("#orderTotalPrice").html(orderTotalPrice.toLocaleString() + '원');
    }

    // 수거 신청 버튼 - order 데이터베이스에 넣기
    $('#collectBtn').on('click', function() {

            if ($('.selected-item').length === 0) {
                alertShow("안내", "선택된 품목이 없습니다. 품목을 선택해주세요.");
                return; // 품목이 없으면 함수를 종료
            }

        confirmShow("수거 신청", "선택한 품목을 확인하고 신청하시겠습니까?", "confirm")

    });

    // confirmOk 함수
    window.confirmOk = function(confirm_message) {
        var form = $('<form>', {
            action: '/order',
            method: 'post'
        });

        // CSRF 토큰 추가
        form.append($('<input>', {
            type: 'hidden',
            name: '_csrf',
            value: $('meta[name="_csrf"]').attr('content') // meta 태그에서 CSRF 토큰 값 가져오기
        }));

        $('.selected-item').each(function(index) {
            var itemName = $(this).find('select').val();
            var count = $(this).find('.item-count').val();
            var price = $('#orderTotalPrice').text().replace(/,/g, '').replace('원', '').trim();

            form.append($('<input>', { type: 'hidden', name: 'orderItems[' + index + '].itemName', value: itemName }));
            form.append($('<input>', { type: 'hidden', name: 'orderItems[' + index + '].count', value: count }));
            form.append($('<input>', { type: 'hidden', name: 'orderItems[' + index + '].orderPrice', value: price }));
        });

        $('body').append(form);
        form.submit();
    }

    $("#uploadImageLogin").click(function(event) {
        event.preventDefault();
        alertShow("이용 안내", "❌ 로그인이 필요한 기능입니다.")
    });

    $("#uploadVideoLogin").click(function(event) {
        event.preventDefault();
        alertShow("이용 안내", "❌ 로그인이 필요한 기능입니다.")
    });

    $("#uploadImage").click(function() {
        $("#uploadImage").val('');
        $("#responseData").html("");
    });

    $("#uploadImage").change(function(){

        const file = this.files[0];
        const allowedTypes = ["image/png", "image/jpeg"];
        if (!allowedTypes.includes(file.type)) {
          alertShow("안내", "❌ PNG 또는 JPEG 파일만 업로드 가능합니다.");
          return;
        }

        standbyShow("AI 탐지 중", "이미지를 분석 중입니다. 잠시만 기다려 주세요.");

        var formData = new FormData();
        formData.append("file", file);

        $.ajax({
            url: "/image_service",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            beforeSend: function(xhr) {
                var csrfToken = $("meta[name='_csrf']").attr("content");
                var csrfHeader = $("meta[name='_csrf_header']").attr("content");
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function(response) {
                standbyHide();
                console.log(response);
                if (typeof response === "string") {
                    response = JSON.parse(response);
                }

                var resultValues = response.json_data.result;
                var items = [];

                if (resultValues.length === 0) {
                    $("#responseData").html('<p style="font-size: 18px; text-align: center;">탐지된 품목이 없습니다.</p>');
                } else {
                    resultValues.forEach(function(item) {
                        items.push(item);
                    });

                    var resultHtml = '';
                    items.forEach(function(item) {
                        resultHtml += '<input type="text" class="form-control mb-2" value="' + item + '" readonly style="text-align: center;">';
                    });

                    $("#responseData").html(resultHtml);
                }
                $('#responseModal').modal('show');
            },
            error: function(xhr, status, error) {
                console.error("ERROR: " + error);
            },
             complete: function() {
                 standbyHide();
             }
        });
    });

    $("#addItemBtn").click(function() {
        // 모달에서 추가된 품목 가져오기
        var items = $("#responseData input").map(function() {
            return $(this).val();
        }).get();

        // 모달에서 품목이 선택되었을 경우, 각 품목을 처리
        items.forEach(function(item) {
            // 품목 클릭 시 선택된 품목 표시
            var selectedItem = item;  // 모달에서 추가된 품목의 itemName (아이디)

            // 품목에 대한 가격 가져오기 (여기서 item은 itemName)
            var itemPrice = $('#' + selectedItem).data('price');  // 'data-price'에서 가격 가져오기
            var existingItem = $('#selectedItemsContainer .selected-item[data-item-id="' + selectedItem + '"]');

            if (existingItem.length > 0) {
                // 품목이 이미 있으면 수량 증가
                var countInput = existingItem.find('.item-count');
                countInput.val(parseInt(countInput.val()) + 1);
                updatePrice(existingItem, itemPrice);
            } else {
                // 품목이 없으면 새로운 품목 추가
                var selectBox = $('<select class="form-select"></select>');
                // 품목 리스트를 셀렉트박스 옵션에 추가
                $('.item-btn').each(function() {
                    var itemOption = $('<option></option>');
                    itemOption.val($(this).attr('id')).text($(this).text());
                    selectBox.append(itemOption);
                });
                var countInput = $('<input type="number" class="form-control item-count" min="1" value="1" data-price="' + itemPrice + '">');
                var priceInput = $('<input type="text" class="form-control item-price" readonly placeholder="' + itemPrice + ' 원">');
                var deleteBtn = $('<button class="btn btn-danger btn-sm delete-btn">삭제</button>');

                selectBox.find('option').each(function() {
                    if ($(this).val() === selectedItem) {
                        $(this).prop('selected', true);
                    }
                });

                var itemContainer = $('<div class="selected-item" data-price="' + itemPrice + '" data-item-id="' + selectedItem + '"></div>');
                itemContainer.append(selectBox, countInput, priceInput, deleteBtn);

                $('#selectedItemsContainer').append(itemContainer);
                updatePrice(itemContainer, itemPrice);
            }

        });

        // 모달 닫기
        $('#responseModal').modal('hide');

        // 총 금액 업데이트
        getOrderTotalPrice();
    });


});
