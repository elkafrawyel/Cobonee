package com.cobonee.app.ui.main.searchFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chad.library.adapter.base.BaseQuickAdapter

import com.cobonee.app.R
import com.cobonee.app.entity.Offer
import com.cobonee.app.utily.MyUiStates
import com.cobonee.app.utily.snackBar
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel
    private var searchAdapter = AdapterSearch()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onSearchResult(it) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()
        searchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isBlank() == true) {
//                searchView.error = getString(R.string.error_required_search_text)
                    viewModel.resetSearch()
                    searchAdapter.data.clear()
                    searchAdapter.notifyDataSetChanged()
                } else {
                    viewModel.resetSearch()
                    searchAdapter.data.clear()
                    searchAdapter.notifyDataSetChanged()
                    viewModel.newQuery(searchView.text.toString())
                }
            }
        })
    }

    private fun setUpAdapter() {
        searchAdapter = AdapterSearch().apply {
            setEnableLoadMore(true)
        }

        searchAdapter.onItemChildClickListener = this

        searchAdapter.setOnLoadMoreListener({ viewModel.loadMore() }, searchRv)

        searchAdapter.setEnableLoadMore(true)

        searchRv.adapter = searchAdapter
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.offerSearchTitle -> {
                val action =
                    SearchFragmentDirections.actionSearchFragmentToDetailsFragment(adapter!!.data[position] as Offer)
                findNavController().navigate(action)
            }
        }
    }

    private fun onSearchResult(states: MyUiStates?) {
        when (states) {
            MyUiStates.Loading -> {
                searchBg.visibility = View.VISIBLE
            }
            MyUiStates.Success -> {
                searchBg.visibility = View.GONE
                searchRv.visibility = View.VISIBLE
                searchAdapter.addData(viewModel.offersList)
                searchAdapter.loadMoreComplete()
            }
            MyUiStates.LastPage -> {
                searchBg.visibility = View.GONE
                searchAdapter.loadMoreEnd(true)
            }
            is MyUiStates.Error -> {
                searchAdapter.loadMoreFail()
                searchBg.visibility = View.GONE
                activity?.snackBar(states.message, searchRootView)
            }
            MyUiStates.NoConnection -> {
                searchBg.visibility = View.GONE
                activity?.snackBar(getString(R.string.no_connection_error), searchRootView)
            }
            null -> {
            }
        }
    }

}
