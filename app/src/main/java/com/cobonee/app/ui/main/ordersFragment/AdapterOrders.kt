package com.cobonee.app.ui.main.ordersFragment

import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cobonee.app.R
import com.cobonee.app.entity.DataOrders

class AdapterOrders : BaseQuickAdapter<DataOrders, BaseViewHolder>(R.layout.item_orders_view) {

    override fun convert(helper: BaseViewHolder, item: DataOrders) {

        val discount = mContext.resources.getString(R.string.label_discount) + "  ${item.offer.discount}%"
        val price = mContext.resources.getString(R.string.label_price,  item.offer.price)
        helper.setText(R.id.orderCodeValueTv, item.coupon_number.toString())
            .setText(R.id.orderQuantityValueTv, item.quantity.toString())
            .setText(R.id.orderDescTv, item.offer.offerHeader.toString())
            .setText(R.id.orderDiscountPercentTv, discount)
            .setText(R.id.orderOwnerMbtn, item.offer.ownerName.toString())

        if(item.status != "new")
        helper.setVisible(R.id.orderExpiredImgv,true)
    }

}