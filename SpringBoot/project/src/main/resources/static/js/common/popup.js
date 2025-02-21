//팝업
//팝업 공통 닫기
function commonHide(){
    $('body').css('overflow', 'auto');
    $('.common_pop').hide();
}
//alert
function alertHide(){
    $('body').css('overflow', 'auto');
    $('.alert_whole').hide();
}
function alertShow(title, subtitle){
    $('body').css('overflow', 'hidden');
    $('.alert_whole').show();
    $('#alert_title').html(title);
    $('#alert_subtitle').html(subtitle);
    $('#common_pop_input').focus();
}
//confirm
function confirmHide(){
    $('body').css('overflow', 'auto');
    $('.confirm_whole').hide();
}
function confirmShow(title, subtitle, type){
    $('body').css('overflow', 'hidden');
    $('.confirm_whole').show();
    $('#confirm_title').html(title);
    $('#confirm_subtitle').html(subtitle);
    $('#confirm_message').attr('value', type);
    $('#common_pop_input').focus();
}
function popConfirm(){
    let confirm_message = $('#confirm_message').val();
    if(confirm_message == 'none'){
        commonHide();
    }
    else{
        confirmHide();
        confirmOk(confirm_message);
    }
}
//standby
function standbyHide(){
    $('body').css('overflow', 'auto');
    $('.standby_whole').hide();
}
function standbyShow(title, subtitle){
    $('body').css('overflow', 'hidden');
    $('.standby_whole').show();
    $('#standby_title').html(title);
    $('#standby_subtitle').html(subtitle);
}

function enterClick(e){
    if(e.keyCode == '13'){
        $('.btn_confirm').click();
    }
}