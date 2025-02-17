//list
function goBoardDetail(id){
    location.href='/board/detail/'+id;
}
//detail
function boardDelete() {
    confirmShow('삭제 확인', '정말 삭제하시겠습니까?<br/>한 번 삭제하시면 되돌릴 수 없습니다.', 'boardDelete');
}
function confirmOk(object){
    if(object == 'boardDelete'){
        standbyShow('삭제 진행', '삭제 진행 중입니다.<br/>잠시만 기다려주세요.');
        setTimeout(function(){
            $('#deleteForm').submit();
        },1500);
    }
}