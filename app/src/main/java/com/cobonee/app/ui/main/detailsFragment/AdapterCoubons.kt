package com.cobonee.app.ui.main.detailsFragment

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cobonee.app.R
import com.cobonee.app.entity.Coubone

class AdapterCoubons : BaseQuickAdapter<Coubone, BaseViewHolder>(R.layout.item_details_cobone_type_view) {

    override fun convert(helper: BaseViewHolder, coubone: Coubone) {
        val total: Float = coubone.priceAfterDiscount!! * coubone.quantity
        helper
            .setText(R.id.coboneeTypeDesc, coubone.offerHeader)
            .setText(R.id.offerDiscountPriceTv, total.toString())
            .setText(R.id.coboneeQuantityValueTv, coubone.quantity.toString())
            .addOnClickListener(
                R.id.increaseCoboneeQuantity,
                R.id.decreaseCoboneeQuantity,
                R.id.offerDetailsItemAddToCart
            )

        if (coubone.quantity>0){
            helper.setEnabled(R.id.offerDetailsItemAddToCart,true)
        }else{
            helper.setEnabled(R.id.offerDetailsItemAddToCart,false)
        }

    }
}