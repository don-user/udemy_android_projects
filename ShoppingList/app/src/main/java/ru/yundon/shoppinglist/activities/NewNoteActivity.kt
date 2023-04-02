package ru.yundon.shoppinglist.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import ru.yundon.shoppinglist.R
import ru.yundon.shoppinglist.databinding.ActivityNewNoteBinding
import ru.yundon.shoppinglist.entities.NoteItem
import ru.yundon.shoppinglist.fragments.NoteFragment.Companion.EDIT_STATE_KEY
import ru.yundon.shoppinglist.fragments.NoteFragment.Companion.NEW_NOTE_KEY
import ru.yundon.shoppinglist.utils.HtmlManager
import ru.yundon.shoppinglist.utils.MyOnTouchListener
import ru.yundon.shoppinglist.utils.TimeManager.getCurrentTime
import java.text.SimpleDateFormat
import java.util.*

class NewNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewNoteBinding
    private var note: NoteItem? = null
    private var pref: SharedPreferences? = null
    private lateinit var defPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        defPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        setContentView(binding.root)

        actionBarSettings()
        getNote()
        onClickColorPicker()
        init()
        setTextSize()
        actionMenuCallback()
    }

    //раздуваем меню new_note_menu в акшионбаре
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //клик на кнопки в меню нью_ноте_меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //сохранение при редактировании
            R.id.id_save -> setMainResult()
            //возврат домой
            android.R.id.home -> finish()
            //Выделение жирным и обычным текстом выделенного фрагмента
            R.id.id_bold -> setBoldSelectedText()
            R.id.id_color ->
                if (binding.colorPicker.isShown) { //isShown показывает видимость объекта
                    closeColorPicker()
                } else {
                    openColorPicker()
                }
        }
        return super.onOptionsItemSelected(item)
    }
//выбираем новый цвет выделенному тексту при нажатии на кнопку с цветом
    private fun onClickColorPicker() = with(binding){
        imageButtonRed.setOnClickListener{
            setColorSelectedText(R.color.picker_red)
        }
        imageButtonGreen.setOnClickListener{
            setColorSelectedText(R.color.picker_green)
        }
        imageButtonOrange.setOnClickListener{
            setColorSelectedText(R.color.picker_orange)
        }
        imageButtonYellow.setOnClickListener{
            setColorSelectedText(R.color.picker_yellow)
        }
        imageButtonBlue.setOnClickListener{
            setColorSelectedText(R.color.picker_blue)
        }
        imageButtonBlack.setOnClickListener{
            setColorSelectedText(R.color.picker_black)
        }

    }

    //инициализация для передвижения колор пикера по экрану
    @SuppressLint("ClickableViewAccessibility")
    private fun init(){
        binding.colorPicker.setOnTouchListener(MyOnTouchListener())
        //инизиализируем шаред преференс
        pref = PreferenceManager.getDefaultSharedPreferences(this)
    }

    //функция по выделению жирным или обычным текстом в выделенном тексте
    private fun setBoldSelectedText() = with(binding){
        //начало позиции выделенного текста
        val startPosition = edDescription.selectionStart
        //конец позиции выделенного текста
        val endPosition = edDescription.selectionEnd
        // определение куска выделенного текста
        val styles = edDescription.text.getSpans(startPosition, endPosition, StyleSpan::class.java)
        //переменная выделения жирным
        var boldStyle: StyleSpan? = null
        //условие если есть стиль то убираем, если нет то выделяем жирным (styles[0] - жирным)

            if (styles.isNotEmpty()) edDescription.text.removeSpan(styles[0])
            else boldStyle = StyleSpan(Typeface.BOLD)

        //устанавливаем жирный текст если нет жирного и наоборот, от начало и о конца выделенного фрагмента
        edDescription.text.setSpan(boldStyle, startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        //удаляем лишнии пробелы
        edDescription.text.trim()
        //возвращаем курсор на начало позиции
        edDescription.setSelection(startPosition)

    }

    //выбор цвета из Колор пикера и возврат к стандартному
    private fun setColorSelectedText(colorId: Int) = with(binding){
        //начало позиции выделенного текста
        val startPosition = edDescription.selectionStart
        //конец позиции выделенного текста
        val endPosition = edDescription.selectionEnd
        // определение куска выделенного текста
        val styles = edDescription.text.getSpans(startPosition, endPosition, ForegroundColorSpan::class.java)
        //условие если есть стиль то убираем, если нет то выделяем жирным (styles[0] - жирным)
        if (styles.isNotEmpty()) edDescription.text.removeSpan(styles[0])

        //устанавливаем цвет текста от начало и о конца выделенного фрагмента
        edDescription.text.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this@NewNoteActivity, colorId)),
            startPosition,
            endPosition,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        //удаляем лишнии пробелы
        edDescription.text.trim()
        //возвращаем курсор на начало позиции
        edDescription.setSelection(startPosition)

    }

    private fun getNote(){
        val sNote = intent.getSerializableExtra(NEW_NOTE_KEY)
        if (sNote != null) note = sNote as NoteItem
        fillNote()
    }
//заполняем заметку при создании и редактировании
    private fun fillNote() = with(binding){
            edTitle.setText(note?.title)
            edDescription.setText(HtmlManager.getFromHtml(note?.content ?: "").trim())

    }

    private fun setMainResult(){

        var editState = "new"
        val tempNote: NoteItem? =
            if (note == null) createNewNote()
            else {
                editState = "update"
                updateNote()
            }

        intent = Intent().apply {
            putExtra(NEW_NOTE_KEY, tempNote)
            putExtra(EDIT_STATE_KEY, editState)
        }

        setResult(RESULT_OK, intent)
        finish()
    }

    //обновление записей в базе данных
    private fun updateNote(): NoteItem? = with(binding){
        //возвращает копию NoteItem(всей записи)
        return note?.copy(
            title = edTitle.text.toString(),
            //перевод текста description в HTML для сохранения в базу
            content = HtmlManager.toHtml(edDescription.text)
        )
    }

    //создание новой записи, возвращаем всю заметку
    private fun createNewNote(): NoteItem{
        return NoteItem(
            null,
            binding.edTitle.text.toString(),
            //при создании заметки тоже записываем ввиде HTML
            HtmlManager.toHtml(binding.edDescription.text),
            getCurrentTime(),
            ""              //пустая строка на всякий случай вдруг потом нужно будет добавить категорию
        )
    }



    //настройка ActionBar
    private fun actionBarSettings(){
        val ab = supportActionBar
        //Возврат home
        ab?.setDisplayHomeAsUpEnabled(true)
    }
//открывает коллер пикер с анимацией
    private fun openColorPicker() = with(binding){
        colorPicker.visibility = View.VISIBLE
        val openAnim = AnimationUtils.loadAnimation(this@NewNoteActivity, R.anim.open_color_picker)
        colorPicker.startAnimation(openAnim)
    }

    //закрываем коллер пикер с анимацией
    private fun closeColorPicker() = with(binding){
        val openAnim = AnimationUtils.loadAnimation(this@NewNoteActivity, R.anim.close_color_picker)
        //слушатель на анимацию чтоб понимать когда анимация закончилась
        openAnim.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
            }
            //когда заканчивается делаем колор пикер невидимым
            override fun onAnimationEnd(p0: Animation?) {
                colorPicker.visibility = View.GONE
            }
            override fun onAnimationRepeat(p0: Animation?) {
            }

        })
        colorPicker.startAnimation(openAnim)
    }

    //настройка меню которое выскакивает при выделении текста (копировать вставить и т.п)
    private fun actionMenuCallback(){
        val actionCallback = object : ActionMode.Callback{
            override fun onCreateActionMode(p0: ActionMode?, menu: Menu?): Boolean {
                return false
            }
            override fun onPrepareActionMode(p0: ActionMode?, menu: Menu?): Boolean {
                return false
            }
            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                return false
            }
            override fun onDestroyActionMode(p0: ActionMode?) {
            }
        }
        binding.edDescription.customSelectionActionModeCallback = actionCallback
    }

    private fun setTextSize() = with(binding){
        edTitle.setTextSize(pref?.getString("title_size_key", "16"))
       edDescription.setTextSize(pref?.getString("content_size_key", "14"))
    }

    //Экстеншион функция для установления разммера текста у EditText, по умолчанию у него нет изменения размера текста
    private fun EditText.setTextSize(size: String?){
        if (size != null) this.textSize = size.toFloat()
    }

    //функция по выбору цвета темы
    private fun getSelectedTheme(): Int{
        return if (defPreferences.getString("theme_key", "blue") == "blue") R.style.Theme_ShoppingListBlue
        else R.style.Theme_ShoppingLisOrange
    }
}