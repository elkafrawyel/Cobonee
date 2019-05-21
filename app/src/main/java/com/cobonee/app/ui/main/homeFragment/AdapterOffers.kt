package com.cobonee.app.ui.main.homeFragment

import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cobonee.app.R
import com.cobonee.app.entity.Offer
import com.cobonee.app.entity.OfferPhoto
import com.cobonee.app.ui.main.detailsFragment.ImageSliderAdapter
import com.rd.PageIndicatorView

class AdapterOffers : BaseQuickAdapter<Offer, BaseViewHolder>(R.layout.item_offer_view) {

    override fun convert(helper: BaseViewHolder, offer: Offer) {

        val discount = mContext.resources.getString(R.string.label_discount) + "  ${offer.discount}%"
        val price = mContext.resources.getString(R.string.label_price, offer.price)
        helper.setText(R.id.offerOwnerTv, offer.ownerName)
            .setText(R.id.offerDiscountPercentTv, discount)
            .setText(R.id.offerHeaderTv, offer.offerHeader)
            .setText(R.id.offerBodyTv, offer.offerBody)
            .setText(R.id.offerDiscountPriceTv, offer.priceAfterDiscount.toString())
            .setText(R.id.offerPriceTv, price)
            .addOnClickListener(R.id.offerCv, R.id.offerSaveImgv)


        helper.getView<ViewPager>(R.id.bannerSliderVp).adapter = ImageSliderAdapter().also {
            val images: List<OfferPhoto> = listOf(
                offer.photos?.get(0) as OfferPhoto,
                offer.photos[0] as OfferPhoto,
                offer.photos[0] as OfferPhoto,
                offer.photos[0] as OfferPhoto
            )
            it.submitList(images)
        }
        helper.getView<PageIndicatorView>(R.id.pageIndicator).setViewPager(helper.getView(R.id.bannerSliderVp))

        Glide.with(mContext).load(offer.photos!![0]!!.large).into(helper.getView(R.id.offerImgv))

        if (offer.isFav) {
            helper.setImageResource(R.id.offerSaveImgv, R.drawable.ic_favorite_white)
        } else {
            helper.setImageResource(R.id.offerSaveImgv, R.drawable.ic_favorite_stock)
        }

    }
}