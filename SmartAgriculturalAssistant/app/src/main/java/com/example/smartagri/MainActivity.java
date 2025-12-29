package com.example.smartagri;

import android.app.TabActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Main Activity with TabHost layout
 * 主界面，使用TabHost布局
 */
@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
    private static final String PREFS_NAME = "SmartAgriPrefs";
    
    private TabHost tabHost;
    private TextView weatherInfo;
    private TextView statusText;
    private ImageButton btnWater, btnFertilize, btnLoosenSoil;
    private CheckBox checkWater, checkFertilize, checkLoosenSoil;
    private Spinner spinnerPlantType, spinnerCity;
    private Button btnSaveSettings;
    private EditText searchPlant;
    private ExpandableListView plantList;
    
    private SharedPreferences preferences;
    private ReminderManager reminderManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        reminderManager = new ReminderManager(this);
        
        setupTabs();
        initializeViews();
        setupListeners();
        loadSettings();
        loadWeather();
        loadPlantData();
    }
    
    /**
     * Setup TabHost
     * 设置TabHost
     */
    private void setupTabs() {
        tabHost = getTabHost();
        
        // Plant Query Tab
        TabHost.TabSpec spec1 = tabHost.newTabSpec("plant_query");
        spec1.setIndicator(getString(R.string.tab_plant_query));
        spec1.setContent(R.id.tab_plant_query);
        tabHost.addTab(spec1);
        
        // Home Tab
        TabHost.TabSpec spec2 = tabHost.newTabSpec("home");
        spec2.setIndicator(getString(R.string.tab_home));
        spec2.setContent(R.id.tab_home);
        tabHost.addTab(spec2);
        
        // Settings Tab
        TabHost.TabSpec spec3 = tabHost.newTabSpec("settings");
        spec3.setIndicator(getString(R.string.tab_settings));
        spec3.setContent(R.id.tab_settings);
        tabHost.addTab(spec3);
        
        // Set default tab
        tabHost.setCurrentTab(1); // Start with Home tab
    }
    
    /**
     * Initialize all views
     * 初始化所有视图
     */
    private void initializeViews() {
        // Home tab views
        weatherInfo = findViewById(R.id.weather_info);
        statusText = findViewById(R.id.status_text);
        btnWater = findViewById(R.id.btn_water);
        btnFertilize = findViewById(R.id.btn_fertilize);
        btnLoosenSoil = findViewById(R.id.btn_loosen_soil);
        
        // Settings tab views
        checkWater = findViewById(R.id.check_water_reminder);
        checkFertilize = findViewById(R.id.check_fertilize_reminder);
        checkLoosenSoil = findViewById(R.id.check_loosen_soil_reminder);
        spinnerPlantType = findViewById(R.id.spinner_plant_type);
        spinnerCity = findViewById(R.id.spinner_city);
        btnSaveSettings = findViewById(R.id.btn_save_settings);
        
        // Plant query tab views
        searchPlant = findViewById(R.id.search_plant);
        plantList = findViewById(R.id.plant_list);
        
        // Setup spinners
        setupSpinners();
    }
    
    /**
     * Setup spinner data
     * 设置下拉列表数据
     */
    private void setupSpinners() {
        // Plant types
        String[] plantTypes = {
            "绿萝", "吊兰", "芦荟", "仙人掌", "多肉植物",
            "月季", "茉莉", "君子兰", "发财树", "富贵竹"
        };
        ArrayAdapter<String> plantAdapter = new ArrayAdapter<>(
            this, android.R.layout.simple_spinner_item, plantTypes);
        plantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlantType.setAdapter(plantAdapter);
        
        // Cities
        String[] cities = {
            "北京", "上海", "广州", "深圳", "杭州",
            "南京", "成都", "武汉", "西安", "重庆",
            "天津", "苏州", "长沙", "郑州", "青岛"
        };
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(
            this, android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(cityAdapter);
    }
    
    /**
     * Setup click listeners
     * 设置点击监听器
     */
    private void setupListeners() {
        // Home tab buttons
        btnWater.setOnClickListener(v -> handleWaterClick());
        btnFertilize.setOnClickListener(v -> handleFertilizeClick());
        btnLoosenSoil.setOnClickListener(v -> handleLoosenSoilClick());
        
        // Settings save button
        btnSaveSettings.setOnClickListener(v -> saveSettings());
    }
    
    /**
     * Handle water button click
     * 处理浇水按钮点击
     */
    private void handleWaterClick() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("watered", true);
        editor.putLong("last_water_time", System.currentTimeMillis());
        editor.apply();
        
        updateStatus();
        Toast.makeText(this, "已完成浇水", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Handle fertilize button click
     * 处理施肥按钮点击
     */
    private void handleFertilizeClick() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("fertilized", true);
        editor.putLong("last_fertilize_time", System.currentTimeMillis());
        editor.apply();
        
        updateStatus();
        Toast.makeText(this, "已完成施肥", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Handle loosen soil button click
     * 处理松土按钮点击
     */
    private void handleLoosenSoilClick() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("loosened", true);
        editor.putLong("last_loosen_time", System.currentTimeMillis());
        editor.apply();
        
        updateStatus();
        Toast.makeText(this, "已完成松土", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Update plant status
     * 更新植物状态
     */
    private void updateStatus() {
        boolean watered = preferences.getBoolean("watered", false);
        boolean fertilized = preferences.getBoolean("fertilized", false);
        boolean loosened = preferences.getBoolean("loosened", false);
        
        if (watered && fertilized && loosened) {
            statusText.setText(R.string.status_normal);
            statusText.setTextColor(getResources().getColor(R.color.status_normal));
        } else if (!watered) {
            statusText.setText(R.string.status_need_water);
            statusText.setTextColor(getResources().getColor(R.color.status_alert));
        } else if (!fertilized) {
            statusText.setText(R.string.status_need_fertilize);
            statusText.setTextColor(getResources().getColor(R.color.status_warning));
        } else {
            statusText.setText(R.string.status_need_loosen);
            statusText.setTextColor(getResources().getColor(R.color.status_warning));
        }
    }
    
    /**
     * Load settings from SharedPreferences
     * 从SharedPreferences加载设置
     */
    private void loadSettings() {
        checkWater.setChecked(preferences.getBoolean("water_reminder_enabled", false));
        checkFertilize.setChecked(preferences.getBoolean("fertilize_reminder_enabled", false));
        checkLoosenSoil.setChecked(preferences.getBoolean("loosen_reminder_enabled", false));
        
        int plantTypeIndex = preferences.getInt("plant_type_index", 0);
        int cityIndex = preferences.getInt("city_index", 0);
        
        spinnerPlantType.setSelection(plantTypeIndex);
        spinnerCity.setSelection(cityIndex);
        
        updateStatus();
    }
    
    /**
     * Save settings to SharedPreferences
     * 保存设置到SharedPreferences
     */
    private void saveSettings() {
        SharedPreferences.Editor editor = preferences.edit();
        
        boolean waterEnabled = checkWater.isChecked();
        boolean fertilizeEnabled = checkFertilize.isChecked();
        boolean loosenEnabled = checkLoosenSoil.isChecked();
        
        editor.putBoolean("water_reminder_enabled", waterEnabled);
        editor.putBoolean("fertilize_reminder_enabled", fertilizeEnabled);
        editor.putBoolean("loosen_reminder_enabled", loosenEnabled);
        editor.putInt("plant_type_index", spinnerPlantType.getSelectedItemPosition());
        editor.putInt("city_index", spinnerCity.getSelectedItemPosition());
        editor.apply();
        
        // Setup reminders
        if (waterEnabled) {
            reminderManager.setWaterReminder(3); // Every 3 days
        } else {
            reminderManager.cancelWaterReminder();
        }
        
        if (fertilizeEnabled) {
            reminderManager.setFertilizeReminder(7); // Every 7 days
        } else {
            reminderManager.cancelFertilizeReminder();
        }
        
        if (loosenEnabled) {
            reminderManager.setLoosenSoilReminder(14); // Every 14 days
        } else {
            reminderManager.cancelLoosenSoilReminder();
        }
        
        Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Load weather information
     * 加载天气信息
     */
    private void loadWeather() {
        String cityName = spinnerCity.getSelectedItem() != null ? 
            spinnerCity.getSelectedItem().toString() : "北京";
        
        WeatherService.getWeatherByCityName(cityName, new WeatherService.WeatherCallback() {
            @Override
            public void onWeatherLoaded(String weather) {
                weatherInfo.setText(weather);
            }
            
            @Override
            public void onWeatherError(String error) {
                weatherInfo.setText(getString(R.string.weather_error) + "\n" + error);
            }
        });
    }
    
    /**
     * Load plant data
     * 加载植物数据
     */
    private void loadPlantData() {
        // Create sample plant data
        List<String> groupList = new ArrayList<>();
        HashMap<String, List<String>> childMap = new HashMap<>();
        
        // Add plant categories
        String[] categories = {"常见植物", "多肉植物", "观花植物"};
        
        for (String category : categories) {
            groupList.add(category);
            
            List<String> children = new ArrayList<>();
            if (category.equals("常见植物")) {
                children.add("绿萝 - 喜阴湿，每周浇水2-3次");
                children.add("吊兰 - 喜光，每周浇水1-2次");
                children.add("发财树 - 耐旱，每月浇水2-3次");
            } else if (category.equals("多肉植物")) {
                children.add("仙人掌 - 耐旱，每月浇水1-2次");
                children.add("芦荟 - 喜光，每周浇水1次");
                children.add("多肉 - 喜光，每周浇水1次");
            } else {
                children.add("月季 - 喜光，每2-3天浇水1次");
                children.add("茉莉 - 喜光喜湿，每天浇水1次");
                children.add("君子兰 - 喜半阴，每周浇水2次");
            }
            
            childMap.put(category, children);
        }
        
        PlantExpandableListAdapter adapter = new PlantExpandableListAdapter(
            this, groupList, childMap);
        plantList.setAdapter(adapter);
    }
}
