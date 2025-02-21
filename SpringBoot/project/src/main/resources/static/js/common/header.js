//헤더
function goHome(){
    if(win_path == '/' || win_path == '/main'){
        window.scrollTo({
            top: 0, behavior: 'smooth'
        });
    }
    else{
        window.location.href = '/';
    }
}//헤더 이미지 클릭 시 pathname에 따라 index 페이지 이동
function goMenu(){
    $('.header_top').toggleClass('header_on');
}//햄버거 메뉴 클릭시