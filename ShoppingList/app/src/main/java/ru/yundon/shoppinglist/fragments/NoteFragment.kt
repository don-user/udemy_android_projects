package ru.yundon.shoppinglist.fragments

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.yundon.shoppinglist.activities.MainApp
import ru.yundon.shoppinglist.activities.NewNoteActivity
import ru.yundon.shoppinglist.databinding.FragmentNoteBinding
import ru.yundon.shoppinglist.db.NoteAdapter
import ru.yundon.shoppinglist.entities.NoteItem
import ru.yundon.shoppinglist.viewmodel.MainViewModel

class NoteFragment : BaseFragment(), NoteAdapter.Listener {

    private lateinit var binding: FragmentNoteBinding
    //Лаунчер для отрытия и передачи данных в активити и результата от активити
    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    lateinit var adapter: NoteAdapter

    //настройка и покдлючение вьюмодель используюя глобальную датабэйз из класса MainApp
    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).dataBase)
    }
    private lateinit var defPref: SharedPreferences

    //обработка нажатия кнопки new из ботом навигэйшионвью в текущем фрагмента.
    override fun onClickNew() {
       editLauncher.launch(Intent(activity, NewNoteActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onEditResult()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observer()
    }
//настройка и подключение ресайклервью
    private fun initRecyclerView() = with(binding){
        defPref = PreferenceManager.getDefaultSharedPreferences(activity)
        recyclerViewNote.layoutManager = getLayoutManager()
        adapter = NoteAdapter(this@NoteFragment, defPref)
        recyclerViewNote.adapter = adapter
    }

    //определяет какой лайоут манагер выбирать для ресайклервью
    private fun getLayoutManager(): RecyclerView.LayoutManager{
        return if (defPref.getString("note_style_key", "Linear") == "Linear") {
            LinearLayoutManager(activity)
        }
        else {
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }
//покдлючение наблюдателя по обновляемому списку для адаптера и подключение обновленного списка в адаптер
    private fun observer(){
        mainViewModel.allNotes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
}

    private fun onEditResult(){
        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == Activity.RESULT_OK){

                val editState = it.data?.getStringExtra(EDIT_STATE_KEY)

                if (editState == "update") mainViewModel.updateNote(it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem)
                else mainViewModel.insertNote(it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem)
            }
        }
    }
// удаления итема при нажатии кнопки удалить на итеме
    override fun deleteItem(id: Int) {
        mainViewModel.deleteNote(id)
    }
    //подключение клика по итему рейсайклера и переход в ативити NewNoteActivity и передача данных через Интент
    override fun onClickItem(note: NoteItem) {
        val intent = Intent(activity, NewNoteActivity::class.java).putExtra(NEW_NOTE_KEY, note)
        editLauncher.launch(intent)
    }

    companion object {

        const val NEW_NOTE_KEY = "new_note_key"
        const val EDIT_STATE_KEY = "edit_state_key"

        @JvmStatic
        fun newInstance() = NoteFragment()
    }
}