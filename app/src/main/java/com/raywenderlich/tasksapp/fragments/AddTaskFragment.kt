package com.raywenderlich.tasksapp.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.raywenderlich.tasksapp.MainActivity
import com.raywenderlich.tasksapp.tools.AlarmReceiver
import com.raywenderlich.tasksapp.R
import com.raywenderlich.tasksapp.databinding.FragmentAddTaskBinding
import com.raywenderlich.tasksapp.tools.IDGenerator
import com.raywenderlich.tasksapp.viewmodels.SharedViewModel
import com.raywenderlich.tasksapp.viewmodels.TasksViewModel
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class AddTaskFragment : Fragment() {
    private lateinit var binding: FragmentAddTaskBinding
    private lateinit var viewModel: TasksViewModel
    private val args by navArgs<AddTaskFragmentArgs>()
    private lateinit var timePicker: MaterialTimePicker
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var calendar : Calendar
    private val sharedViewModel: SharedViewModel by lazy {
        (requireActivity() as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTaskBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[TasksViewModel::class.java]
        setUpCurrTask()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPriority()
        setUpReminderButton()
        val taskId : Long = IDGenerator.generateID()
        binding.createBtn.setOnClickListener {
            if(timeScheduled()){
                setAlarm(taskId)
            }
            createTask(taskId)
        }
        binding.backButton.setOnClickListener {
            findNavController().navigate(AddTaskFragmentDirections.actionAddTaskFragmentToViewPagerFragment2())
        }
    }

    private fun timeScheduled(): Boolean {
        return binding.reminderButton.text.toString().lowercase(Locale.getDefault()) != "set reminder"
    }

    private fun setUpReminderButton() {
        binding.reminderButton.setOnClickListener {
            setUpTimePicker()
        }
    }

    private fun setUpPriority() {
        val priorites = resources.getStringArray(R.array.priorites)
        val arrAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,priorites)
        binding.etPriority.setAdapter(arrAdapter)
    }
    private fun setUpTimePicker(){
        calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(currentHour)
            .setMinute(currentMinute)
            .setTitleText("Select Time")
            .build()

        timePicker.show(requireActivity().supportFragmentManager, "time_picker")
        timePicker.addOnPositiveButtonClickListener {
            val chosenHour = timePicker.hour
            val chosenMinute = timePicker.minute
            val selectedTime = formatTime(chosenHour, chosenMinute)
            calendar[Calendar.HOUR_OF_DAY] = timePicker.hour
            calendar[Calendar.MINUTE] = timePicker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            val currentTime = Calendar.getInstance()
            if(calendar.get(Calendar.HOUR_OF_DAY) < currentTime.get(Calendar.HOUR_OF_DAY)
                && calendar.get(Calendar.MINUTE) < currentTime.get(Calendar.MINUTE)){
                binding.reminderButton.text = "Tomorrow $selectedTime"
            }else{
                binding.reminderButton.text = " Today $selectedTime"
            }
        }
    }
    private fun formatTime(hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val format = SimpleDateFormat("h:mm a", Locale.getDefault())
        return format.format(calendar.time)
    }
    private fun setAlarm(taskId : Long){
        alarmManager = requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        intent.putExtra("taskId",taskId)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), taskId.toInt(), intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
    private fun cancelAlarm(taskId: Long) {
        alarmManager = requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireContext(), taskId.toInt(), intent, 0)
        alarmManager.cancel(pendingIntent)
    }
    private fun setUpCurrTask(){
//        binding.etTitle.setText(args.currTask?.title)
//        binding.etDescription.setText(args.currTask?.description)
//        binding.etPriority.setText(args.currTask?.priority?.let { getPriorityForColor(it) })
//        binding.reminderButton.text = args.currTask?.alarmTime
    }


    private fun createTask(taskId: Long){
        var text = "Rings${binding.reminderButton.text}"
        if(!timeScheduled())
            text = "Set reminder"

        viewModel.insertDataToDatabase(taskId,binding.etTitle,binding.etDescription,getColorForPriority(binding.etPriority.text.toString()),text)
        findNavController().navigate(AddTaskFragmentDirections.actionAddTaskFragmentToViewPagerFragment2())
    }
    fun getColorForPriority(priority: String): Int {
        return when(priority) {
            "HIGH" ->R.color.red
            "MEDIUM" -> R.color.orange
            else -> R.color.light_blue
        }
    }
    fun getPriorityForColor(color: Int): String {
        return when(color) {
            R.color.red -> "HIGH"
            R.color.orange -> "MEDIUM"
            else -> "LOW"
        }
    }

    private fun updateTask(taskId: Long){
        var text = "Rings${binding.reminderButton.text}"
        if(!timeScheduled())
            text = "Set reminder"

        viewModel.updateData(taskId,binding.etTitle,binding.etDescription,getColorForPriority(binding.etPriority.text.toString()),text)
    }

    private fun inputCheck(title : String,description : String) : Boolean{
        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(description))
    }
}