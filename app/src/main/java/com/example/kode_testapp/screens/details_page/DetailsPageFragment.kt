package com.example.kode_testapp.screens.details_page

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.MaskFilter
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.URLSpan
import android.text.util.Linkify
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.kode_testapp.MainActivity
import com.example.kode_testapp.databinding.FragmentDetailsPageBinding
import com.example.kode_testapp.screens.factory
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class DetailsPageFragment : Fragment() {

    private val detailsPageViewModel by viewModels<DetailsPageViewModel> {factory()}

    var binding: FragmentDetailsPageBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            arguments?.let { detailsPageViewModel.loadWorker(it.getString("ID_WORKER", "EMPTY")) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentDetailsPageBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailsPageViewModel.uiStateChanges.observe(viewLifecycleOwner) { workerUiState ->
            renderState(workerUiState)
        }
    }

    private fun renderState(workerUiState: WorkerUiState) {
        when {
            workerUiState.worker !== null && workerUiState.error == null  -> {
                val worker = workerUiState.worker
                with(binding!!) {
                    Glide.with(requireContext())
                        .load(workerUiState.worker.avatarUrl)
                        .circleCrop()
                        .into(imageViewWorker)
                    textViewFullName.text = "${worker.firstName} ${worker.lastName}"
                    textViewDepartment.text = worker.department.substring(0, 1).toUpperCase() + worker.department.substring(1)
                    textViewDateBirthday.text = formatDate(worker.birthday)
                    textViewUserTag.text = worker.userTag.toLowerCase()
                    textViewAge.text = calculatePeriod(worker.birthday)
                    textViewPhoneNumber.text = "+7 ${worker.phone}"
                    textViewPhoneNumber.autoLinkMask = Linkify.PHONE_NUMBERS
                    stripUnderlines(textViewPhoneNumber)

                    imageViewButtonBack.setOnClickListener {
                        this@DetailsPageFragment.requireActivity().onBackPressed()
                    }
                }
            }
        }
    }

    private fun stripUnderlines(textView: TextView) {
        val s: Spannable = SpannableString(textView.text)
        val spans = s.getSpans(0, s.length, URLSpan::class.java)
        for (span in spans) {
            val start = s.getSpanStart(span)
            val end = s.getSpanEnd(span)
            s.removeSpan(span)
            val spanNew = URLSpanNoUnderline(span.url)
            s.setSpan(spanNew, start, end, 0)
        }
        textView.text = s.toString()
    }

    private fun calculatePeriod(birthday: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localDate = LocalDate.parse(birthday, inputFormatter)
        val today = LocalDate.now()
        val period = Period.between(localDate, today)
        return "${period.years.toString()} лет"
    }

    private fun formatDate(birthday: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
        val localDate = LocalDate.parse(birthday, inputFormatter)
        val localDateTime = localDate.atStartOfDay()
        val zonedDateTime = localDateTime.atZone(ZoneId.of("Europe/Paris"))
        return outputFormatter.format(zonedDateTime)
    }
}

private class URLSpanNoUnderline(url: String) : URLSpan(url) {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }
}