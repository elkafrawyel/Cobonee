package com.cobonee.app.ui.main.cartFragment

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cobonee.app.R

class AdapterCartItems : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_cart_view) {

    override fun convert(helper: BaseViewHolder, item: String) {

        helper.addOnClickListener(R.id.offerCv,R.id.offerSaveImgv)
    }

}