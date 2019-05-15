package com.cobonee.app.ui.main.searchFragment

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cobonee.app.R
import com.cobonee.app.entity.Offer

class AdapterSearch : BaseQuickAdapter<Offer, BaseViewHolder>(R.layout.item_search_offer_view) {

    override fun convert(helper: BaseViewHolder, offer: Offer) {
        helper.setText(R.id.offerSearchTitle, offer.offerHeader)
            .addOnClickListener(R.id.offerSearchTitle)

    }
}