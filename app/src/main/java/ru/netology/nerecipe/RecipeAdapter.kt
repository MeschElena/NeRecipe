package ru.netology.nerecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.databinding.RecipeListItemBinding

internal class RecipeAdapter (
    private val interactionListener: RecipeIteractionListener,
    ) : ListAdapter<Recipe, RecipeAdapter.ViewHolder>(RecipeDiffCallback) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = RecipeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding, interactionListener)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val recipe = getItem(position)
            holder.bind(recipe)
        }

        class ViewHolder(
            private val binding: RecipeListItemBinding,
            val listener: RecipeIteractionListener,
        ) : RecyclerView.ViewHolder(binding.root) {

            private lateinit var recipe: Recipe
            private val popupMenu by lazy {
                PopupMenu(itemView.context, binding.menu).apply {
                    inflate(R.menu.options_recipe)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.remove -> {
                                listener.onRemoveClicked(recipe)
                                true
                            }
                            R.id.edit -> {
                                listener.onEditClicked(recipe)
                                true
                            }
                            else -> false
                        }
                    }
                }
            }

            init {
                binding.favourite.setOnClickListener {
                    listener.onFavouriteClicked(recipe)
                }
            }

            fun bind(recipe: Recipe) {
                this.recipe = recipe
                with(binding) {
                    author.setText(recipe.author)
                    nameRecipe.setText(recipe.name)
                    category.text = recipe.categoryRecipe
                    recipeImage.setImageURI(recipe.image)
                    favourite.isChecked = recipe.favourite

                    menu.setOnClickListener { popupMenu.show() }
                }
            }
        }


        private object RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) =
                oldItem == newItem
        }
}