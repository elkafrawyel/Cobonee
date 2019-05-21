package com.cobonee.app.ui.main.homeFragment

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cobonee.app.R
import com.cobonee.app.entity.Department
import com.cobonee.app.utily.Injector

class AdapterDepartment : BaseQuickAdapter<Department, BaseViewHolder>(R.layout.item_department_view) {

    override fun convert(helper: BaseViewHolder, department: Department) {

        if (department.isSelected) {
            helper.setTextColor(
                R.id.deptNameTv,
                Injector.getApplicationContext().resources.getColor(R.color.colorPrimary)
            )
            helper.setVisible(R.id.bottomLine, true)
        } else {
            helper.setTextColor(
                R.id.deptNameTv,
                Injector.getApplicationContext().resources.getColor(android.R.color.darker_gray)
            )
            helper.setVisible(R.id.bottomLine, false)
        }

        helper.setText(R.id.deptNameTv, department.name)
            .addOnClickListener(R.id.departmentItem)

    }

}