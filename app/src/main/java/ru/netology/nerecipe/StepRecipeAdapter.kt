package ru.netology.nerecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.netology.nerecipe.databinding.RecipeListItemBinding
import ru.netology.nerecipe.databinding.StepListItemBinding

internal class StepRecipeAdapter (
    private val interactionListener: StepRecipeIteractionListener,
) : ListAdapter<Step, StepRecipeAdapter.ViewHolder>(StepRecipeDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StepListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val step = getItem(position)
        holder.bind(step)
    }

    class ViewHolder(
        private val binding: StepListItemBinding,
        val listener: StepRecipeIteractionListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var step: Step
        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.menuStep).apply {
                inflate(R.menu.options_recipe)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onStepRemoveClicked(step)
                            true
                        }
                        R.id.edit -> {
                            listener.onStepEditClicked(step)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        fun bind(step: Step) {
            this.step = step
            with(binding) {
                stepImage.setImageURI(step.image)
                stepContent.text = step.content

                menuStep.setOnClickListener { popupMenu.show() }
            }
        }
    }


    private object StepRecipeDiffCallback : DiffUtil.ItemCallback<Step>() {
        override fun areItemsTheSame(oldItem: Step, newItem: Step) =
            oldItem.idStep == newItem.idStep

        override fun areContentsTheSame(oldItem: Step, newItem: Step) =
            oldItem == newItem
    }
}