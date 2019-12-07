using System;
using System.Collections.Generic;
using System.Net.Http;
using Newtonsoft.Json.Linq;
using WeatherApp.ViewModel;

namespace WeatherApp.Models
{
    public class WeatherList
    {
        private City City;
        private FavoriteCitiesViewModel FavoriteCitiesViewModel;
        private Dictionary<int, List<WeatherByHour>> _weatherByDays;
        string apiBase = "http://api.openweathermap.org/data/2.5/forecast?appid=9c9527823d0d06d5fac5e11d5865d56f&q=";
        
        public WeatherList(City city, FavoriteCitiesViewModel favoriteCitiesViewModel)
        {
            City = city;
            FavoriteCitiesViewModel = favoriteCitiesViewModel;

            var url = apiBase + city.Name;
            GetWeather(url);
        }
        
        public async void GetWeather (string url)
        {
            var client = new HttpClient ();
            var uri = new Uri (string.Format (url, string.Empty));
            var response = await client.GetAsync (uri);
            if (response.IsSuccessStatusCode)
            {
                var responseString = await response.Content.ReadAsStringAsync();
                ParseJsonResponse(responseString);
            }
        }

        private void ParseJsonResponse(string response)
        {
            _weatherByDays = new Dictionary<int, List<WeatherByHour>>();
            
            var resultObject = JObject.Parse(response);
            var json = resultObject["list"];

            var previousHour = 0;
            var day = 0;
            var weatherByHours = new List<WeatherByHour>();
            
            foreach (var item in json)
            {
                var weather = new WeatherByHour(item);
                
                var date = DateTime.ParseExact(weather.dt_txt, "yyyy-MM-dd HH:mm:ss",
                    System.Globalization.CultureInfo.InvariantCulture);

                var hour = date.Hour;
                if (hour < previousHour)
                {
                    _weatherByDays[day] = weatherByHours;
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

            City.WeatherByDays = _weatherByDays;
            FavoriteCitiesViewModel.UpdateViewModel(City);
        }
    }
}