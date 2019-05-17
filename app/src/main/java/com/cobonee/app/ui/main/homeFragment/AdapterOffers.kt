package com.cobonee.app.ui.main.homeFragment

import android.graphics.drawable.Drawable
import android.widget.Adapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cobonee.app.R
import com.cobonee.app.entity.Offer
import com.cobonee.app.ui.main.detailsFragment.ImageSliderAdapter
import com.rd.PageIndicatorView

class AdapterOffers : BaseQuickAdapter<Offer, BaseViewHolder>(R.layout.item_offer_view) {


    override fun convert(helper: BaseViewHolder, offer: Offer) {

        val imageSliderAdapter = ImageSliderAdapter().also {
            it.submitList(offer.photos!!.filterNotNull())
        }
        val discount = mContext.resources.getString(R.string.label_discount) + "  ${offer.discount}%"
        val price = mContext.resources.getString(R.string.label_price, offer.price)
        helper.setText(R.id.offerOwnerTv, offer.ownerName)
            .setText(R.id.offerDiscountPercentTv, discount)
            .setText(R.id.offerHeaderTv, offer.offerHeader)
            .setText(R.id.offerBodyTv, offer.offerBody)
            .setText(R.id.offerDiscountPriceTv, offer.priceAfterDiscount.toString())
            .setText(R.id.offerPriceTv, price)
            .addOnClickListener(R.id.offerCv, R.id.offerSaveImgv)
            .setAdapter(R.id.bannerSliderVp,imageSliderAdapter as Adapter)
        helper.getView<PageIndicatorView>(R.id.pageIndicator).setViewPager(helper.getView<ViewPager>(R.id.bannerSliderVp))
        Glide.with(mContext).load(offer.photos!![0]!!.large).addListener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable?>?,
                isFirstResource: Boolean
            ): Boolean {
                helper.setGone(R.id.offerImageLoading, false)
                helper.setImageResource(R.id.offerImgv, R.drawable.logo)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                helper.setGone(R.id.offerImageLoading, false)
                return false
            }
        }).into(helper.getView(R.id.offerImgv))

        if (offer.isFav) {
            helper.setImageResource(R.id.offerSaveImgv,R.drawable.ic_favorite_white)
        } else {
            helper.setImageResource(R.id.offerSaveImgv,R.drawable.ic_favorite_stock)
        }

    }
}