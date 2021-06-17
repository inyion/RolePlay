package com.samsia.roleplay

import com.samsia.roleplay.databinding.ActivityMainBinding
import com.samsia.roleplay.viewmodel.TodoViewModel

/**
 * todo list idea
 * 1. 작성란은? edit text하나로 검색도, 추가도 간편하게
 *  detail? 입력까지는 검색, 빈칸이 아니면 ADD 버튼 활성 검색 조건은 Tag %검색어%, 할일 %검색어%
 *          이영역은 제스처나 작은 버튼으로 애니메이션되어 나타나는게 화면을 가리지 않아 좋을듯
 *
 * 2. 리스트영역은? 상세보기나 설정을 하는곳
 *  detail? 상세보기까지 있는 만큼 의미가 있는 작업을 만드는 공간이 되었으면 좋겠고 이뻐야함
 *          상세보기가 없는경우, 일상적인 할일을 잊기 않기 위해 담는정도인게 좋겠고 완료된 할일은 자동 삭제되어야할듯
 *
 *
 *  ETC -
 *      검색기능 (이미지검색, 영상검색, 상품검색(남의 상품 안됨 이슈))
 *      활동로그 (할일들은 자동삭제되도 내가 열심히 살았는지는 기록이 남아 일정기간뒤에 본인이 체크)
 *      컨텐츠 제작 기능
 *          1. 내가 찾고 연구한 나만의 정보가 의미있게 남기거나 또는 발행 마크다운 활용?
 *          2. 누구나 알고리즘을 쉽게 만들도록 json을 활용해서 컨텐츠를 만들고 놀고 발행
 *              ex) { "type" : "batch_game", "input_arr" : ["가위", "바위", "보"], "rule" : {} }
 *
 */
class MainActivity : ViewModelActivity() {

    override fun onCreate() {
        setLayout(R.layout.activity_main, TodoViewModel::class.java)
        (viewModel as TodoViewModel).initDatabase(this)

    }

    fun getBinding(): ActivityMainBinding {
        return viewBinding as ActivityMainBinding
    }

}