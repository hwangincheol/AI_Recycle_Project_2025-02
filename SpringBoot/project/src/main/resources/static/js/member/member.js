if(win_href.includes('/signup')
|| win_href.includes('/mypage')
|| win_href.includes('/find')){
        $('head').append('<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>');
}

//유효성코드 모음
//이름
let korEng = /^[가-힣A-Za-z]{2,10}$/;
//아이디
let checkId = /^[A-Za-z0-9]{5,12}$/;
//비밀번호
let checkPw = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+~`\-={}[\]:;"'<>,.?/\\]).{8,16}$/;
//전화번호
let checkTel = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$/;
//상세주소
let korEngNum = /^[가-힣A-Za-z0-9\s]+$/;

/*아이디 중복 확인*/
function idChk(){
    var id = $('#id').val();
    if(id == null || id == ""){
        alertShow('아이디 오류', '아이디를 입력하세요.');
        return false;
    }
    else if(!checkId.test(id)){
        alertShow('아이디 오류', '아이디는 영어, 숫자로만 5자 이상 15자 이하여야 합니다.');
        return false;
    }
    else{
        $.ajax({
            type: "post",
            url: "/memberChk",
            async: true,
            data: {"id":id},
            success:function(data){
                $('#id_none').hide();
                if(data == null || data == "null" || data == ""){
                    $('#id_ok').show();
                    $('#idcheck').val("ok");
                }
                else{
                    $('#id_no').show();
                    $('#idcheck').val("no");
                }
            },
            error:function(){
                alertShow('에러','아이디를 다시 입력해주세요');
            },
            beforeSend : function(xhr) {
                xhr.setRequestHeader(token_header, token_content);
            }
        });
    };
};
/*아이디 입력 시 중복확인 리셋*/
function idChkReset(){
    $('#idcheck').attr('value', 'no');
    $('#id_ok').hide();
    $('#id_no').hide();
    $('#id_none').show();
}
/*비밀번호 타입 변경*/
function pwTxtPw(ths){
    var pw_id = ths.dataset.id;
    var pw_type = $('#'+pw_id).attr('type');
    if(pw_type == 'password'){
        $('#'+pw_id).attr('type', 'text');
        $('#'+pw_id).next('button').find('img').attr('src', '/img/icon/common/eye_close.png');
    }
    else{
        $('#'+pw_id).attr('type', 'password');
        $('#'+pw_id).next('button').find('img').attr('src', '/img/icon/common/eye_open.png');
    }
}
/*비밀번호 유효성 검사*/
/*비밀번호 일치 여부*/
function pwChange(){
    let pw_val = $('#pw').val();
    let pw_val2 = $('#pw2').val();
    //유효성 검사
    if(!checkPw.test(pw_val)){
        $('#pwchk1').show();
        $("#pwcheck").attr('value', "no");
    }
    else{
        $('#pwchk1').hide();
    }
    //빈 값인지 아닌지
    if(pw_val == ''
    || pw_val2 == ''){
        $('#pwchk3').show();
        $("#pwcheck").attr('value', "no");
    }
    else{
        $('#pwchk3').hide();
    }
    //유효성 검사 통과 및 비밀번호 / 비밀번호 확인이 일치하는지
    if(pw_val == pw_val2){
        if(checkPw.test(pw_val)){
            $('#pwchk1').hide();
            $("#pwcheck").attr('value', "ok");
        }
        else{
            $('#pwchk1').show();
            $("#pwcheck").attr('value', "no");
        }
        $('#pwchk2').hide();
    }
    else{
        $('#pwchk2').show();
        $("#pwcheck").attr('value', "no");
    }
}
/*이름(한글, 영어만 가능)*/
function nameChk(){
    let name_val = $('#name').val();
    if(korEng.test(name_val)){
        $('#namechk').hide();
        $("#namecheck").attr('value', "ok");
    }
    else{
        $('#namechk').show();
        $("#namecheck").attr('value', "no");
    }
}
/*주소*/
//주소 API CDN 방식 사용
function execDaumPostcode(){
    new daum.Postcode({
        oncomplete: function(data){
            // 팝업을 통한 검색 결과 항목 클릭 시 실행
            var addr = ''; // 주소_결과값이 없을 경우 공백
            var extraAddr = ''; // 참고항목
            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R'){ // 도로명 주소를 선택
                addr = data.roadAddress;
            } else{ // 지번 주소를 선택
                addr = data.jibunAddress;
            }
            if(data.userSelectedType === 'R'){
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
            } else{
                document.getElementById("streetaddr").value = '';
            }
            // 선택된 우편번호와 주소 정보를 input 박스에 넣는다.
            document.getElementById('addr').value = data.zonecode;
            document.getElementById("streetaddr").value = addr;
            document.getElementById("streetaddr").value += extraAddr;
            // 우편번호 + 주소 입력이 완료되었음으로 상세주소로 포커스 이동
            document.getElementById("detailaddr").focus();
            $('#detailaddr').val('');
            $("#detailaddr").prop('readonly', false);
            $('#addresscheck').attr('value', 'no');
            $('#addresschk1').show();
        }
    }).open();
}
function detailAddrWrite(){
    let addr_val = $('#addr').val();
    let streetaddr_val = $('#streetaddr').val();
    let detailaddr_val = $('#detailaddr').val();
    if(addr_val == null
    || addr_val == ''
    || streetaddr_val == null
    || streetaddr_val == ''
    || detailaddr_val == null
    || detailaddr_val == ''
    ){
        $("#addresscheck").attr('value', "no");
        $('#addresschk1').show();
    }
    else{
        $('#addresschk1').hide();
        if(korEngNum.test(detailaddr_val)){
            $("#addresscheck").attr('value', "ok");
            $('#addresschk2').hide();
        }
        else{
            $("#addresscheck").attr('value', "no");
            $('#addresschk2').show();
        }
    }
}
/*전화번호 유효성 검사*/
function telWrite(){
    let tel_val1 = $('#tel1').val();
    let tel_val2 = $('#tel2').val();
    let tel_val3 = $('#tel3').val();
    if(tel_val1 == '' && tel_val2 == '' && tel_val3 == ''){
        $('#telchk1').show();
        $('#telchk2').hide();
        $('#telchk3').hide();
        $('#telchk4').hide();
    }
    else{
        $('#telchk1').hide();
        if(tel_val1 == '' || tel_val1.length < 2){
            $('#telchk2').show();
        }
        else{
            $('#telchk2').hide();
        }
        if(tel_val2 == '' || tel_val2.length < 3){
            $('#telchk3').show();
        }
        else{
            $('#telchk3').hide();
        }
        if(tel_val3 == '' || tel_val3.length < 4){
            $('#telchk4').show();
        }
        else{
            $('#telchk4').hide();
        }
    }

    let tel_whole = tel_val1 + '-' + tel_val2 + '-' + tel_val3;
    if(checkTel.test(tel_whole)){
        $('#tel_whole').attr('value', tel_whole);
        $("#telcheck").attr('value', "ok");
        $('#telchk5').hide();
    }
    else{
        $('#tel_whole').attr('value', '');
        $("#telcheck").attr('value', "no");
        $('#telchk5').show();
    }
}
/*확인 버튼 클릭시 */
function memWholeChk(){
    let idcheck = $('#idcheck').val();
    let pwcheck = $('#pwcheck').val();
    let namecheck = $('#namecheck').val();
    let addresscheck = $('#addresscheck').val();
    let telcheck = $('#telcheck').val();
    if(idcheck == 'ok'
    && pwcheck == 'ok'
    && namecheck == 'ok'
    && addresscheck == 'ok'
    && telcheck == 'ok'){
        $('#memberForm').submit();
    }else{
        alertShow("회원가입 오류", "모든 내용을 적어주세요.");
    }
}

//로그인
function loginWrite(object, e){
    let object_id = object.dataset.id;
    let object_type = object.dataset.type;
    let object_val = $('#'+object_id).val();
    if(object_val != ''){
        $('#'+object_type).addClass('keyon');
        $('#'+object_id).parents('.member_row').find('.common_input').addClass('focus_on');
    }
    else{
        $('#'+object_type).removeClass('keyon');
        $('#'+object_id).parents('.member_row').find('.common_input').removeClass('focus_on');
    }
    if(e.keyCode == '13'){
        $('.btn_submit').click();
    }
}
function idPwChk(){
    let id_val = $('#id').val();
    let pw_val = $('#pw').val();
    $.ajax({
        type: "post",
        url: "/idpwChk",
        async: true,
        data: {"id":id_val,"pw":pw_val},
        success:function(data){
            if(data=='true'){
                standbyShow("로그인 성공", "성공적으로 로그인 되었습니다.<br/>잠시만 기다려주세요.");
                setTimeout(function(){
                    $('#memberLogin').submit();
                },1500);
            }else{
                alertShow("로그인 실패","아이디 혹은 비밀번호가 다릅니다.");
                return false;
            }
        },
        error:function(data){
            alertShow("로그인 오류","다시 한 번 시도해주세요.");
            return false;
        },
        beforeSend : function(xhr) {
            xhr.setRequestHeader(token_header, token_content);
        }
    });
}

/*마이페이지*/
/*정보 수정 시 비밀번호 확인*/
function pwChkKeyup(e){
    if(e.keyCode == '13'){
        $('.btn_submit').click();
    }
}
function mypageGo(){
    let my_id = $('#header_mypage').text();
    let my_pw = $('#pw').val();
    if(my_pw == '' || my_pw == null){
        alertShow('입력 오류', '비밀번호를 입력해주세요.');
        return false;
    }
    $.ajax({
        type: "post",
        url: "/idpwChk",
        async: true,
        data: {"id":my_id,"pw":my_pw},
        success:function(data){
            if(data=='true'){
                standbyShow("확인 성공", "정보 수정 페이지로 이동합니다.<br/>잠시만 기다려주세요.");
                setTimeout(function(){
                    window.location.href='/mypage';
                },1500);
            }else{
                alertShow("확인 실패","비밀번호가 다릅니다.");
                return false;
            }
        },
        error:function(data){
            alertShow("확인 오류","다시 한 번 시도해주세요.");
            return false;
        },
         beforeSend : function(xhr) {
             xhr.setRequestHeader(token_header, token_content);
         }
    });
}
function mypagePwShow(){
    $('body').css('overflow', 'hidden');
    $('.pwchkpop_whole').show();
    $('#pw').focus();
}
function mypagePwHide(){
    $('body').css('overflow', 'auto');
    $('.pwchkpop_whole').hide();
}
function pwChangeShow(ths){
    let pw_id = ths.dataset.id;
    let pw_type = ths.dataset.type;
    $('#pw').val('');
    $('#pw2').val('');
    $('#pw').attr('type', 'password');
    $('#pw2').attr('type', 'password');
    $('#pwchk1').hide();
    $('#pwchk2').hide();
    $('.member_pw').find('img').attr('src', '/img/icon/common/eye_open.png');
    $('#pwcheck').attr('value', 'no');
    if(pw_type == 'off'){
        $('.common_pw').show();
        $('#'+pw_id).text('수정 취소');
        $('#'+pw_id).attr('data-type', 'on');
    }
    else{
        $('.common_pw').hide();
        $('#'+pw_id).text('비밀번호 수정');
        $('#'+pw_id).attr('data-type', 'off');
    }
}
function myAllChk(){
    let pw_change_val = $('#pw_change_btn').attr('data-type');
    let pwcheck = $('#pwcheck').val();
    let addresscheck = $('#addresscheck').val();
    let telcheck = $('#telcheck').val();
    if(pw_change_val == 'off'){
        if(addresscheck != 'ok'
        || telcheck != 'ok'
        ){
            alertShow('수정 실패', '아이디, 이름, 비밀번호를 제외한 정보를 입력해주세요.');
            return false;
        }
    }
    else{
        if(addresscheck != 'ok'
        || telcheck != 'ok'
        || pwcheck != 'ok'
        ){
            alertShow('수정 실패', '아이디, 이름을 제외한 정보를 입력해주세요.');
            return false;
        }
    }
    standbyShow("정보 수정 중", "정보를 수정 중입니다.<br/>잠시만 기다려주세요.");
    setTimeout(function(){
        $('#myForm').submit();
    },1000);
}
//아이디/비밀번호 찾기
function memFindChk(){
    let val_id = $('#id').val();
    let val_name = $('#name').val();
    let val_tel_whole = $('#tel_whole').val();
    if(win_href.includes('?type=pw')){
        if(val_id != ''
        && val_name != ''
        && val_tel_whole != ''
        ){
            $.ajax({
                type: "post",
                url: "/pwFind",
                async: true,
                data: {
                    "id":val_id,
                    "name":val_name,
                    "tel":val_tel_whole
                },
                success:function(data){
                    if(data == null || data == "null" || data == ""){
                        alertShow("확인 결과", "등록된 정보가 없습니다.");
                    }
                    else{
                        standbyShow("확인 완료", "비밀번호 재설정 페이지로 이동합니다.<br/>잠시만 기다려주세요.");
                        setTimeout(function(){
                            window.location.href='/password?id='+data;
                        },1500);
                    }
                },
                error:function(data){
                    alertShow("확인 오류","다시 한 번 시도해주세요.");
                    return false;
                },
                beforeSend : function(xhr) {
                    xhr.setRequestHeader(token_header, token_content);
                }
            });
        }else{
            alertShow("확인 실패","모든 정보를 입력해주세요.");
        }
    }
    else{
        if(val_name != ''
        && val_tel_whole != ''
        ){
            $.ajax({
                type: "post",
                url: "/idFind",
                async: true,
                data: {
                    "name":val_name,
                    "tel":val_tel_whole
                },
                success:function(data){
                    if(data == null || data == "null" || data == ""){
                        alertShow("확인 결과", "등록된 정보가 없습니다.");
                    }
                    else{
                        alertShow("확인 완료", "회원님의 아이디는 "+data+" 입니다.");
                    }
                },
                error:function(){
                    alertShow("확인 오류","다시 한 번 시도해주세요.");
                },
                beforeSend : function(xhr) {
                    xhr.setRequestHeader(token_header, token_content);
                }
            });
        }else{
            alertShow("확인 실패","모든 정보를 입력해주세요.");
        }
    }
}
function pwOkChk(){
    let pw_chk_val = $('#pwcheck').val();
    if(pw_chk_val == 'no'){
        alertShow("비밀번호 오류","비밀번호를 확인해주세요.");
        return false;
    }
    standbyShow("비밀번호 재설정 중", "비밀번호를 재설정 중입니다.<br/>잠시만 기다려주세요.");
    $('#pwSetting').submit();
}
//모든 비밀번호 입력 시 input outline 대신 상위 div border 색상 변하도록
function pwWholeKeyup(ths){
    let pw_common_id = ths.dataset.id;
    $('#'+pw_common_id).parents('.member_pw').css('border-color','rgb(var(--lite-yellow))');
}
function pwWholeKeydw(ths){
    let pw_common_id = ths.dataset.id;
    $('#'+pw_common_id).parents('.member_pw').css('border-color','rgb(var(--lite-gray))');
}
$(document).ready(function(){
    if(win_path.includes('/mypage') && win_path != ('/mypage/list')){
        let my_tel_whole = $('#tel_whole').val().split('-');
        $('#tel1').val(my_tel_whole[0]);
        $('#tel2').val(my_tel_whole[1]);
        $('#tel3').val(my_tel_whole[2]);
    }
    else if(win_path.includes('/find')){
        $('.find_link').removeClass('find_on');
        if(win_href.includes('?type=pw')){
            $('.find_link2').addClass('find_on');
            $('.find_pw').show();
        }
        else{
            $('.find_link1').addClass('find_on');
            $('.find_pw').hide();
        }
    }
});