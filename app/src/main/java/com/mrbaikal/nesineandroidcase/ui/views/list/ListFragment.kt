package com.mrbaikal.nesineandroidcase.ui.views.list

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mrbaikal.nesineandroidcase.R
import com.mrbaikal.nesineandroidcase.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {

    private val activityViewModel: MainViewViewModel by activityViewModels()
    private val viewModel: ListViewModel by viewModels()
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val postAdapter: PostAdapter by lazy {
        PostAdapter {
            activityViewModel.setPostModel(it )
            findNavController().navigate(ListFragmentDirections.listToDetail())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getPosts()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayout()
        setupObservers()
    }

    private fun setupLayout() {
        binding.listPost.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(v: RecyclerView, h: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false

                override fun onSwiped(h: RecyclerView.ViewHolder, dir: Int) {
                    viewModel.deleteItem(h.adapterPosition)
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                        val itemView = viewHolder.itemView
                        val background = ColorDrawable(Color.RED)

                        background.setBounds(
                            itemView.right + dX.toInt(),
                            itemView.top,
                            itemView.right,
                            itemView.bottom
                        )
                        background.draw(c)

                        val deleteIcon = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_delete
                        )

                        deleteIcon?.let {
                            val iconMargin = (itemView.height - it.intrinsicHeight) / 2
                            val iconTop = itemView.top + iconMargin
                            val iconBottom = iconTop + it.intrinsicHeight

                            val iconRight = itemView.right - (iconMargin / 2)
                            val iconLeft = iconRight - it.intrinsicWidth

                            it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                            it.draw(c)
                        }
                    }

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }).attachToRecyclerView(this)
        }
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.getPosts() }
    }

    private fun setupObservers() {
        viewModel.postList.observe(viewLifecycleOwner) {
            postAdapter.submitList(it)
            binding.swipeRefreshLayout.isRefreshing = false
        }
        activityViewModel.selectedPost.observe(viewLifecycleOwner) {
            viewModel.updateItem(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}