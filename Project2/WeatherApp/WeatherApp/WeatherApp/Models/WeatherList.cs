using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace WeatherApp.Models
{
    public class WeatherList
    {
        
        string apiBase = "http://api.openweathermap.org/data/2.5/forecast?appid=9c9527823d0d06d5fac5e11d5865d56f&q=";

        private string template =
            "{\"cod\":\"200\",\"message\":0,\"cnt\":40,\"list\":[{\"dt\":1575309600,\"main\":{\"temp\":284.74,\"temp_min\":284.74,\"temp_max\":284.76,\"pressure\":1017,\"sea_level\":1017,\"grnd_level\":998,\"humidity\":59,\"temp_kf\":-0.02},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":4.14,\"deg\":35},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-02 18:00:00\"},{\"dt\":1575320400,\"main\":{\"temp\":282.75,\"temp_min\":282.75,\"temp_max\":282.76,\"pressure\":1019,\"sea_level\":1019,\"grnd_level\":999,\"humidity\":63,\"temp_kf\":-0.01},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.45,\"deg\":63},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-02 21:00:00\"},{\"dt\":1575331200,\"main\":{\"temp\":281.16,\"temp_min\":281.16,\"temp_max\":281.17,\"pressure\":1019,\"sea_level\":1019,\"grnd_level\":999,\"humidity\":66,\"temp_kf\":-0.01},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.93,\"deg\":64},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-03 00:00:00\"},{\"dt\":1575342000,\"main\":{\"temp\":280.15,\"temp_min\":280.15,\"temp_max\":280.15,\"pressure\":1019,\"sea_level\":1019,\"grnd_level\":998,\"humidity\":67,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.77,\"deg\":67},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-03 03:00:00\"},{\"dt\":1575352800,\"main\":{\"temp\":279.24,\"temp_min\":279.24,\"temp_max\":279.24,\"pressure\":1018,\"sea_level\":1018,\"grnd_level\":998,\"humidity\":65,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.97,\"deg\":77},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-03 06:00:00\"},{\"dt\":1575363600,\"main\":{\"temp\":279.78,\"temp_min\":279.78,\"temp_max\":279.78,\"pressure\":1019,\"sea_level\":1019,\"grnd_level\":998,\"humidity\":62,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":3.18,\"deg\":87},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2019-12-03 09:00:00\"},{\"dt\":1575374400,\"main\":{\"temp\":284.75,\"temp_min\":284.75,\"temp_max\":284.75,\"pressure\":1017,\"sea_level\":1017,\"grnd_level\":997,\"humidity\":46,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":1.74,\"deg\":74},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2019-12-03 12:00:00\"},{\"dt\":1575385200,\"main\":{\"temp\":286.11,\"temp_min\":286.11,\"temp_max\":286.11,\"pressure\":1015,\"sea_level\":1015,\"grnd_level\":995,\"humidity\":44,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":1.43,\"deg\":44},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2019-12-03 15:00:00\"},{\"dt\":1575396000,\"main\":{\"temp\":282.53,\"temp_min\":282.53,\"temp_max\":282.53,\"pressure\":1015,\"sea_level\":1015,\"grnd_level\":994,\"humidity\":59,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.04,\"deg\":100},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-03 18:00:00\"},{\"dt\":1575406800,\"main\":{\"temp\":280.53,\"temp_min\":280.53,\"temp_max\":280.53,\"pressure\":1016,\"sea_level\":1016,\"grnd_level\":995,\"humidity\":64,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.09,\"deg\":98},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-03 21:00:00\"},{\"dt\":1575417600,\"main\":{\"temp\":279.71,\"temp_min\":279.71,\"temp_max\":279.71,\"pressure\":1014,\"sea_level\":1014,\"grnd_level\":994,\"humidity\":61,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":1.85,\"deg\":102},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-04 00:00:00\"},{\"dt\":1575428400,\"main\":{\"temp\":279.16,\"temp_min\":279.16,\"temp_max\":279.16,\"pressure\":1014,\"sea_level\":1014,\"grnd_level\":994,\"humidity\":64,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":1.42,\"deg\":96},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-04 03:00:00\"},{\"dt\":1575439200,\"main\":{\"temp\":278.98,\"temp_min\":278.98,\"temp_max\":278.98,\"pressure\":1013,\"sea_level\":1013,\"grnd_level\":993,\"humidity\":69,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":1.62,\"deg\":86},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-04 06:00:00\"},{\"dt\":1575450000,\"main\":{\"temp\":279.97,\"temp_min\":279.97,\"temp_max\":279.97,\"pressure\":1014,\"sea_level\":1014,\"grnd_level\":994,\"humidity\":69,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":1},\"wind\":{\"speed\":1.9,\"deg\":81},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2019-12-04 09:00:00\"},{\"dt\":1575460800,\"main\":{\"temp\":285.03,\"temp_min\":285.03,\"temp_max\":285.03,\"pressure\":1013,\"sea_level\":1013,\"grnd_level\":994,\"humidity\":53,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":1},\"wind\":{\"speed\":1.61,\"deg\":43},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2019-12-04 12:00:00\"},{\"dt\":1575471600,\"main\":{\"temp\":286.13,\"temp_min\":286.13,\"temp_max\":286.13,\"pressure\":1012,\"sea_level\":1012,\"grnd_level\":993,\"humidity\":52,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":3},\"wind\":{\"speed\":2.7,\"deg\":29},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2019-12-04 15:00:00\"},{\"dt\":1575482400,\"main\":{\"temp\":283.25,\"temp_min\":283.25,\"temp_max\":283.25,\"pressure\":1014,\"sea_level\":1014,\"grnd_level\":994,\"humidity\":64,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":1},\"wind\":{\"speed\":2.08,\"deg\":34},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-04 18:00:00\"},{\"dt\":1575493200,\"main\":{\"temp\":281.78,\"temp_min\":281.78,\"temp_max\":281.78,\"pressure\":1016,\"sea_level\":1016,\"grnd_level\":996,\"humidity\":68,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":1.76,\"deg\":48},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-04 21:00:00\"},{\"dt\":1575504000,\"main\":{\"temp\":280.94,\"temp_min\":280.94,\"temp_max\":280.94,\"pressure\":1017,\"sea_level\":1017,\"grnd_level\":997,\"humidity\":68,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":1.88,\"deg\":59},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-05 00:00:00\"},{\"dt\":1575514800,\"main\":{\"temp\":280.68,\"temp_min\":280.68,\"temp_max\":280.68,\"pressure\":1018,\"sea_level\":1018,\"grnd_level\":998,\"humidity\":68,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.11,\"deg\":67},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-05 03:00:00\"},{\"dt\":1575525600,\"main\":{\"temp\":280.82,\"temp_min\":280.82,\"temp_max\":280.82,\"pressure\":1019,\"sea_level\":1019,\"grnd_level\":998,\"humidity\":69,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.11,\"deg\":87},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-05 06:00:00\"},{\"dt\":1575536400,\"main\":{\"temp\":281.71,\"temp_min\":281.71,\"temp_max\":281.71,\"pressure\":1021,\"sea_level\":1021,\"grnd_level\":1001,\"humidity\":67,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":1.87,\"deg\":76},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2019-12-05 09:00:00\"},{\"dt\":1575547200,\"main\":{\"temp\":286.99,\"temp_min\":286.99,\"temp_max\":286.99,\"pressure\":1021,\"sea_level\":1021,\"grnd_level\":1001,\"humidity\":50,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.47,\"deg\":97},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2019-12-05 12:00:00\"},{\"dt\":1575558000,\"main\":{\"temp\":287.94,\"temp_min\":287.94,\"temp_max\":287.94,\"pressure\":1020,\"sea_level\":1020,\"grnd_level\":1000,\"humidity\":49,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.36,\"deg\":59},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2019-12-05 15:00:00\"},{\"dt\":1575568800,\"main\":{\"temp\":284.19,\"temp_min\":284.19,\"temp_max\":284.19,\"pressure\":1021,\"sea_level\":1021,\"grnd_level\":1001,\"humidity\":65,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":1.75,\"deg\":74},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-05 18:00:00\"},{\"dt\":1575579600,\"main\":{\"temp\":282.64,\"temp_min\":282.64,\"temp_max\":282.64,\"pressure\":1023,\"sea_level\":1023,\"grnd_level\":1003,\"humidity\":68,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":1.79,\"deg\":76},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-05 21:00:00\"},{\"dt\":1575590400,\"main\":{\"temp\":282.14,\"temp_min\":282.14,\"temp_max\":282.14,\"pressure\":1023,\"sea_level\":1023,\"grnd_level\":1003,\"humidity\":73,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.27,\"deg\":86},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-06 00:00:00\"},{\"dt\":1575601200,\"main\":{\"temp\":281.88,\"temp_min\":281.88,\"temp_max\":281.88,\"pressure\":1023,\"sea_level\":1023,\"grnd_level\":1003,\"humidity\":75,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.07,\"deg\":87},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-06 03:00:00\"},{\"dt\":1575612000,\"main\":{\"temp\":281.63,\"temp_min\":281.63,\"temp_max\":281.63,\"pressure\":1023,\"sea_level\":1023,\"grnd_level\":1003,\"humidity\":73,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":1.72,\"deg\":93},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-06 06:00:00\"},{\"dt\":1575622800,\"main\":{\"temp\":282.53,\"temp_min\":282.53,\"temp_max\":282.53,\"pressure\":1025,\"sea_level\":1025,\"grnd_level\":1004,\"humidity\":69,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":4},\"wind\":{\"speed\":1.61,\"deg\":93},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2019-12-06 09:00:00\"},{\"dt\":1575633600,\"main\":{\"temp\":287.15,\"temp_min\":287.15,\"temp_max\":287.15,\"pressure\":1024,\"sea_level\":1024,\"grnd_level\":1004,\"humidity\":53,\"temp_kf\":0},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"clouds\":{\"all\":48},\"wind\":{\"speed\":0.88,\"deg\":328},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2019-12-06 12:00:00\"},{\"dt\":1575644400,\"main\":{\"temp\":287.09,\"temp_min\":287.09,\"temp_max\":287.09,\"pressure\":1023,\"sea_level\":1023,\"grnd_level\":1003,\"humidity\":55,\"temp_kf\":0},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"clouds\":{\"all\":100},\"wind\":{\"speed\":1.46,\"deg\":332},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2019-12-06 15:00:00\"},{\"dt\":1575655200,\"main\":{\"temp\":284.56,\"temp_min\":284.56,\"temp_max\":284.56,\"pressure\":1024,\"sea_level\":1024,\"grnd_level\":1004,\"humidity\":67,\"temp_kf\":0},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04n\"}],\"clouds\":{\"all\":98},\"wind\":{\"speed\":1.5,\"deg\":350},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-06 18:00:00\"},{\"dt\":1575666000,\"main\":{\"temp\":283.77,\"temp_min\":283.77,\"temp_max\":283.77,\"pressure\":1025,\"sea_level\":1025,\"grnd_level\":1005,\"humidity\":73,\"temp_kf\":0},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04n\"}],\"clouds\":{\"all\":100},\"wind\":{\"speed\":0.99,\"deg\":44},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-06 21:00:00\"},{\"dt\":1575676800,\"main\":{\"temp\":283,\"temp_min\":283,\"temp_max\":283,\"pressure\":1025,\"sea_level\":1025,\"grnd_level\":1005,\"humidity\":75,\"temp_kf\":0},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04n\"}],\"clouds\":{\"all\":92},\"wind\":{\"speed\":0.83,\"deg\":74},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-07 00:00:00\"},{\"dt\":1575687600,\"main\":{\"temp\":282.36,\"temp_min\":282.36,\"temp_max\":282.36,\"pressure\":1025,\"sea_level\":1025,\"grnd_level\":1005,\"humidity\":76,\"temp_kf\":0},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04n\"}],\"clouds\":{\"all\":64},\"wind\":{\"speed\":1.16,\"deg\":84},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-07 03:00:00\"},{\"dt\":1575698400,\"main\":{\"temp\":281.79,\"temp_min\":281.79,\"temp_max\":281.79,\"pressure\":1025,\"sea_level\":1025,\"grnd_level\":1005,\"humidity\":75,\"temp_kf\":0},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03n\"}],\"clouds\":{\"all\":39},\"wind\":{\"speed\":1.13,\"deg\":54},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2019-12-07 06:00:00\"},{\"dt\":1575709200,\"main\":{\"temp\":282.66,\"temp_min\":282.66,\"temp_max\":282.66,\"pressure\":1027,\"sea_level\":1027,\"grnd_level\":1007,\"humidity\":69,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":1.05,\"deg\":56},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2019-12-07 09:00:00\"},{\"dt\":1575720000,\"main\":{\"temp\":287.2,\"temp_min\":287.2,\"temp_max\":287.2,\"pressure\":1027,\"sea_level\":1027,\"grnd_level\":1007,\"humidity\":53,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":2},\"wind\":{\"speed\":1.14,\"deg\":357},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2019-12-07 12:00:00\"},{\"dt\":1575730800,\"main\":{\"temp\":287.92,\"temp_min\":287.92,\"temp_max\":287.92,\"pressure\":1026,\"sea_level\":1026,\"grnd_level\":1007,\"humidity\":54,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.3,\"deg\":318},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2019-12-07 15:00:00\"}],\"city\":{\"id\":2735943,\"name\":\"Porto\",\"coord\":{\"lat\":41.1495,\"lon\":-8.6108},\"country\":\"PT\",\"population\":249633,\"timezone\":0,\"sunrise\":1575272471,\"sunset\":1575306399}}";

        public Dictionary<int, List<WeatherByHour>> WeatherByDays;
        public WeatherList(City city)
        {
            WeatherByDays = new Dictionary<int, List<WeatherByHour>>();

            var url = apiBase + city.Name;
            
            //TODO chamar api
            
            ParseJsonResponse(template);
            
            getWeather(url);
            
        }
        
        public async void getWeather (string url)
        {
            var _client = new HttpClient ();
            var uri = new Uri (string.Format (url, string.Empty));
            Console.WriteLine("oi");
            Console.WriteLine(uri.ToString());
            var response = await _client.GetAsync (uri);
            if (response.IsSuccessStatusCode)
            {
                var responseString = await response.Content.ReadAsStringAsync();
                Console.WriteLine("aqui");
                Console.WriteLine(responseString);
                Console.WriteLine("aqui2");
                ParseJsonResponse(responseString);
            }
            else
            {
                Console.WriteLine("Rip");
            }
        }
        private void ParseJsonResponse2(string response)
        {
            
            var json = JArray.Parse(response);

            var previousHour = 0;
            var day = 0;
            var weatherByHours = new List<WeatherByHour>();
            
            foreach (var item in json)
            {
                var weather = new WeatherByHour(item);
                
                DateTime date = DateTime.ParseExact(weather.dt_txt, "yyyy-MM-dd HH:mm:ss",
                    System.Globalization.CultureInfo.InvariantCulture);

                var hour = date.Hour;
                if (hour < previousHour)
                {
                    WeatherByDays[day] = weatherByHours;
                    day++;
                    if (day > 1)
                    {
                        break;
                    }
                    
                    weatherByHours = new List<WeatherByHour>();
                }

                previousHour = hour;
                weatherByHours.Add(weather);
            }
        }

        private void ParseJsonResponse(string response)
        {
            var resultObject = JObject.Parse(response);
            var json = resultObject["list"];
//            Console.WriteLine(json);

            var previousHour = 0;
            var day = 0;
            var weatherByHours = new List<WeatherByHour>();
            
            foreach (var item in json)
            {
                var weather = new WeatherByHour(item);
                
                DateTime date = DateTime.ParseExact(weather.dt_txt, "yyyy-MM-dd HH:mm:ss",
                    System.Globalization.CultureInfo.InvariantCulture);

                var hour = date.Hour;
                if (hour < previousHour)
                {
                    WeatherByDays[day] = weatherByHours;
                    day++;
                    if (day > 1)
                    {
                        break;
                    }
                    
                    weatherByHours = new List<WeatherByHour>();
                }

                previousHour = hour;
                weatherByHours.Add(weather);
            }
        }
    }
}