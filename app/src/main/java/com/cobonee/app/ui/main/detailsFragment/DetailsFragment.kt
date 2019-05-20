package com.cobonee.app.ui.main.detailsFragment

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.cobonee.app.ui.main.MainViewModel
import com.cobonee.app.utily.MyUiStates
import com.cobonee.app.utily.observeEvent
import com.cobonee.app.utily.snackBar
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
    private lateinit var mainViewModel: MainViewModel
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

    private fun onOfferRemovedResponse(states: MyUiStates) {
        when (states) {
            MyUiStates.Success -> {
                Glide.with(context!!).load(R.drawable.ic_favorite_stock).into(offerSaveImgv)
            }
            is MyUiStates.Error -> {
                activity?.snackBar(states.message, detailsRootView)
            }
        }
    }

    private fun onOfferAddedResponse(states: MyUiStates) {
        when (states) {
            MyUiStates.Success -> {
                Glide.with(context!!).load(R.drawable.ic_favorite_white).into(offerSaveImgv)
            }
            is MyUiStates.Error -> {
                activity?.snackBar(states.message, detailsRootView)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterCoubons.onItemChildClickListener = this
        offerCoboneeRv.adapter = adapterCoubons
        bannerSliderVp.adapter = imageSliderAdapter
        pageIndicator.setViewPager(bannerSliderVp)
        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        mainViewModel.addCartItemsUiState.observeEvent(this) { myUiStates: MyUiStates ->
            when (myUiStates) {
                MyUiStates.Success -> {
                    activity?.snackBar(getString(R.string.cart_item_added), detailsRootView)
                }
                is MyUiStates.Error -> {
                    activity?.snackBar(myUiStates.message, detailsRootView)
                }
            }
        }
        mainViewModel.addOfferUiState.observeEvent(this) { onOfferAddedResponse(it) }
        mainViewModel.removeOfferUiState.observeEvent(this) { onOfferRemovedResponse(it) }
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

        offerSaveImgv.setOnClickListener {
            mainViewModel.addOffer(offer?.id!!)
        }
    }

    private fun addOfferToCart() {
        mainViewModel.addCartItems(offer?.id!!, 1)
    }

    private fun setOffer(offer: Offer) {

        val discount = context?.resources?.getString(R.string.label_discount) + "  ${offer.discount}%"
        val price = context?.resources?.getString(R.string.label_price, offer.price)

        offerDiscountPercentTv.text = discount
        offerPriceTv.text = offer.priceAfterDiscount.toString()
        offerHeaderTv.text = offer.offerHeader
        offerBodyTv.text = offer.offerBody
        offerDiscountPriceTv.text = price

        if (offer.features==null || offer.features==""){
            advantagesTv.visibility = View.GONE
            advantagesValueTv.visibility = View.GONE
        }else{
            advantagesTv.visibility = View.VISIBLE
            advantagesValueTv.visibility = View.VISIBLE
            advantagesValueTv.text = offer.features
        }

        offerOwnerTv.text = offer.ownerName
        ownerPhoneTv.text = offer.ownerPhone
        locationsTv.text = offer.address

        facebookTv.text = offer.facebook
        instagramTv.text = offer.instagram
        twitterTv.text = offer.twitter

        if (offer.isFav) {
            Glide.with(context!!).load(R.drawable.ic_favorite_white).into(offerSaveImgv)
        } else {
            Glide.with(context!!).load(R.drawable.ic_favorite_stock).into(offerSaveImgv)
        }

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

        if (coubones.size == 0) {
            coboneeLayout.visibility = View.GONE
        } else {
            coboneeLayout.visibility = View.VISIBLE
            adapterCoubons.replaceData(coubones)
        }

        val images: List<OfferPhoto> = listOf(
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

            R.id.offerDetailsItemAddToCart -> {
                val coubone = adapter?.data?.get(position) as Coubone
                if (coubone.quantity > 0) {
                    //add to cart
                    mainViewModel.addCartItems(coubone.id!!, coubone.quantity)
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
