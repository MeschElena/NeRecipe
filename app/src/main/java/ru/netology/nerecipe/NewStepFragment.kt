package ru.netology.nerecipe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import ru.netology.nerecipe.databinding.FragmentNewRecipeBinding
import ru.netology.nerecipe.databinding.FragmentNewStepBinding

class NewStepFragment: Fragment() {
    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    private val argsStep by navArgs<NewStepFragmentArgs>()
    private var imageUri : Uri = Uri.parse("android.resource://ru.netology.nerecipe./drawable/pngegg.png")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentNewStepBinding.inflate(inflater, container, false).also { binding ->

        binding.stepContentEdit.requestFocus()
        binding.stepContentEdit.setText(viewModel.currentStep.value?.content)
        binding.stepImageEdit.setImageURI(viewModel.currentStep.value?.image)
        if (argsStep.initialIdStep != -1L) {
            imageUri = viewModel.currentStep.value?.image!!
        }


        val image = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
          //  Snackbar.make(binding.root, it.toString(), Snackbar.LENGTH_LONG).show()
            requireActivity().contentResolver.takePersistableUriPermission(requireNotNull(it), Intent.FLAG_GRANT_READ_URI_PERMISSION)
            binding.stepImageEdit.setImageURI(it)
            imageUri = it
            Log.d("TAG", "imageUri = ${imageUri}")
        }

        binding.fabAddImageStep.setOnClickListener{
            image.launch(arrayOf("image/jpeg" , "image/png"))
        }

        binding.fabSaveStep.setOnClickListener{
            onOkButtonClicked(binding)
        }

    }.root


    private fun onOkButtonClicked(binding: FragmentNewStepBinding) {

        val text = binding.stepContentEdit.text
        if (argsStep.initialIdStep != -1L) {
            val newStep =
                Step(argsStep.initialIdStep, viewModel.idEditeRecipe, text.toString(), imageUri)
            viewModel.onSaveStepButtonClicked(newStep)
        } else {
            val newStep =
                Step(0L, viewModel.idEditeRecipe, text.toString(), imageUri)
            viewModel.onSaveStepButtonClicked(newStep)
        }

//        if (!text.isNullOrBlank()) {
//            val resultBundle = Bundle(2)
//            resultBundle.putString(RESULT_KEY, text.toString())
//            resultBundle.putString(RESULT_KEY_IMG, imageUri.toString())
//            setFragmentResult(REQUEST_KEY,resultBundle)
//        }
        findNavController().navigateUp()//popBackStack()
    }


    companion object {
        const val REQUEST_KEY = "requestkey"
        const val RESULT_KEY = "stepContent"
        const val RESULT_KEY_IMG = "stepimage"


        //var Bundle.textArg: String? by StringArg


    }
}