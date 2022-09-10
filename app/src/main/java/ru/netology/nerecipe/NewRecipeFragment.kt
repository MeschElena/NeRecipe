package ru.netology.nerecipe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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

    private val steps: MutableLiveData<List<Step>> by lazy {
        MutableLiveData<List<Step>>().apply {
            value = emptyList()
        }
    }

    fun checkSaveRecipe() : Boolean {
        return viewModel.idEditeRecipe.value !== null
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentNewRecipeBinding.inflate(inflater, container, false).also { binding ->

        var imageUri :Uri = Uri.parse("android.resource://ru.netology.nerecipe./drawable/pngwing.png")
            if (args.initialIdRecipe != -1L) {
                val recipeEdit = viewModel.getRecipe(args.initialIdRecipe)
                viewModel.idEditeRecipe.value = recipeEdit.id
                val step = viewModel.getStepsByRecipeId(recipeEdit.id)
                with(binding) {
                    author.setText(recipeEdit.author)
                    nameRecipe.setText(recipeEdit.name)
                    category.text = recipeEdit.categoryRecipe
                    recipeContent.setText(recipeEdit.content)
                    recipeImage.setImageURI(recipeEdit.image)

//                    val adapter = StepRecipeAdapter(viewModel)
//                    binding.stepsRecyclerView.adapter = adapter
//                    adapter.submitList(step)
                }
            }

        val adapter = StepRecipeAdapter(viewModel)
        binding.stepsRecyclerView.adapter = adapter
        viewModel.dataStep.observe(viewLifecycleOwner) {steps ->
//            adapter.submitList(steps.filter{it.idRecipe == viewModel.idEditeRecipe.value!!})
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
            Snackbar.make(binding.root, it.toString(), Snackbar.LENGTH_LONG).show()
            binding.recipeImage.setImageURI(it)
        }


        binding.fabAddImage.setOnClickListener{
            image.launch(arrayOf("image/jpeg" , "image/png"))
        }

        binding.fabStep.setOnClickListener {
            if (checkSaveRecipe()) {
                viewModel.onAddClickedStep()
            } else {
                val newRecipe = Recipe(
                    0L, binding.nameRecipe.text.toString(), binding.author.text.toString(),
                    binding.recipeContent.text.toString(), binding.category.text.toString(), Uri.parse(binding.recipeImage.toString())
                )
                viewModel.onSaveButtonClicked(newRecipe)
                viewModel.onAddClickedStep()
            }
        }

        binding.fabSave.setOnClickListener {
            val newRecipe = Recipe(0L, binding.nameRecipe.text.toString(), binding.author.text.toString(),
                binding.recipeContent.text.toString(), binding.category.text.toString(), Uri.parse(binding.recipeImage.toString()))
            viewModel.onSaveButtonClicked(newRecipe)
            viewModel.idEditeRecipe.value = null
            findNavController().navigateUp()
        }

    }.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(
            requestKey = NewStepFragment.REQUEST_KEY
        ) {requestKey, bundle ->
            if (requestKey != NewStepFragment.REQUEST_KEY) return@setFragmentResultListener

            val newStepContent = bundle.getString(NewStepFragment.RESULT_KEY) ?: return@setFragmentResultListener
            val newStepImage = bundle.getString(NewStepFragment.RESULT_KEY_IMG) ?: return@setFragmentResultListener
            val newStep = Step(0L, viewModel.idEditeRecipe.value!!, newStepContent, newStepImage.toUri())
            viewModel.onSaveStepButtonClicked(newStep)
        }

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