package com.cobonee.app.ui.main.helpFragment

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cobonee.app.R
import com.cobonee.app.entity.Quetion

class AdapterQuestions : BaseQuickAdapter<Quetion, BaseViewHolder>(R.layout.item_questions_view) {

    override fun convert(helper: BaseViewHolder, quetion: Quetion) {
        helper.setText(R.id.questionsHeaderTv, quetion.title)
              .setText(R.id.questionsBody, quetion.content)
    }

}