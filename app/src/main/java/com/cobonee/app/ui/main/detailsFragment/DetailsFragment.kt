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

import com.cobonee.app.R
import com.cobonee.app.entity.Offer
import kotlinx.android.synthetic.main.details_fragment.*
import kotlinx.android.synthetic.main.details_fragment.offerBodyTv
import kotlinx.android.synthetic.main.details_fragment.offerDiscountPercentTv
import kotlinx.android.synthetic.main.details_fragment.offerDiscountPriceTv
import kotlinx.android.synthetic.main.details_fragment.offerHeaderTv
import kotlinx.android.synthetic.main.details_fragment.offerImageLoading
import kotlinx.android.synthetic.main.details_fragment.offerOwnerTv
import kotlinx.android.synthetic.main.details_fragment.offerPriceTv
import kotlinx.android.synthetic.main.item_offer_view.*

class DetailsFragment : Fragment() {

    companion object {
        fun newInstance() = DetailsFragment()
    }

    private lateinit var viewModel: DetailsViewModel
    private var offer: Offer? = null

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

    }

    private fun addOfferToCart() {
        findNavController().navigate(R.id.cartFragment)
    }

    private fun setOffer(offer: Offer) {

        val discount = context?.resources?.getString(R.string.label_discount) + "  ${offer.discount}%"
        val price = context?.resources?.getString(R.string.label_price, offer.price)

        offerDiscountPercentTv.text = discount
        offerPriceTv.text = price
        offerHeaderTv.text = offer.offerHeader
        offerBodyTv.text = offer.offerBody
        offerDiscountPriceTv.text = offer.priceAfterDiscount.toString()
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
                offerImgv.setImageResource(R.drawable.logo)
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
    }

}
