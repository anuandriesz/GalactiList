package com.example.galactilist.ui.planetList

import PlanetListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.galactilist.R
import com.example.galactilist.constants.Constants.Companion.PLANET_DETAILS
import com.example.galactilist.databinding.FragmentPlanetListBinding
import com.example.galactilist.ui.MainActivity
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
        viewModel.performGetPlanets().observe(viewLifecycleOwner) { resource ->
            binding.loading.visibility = if (resource is Resource.Loading) View.VISIBLE else View.GONE
            when (resource) {
                is Resource.Loading -> {
                }

                is Resource.Success -> {
                    resource.data.let { planets ->
                        planetListAdapter?.submitList(planets.results)
                    }
                }

                is Resource.Error -> {
                    context?.showAlertDialog(
                        title = getString(R.string.error_occurred),
                        message = getString(R.string.please_restart_the_search),
                        ok = Pair(getString(R.string.ok)) {}
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
    }

    companion object {
        const val TAG = "PlanetList"
    }
}