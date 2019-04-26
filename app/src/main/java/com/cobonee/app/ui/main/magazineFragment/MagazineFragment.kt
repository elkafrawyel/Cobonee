package com.cobonee.app.ui.main.magazineFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cobonee.app.R

class MagazineFragment : Fragment() {

    companion object {
        fun newInstance() = MagazineFragment()
    }

    private lateinit var viewModel: MagazineViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.magazine_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MagazineViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
