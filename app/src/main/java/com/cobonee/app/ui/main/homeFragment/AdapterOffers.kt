package com.cobonee.app.ui.main.homeFragment

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cobonee.app.R

class AdapterOffers : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_offer_view) {

    override fun convert(helper: BaseViewHolder, item: String) {

        helper.addOnClickListener(R.id.offerCv,R.id.offerSaveImgv)
    }

}