package com.netguru.codereview.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.netguru.codereview.network.model.ShopListItemResponse
import com.netguru.codereview.network.model.ShopListResponse
import com.netguru.codereview.shoplist.R
import com.netguru.codereview.ui.model.ShopList
import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    private var viewModel: MainViewModel? = null
    lateinit var progressBar: ProgressBar
    lateinit var latestIcon: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        layoutInflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        progressBar = view.findViewById(R.id.message)
        latestIcon = view.findViewById(R.id.latest_list_icon)

        viewModel?.doNetWorkCall()
        progressBar.isVisible = true

        viewModel!!.shopLists.observe(viewLifecycleOwner) { lists ->

            val shopLists = lists.map { mapShopList(it.first, it.second) }.also {
                latestIcon.load(it.first().iconUrl)
            }
            Log.i("LOGTAG", "LOLOLOL Is it done already?")


            // Display the list in recyclerview
            // adapter.submitList(shopLists)
        }
        viewModel!!.events().observe(viewLifecycleOwner) {
            progressBar.isVisible = false
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun mapShopList(list: ShopListResponse, items: List<ShopListItemResponse>) =
        ShopList(
            list.list_id,
            list.userId,
            list.listName,
            list.listName,
            items
        )
}
