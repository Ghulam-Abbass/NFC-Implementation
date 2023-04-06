package com.app.nfc_scanner.screens

import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.nfc.NfcAdapter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.app.nfc_scanner.MyApplication
import com.app.nfc_scanner.NFC.NfcReaderUtils
import com.app.nfc_scanner.Utils.ApisResponse
import com.app.nfc_scanner.databinding.ActivityNfcactivityBinding
import com.app.nfc_scanner.factory.ViewModelFactory
import com.app.nfc_scanner.model.nfc_model.NfcModel
import com.app.nfc_scanner.viewModel.NfcViewModel
import com.example.myapplication.Utils.SharedPref
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.app.nfc_scanner.R
import com.app.nfc_scanner.model.Status
import com.app.nfc_scanner.model.nfc_model.BarcodeData
import com.example.myapplication.Utils.Functions.openNfcSettings
import com.example.myapplication.Utils.Functions.setColor
import com.example.myapplication.Utils.Functions.showToast
import javax.inject.Inject

class NFCActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNfcactivityBinding
    private var context = this
    private var pendingIntent: PendingIntent? = null
    private var nfcAdapter: NfcAdapter? = null
    private lateinit var nfcReaderUtils: NfcReaderUtils
    private lateinit var sp: SharedPref
    var skiId: Int = 0
    var manufactureName: String = ""
    var isSkiInventory: Boolean = false
    private lateinit var viewModel: NfcViewModel
    private var TAG = "API RESPONSE"

    @Inject
    lateinit var factory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNfcactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }


    private fun initUI() {
        sp = SharedPref(context)
        setUpViewModel()
        clickListeners()
        nfcBarcodeInitialise()
    }

    private fun nfcBarcodeInitialise() {
        pendingIntent = if (Build.VERSION.SDK_INT >= 31) {
            PendingIntent.getActivity(
                context, 0, Intent(this, javaClass)
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                context, 0,
                Intent(context, this.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
            )
        }

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcReaderUtils = NfcReaderUtils()

        nfcReaderUtils.setOnNfcScanListener(object : NfcReaderUtils.OnNfcScanListener {
            override fun nfcBarcode(nfcCode: String) {
                binding.etNfc.setText(nfcCode)
                val searchType = if (binding.cbHelmet.isChecked) "addon" else "inventory"

                viewModel.hitNfcScan(
                    sp.getProfile()?.accessToken.toString(),
                    binding.etNfc.text.toString(), searchType
                )
            }
        })

        nfcAdapter.let { nfc ->
            nfc?.isEnabled?.let { enable ->
                if (!enable) {
                    openNfcSettings(context)
                }
            }
        }
    }

    private fun clickListeners() {
        binding.btnNfScan.setOnClickListener {
            if (binding.etNfc.text.toString().isNotEmpty() &&
                binding.etNfc.text.toString().isNotBlank()
            ) {
                val searchType = if (binding.cbHelmet.isChecked) "addon" else "inventory"

                viewModel.hitNfcScan(
                    sp.getProfile()?.accessToken.toString(),
                    binding.etNfc.text.toString(), searchType
                )

            } else {
                showToast(context, "Please enter barcode")
            }
        }

//        binding.btnForceAvailable.setOnClickListener {
//            if (binding.etNfc.text.toString().isNotEmpty() &&
//                binding.etNfc.text.toString().isNotBlank()
//            ) {
//                viewModel.hitForceAvailable(
//                    sharedPrefsHelper.getUser()?.accessToken.toString(),
//                    binding.etNfc.text.toString()
//                )
//            } else {
//                showToast(context, "Please enter barcode")
//            }
//        }

//        binding.btnAddTorque.setOnClickListener {
//            startActivity(
//                Intent(this, AddTorqueTestingActivity::class.java)
//                    .putExtra("barcode", binding.etNfc.text.toString())
//                    .putExtra("skiId", skiId.toString())
//                    .putExtra("manufactureName", manufactureName)
//            )
//        }

        binding.cbHelmet.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (binding.etNfc.text.toString().isNotEmpty() &&
                    binding.etNfc.text.toString().isNotBlank()
                ) {
                    val searchType = if (binding.cbHelmet.isChecked) "addon" else "inventory"

                    viewModel.hitNfcScan(
                        sp.getProfile()?.accessToken.toString(),
                        binding.etNfc.text.toString(), searchType
                    )
                }
            }
        }
    }

    fun setUpViewModel() {
        MyApplication.getAppComponent(this).doInjection(this)
        viewModel = ViewModelProvider(this, factory).get(NfcViewModel::class.java)

        viewModel.nfcScanResponse().observe(this) {
            consumeResponse(it)
        }
    }

    private fun consumeResponse(apiResponse: ApisResponse<NfcModel>?) {
        when (apiResponse?.status) {
            Status.LOADING -> {
                hideAllViews()
            }

            Status.SUCCESS -> {
                binding.cbHelmet.isChecked = false
                val nfcModel = apiResponse.data as NfcModel

                if (nfcModel.status.equals("not-found", true)) {
                    binding.tvStatusName.visibility = View.VISIBLE
                    binding.tvStatusName.text = "Item not found"

                } else {
                    showData(nfcModel)
                }
            }

            Status.ERROR -> {
                hideAllViews()
                showToast(context, apiResponse.error.toString())
            }

            else -> {
            }
        }
    }

    private fun hideAllViews() {
        binding.run {
            tvType.text = ""
            tvHelmetType.text = ""
            tvManufacture.text = ""
            tvHelemtManufacture.text = ""
            tvModel.text = ""
            tvHelmetModel.text = ""
            tvSize.text = ""
            tvHelmetSize.text = ""
            tvLength.text = ""
            tvLocation.text = ""
            tvPackage.text = ""
            tvBrokenAt.text = ""
            tvVTSkierCode.text = ""
            tvVTSoleLength.text = ""
            tvVTDinSetting.text = ""
            tvVTTwistLeftReading.text = ""
            tvVTTwistRightReading.text = ""
            tvVTForwardReading.text = ""
            tvVTTwistLeftReading2.text = ""
            tvVTTwistRightReading2.text = ""
            tvVTForwardReading2.text = ""
            tvVTConditions.text = ""
            tvVTNotes.text = ""
            tvReservationId.text = ""
            tvReservationName.text = ""
            tvRenterName.text = ""
            tvReturnDate.text = ""
            tvStatusName.visibility = View.GONE
            btnAddTorque.visibility = View.GONE
            btnForceAvailable.visibility = View.GONE
            layBroken.visibility = View.GONE

            layInventoryDetails.visibility = View.VISIBLE
            cvHelmetDetails.visibility = View.GONE
            tvRenterResDetailsLay.visibility = View.GONE
            tvVTLay.visibility = View.GONE
        }
    }

    fun showData(nfcModel: NfcModel) {
        val barcodeResponse = nfcModel.`object`

        if (barcodeResponse != null) {
            binding.run {

                if (barcodeResponse.search_type.equals("Inventory", true)) {
                    cvHelmetDetails.visibility = View.GONE
                    layInventoryDetails.visibility = View.VISIBLE
                    skiId = barcodeResponse.id

                    if (!barcodeResponse.type.isNullOrEmpty()) {
                        tvType.text = barcodeResponse.type
                        isSkiInventory = barcodeResponse.type.equals("ski", ignoreCase = true) ||
                                barcodeResponse.type.equals("skier", ignoreCase = true)
                    }

                    if (!barcodeResponse.manufacture.isNullOrEmpty()) {
                        manufactureName = barcodeResponse.manufacture
                        tvManufacture.text = manufactureName
                    }

                    if (!barcodeResponse.model.isNullOrEmpty()) {
                        tvModel.text = barcodeResponse.model
                    }

                    if (!barcodeResponse.size.isNullOrEmpty()) {
                        laySize.visibility = View.VISIBLE
                        tvSize.text = barcodeResponse.size
                    } else {
                        laySize.visibility = View.GONE
                    }

                    if (!barcodeResponse.length.isNullOrEmpty()) {
                        layLength.visibility = View.VISIBLE
                        tvLength.text = barcodeResponse.length
                    } else {
                        layLength.visibility = View.GONE
                    }

                    if (!barcodeResponse.inventory_location_name.isNullOrEmpty()) {
                        tvLocation.text = barcodeResponse.inventory_location_name
                    }

                    if (barcodeResponse.packages_resort != null) {
                        if (barcodeResponse.packages_resort.isNotEmpty()) {
                            tvPackage.text = (barcodeResponse.packages_resort[0])
                        }
                    }

                    if (!barcodeResponse.status.isNullOrEmpty()) {
                        tvStatusName.visibility = View.VISIBLE
                        tvStatusName.text = barcodeResponse.status
                        if (barcodeResponse.status.equals("Rented", true)) {
                            tvStatusName.setBackgroundColor(
                                setColor(context, R.color.yellow)
                            )

                            showDetailsCard(barcodeResponse)

                        } else {
                            tvStatusName.setBackgroundColor(
                                setColor(context, R.color.purple_500)
                            )
                        }

                        if (barcodeResponse.status.equals("Broken", true)) {
                            layBroken.visibility = View.VISIBLE
                            tvBrokenAt.text = barcodeResponse.broken_at
                        } else {
                            layBroken.visibility = View.GONE
                            tvBrokenAt.text = ""
                        }
                    }

                    if (!barcodeResponse.status.equals("available", true)) {
                        binding.btnForceAvailable.visibility = View.VISIBLE
                    }

                } else {
                    cvHelmetDetails.visibility = View.VISIBLE
                    layInventoryDetails.visibility = View.GONE
                    tvHelmetType.text = barcodeResponse.type

                    if (!barcodeResponse.manufacture.isNullOrEmpty()) {
                        tvHelemtManufacture.text = barcodeResponse.manufacture
                    }

                    if (!barcodeResponse.model.isNullOrEmpty()) {
                        tvHelmetModel.text = barcodeResponse.model
                    }

                    if (!barcodeResponse.size.isNullOrEmpty()) {
                        layHelmetSize.visibility = View.VISIBLE
                        tvHelmetSize.text = barcodeResponse.size
                    } else {
                        layHelmetSize.visibility = View.GONE
                    }

                    if (!barcodeResponse.status.isNullOrEmpty()) {
                        tvHelmetStatus.visibility = View.VISIBLE
                        tvHelmetStatus.text = barcodeResponse.status

                        if (barcodeResponse.status.equals("Rented", true)
                            || barcodeResponse.status.equals("Fitted", true)
                        ) {
                            tvHelmetStatus.setBackgroundColor(
                                setColor(context, R.color.yellow)
                            )
                            showDetailsCard(barcodeResponse)

                        } else {
                            tvHelmetStatus.setBackgroundColor(
                                setColor(context, R.color.purple_500)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun showDetailsCard(barcodeResponse: BarcodeData) {
        binding.run {
            if (barcodeResponse.detail != null) {
                tvRenterResDetailsLay.visibility = View.VISIBLE
                tvReservationId.text = barcodeResponse.detail.reservation_id
                tvReservationName.text = barcodeResponse.detail.reservation_name
                tvRenterName.text = barcodeResponse.detail.renter_name
                tvReturnDate.text = barcodeResponse.detail.return_date
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        nfcReaderUtils.resolveIntent(intent!!)
    }

    override fun onResume() {
        super.onResume()

        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)

        if (binding.etNfc.text.toString().isNotEmpty() &&
            binding.etNfc.text.toString().isNotBlank()
        ) {
            val searchType = if (binding.cbHelmet.isChecked) "addon" else "inventory"

            viewModel.hitNfcScan(
                sp.getProfile()?.accessToken.toString(),
                binding.etNfc.text.toString(), searchType
            )
        }

    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

//    private fun getTodo(){
//        val stringRequest = object : StringRequest(Request.Method.GET,gets,
//            Response.Listener { response ->
//                Log.e("Response",response.toString())
//            }, Response.ErrorListener { error ->
//                Toast.makeText(context, "Volley error: ${error.message}", Toast.LENGTH_LONG).show()
//            }){
//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params["Authorization"] = "Bearer " + sp.getProfile()!!.accessToken
//                return params
//            }
//        }
//        ExecuteApis.getInstance(this)?.ApisVolleyRequest(stringRequest)
//        ExecuteApis.getInstance(this)?.requestQueue?.getCache()?.clear()
//    }
}