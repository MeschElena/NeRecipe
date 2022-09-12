package ru.netology.nerecipe

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
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
        //  arguments?.textArg?.let (binding.edit::setText)

        binding.stepContentEdit.requestFocus()
        binding.stepContentEdit.setText(viewModel.currentStep.value?.content)
        binding.stepImageEdit.setImageURI(viewModel.currentStep.value?.image)

        val image = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            Snackbar.make(binding.root, it.toString(), Snackbar.LENGTH_LONG).show()
            binding.stepImageEdit.setImageURI(it)
            if (it != null) {
                imageUri = it
            }
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

        if (!text.isNullOrBlank()) {
            val resultBundle = Bundle(2)
            resultBundle.putString(RESULT_KEY, text.toString())
            resultBundle.putString(RESULT_KEY_IMG, imageUri.toString())
            setFragmentResult(REQUEST_KEY,resultBundle)
        }
        findNavController().navigateUp()//popBackStack()
    }


    companion object {
        const val REQUEST_KEY = "requestkey"
        const val RESULT_KEY = "stepContent"
        const val RESULT_KEY_IMG = "stepimage"


        //var Bundle.textArg: String? by StringArg


    }
}