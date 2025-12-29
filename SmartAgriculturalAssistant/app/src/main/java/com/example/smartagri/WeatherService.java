package com.example.smartagri;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Weather service using WebService (ksoap2) to get weather information
 * 天气服务类，使用WebService获取天气信息
 */
public class WeatherService {
    private static final String TAG = "WeatherService";
    
    // WebService namespace
    private static final String NAMESPACE = "http://WebXml.com.cn/";
    
    // WebService URL
    private static final String URL = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx";
    
    // Method name
    private static final String METHOD_NAME = "getWeatherbyCityName";
    
    // SOAP Action
    private static final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
    
    /**
     * Interface for weather callback
     */
    public interface WeatherCallback {
        void onWeatherLoaded(String weatherInfo);
        void onWeatherError(String error);
    }
    
    /**
     * Get weather information by city name asynchronously
     * 异步获取城市天气信息
     * 
     * @param cityName City name in Chinese (e.g., "北京", "上海")
     * @param callback Callback for result
     */
    public static void getWeatherByCityName(String cityName, WeatherCallback callback) {
        new GetWeatherTask(callback).execute(cityName);
    }
    
    /**
     * AsyncTask to get weather information
     */
    private static class GetWeatherTask extends AsyncTask<String, Void, String> {
        private WeatherCallback callback;
        private String errorMessage;
        
        public GetWeatherTask(WeatherCallback callback) {
            this.callback = callback;
        }
        
        @Override
        protected String doInBackground(String... params) {
            if (params == null || params.length == 0) {
                errorMessage = "城市名称为空";
                return null;
            }
            
            String cityName = params[0];
            
            try {
                // Step 1: Create SoapObject
                SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_NAME);
                
                // Step 2: Set parameters
                soapObject.addProperty("theCityName", cityName);
                
                // Step 3: Create SoapSerializationEnvelope
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.bodyOut = soapObject;
                envelope.dotNet = true; // Important for .NET WebService
                envelope.setOutputSoapObject(soapObject);
                
                // Step 4: Create HttpTransportSE
                HttpTransportSE httpTransport = new HttpTransportSE(URL);
                httpTransport.debug = true;
                
                // Step 5: Call WebService
                httpTransport.call(SOAP_ACTION, envelope);
                
                // Step 6: Get response
                if (envelope.getResponse() != null) {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    SoapObject detail = (SoapObject) result.getProperty("getWeatherbyCityNameResult");
                    
                    // Parse weather data
                    return parseWeather(detail);
                } else {
                    errorMessage = "获取天气信息失败：响应为空";
                    return null;
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error getting weather", e);
                errorMessage = "网络错误: " + e.getMessage();
                return null;
            }
        }
        
        @Override
        protected void onPostExecute(String result) {
            if (callback != null) {
                if (result != null) {
                    callback.onWeatherLoaded(result);
                } else {
                    callback.onWeatherError(errorMessage != null ? errorMessage : "未知错误");
                }
            }
        }
        
        /**
         * Parse weather XML data
         * 解析天气XML数据
         */
        private String parseWeather(SoapObject detail) {
            if (detail == null) {
                return "暂无天气数据";
            }
            
            StringBuilder weatherInfo = new StringBuilder();
            
            try {
                int count = detail.getPropertyCount();
                
                // Usually the response contains:
                // [0]: City name
                // [1]: City info
                // [2-5]: Today's weather details
                // [6-9]: Tomorrow's weather details
                // etc.
                
                if (count > 0) {
                    // City and date
                    weatherInfo.append("城市: ").append(detail.getProperty(0).toString()).append("\n");
                }
                
                if (count > 1) {
                    weatherInfo.append(detail.getProperty(1).toString()).append("\n\n");
                }
                
                // Today's weather
                if (count > 4) {
                    weatherInfo.append("今日天气:\n");
                    weatherInfo.append(detail.getProperty(4).toString()).append("\n");
                    weatherInfo.append(detail.getProperty(5).toString()).append("\n");
                }
                
                // Tomorrow's weather
                if (count > 8) {
                    weatherInfo.append("\n明日天气:\n");
                    weatherInfo.append(detail.getProperty(8).toString()).append("\n");
                    weatherInfo.append(detail.getProperty(9).toString()).append("\n");
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error parsing weather", e);
                return "解析天气数据失败";
            }
            
            return weatherInfo.toString();
        }
    }
    
    /**
     * Get weather information synchronously (for testing)
     * 同步获取天气信息（用于测试）
     * Note: Must be called from a background thread
     */
    public static String getWeatherByCityNameSync(String cityName) {
        try {
            SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_NAME);
            soapObject.addProperty("theCityName", cityName);
            
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = soapObject;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(soapObject);
            
            HttpTransportSE httpTransport = new HttpTransportSE(URL);
            httpTransport.call(SOAP_ACTION, envelope);
            
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject detail = (SoapObject) result.getProperty("getWeatherbyCityNameResult");
                
                StringBuilder weatherInfo = new StringBuilder();
                int count = detail.getPropertyCount();
                
                for (int i = 0; i < Math.min(count, 10); i++) {
                    weatherInfo.append(detail.getProperty(i).toString()).append("\n");
                }
                
                return weatherInfo.toString();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in sync weather call", e);
            return "获取天气失败: " + e.getMessage();
        }
        
        return "无法获取天气信息";
    }
}
