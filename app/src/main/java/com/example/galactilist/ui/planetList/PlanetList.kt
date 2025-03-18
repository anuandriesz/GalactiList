package com.example.galactilist.ui.planetList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.galactilist.R
import com.example.galactilist.constants.Constants.Companion.PLANET_DETAILS
import com.example.galactilist.databinding.FragmentPlanetListBinding
import com.example.galactilist.ui.MainActivity
import com.example.galactilist.ui.adapters.PlanetListAdapter
import com.example.galactilist.ui.planetDetailView.PlanetDetailViewFragment
import com.example.galactilist.utils.Dialog.showAlertDialog
import com.example.galactilist.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlanetList : Fragment() {
    private val viewModel: PlanetListViewModel by viewModels()
    private lateinit var binding: FragmentPlanetListBinding
    private var planetListAdapter: PlanetListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlanetListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerView()
        initAPI()
    }

    private fun initAPI() {
        viewModel.performGetPlanets()
        viewModel.planetList.observe(viewLifecycleOwner) { resource ->
            binding.loading.visibility =
                if (resource is Resource.Loading) View.VISIBLE else View.GONE
            when (resource) {
                is Resource.Loading -> {}

                is Resource.Success -> {
                    resource.data.let { planets ->
                        planetListAdapter?.submitList(planets)
                    }
                }

                is Resource.Error -> {
                    context?.showAlertDialog(
                        title = getString(R.string.error_occurred),
                        message = getString(R.string.please_restart_the_app),
                        ok = Pair(getString(R.string.ok)) {

                        }
                    )
                }

                else -> {
                    // Handle any other cases
                }
            }
        }
    }

    private fun initViews() {
        setupToolbar()
        setupRecyclerView()
        initAPI()
    }

    private fun setupToolbar() {
        (activity as? MainActivity)?.binding?.toolbarLayout?.apply {
            toolbarTitle.text = getString(R.string.welcome)
            toolbarBackButton.visibility = View.GONE
        }
    }

    private fun setupRecyclerView() {
        planetListAdapter = PlanetListAdapter { selectedPlanet ->
            val bundle = Bundle().apply { putParcelable(PLANET_DETAILS, selectedPlanet) }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, PlanetDetailViewFragment::class.java, bundle)
                .addToBackStack(TAG)
                .commit()
        }
        binding.rvPlanets.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = planetListAdapter
            setHasFixedSize(true)
        }

        binding.rvPlanets.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            /**
             * Called when the RecyclerView has been scrolled.
             *
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx The amount of horizontal scroll.
             * @param dy The amount of vertical scroll.
             */
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Get the layout manager of the RecyclerView
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                // Find the position of the last visible item
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                // Get the total number of items in the RecyclerView
                val totalItemCount = layoutManager.itemCount

                // Check if the last visible item is the last item in the list
                if (lastVisibleItemPosition == totalItemCount - 1) {
                    // Load more planets when the end of the list is reached
                    viewModel.performGetPlanets()
                }
            }
        })
    }

    companion object {
        const val TAG = "PlanetList"
    }
}