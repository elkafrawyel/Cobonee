package com.cobonee.app.ui.main.helpFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter

import com.cobonee.app.R
import kotlinx.android.synthetic.main.help_fragment.*
import kotlinx.android.synthetic.main.orders_fragment.*

class HelpFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = HelpFragment()
    }

    private lateinit var viewModel: HelpViewModel
    private val questionsAdapter= AdapterQuestions()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.help_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HelpViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        questionsAdapter.addData("A")
        questionsAdapter.addData("B")
        questionsAdapter.addData("C")
        questionsAdapter.addData("A")
        questionsAdapter.addData("A")
        questionsAdapter.addData("A")

        questionsAdapter.onItemChildClickListener = this

        questionsRv.adapter = questionsAdapter
    }
    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }
}
