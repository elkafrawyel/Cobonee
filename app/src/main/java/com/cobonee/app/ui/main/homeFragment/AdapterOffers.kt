package com.cobonee.app.ui.main.homeFragment

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cobonee.app.R
import com.cobonee.app.entity.Offer
import com.cobonee.app.utily.Injector

class AdapterOffers : BaseQuickAdapter<Offer, BaseViewHolder>(R.layout.item_offer_view) {

    override fun convert(helper: BaseViewHolder, offer: Offer) {

        val discount = mContext.resources.getString(R.string.label_discount, offer.discount)+"%"
        val price = mContext.resources.getString(R.string.label_price, offer.price)
        helper.setText(R.id.offerOwnerMbtn, "OwnerName")
            .setText(R.id.offerDiscountPercentTv, discount)
            .setText(R.id.offerHeaderTv, offer.name)
            .setText(R.id.offerBodyTv, offer.description)
            .setText(R.id.offerDiscountPriceTv, offer.priceAfterDiscount.toString())
            .setText(R.id.offerPriceTv, price)
            .addOnClickListener(R.id.offerCv, R.id.offerSaveImgv)
    }
}