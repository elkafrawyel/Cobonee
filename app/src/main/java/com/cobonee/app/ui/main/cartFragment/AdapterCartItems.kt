package com.cobonee.app.ui.main.cartFragment

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cobonee.app.R
import com.cobonee.app.entity.Offer

class AdapterCartItems : BaseQuickAdapter<Offer, BaseViewHolder>(R.layout.item_cart_view) {

    override fun convert(helper: BaseViewHolder, offer: Offer) {

//        val total: Float = offer.priceAfterDiscount!! * offer.quantity

        Glide.with(mContext).load(offer.photos!![0]!!.original).into(helper.getView(R.id.item_image))
        helper.setText(R.id.titles1, offer.offerHeader)
//            .setText(R.id.coast, total.toString())
//            .setText(R.id.coboneeQuantityValueTv,offer.quantity)

        helper.addOnClickListener(R.id.icon_delete)
    }

}