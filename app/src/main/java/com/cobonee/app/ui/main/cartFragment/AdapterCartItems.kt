package com.cobonee.app.ui.main.cartFragment

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cobonee.app.R
import com.cobonee.app.entity.Coubone
import com.cobonee.app.entity.Offer

class AdapterCartItems : BaseQuickAdapter<Coubone, BaseViewHolder>(R.layout.item_cart_view) {

    override fun convert(helper: BaseViewHolder, coubone: Coubone) {

        val total: Float = coubone.priceAfterDiscount!! * coubone.quantity

        Glide.with(mContext).load(coubone.photos!![0]!!.original).into(helper.getView(R.id.item_image))
        helper.setText(R.id.titles1, coubone.offerHeader)
            .setText(R.id.coast, total.toString())
            .setText(R.id.coboneeQuantityValueTv,coubone.quantity.toString())

        helper.addOnClickListener(R.id.icon_delete)
    }

}