package com.geeklabs.spiffyshow.ui.components.main.search

import com.geeklabs.spiffyshow.ui.base.BasePresenter
import java.util.*
import javax.inject.Inject

class SearchPresenter @Inject constructor() : BasePresenter<SearchContract.View>(),
    SearchContract.Presenter {

    private val items = mutableListOf<String>()

    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
        loadSearchItems()
    }

    private fun loadSearchItems() {
        items.add("Apple")
        items.add("Apple")
        items.add("Apple")
        items.add("Apple")
        items.add("Ball")
        items.add("Ball")
        items.add("Ball")
        items.add("Cat")
        items.add("Cat")
        items.add("Cat")
        items.add("Donkey")
        items.add("Elephant")
        items.add("Fish")
        items.add("Goat")
        items.add("Goat")
        items.add("Goat")
        items.add("Home")
        items.add("Iphone")
        items.add("Jack")
        items.add("Kite")
        items.add("Light")
        items.add("Mango")
        items.add("Mango")
        items.add("Mango")
        items.add("Night")
        items.add("Night")
        items.add("Night")
        items.add("Opacity")
        items.add("Opacity")
        items.add("Opacity")
        items.add("Parrot")
        items.add("Parrot")
        items.add("Parrot")
        items.add("Queen")
        items.add("Queen")
        items.add("Queen")
        items.add("Road")
        items.add("Road")
        items.add("Road")
        items.add("Street")
        items.add("Tree")
        items.add("Universal")
        items.add("Universal")
        items.add("Universal")
        items.add("View")
        items.add("View")
        items.add("View")
        items.add("White")
        items.add("Xerox")
        items.add("Xerox")
        items.add("Xerox")
        items.add("Yellow")
        items.add("Yellow")
        items.add("Yellow")
        items.add("Yellow")
        items.add("Yellow")
        items.add("Zebra")
        items.add("Zebra")
        items.add("Zebra")
        items.add("Zebra")
    }

    override fun onSearch(query: String) {
        val inputQuery = query.toLowerCase(Locale.getDefault())
        val finalList = if (inputQuery.isEmpty()) listOf() else items.filter {
            it.toLowerCase(Locale.getDefault()).contains(inputQuery, true) ||
                    it.contains(inputQuery, true)
        }
        finalList.sortedBy { it }
        getView()?.showItems(finalList.toMutableList())
    }
}