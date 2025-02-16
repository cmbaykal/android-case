package com.mrbaikal.nesineandroidcase.ui.views.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import coil3.load
import coil3.request.crossfade
import coil3.request.transformations
import coil3.transform.CircleCropTransformation
import com.mrbaikal.nesineandroidcase.base.ui.view.BaseFragment
import com.mrbaikal.nesineandroidcase.databinding.FragmentDetailBinding
import com.mrbaikal.nesineandroidcase.ext.hideKeyboard
import com.mrbaikal.nesineandroidcase.ext.showKeyboard
import com.mrbaikal.nesineandroidcase.ui.views.MainViewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<DetailViewModel>() {

    private val activityViewModel: MainViewViewModel by activityViewModels()
    override val viewModel: DetailViewModel by viewModels()
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayout()
        setupObservers()
    }

    private fun setupLayout() {
        with(binding) {
            imageEdit.setOnClickListener {
                viewModel.setEditMode(true)
            }
            imageEditDone.setOnClickListener {
                val title = edittextTitle.text.toString()
                val body = edittextBody.text.toString()
                activityViewModel.editPost(title, body)
                viewModel.setEditMode(false)
                context?.hideKeyboard(view)
            }
        }
    }

    private fun setupObservers() {
        activityViewModel.selectedPost.observe(viewLifecycleOwner) {
            binding.imagePost.load(it?.imgUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
            binding.textTitle.text = it?.title.orEmpty()
            binding.edittextTitle.setText(it?.title.orEmpty())
            binding.textBody.text = it?.body.orEmpty()
            binding.edittextBody.setText(it?.body.orEmpty())
        }
        viewModel.editMode.observe(viewLifecycleOwner) {
            binding.textTitle.isVisible = !it
            binding.textBody.isVisible = !it
            binding.edittextTitle.isVisible = it
            binding.edittextBody.isVisible = it
            binding.imageEdit.isVisible = !it
            binding.imageEditDone.isVisible = it

            if (it) {
                binding.edittextTitle.requestFocus()
                binding.edittextTitle.setSelection(binding.edittextTitle.text.length)
                context?.showKeyboard(binding.edittextTitle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}