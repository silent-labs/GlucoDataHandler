package de.michelinside.glucodatahandler

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuCompat
import androidx.preference.PreferenceManager
import de.michelinside.glucodatahandler.android_auto.CarModeReceiver
import de.michelinside.glucodatahandler.common.Constants
import de.michelinside.glucodatahandler.common.ReceiveData
import de.michelinside.glucodatahandler.common.Utils
import de.michelinside.glucodatahandler.common.WearPhoneConnection
import de.michelinside.glucodatahandler.common.notifier.InternalNotifier
import de.michelinside.glucodatahandler.common.notifier.NotifierInterface
import de.michelinside.glucodatahandler.common.notifier.NotifySource
import de.michelinside.glucodatahandler.common.tasks.DataSourceTask
import de.michelinside.glucodatahandler.common.R as CR

class MainActivity : AppCompatActivity(), NotifierInterface {
    private lateinit var txtBgValue: TextView
    private lateinit var viewIcon: ImageView
    private lateinit var txtLastValue: TextView
    private lateinit var txtVersion: TextView
    private lateinit var txtWearInfo: TextView
    private lateinit var txtCarInfo: TextView
    private lateinit var txtSourceInfo: TextView
    private lateinit var txtBatteryOptimization: TextView
    private lateinit var sharedPref: SharedPreferences
    private val LOG_ID = "GDH.Main"
    private var requestNotificationPermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            Log.v(LOG_ID, "onCreate called")

            GlucoDataServiceMobile.start(this, true)

            txtBgValue = findViewById(R.id.txtBgValue)
            viewIcon = findViewById(R.id.viewIcon)
            txtLastValue = findViewById(R.id.txtLastValue)
            txtWearInfo = findViewById(R.id.txtWearInfo)
            txtCarInfo = findViewById(R.id.txtCarInfo)
            txtSourceInfo = findViewById(R.id.txtSourceInfo)
            txtBatteryOptimization = findViewById(R.id.txtBatteryOptimization)

            PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
            sharedPref = this.getSharedPreferences(Constants.SHARED_PREF_TAG, Context.MODE_PRIVATE)

            ReceiveData.initData(this)

            txtVersion = findViewById(R.id.txtVersion)
            txtVersion.text = BuildConfig.VERSION_NAME

            val sendToAod = sharedPref.getBoolean(Constants.SHARED_PREF_SEND_TO_GLUCODATA_AOD, false)

            if(!sharedPref.contains(Constants.SHARED_PREF_GLUCODATA_RECEIVERS)) {
                val receivers = HashSet<String>()
                if (sendToAod)
                    receivers.add("de.metalgearsonic.glucodata.aod")
                Log.i(LOG_ID, "Upgrade receivers to " + receivers.toString())
                with(sharedPref.edit()) {
                    putStringSet(Constants.SHARED_PREF_GLUCODATA_RECEIVERS, receivers)
                    apply()
                }
            }

            if(!sharedPref.contains(Constants.SHARED_PREF_XDRIP_RECEIVERS)) {
                val receivers = HashSet<String>()
                receivers.add("com.eveningoutpost.dexdrip")
                Log.i(LOG_ID, "Upgrade receivers to " + receivers.toString())
                with(sharedPref.edit()) {
                    putStringSet(Constants.SHARED_PREF_XDRIP_RECEIVERS, receivers)
                    apply()
                }
            }

            if (requestPermission())
                GlucoDataServiceMobile.start(this, true)
        } catch (exc: Exception) {
            Log.e(LOG_ID, "onCreate exception: " + exc.message.toString() )
        }
    }

    override fun onPause() {
        try {
            super.onPause()
            InternalNotifier.remNotifier(this, this)
            Log.v(LOG_ID, "onPause called")
        } catch (exc: Exception) {
            Log.e(LOG_ID, "onPause exception: " + exc.message.toString() )
        }
    }

    override fun onResume() {
        try {
            super.onResume()
            Log.v(LOG_ID, "onResume called")
            update()
            InternalNotifier.addNotifier(this, this, mutableSetOf(
                NotifySource.BROADCAST,
                NotifySource.MESSAGECLIENT,
                NotifySource.CAPILITY_INFO,
                NotifySource.NODE_BATTERY_LEVEL,
                NotifySource.SETTINGS,
                NotifySource.CAR_CONNECTION,
                NotifySource.OBSOLETE_VALUE,
                NotifySource.SOURCE_STATE_CHANGE))
            checkBatteryOptimization()

            if (requestNotificationPermission && Utils.checkPermission(this, android.Manifest.permission.POST_NOTIFICATIONS, Build.VERSION_CODES.TIRAMISU)) {
                Log.i(LOG_ID, "Notification permission granted")
                requestNotificationPermission = false
                GlucoDataServiceMobile.start(this, true)
            }
        } catch (exc: Exception) {
            Log.e(LOG_ID, "onResume exception: " + exc.message.toString() )
        }
    }

    fun requestPermission() : Boolean {
        requestNotificationPermission = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!Utils.checkPermission(this, android.Manifest.permission.POST_NOTIFICATIONS, Build.VERSION_CODES.TIRAMISU)) {
                Log.i(LOG_ID, "Request notification permission...")
                requestNotificationPermission = true
                this.requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 3)
                return false
            }
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.i(LOG_ID, "Request exact alarm permission...")
                startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
            }
        }
        return true
    }

    private fun checkBatteryOptimization() {
        try {
            val pm = getSystemService(POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                Log.w(LOG_ID, "Battery optimization is inactive")
                txtBatteryOptimization.visibility = View.VISIBLE
                txtBatteryOptimization.setOnClickListener {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:$packageName")
                    startActivity(intent)
                }
            } else {
                txtBatteryOptimization.visibility = View.GONE
                Log.i(LOG_ID, "Battery optimization is active")
            }
        } catch (exc: Exception) {
            Log.e(LOG_ID, "checkBatteryOptimization exception: " + exc.message.toString() )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        try {
            Log.v(LOG_ID, "onCreateOptionsMenu called")
            val inflater = menuInflater
            inflater.inflate(R.menu.menu_items, menu)
            MenuCompat.setGroupDividerEnabled(menu!!, true)
            return true
        } catch (exc: Exception) {
            Log.e(LOG_ID, "onCreateOptionsMenu exception: " + exc.message.toString() )
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        try {
            Log.v(LOG_ID, "onOptionsItemSelected for " + item.itemId.toString())
            when(item.itemId) {
                R.id.action_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    intent.putExtra(SettingsActivity.FRAGMENT_EXTRA, SettingsFragmentClass.SETTINGS_FRAGMENT.value)
                    startActivity(intent)
                    return true
                }
                R.id.action_sources -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    intent.putExtra(SettingsActivity.FRAGMENT_EXTRA, SettingsFragmentClass.SORUCE_FRAGMENT.value)
                    startActivity(intent)
                    return true
                }
                R.id.action_help -> {
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(resources.getText(CR.string.help_link).toString())
                    )
                    startActivity(browserIntent)
                    return true
                }
                R.id.action_support -> {
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(resources.getText(CR.string.support_link).toString())
                    )
                    startActivity(browserIntent)
                    return true
                }
                else -> return super.onOptionsItemSelected(item)
            }
        } catch (exc: Exception) {
            Log.e(LOG_ID, "onOptionsItemSelected exception: " + exc.message.toString() )
        }
        return super.onOptionsItemSelected(item)
    }

    private fun update() {
        try {
            Log.v(LOG_ID, "update values")
            txtBgValue.text = ReceiveData.getClucoseAsString()
            txtBgValue.setTextColor(ReceiveData.getClucoseColor())
            if (ReceiveData.isObsolete(Constants.VALUE_OBSOLETE_SHORT_SEC) && !ReceiveData.isObsolete()) {
                txtBgValue.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                txtBgValue.paintFlags = 0
            }
            viewIcon.setImageIcon(Utils.getRateAsIcon())
            txtLastValue.text = ReceiveData.getAsString(this)
            if (WearPhoneConnection.nodesConnected) {
                txtWearInfo.text = resources.getString(CR.string.activity_main_connected_label, WearPhoneConnection.getBatterLevelsAsString())
            }
            else
                txtWearInfo.text = resources.getText(CR.string.activity_main_disconnected_label)
            if (Utils.isPackageAvailable(this, Constants.PACKAGE_GLUCODATAAUTO)) {
                txtCarInfo.text = if (CarModeReceiver.connected) resources.getText(CR.string.activity_main_car_connected_label) else resources.getText(CR.string.activity_main_car_disconnected_label)
                txtCarInfo.visibility = View.VISIBLE
            } else {
                txtCarInfo.visibility = View.GONE
            }

            txtSourceInfo.text = DataSourceTask.getState(this)
        } catch (exc: Exception) {
            Log.e(LOG_ID, "update exception: " + exc.message.toString() )
        }
    }

    override fun OnNotifyData(context: Context, dataSource: NotifySource, extras: Bundle?) {
        Log.v(LOG_ID, "new intent received")
        update()
    }
}