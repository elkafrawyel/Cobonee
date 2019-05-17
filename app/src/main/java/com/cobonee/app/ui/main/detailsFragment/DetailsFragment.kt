package com.cobonee.app.ui.main.detailsFragment

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chad.library.adapter.base.BaseQuickAdapter

import com.cobonee.app.R
import com.cobonee.app.entity.Coubone
import com.cobonee.app.entity.Offer
import com.cobonee.app.entity.OfferPhoto
import kotlinx.android.synthetic.main.details_fragment.*
import kotlinx.android.synthetic.main.details_fragment.offerBodyTv
import kotlinx.android.synthetic.main.details_fragment.offerDiscountPercentTv
import kotlinx.android.synthetic.main.details_fragment.offerDiscountPriceTv
import kotlinx.android.synthetic.main.details_fragment.offerHeaderTv
import kotlinx.android.synthetic.main.details_fragment.offerImageLoading
import kotlinx.android.synthetic.main.details_fragment.offerOwnerTv
import kotlinx.android.synthetic.main.details_fragment.offerPriceTv
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timerTask

class DetailsFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = DetailsFragment()
    }

    private lateinit var viewModel: DetailsViewModel
    private var offer: Offer? = null

    private val imageSliderAdapter = ImageSliderAdapter()
    private val adapterCoubons = AdapterCoubons()
    private var timer: Timer? = null
    var coubones: ArrayList<Coubone> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        arguments?.let {
            offer = DetailsFragmentArgs.fromBundle(it).offer
            offer?.let {
                viewModel.offer = offer
                setOffer(offer!!)
            }
        }

        offerAddToCart.setOnClickListener {
            addOfferToCart()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterCoubons.onItemChildClickListener = this
        offerCoboneeRv.adapter = adapterCoubons
        bannerSliderVp.adapter = imageSliderAdapter
        pageIndicator.setViewPager(bannerSliderVp)

    }

    private fun addOfferToCart() {
        findNavController().navigate(R.id.cartFragment)
    }

    private fun setOffer(offer: Offer) {

        val discount = context?.resources?.getString(R.string.label_discount) + "  ${offer.discount}%"
        val price = context?.resources?.getString(R.string.label_price, offer.price)

        offerDiscountPercentTv.text = discount
        offerPriceTv.text = offer.priceAfterDiscount.toString()
        offerHeaderTv.text = offer.offerHeader
        offerBodyTv.text = offer.offerBody
        offerDiscountPriceTv.text = price
        advantagesValueTv.text = offer.features
        termsValueTv.text = ""

        offerOwnerTv.text = offer.ownerName
        ownerPhoneTv.text = offer.ownerPhone
        locationsTv.text = offer.address

        facebookTv.text = offer.facebook
        instagramTv.text = offer.instagram
        twitterTv.text = offer.twitter


        Glide.with(requireContext()).load(offer.photos!![0]!!.large).addListener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable?>?,
                isFirstResource: Boolean
            ): Boolean {
                offerImageLoading.visibility = View.GONE
                offerImage.setImageResource(R.drawable.logo)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                offerImageLoading.visibility = View.GONE
                return false
            }
        }).into(offerImage)

        coubones = offer.coubones as ArrayList<Coubone>
        coubones.add(Coubone(offer.id, offer.offerHeader, offer.priceAfterDiscount, 1))
        coubones.add(Coubone(offer.id, offer.offerHeader, offer.priceAfterDiscount, 1))
        coubones.add(Coubone(offer.id, offer.offerHeader, offer.priceAfterDiscount, 1))
        adapterCoubons.replaceData(coubones)

        var images: List<OfferPhoto> = listOf(
            offer.photos[0] as OfferPhoto,
            offer.photos[0] as OfferPhoto,
            offer.photos[0] as OfferPhoto,
            offer.photos[0] as OfferPhoto
        )


        imageSliderAdapter.submitList(images)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.increaseCoboneeQuantity -> {
                coubones[position].quantity = coubones[position].quantity.plus(1)
                adapterCoubons.notifyDataSetChanged()
            }
            R.id.decreaseCoboneeQuantity -> {
                if (coubones[position].quantity > 0) {
                    coubones[position].quantity = coubones[position].quantity.minus(1)
                    adapterCoubons.notifyDataSetChanged()
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        timer = Timer()
        timer?.scheduleAtFixedRate(timerTask {
            requireActivity().runOnUiThread {
                if (bannerSliderVp != null) {
                    if (bannerSliderVp.currentItem < imageSliderAdapter.count - 1) {
                        bannerSliderVp.setCurrentItem(bannerSliderVp.currentItem + 1, true)
                    } else {
                        bannerSliderVp.setCurrentItem(0, true)
                    }
                }
            }
        }, 5000, 5000)
    }

    override fun onPause() {
        timer?.cancel()
        super.onPause()
    }
}
