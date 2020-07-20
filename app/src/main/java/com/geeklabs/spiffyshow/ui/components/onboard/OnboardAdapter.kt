package com.geeklabs.spiffyshow.ui.components.onboard

import android.content.Context
import android.os.Parcelable
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.extensions.inflate
import com.geeklabs.spiffyshow.models.WelcomeScreen
import com.geeklabs.spiffyshow.utils.Utils
import kotlinx.android.synthetic.main.item_welcome.view.*

class OnboardAdapter : PagerAdapter() {

    private lateinit var view: View
    lateinit var context: Context
    var list = mutableListOf<WelcomeScreen>()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        context = container.context
        view = container.inflate(R.layout.item_welcome, false)
        val welcomePage = list[position]
        view.title.text = welcomePage.titleText
        view.featureImage.setImageDrawable(context.getDrawable(welcomePage.image))
        view.description.text = welcomePage.descriptionText
        view.description.movementMethod = ScrollingMovementMethod()
        Utils.showHideViews(false, view.description, view.title)
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount() = list.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }

    override fun saveState(): Parcelable? = null
}