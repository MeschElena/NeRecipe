package ru.netology.nerecipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import ru.netology.nerecipe.databinding.FragmentFeedBinding


class FeedFragment : Fragment() {
    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private var clickedFavorites = false
    private fun searchRecipes (query: String): List<Recipe> {
        viewModel.querySearch = query
        if (!clickedFavorites) {
            return viewModel.getRecipesByQuery(query)
        } else {
            return viewModel.getFavoritesByQuery(query)
        }
    }

    private fun getFavotites() = viewModel.getFavotites()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.navigateToRecipeScreenEven.observe(this){initialIdRecipe ->
            val directionView = FeedFragmentDirections.actionFeedFragmentToNewRecipeFragment(initialIdRecipe)
            findNavController().navigate(directionView)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentFeedBinding.inflate(layoutInflater, container, false).also { binding ->
        val adapter = RecipeAdapter(viewModel)

        binding.recipesRecyclerView.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) {
            if (viewModel.querySearch == "") {
                if (!clickedFavorites) {
                    adapter.submitList(viewModel.data.value)
                    binding.imageNotFound.visibility = View.GONE
                } else {
                    val favorites = getFavotites()
                    if (favorites.isNotEmpty()) {
                        adapter.submitList(favorites)
                        binding.imageNotFound.visibility = View.GONE
                    } else {
                        adapter.submitList(favorites)
                        binding.imageNotFound.visibility = View.VISIBLE
                    }
                }
            } else {
                val query = binding.search.text.toString()
                val searchList = searchRecipes(query)
                if (searchList.isNotEmpty()) {
                    adapter.submitList(searchList)
                } else {
                    adapter.submitList(searchList)
                    binding.imageNotFound.visibility = View.VISIBLE
                }
            }
        }

        binding.fab.setOnClickListener {
            viewModel.onAddClicked()
        }

        binding.fabSearch.setOnClickListener {
            val query = binding.search.text.toString()
            val searchList = searchRecipes(query)
            if (searchList.isNotEmpty()) {
                adapter.submitList(searchList)
            } else {
                adapter.submitList(searchList)
                binding.imageNotFound.visibility = View.VISIBLE
            }
        }

        binding.fabClear.setOnClickListener {
            viewModel.querySearch = ""
            if (!clickedFavorites) {
                adapter.submitList(viewModel.data.value)
                binding.search.setText("")
                binding.imageNotFound.visibility = View.GONE
            } else {
                val favorites = getFavotites()
                binding.search.setText("")
                if (favorites.isNotEmpty()) {
                    adapter.submitList(favorites)
                    binding.imageNotFound.visibility = View.GONE
                } else {
                    adapter.submitList(favorites)
                    binding.imageNotFound.visibility = View.VISIBLE
                }
            }

        }

        binding.toggleButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) {
                clickedFavorites = false
                adapter.submitList(viewModel.data.value)
                binding.imageNotFound.visibility = View.GONE
            } else {
                clickedFavorites = true
                val favorites = getFavotites()
                if (favorites.isNotEmpty()) {
                    adapter.submitList(favorites)
                    binding.imageNotFound.visibility = View.GONE
                } else {
                    adapter.submitList(favorites)
                    binding.imageNotFound.visibility = View.VISIBLE
                }
            }
        }

    }.root

    companion object{
        const val TAG = "FeedFragment"
    }


}