package ru.netology.nerecipe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import ru.netology.nerecipe.databinding.FragmentNewRecipeBinding
import java.net.URL
import kotlin.properties.Delegates

class NewRecipeFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    private val args by navArgs<NewRecipeFragmentArgs>()

    private fun checkSaveRecipe() : Boolean {
        return viewModel.idEditeRecipe != -1L
    }
    private fun IdRecipeEdit() : Long {
        return if (viewModel.idEditeRecipe != -1L) {
            viewModel.idEditeRecipe
        } else {
            args.initialIdRecipe
        }
    }

    private fun setImageRecipe(imageUri: Uri){
        viewModel.imageRecipe = imageUri
    }

    private fun getImageRecipe(): Uri {
        return viewModel.imageRecipe
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentNewRecipeBinding.inflate(inflater, container, false).also { binding ->
        viewModel.data.observe(viewLifecycleOwner) {
            if (args.initialIdRecipe != -1L || IdRecipeEdit() != -1L) {
                if (args.initialIdRecipe != -1L) {
                    val recipeEdit = viewModel.getRecipe(args.initialIdRecipe)
                    viewModel.idEditeRecipe = recipeEdit.id
                    viewModel.imageRecipe = recipeEdit.image
                    with(binding) {
                        author.setText(recipeEdit.author)
                        nameRecipe.setText(recipeEdit.name)
                        category.text = recipeEdit.categoryRecipe
                        recipeContent.setText(recipeEdit.content)
                        recipeImage.setImageURI(recipeEdit.image)
                    }
               }
                else {
                    val recipeEdit = viewModel.currentRecipe.value!!
                    with(binding) {
                        author.setText(recipeEdit.author)
                        nameRecipe.setText(recipeEdit.name)
                        category.text = recipeEdit.categoryRecipe
                        recipeContent.setText(recipeEdit.content)
                        recipeImage.setImageURI(recipeEdit.image)
                    }
                }
            }
        }

        val adapter = StepRecipeAdapter(viewModel)
        binding.stepsRecyclerView.adapter = adapter
        viewModel.dataStep.observe(viewLifecycleOwner) {
            adapter.submitList(viewModel.getStepsByRecipeId(IdRecipeEdit()))
        }

        val spinnerAdapter: ArrayAdapter<String> =
            ArrayAdapter(inflater.context, android.R.layout.simple_spinner_item, viewModel.categoryRecipe)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //setting the ArrayAdapter data on the Spinner
        binding.spinner.adapter = spinnerAdapter

        //spinner item click handler
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    binding.category.text = "Европейская"
                }
                if (position == 1) {
                    binding.category.text = "Азиатская"
                }
                if (position == 2) {
                    binding.category.text = "Паназиатская"
                }
                if (position == 3) {
                    binding.category.text = "Восточная"
                }
                if (position == 4) {
                    binding.category.text = "Американская"
                }
                if (position == 5) {
                    binding.category.text = "Русская"
                }
                if (position == 6) {
                    binding.category.text = "Среднеземноморская"
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        val image = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            requireActivity().contentResolver.takePersistableUriPermission(requireNotNull(it), Intent.FLAG_GRANT_READ_URI_PERMISSION)
            binding.recipeImage.setImageURI(it)
            Log.d("TAG", "viewModel.imageRecipe = ${viewModel.imageRecipe}")
            setImageRecipe(it)
        }


        binding.fabAddImage.setOnClickListener{
            image.launch(arrayOf("image/jpeg" , "image/png"))
        }

        binding.fabStep.setOnClickListener {
            if (checkSaveRecipe()) {
                viewModel.onAddClickedStep()
            } else {
                val newRecipe = Recipe(
                    id = 0L,
                    name = binding.nameRecipe.text.toString(),
                    author = binding.author.text.toString(),
                    categoryRecipe = binding.category.text.toString(),
                    content = binding.recipeContent.text.toString(),
                    image = getImageRecipe()
                )
                viewModel.onSaveButtonClicked(newRecipe)
                viewModel.onAddClickedStep()
            }
        }

        binding.fabSave.setOnClickListener {
            if (checkSaveRecipe()) {
                val newRecipe = Recipe(
                    id = IdRecipeEdit(),
                    name = binding.nameRecipe.text.toString(),
                    author = binding.author.text.toString(),
                    content = binding.recipeContent.text.toString(),
                    categoryRecipe = binding.category.text.toString(),
                    image = getImageRecipe()
                )
                viewModel.onSaveButtonClicked(newRecipe)
            } else {
                val newRecipe = Recipe(
                    0L,
                    binding.nameRecipe.text.toString(),
                    binding.author.text.toString(),
                    binding.category.text.toString(),
                    binding.recipeContent.text.toString(),
                    getImageRecipe()
                )
                viewModel.onSaveButtonClicked(newRecipe)
            }
            viewModel.idEditeRecipe = -1L
            viewModel.currentRecipe.value = null
            findNavController().navigateUp()
        }

    }.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setFragmentResultListener(
//            requestKey = NewStepFragment.REQUEST_KEY
//        ) {requestKey, bundle ->
//            if (requestKey != NewStepFragment.REQUEST_KEY) return@setFragmentResultListener
//
//            val newStepContent = bundle.getString(NewStepFragment.RESULT_KEY) ?: return@setFragmentResultListener
//            val newStepImage = bundle.getString(NewStepFragment.RESULT_KEY_IMG) ?: return@setFragmentResultListener

//            val newStep = Step(0L, viewModel.idEditeRecipe, newStepContent, newStepImage.toUri())
//            viewModel.onSaveStepButtonClicked(newStep)
//        }

        viewModel.navigateToStepScreenView.observe(this){initialIdStep ->
            val direction = NewRecipeFragmentDirections.actionNewRecipeFragmentToNewStepFragment(initialIdStep)
            findNavController().navigate(direction)
        }
    }

    companion object {
        const val REQUEST_KEY = "requestkey"
        const val RESULT_KEY = "recipeId"
    }

}