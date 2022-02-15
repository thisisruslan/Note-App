package uz.gita.mynotesapplication.ui.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import uz.gita.mynotesapplication.R
import uz.gita.mynotesapplication.app.App
import uz.gita.mynotesapplication.data.entity.NoteEntity
import uz.gita.mynotesapplication.databinding.ScreenMainBinding
import uz.gita.mynotesapplication.html2text
import uz.gita.mynotesapplication.ui.EventDialog
import uz.gita.mynotesapplication.ui.adapter.NoteAdapter
import uz.gita.mynotesapplication.ui.viewmodel.MainViewModel

class MainScreen : Fragment(R.layout.screen_main) {
    private var _binding: ScreenMainBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { NoteAdapter() }
    private val viewModel: MainViewModel by viewModels()
    private lateinit var handler: Handler
    private var querySt = ""

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = ScreenMainBinding.bind(view)
        handler = Handler(Looper.getMainLooper())
        binding.noteList.adapter = adapter
        binding.noteList.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

        viewModel.loadData()
        viewModel.noteListLiveData.observe(viewLifecycleOwner, noteListObserver)

        adapter.setItemClickListener {
            val bundle = Bundle()
            bundle.putSerializable("data", it)
            findNavController().navigate(R.id.action_mainScreen_to_infoEditNoteScreen, bundle)
        }

        binding.shareApp.setOnClickListener { shareApp() }

        //bottom sheet dialog
        adapter.setItemLongClickListener {
            val bottomDialog = EventDialog()

            bottomDialog.setPinListener {
                it.isPinned = !(it.isPinned)
                viewModel.updateData(it)
            }

            bottomDialog.setShareListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                val text = ("${html2text(it.title)}:\n${html2text(it.message)}")
                intent.putExtra(Intent.EXTRA_TEXT, text)
                startActivity(Intent.createChooser(intent, "Share:"))
            }

            bottomDialog.setDeleteListener { viewModel.removeData(it) }
            bottomDialog.show(childFragmentManager, "event")
        }

        binding.buttonAdd.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreen_to_addNoteScreen)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextSubmit(query: String?): Boolean {
                handler.removeCallbacksAndMessages(null)
                query?.let {
                    querySt = it.trim()
                    if (viewModel.searchNote("%${querySt}%").isNotEmpty()) {
                        binding.notFound.visibility = View.GONE
                        adapter.submitList(viewModel.searchNote("%${querySt}%"))
                    } else {
                        binding.notFound.visibility = View.VISIBLE
                    }
                    adapter.notifyDataSetChanged()
                    binding.searchView.setQuery(querySt, false)
                }
                return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    newText?.let {
                        querySt = it.trim()
                        if (viewModel.searchNote("%${querySt}%").isNotEmpty()) {
                            binding.notFound.visibility = View.GONE
                            adapter.submitList(viewModel.searchNote("%${querySt}%"))
                        } else {
                            binding.notFound.visibility = View.VISIBLE
                        }
                        adapter.notifyDataSetChanged()
                        binding.searchView.setQuery(querySt, false)
                    }
                }, 300)
                return true
            }
        })

        val closeButton = binding.searchView.findViewById(R.id.search_close_btn) as ImageView
        closeButton.setOnClickListener {
            clearSearch()
        }

    }

    private fun clearSearch() {
        adapter.submitList(viewModel.getAllNotes())
        adapter.notifyDataSetChanged()
        binding.searchView.setQuery(null, false)
        binding.searchView.clearFocus()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    private fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val text = """
             https://play.google.com/store/apps/details?id=${App.instance.packageName}
             """.trimIndent()
        intent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(Intent.createChooser(intent, "Share App:"))
    }

    private val noteListObserver = Observer<List<NoteEntity>> {
        if (it.isEmpty()) binding.empty.visibility = View.VISIBLE
        else binding.empty.visibility = View.GONE
        adapter.submitList(it)
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        clearSearch()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}